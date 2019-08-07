package com.fosun.data.cleanup.comment.tag.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.nlp.AipNlp;
import com.baidu.aip.nlp.ESimnetType;
import com.fosun.data.cleanup.comment.tag.dto.po.db.CommentTagPo;
import com.fosun.data.cleanup.comment.tag.dto.po.db.ShopClassPo;
import com.fosun.data.cleanup.comment.tag.dto.po.db.ShopInfoPo;
import com.fosun.data.cleanup.comment.tag.dto.po.mongodb.MallCommentInfoPo;
import com.fosun.data.cleanup.comment.tag.dto.po.mongodb.MongoCommentTagInfoPo;
import com.fosun.data.cleanup.comment.tag.dto.po.mongodb.ShopCommentInfoPo;
import com.fosun.data.cleanup.comment.tag.enums.SubjectTypeEnum;
import com.fosun.data.cleanup.comment.tag.redis.RedisUtil;
import com.fosun.data.cleanup.comment.tag.repository.CommentTagPoRepository;
import com.fosun.data.cleanup.comment.tag.repository.ShopClassRepository;
import com.fosun.data.cleanup.comment.tag.repository.ShopInfoRepository;
import com.fosun.data.cleanup.comment.tag.tuple.Tuple;
import com.fosun.data.cleanup.comment.tag.utils.DateUtil;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 调用百度api 的接口服务
 * @author: liumch
 * @create: 2019/7/17 15:51
 **/

@Service
@Slf4j
public class BaiduApiServiceImpl {

    /**
     * 默认的百度业态为生活，用于处理商场的业态和店铺未知的业态
     */
    private final static int DEF_BAIDU_BIZ = 12;
    /**
     * 百度业态的映射表
     */
    private static final HashMap<Integer,ESimnetType> BAIDU_BIZ_MAP = initBaiduBizClsMap();
    private static final int MINUS_DAYS = 1;
    private final Pattern pattern = Pattern.compile("<span>(.*?)</span>");
    private static final String rm_pattern = "[^\\u0000-\\uFFFF|[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]]";

//    private static final int  COMMENT_SIZE = 900;

    /**
     * api调用失败的计数
     */
    AtomicInteger failureCount = new AtomicInteger(0);
    /**
     * 初始化一个AipNlp
     */
    @Value("${baidu_api.appid}")
    private String appid;

    @Value("${baidu_api.key}")
    private String key;

    @Value("${baidu_api.secret}")
    private String secret;
    final AipNlp client = new AipNlp(appid, key, secret);


     @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommentTagPoRepository commentTagPoRepository;
    @Autowired
    private ShopInfoRepository shopInfoRepository;
    @Autowired
    private ShopClassRepository shopClassRepository ;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${URL_MALL}")
    private String URL_MALL;

    @Value("${URL_SHOP}")
    private String URL_SHOP;


    /**
     * 初始化百度业态的映射表
     * @return 映射关系
     */
    private static HashMap<Integer,ESimnetType> initBaiduBizClsMap(){
        HashMap<Integer,ESimnetType> baiduBizClsMap = new HashMap<>(14);
        baiduBizClsMap.put(0,ESimnetType.UNSUPPORT_TYPE);
        baiduBizClsMap.put(1,ESimnetType.HOTEL);
        baiduBizClsMap.put(2,ESimnetType.KTV);
        baiduBizClsMap.put(3,ESimnetType.BEAUTY);
        baiduBizClsMap.put(4,ESimnetType.FOOD);
        baiduBizClsMap.put(5,ESimnetType.TRAVEL);
        baiduBizClsMap.put(6,ESimnetType.HEALTH);
        baiduBizClsMap.put(7,ESimnetType.EDU);
        baiduBizClsMap.put(8,ESimnetType.BUSINESS);
        baiduBizClsMap.put(9,ESimnetType.HOUSE);
        baiduBizClsMap.put(10,ESimnetType.CAR);
        baiduBizClsMap.put(11,ESimnetType.LIFE);
        baiduBizClsMap.put(12,ESimnetType.SHOPPING);
        baiduBizClsMap.put(13,ESimnetType._3C);
        return baiduBizClsMap;
    }
     /**
     * 处理评论的标签信息
     * @param ids 商场或店铺的id信息
     * @param type 商场或店铺类型 0：商场， 1：店铺， 其它值，若ids为空，则同时处理店铺和商场的所有数据
     * @param  startDate 起始日期
     * @param  endDate 结束日期 ，如果为空，则默认为当天
     * @return 获取的记录数
     */
    public int dealCommentTag(final List<Long> ids,int type,Date startDate,Date endDate) throws Exception{
        Tuple<List<MallCommentInfoPo>, List<ShopCommentInfoPo>> tuple = queryMongoData(ids, type, startDate,endDate);
        List<MallCommentInfoPo> mallCommentInfoPos  = tuple.t1;
        List<ShopCommentInfoPo> shopCommentInfoPos = tuple.t2;

        int res = 0;
        log.info("所有提取的标签，按照百度的标签分类");
        /**
         * 执行该语句容易导致gc overhead limit 的问题
         * 解决方法：每次只是添加
//        List<MongoCommentTagInfoPo> mongoCommentTagInfoPos = mongoTemplate.find(new Query(), MongoCommentTagInfoPo.class);
         */
        List<MongoCommentTagInfoPo> pos = new ArrayList<>(3000);
        //提取标签
        if( !CollectionUtils.isEmpty(mallCommentInfoPos)){
            log.info("获取到{}条商场的评论数据，start:{},end:{}",mallCommentInfoPos.size(),startDate,endDate);
//            pos = getMongoTagData(mongoCommentTagInfoPos, SubjectTypeEnum.MALL);
            dealMallCommentInfo(mallCommentInfoPos, pos);
            res += mallCommentInfoPos.size();
        }else{
            log.info("没有获取到商场的评论数据，start:{},end:{}",startDate,endDate);
        }
        if( !CollectionUtils.isEmpty(shopCommentInfoPos) ){
             log.info("获取到{}条店铺的评论数据，start:{},end:{}",shopCommentInfoPos.size(),startDate,endDate);
//             pos = getMongoTagData(mongoCommentTagInfoPos,SubjectTypeEnum.SHOP);
             dealShopCommentInfo(shopCommentInfoPos,pos);
             res += shopCommentInfoPos.size();
        }else{
            log.info("没有获取到店铺的评论数据，start:{},end:{}",startDate,endDate);
        }
        log.info("api调用失败的次数为:{},type:{}，startDate：{},endDate：{}",failureCount.intValue(),type,startDate,endDate);
        //删除mongodb中的所有标签信息，然后重新构建新的标签对象后插入
        updateMongodbTagInfo(pos);
        pos = null;
        return res;
    }


    /**
     * 获取mongodb 数据
     *  此操作容易导致gc 问题，注释掉
     * @param mongoCommentTagInfoPos mongodb查询的结果
     * @param typeEnum 类型
     * @return 对象
     */
    @Deprecated
    private List<MongoCommentTagInfoPo> getMongoTagData(final List<MongoCommentTagInfoPo> mongoCommentTagInfoPos,SubjectTypeEnum typeEnum) throws Exception{
        List<MongoCommentTagInfoPo> pos = null;
        if(!CollectionUtils.isEmpty(mongoCommentTagInfoPos)){
            pos = mongoCommentTagInfoPos.stream().filter(x->x.getSource().equals(typeEnum.getValue().intValue())).collect(Collectors.toList());
        }
        return null == pos ?  new ArrayList<>(): pos;
    }

    /**
     * 防止同时修改信息
     * @param pos 处理后的标签信息
     */

    private void updateMongodbTagInfo(final List<MongoCommentTagInfoPo> pos ) {
        long start = System.currentTimeMillis();
        int cnt = 0;
        try{
            log.info("开始更新mongodb数据");
            if(!CollectionUtils.isEmpty(pos)){
                Criteria criteria;
                MongoCommentTagInfoPo mongoCommentTagInfoPo;
                for(MongoCommentTagInfoPo po : pos){
                    criteria = new Criteria();
                    criteria.and("bizType").is(po.getBizType());
                    criteria.and("source").is(po.getSource());
                    mongoCommentTagInfoPo = mongoTemplate.findOne(new Query(criteria), MongoCommentTagInfoPo.class);
                    if(mongoCommentTagInfoPo == null){
                        mongoTemplate.save(po);
                    }else{
                        mongoCommentTagInfoPo.getTag().addAll(po.getTag());
                        mongoTemplate.save(mongoCommentTagInfoPo);
                    }
                    cnt++;
                }
                log.info("插入{}条mongodb标签",pos.size());
            }else{
                log.info("待插入mongodb标签的标签列表为空");
            }

        }catch (Exception e){
            log.error("写入mongodb标签失败，异常信息:" + e.getLocalizedMessage());
        }finally {
            long end = System.currentTimeMillis();
            log.info("写入mongodb 标签结束,一共写入{}条标签,耗时：{}s",cnt,1.0*(end-start)/1000);
        }

    }



    /**
     * 处理店铺评论信息
     * @param shopCommentInfoPos 店铺的评论详情信息
     * @param pos 每个百度业态下对应的评论标签列表
     */
    private void dealShopCommentInfo(final List<ShopCommentInfoPo> shopCommentInfoPos, final List<MongoCommentTagInfoPo> pos) throws Exception {
        long start = System.currentTimeMillis();
        Map<Integer, Map<Long, List<ShopCommentInfoPo>>> shopCommentMap = shopCommentInfoPos.stream()
                .filter(x -> x.getCommentLevel() < 3 || x.getCommentLevel() >= 4)
                .collect(Collectors.groupingBy(x -> x.getCommentLevel() < 3 ? 0 : 1, Collectors.groupingBy(ShopCommentInfoPo::getShopId)));
         shopCommentMap.forEach((commentType,idMap)->idMap.forEach((id,data)->{
             long s1 = System.currentTimeMillis();
            //处理每条店铺的评论信息，抽取其中的标签
            Map<String,Integer> cntMap = new HashMap<>(50);
            //查找大众的业态对应的百度业态id
            ESimnetType eSimnetType = queryBaiduBizClsByShopId(id);
            List<ShopCommentInfoPo> failCommentInfo = new ArrayList<>(data.size());
            data.parallelStream().forEach(x->{
                Tuple<List<String>, String>  contentTuple = requestLTPParse(x.getId(),x.getContent(),eSimnetType,false);
//                 Tuple<List<String>, String>  contentTuple = requestTagParse(x.getId(),x.getContent(),eSimnetType);
                if(!CollectionUtils.isEmpty(contentTuple.t1)){
                    //成功提取了文章的观点信息，则需要保存评论信息和店铺的观点信息
                    x.setHighlightContent(contentTuple.t2);
                    x.setCommentTag(contentTuple.t1);
                    contentTuple.t1.stream().forEach(tag ->{
                        cntMap.putIfAbsent(tag,0);
                        cntMap.computeIfPresent(tag,(k,v)->v+1);
                        saveTags(pos,DEF_BAIDU_BIZ,tag,SubjectTypeEnum.SHOP.getValue().intValue());
                    });
                    updateDocument(x.getId(),contentTuple.t2,contentTuple.t1,1);
                }else{
                    // 保存处理失败的信息
                    failCommentInfo.add(x);
                }
            });
             long e1 = System.currentTimeMillis();
            log.info("处理店铺id：{},评论类型：{},数据量：{}, 耗时：{}s",id,commentType,data.size(),1.0*(e1-s1)/1000);
             List<CommentTagPo> commentTagPos = buildCommentTag(id, SubjectTypeEnum.SHOP, commentType, cntMap);
             if(!CollectionUtils.isEmpty(failCommentInfo)){
                 log.info("开始保存处理失败的店铺:{}评论信息，评论类型：{}，大小：{}",id,commentType,failCommentInfo.size());
                 mongoTemplate.insert(failCommentInfo,"FailExtraTag_Shop_Comment_Info");
             }
             updateCommentTags(commentTagPos);
        }));
        long end = System.currentTimeMillis();
        log.info("：{}, 耗时：{}s",shopCommentInfoPos.size(),1.0*(end-start)/1000);

    }

    /**
     * 根据店铺业态，查询对应的百度业态类型
     *
     * @param shopId 店铺id
     * @return 百度业态枚举值
     */
    private ESimnetType queryBaiduBizClsByShopId(Long shopId) {
        ShopInfoPo shopInfoPo = shopInfoRepository.findByShopId(shopId);
        if(null == shopInfoPo){
            return ESimnetType.LIFE;
        }
        ShopClassPo po = null;
        if(null != shopInfoPo.getSlevelTypeId()){
            po = shopClassRepository.findByClsId(shopInfoPo.getSlevelTypeId().intValue());
            if(null == po && null!= shopInfoPo.getFlevelTypeId() ){
                po = shopClassRepository.findByClsId(shopInfoPo.getFlevelTypeId().intValue());
            }
        }

        return  (null == po || null == po.getBaiduTypeId()) ?  ESimnetType.LIFE : BAIDU_BIZ_MAP.getOrDefault(po.getBaiduTypeId(),ESimnetType.LIFE);
    }

    /**
     * 处理商场的评论信息
     * @param mallCommentInfoPos 商场评论信息
     * @param pos mongodb 中的评论标签信息
     */
    private void dealMallCommentInfo(final List<MallCommentInfoPo> mallCommentInfoPos ,final List<MongoCommentTagInfoPo> pos) throws Exception{

        long start = System.currentTimeMillis();
        Map<Integer, Map<Long, List<MallCommentInfoPo>>> mallCommentMap = mallCommentInfoPos.stream()
                .filter(x -> x.getCommentLevel() < 3 || x.getCommentLevel() >= 4)
                .collect(Collectors.groupingBy(x -> x.getCommentLevel() < 3 ? 0 : 1, Collectors.groupingBy(MallCommentInfoPo::getMallId)));

        mallCommentMap.forEach((commentType,idMap)-> idMap.forEach((id, data)->{
            long s1 = System.currentTimeMillis();
            //处理每条商场的评论信息，抽取其中的标签
            Map<String,Integer> cntMap = new HashMap<>(50);

            //处理失败的商场信息
            List<MallCommentInfoPo> failCommentInfo = new ArrayList<>(data.size());
            data.parallelStream().forEach(x->{
                Tuple<List<String>, String>  contentTuple = requestLTPParse(x.getId(),x.getContent(),ESimnetType.SHOPPING,true);
//                Tuple<List<String>, String> contentTuple = requestTagParse(x.getId(),x.getContent(),ESimnetType.SHOPPING);
                if(!CollectionUtils.isEmpty(contentTuple.t1)){
                    //成功提取了文章的观点信息，则需要保存评论信息和商场的观点信息
                    contentTuple.t1.stream().forEach(tag ->{
                        cntMap.putIfAbsent(tag,0);
                        cntMap.computeIfPresent(tag,(k,v)->v+1);
                        saveTags(pos,DEF_BAIDU_BIZ,tag,SubjectTypeEnum.MALL.getValue().intValue());
                    });
                    updateDocument(x.getId(),contentTuple.t2,contentTuple.t1,0);
                }else{
                    //保存处理失败的信息
                    failCommentInfo.add(x);
                }
             });

            long e1 = System.currentTimeMillis();
            log.info("处理商场id{},评论类型：{}，数据量：{}, 耗时：{}s",id,commentType,data.size(),1.0*(e1-s1)/1000);
            List<CommentTagPo> commentTagPos = buildCommentTag(id, SubjectTypeEnum.MALL, commentType, cntMap);
            if(!CollectionUtils.isEmpty(failCommentInfo)){
                log.info("开始保存处理失败的商场评论信息:{},评论类型：{}，大小:{}",id,commentType,failCommentInfo.size() );
                mongoTemplate.insert(failCommentInfo,"FailExtraTag_Mall_Comment_Info");
            }
            updateCommentTags(commentTagPos);
        }));
        long end = System.currentTimeMillis();
        log.info("处理商场评论数据量：{}, 耗时：{}s",mallCommentInfoPos.size(),1.0*(end-start)/1000);
    }

    /**
     * 更新文档，添加高亮信息和关键词信息
     * @param id 文档id
     * @param highLightContent 高亮文本
     * @param tags 标签列表
     * @param type 0：商场， 1：店铺
     * @return 更新的文档数
     */
    private long updateDocument(String id, String highLightContent,List<String> tags,int type){
        Update update = new Update();
        update.set("commentTag",tags);
        update.set("highlighContent",highLightContent);
        Query query = new Query(new Criteria().and("_id").is(id));
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, type==0?MallCommentInfoPo.class:ShopCommentInfoPo.class);
        log.info("更新文档：{}成功",id);
        return updateResult.getModifiedCount();

    }
    /**
     * 按照百度的业态分类，保存所有标签。用于后续分析
     * @param pos  标签分类信息
     * @param bizCls 百度业态
     * @param tag 提取的标签
     * @param source 来源
     */
    private void saveTags( final List<MongoCommentTagInfoPo> pos ,int bizCls, String tag,int source){
        //将标签按照百度的分类，保存起来
        MongoCommentTagInfoPo po;
        if( !CollectionUtils.isEmpty(pos) ){
            Optional<MongoCommentTagInfoPo> optional = pos.stream()
                    .filter(x->null != x && x.getBizType().equals(bizCls) && x.getSource().equals(source))
                    .findFirst();
            if(optional.isPresent()){
                po = optional.get();
                if(po.getTag() != null){
                    po.getTag().add(tag);
                }else{
                    po.setTag(new HashSet<>(200));
                    po.getTag().add(tag);
                }
            }
        }else{
            po  = new MongoCommentTagInfoPo();
            po.setTag(new HashSet<>());
            po.getTag().add(tag);
            po.setSource(source);
            po.setBizType(bizCls);
            pos.add(po);
        }
    }

    /**
     * 生成评论标签的对象集合
     * @param id 主体id
     * @param subjectTypeEnum 主体类型
     * @param commentType 评论类型 0：差评 1：好评
     * @param dataMap 数据集合
     * @return 标签对象集合
     */
    private List<CommentTagPo> buildCommentTag(Long id,SubjectTypeEnum subjectTypeEnum,Integer commentType,final Map<String,Integer> dataMap){
         List<CommentTagPo> commentTagPos = new ArrayList<>();
        Date dt = new Date();
        List<CommentTagPo> historyData = commentTagPoRepository.queryBySubjectIdAndTypes(id,subjectTypeEnum.getValue(),commentType.shortValue());
        Map<String, Integer> historyMap = historyData.stream().collect(Collectors.toMap(CommentTagPo::getTag, CommentTagPo::getNum,(a,b)->a));

        //合并两个map
        dataMap.forEach((k,v)-> historyMap.merge(k,v,(v1,v2)->v1 + v2));

        historyMap.forEach((tag,cnt)->{
            CommentTagPo po  = new CommentTagPo();
            po.setCommentType(commentType.shortValue());
            po.setCreateTime(dt);
            po.setKeyword(tag);
            po.setTag(tag);
            po.setNum(cnt);
            po.setSubjectId(id);
            po.setSubjectType(subjectTypeEnum.getValue());
            po.setUpdateTime(dt);
            commentTagPos.add(po);
        });
        //倒序排序
        commentTagPos.sort(Comparator.comparingInt(CommentTagPo::getNum).reversed());
        return commentTagPos;
    }

    /**
     * 更新所有的评论标签信息
     * @param commentTagPos 评论标签信息
     * @return 更新的评论数
     */
    private int updateCommentTags(final List<CommentTagPo> commentTagPos ){
        if(!CollectionUtils.isEmpty(commentTagPos)){
            //删除该商场的所有评论标签信息
            CommentTagPo po = commentTagPos.get(0);
            int cntDel = commentTagPoRepository.deleteBySubjectIdAndTypes(po.getSubjectId(),po.getSubjectType(),po.getCommentType());
            log.info("删除{}条记录，subjectId:{},subjectType:{},commentType:{}",cntDel,po.getSubjectId(),po.getSubjectType(),po.getCommentType());
            //重新插入所有数据
            commentTagPoRepository.saveAll(commentTagPos);
            return commentTagPos.size();
        }

       return 0;
    }

    /**
     * 本地ltp-serv 接口，获取标签信息
     * @param content 待解析的评论内容
     * @param eSimnetType 百度业态类型
     * @return tuple 对象 ，属性1：标签集合 属性2： 包含高亮信息评论详情
     */

    private Tuple<List<String>,String> requestLTPParse(String id,String content,ESimnetType eSimnetType,boolean isMall){
        List<String> tags = new ArrayList<>();
        long t1=System.currentTimeMillis();
//        Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE ) ;
        JSONArray arr = requestLTPServer(content,isMall);
       log.info(String.format("request cost %f s",(System.currentTimeMillis()-t1)*1.0/1000));
        if(!CollectionUtils.isEmpty(arr)){
            //一个段落里的每句话，按照句号、问好、感叹号等分隔
            arr.parallelStream().forEach(x->{
                JSONArray subSentenceArr = (JSONArray) x;
                //一个句子中的每个短句
                for (int k = 0; k < subSentenceArr.size(); k++) {
                    com.alibaba.fastjson.JSONObject pos = subSentenceArr.getJSONObject(k);
                    k = pos.getInteger("id");
                    //提取名词,线程安全
                    StringBuilder sb = new StringBuilder();
                    //类似“管理混乱”这种，对于管理一词，是SBV结构，且他的parentId 为混乱，但是混乱是形容词，应该被提取出来
                    if(pos.getString("pos").equalsIgnoreCase("n")
                            ||(pos.getString("pos").equalsIgnoreCase("v") && pos.getString("relate").equalsIgnoreCase("SBV"))){
                        com.alibaba.fastjson.JSONObject pObject = subSentenceArr.getJSONObject(pos.getInteger("parent"));
                        if(pObject.getString("pos").equalsIgnoreCase("a")  ){
                            sb.append(pos.getString("cont")).append(pObject.getString("cont"));
                            k = Math.max(k,pObject.getInteger("id"));
                            if(sb.length()>2){
                                tags.add(sb.toString());
                            }

                        }
//                        else if(pObject.getString("relate").equalsIgnoreCase("SBV")){
//                             这种结构不适合
//                            sb.append(pos.getString("cont")).append(pObject.getString("cont"));
//                            pObject = subSentenceArr.getJSONObject(pObject.getInteger("parent"));
//                            k = Math.max(k,pObject.getInteger("id"));
//                            sb.append(pObject.getString("cont"));
//                            k = Math.max(k,pObject.getInteger("id"));
//                            tags.add(sb.toString());
//                        }
                    }
                }
            });
        }else{
            log.warn("评论id:{}本地ltp接口调用失败",id);
            failureCount.getAndIncrement();
            return new Tuple(new ArrayList<>(0),content);
        }
        long t2 = System.currentTimeMillis();
        log.info("评论id:{}解析成功, cost:{} ",id, (t2 - t1)*0.1/1000 + " s");
        return new Tuple<>(tags,content);
    }

    /**
     *
     * 防止一句话太长导致服务奔溃。所以，需要拆开来处理
     * @param content 拆分后的句子
     * @param isMall 是否是商场
     * @return 句子解析的结果
     */
    private JSONArray requestLTPServer(String content,boolean isMall){
        content = content.replaceAll(rm_pattern,"");

        JSONArray arr = new JSONArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type","application/x-www-form-urlencoded");
        if(StringUtils.isNotBlank(content)){
            MultiValueMap<String, String> requestEntity;
            String[] contents = content.split("[。！？]");
            for(String text : contents){
                if(StringUtils.isNotBlank(text)){
                    requestEntity = new LinkedMultiValueMap<>();
                    requestEntity.add("f","json");
                    requestEntity.add("s",text);
                    HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestEntity,headers);
                    ResponseEntity<JSONArray> response = restTemplate.postForEntity( isMall? URL_MALL:URL_SHOP ,httpEntity, JSONArray.class);
                    if(response.getStatusCode().equals(HttpStatus.OK) && !CollectionUtils.isEmpty(response.getBody())
                            && !CollectionUtils.isEmpty(response.getBody().getJSONArray(0))) {
                        arr.add(response.getBody().getJSONArray(0).getJSONArray(0));
                    }
                }
            }
        }
        return arr;
    }

    /**
     * 请求百度api 接口，获取标签信息
     *  qps 无法达到5，所以暂时不用该方法
     * @param content 待解析的评论内容
     * @param eSimnetType 百度业态类型
     * @return tuple 对象 ，属性1：标签集合 属性2： 包含高亮信息评论详情
     */
    @Deprecated
    private Tuple<List<String>, String> requestTagParse(String id,String content,ESimnetType eSimnetType)  {

        //移除掉所有的表情符号
        content = content.replaceAll(rm_pattern,"");
        // 获取美食评论情感属性
        HashMap<String,Object> options = new HashMap<>(2);
        org.json.JSONObject res = null;
        log.info("开始调用百度api评论：{}",id);
        for( int i = 1; i<4;i++){
            if(redisUtil.existReqLock()){
                log.info("存在百度api锁，等待释放");
                redisUtil.waitReqUnlock();
            }
            log.info("获得百度api锁");
            //建立连接超时的时间
            client.setConnectionTimeoutInMillis(30000);
            //数据传输超时时间
            client.setSocketTimeoutInMillis(30000);
            res = client.commentTag(content,eSimnetType ,options);
//            try{
//                Thread.sleep(400);
//            }catch (InterruptedException e){
//                log.error(e.getLocalizedMessage());
//            }
            redisUtil.setReqLock();
            if(!res.has("error_msg")){
                //没有报错
                break;
            }
            log.info("调用百度api失败,重试次数:{},res:{}",i,res.toString());
        }
        List<String> tags = new ArrayList<>();
        if(!res.has("items")){
            log.warn("评论id:{}百度api调用失败",id);
            failureCount.getAndIncrement();
            return new Tuple(new ArrayList<>(0),content);
        }
        log.info("调用百度api成功");
        org.json.JSONArray arr = res.getJSONArray("items");
        org.json.JSONObject obj;
        Matcher matcher;
        for(int i = 0; i< arr.length();i++) {
            obj = arr.getJSONObject(i);
            String tag = Strings.EMPTY;
            if (StringUtils.isNotBlank(obj.getString("prop"))) {
                tag += obj.getString("prop");
            }
            if (StringUtils.isNotBlank(obj.getString("adj"))) {
                tag += obj.getString("adj");
            }
            if (StringUtils.isNotBlank(tag)) {
                tags.add(tag);
            }
            //处理高亮
            if (StringUtils.isNotBlank(obj.getString("abstract"))) {
                String highlightText = null;
                highlightText = obj.getString("abstract");
                matcher = pattern.matcher(highlightText);
                while (matcher.find()) {
                    String matchWord = matcher.group(1);
                    if (StringUtils.isNotBlank(matchWord)) {

                        if (matchWord.contains("[+.?*]")) {
                            matchWord = matchWord.replaceAll("[?]", ",");
                            content = content.replaceAll("[?]", ",").replaceAll("[.]", ",")
                                    .replaceAll("[+]", "").replaceAll("[*]", "");
                        }
                        try {
                            content = content.replaceAll(matchWord, "<pre>" + matchWord + "</pre>");
                        } catch (Exception e) {
                            System.out.println("parse error: " + content);
                        }
                    }
                }
            }
        }
          return new Tuple(tags,content);

    }


    /**
     * 从mongodb 中查询源数据
     * @param ids  待查询的id .
     * @param type 类型 0：商场， 1：店铺 ，其它值:若ids为空，则同时处理店铺和商场的所有数据
     * @param startDate 起始日期
     * @param endDate 结束日期 ，如果为空，则默认为当天
     * @return 店铺和商场评论列表
     */
    private Tuple<List<MallCommentInfoPo>, List<ShopCommentInfoPo>> queryMongoData(final List<Long> ids,int type, Date startDate,Date endDate) throws Exception{

        List<MallCommentInfoPo> mallCommentInfoPos = null;
        List<ShopCommentInfoPo> shopCommentInfoPos = null;
        Date startTime = startDate == null ? DateUtil.getTimeOfPreviousDays(MINUS_DAYS) : startDate;

        Criteria criteria = new Criteria();
        if(endDate != null){
            criteria.and("commentTime").gte(startTime).lt(endDate);
        }else{
            criteria.and("commentTime").gte(startTime);
        }
        log.info("query mongodb data, start date:{} ,endDate:{}",startDate,endDate);
        if(!CollectionUtils.isEmpty(ids) ){
            if(type == 0){
                criteria.and("mallId").in(ids);
                Query query = new Query(criteria);
                query.fields().include("id").include("content").include("commentLevel").include("mallId");
                mallCommentInfoPos  = mongoTemplate.find(query,MallCommentInfoPo.class);
            }else if(type == 1){
                criteria.and("shopId").in(ids);
                Query query = new Query(criteria);
                query.fields().include("id").include("content").include("commentLevel").include("shopId");
                shopCommentInfoPos = mongoTemplate.find(query,ShopCommentInfoPo.class);
            }else{
                log.error("type 值不能为空");
            }
        }else{
            Query query = new Query(criteria);
            query.fields().include("id").include("content").include("commentLevel");
            if(type == 0){
                 query.fields().include("mallId");
                 mallCommentInfoPos  = mongoTemplate.find(query,MallCommentInfoPo.class);
            }else if(type == 1){
                query.fields().include("shopId");
                 shopCommentInfoPos = mongoTemplate.find(query,ShopCommentInfoPo.class);
            }else{
                query.fields().include("mallId");
                mallCommentInfoPos  = mongoTemplate.find(query,MallCommentInfoPo.class);
                query.fields().include("shopId").exclude("mallId");
                shopCommentInfoPos = mongoTemplate.find(query,ShopCommentInfoPo.class);
            }

        }
        log.info("查询mongodb数据结束，获取数据{}条商场数据，{}条店铺数据",null == mallCommentInfoPos? 0: mallCommentInfoPos.size(),null == shopCommentInfoPos? 0 : shopCommentInfoPos.size());
        return new Tuple(mallCommentInfoPos,shopCommentInfoPos);
    }

    /**
     * 补数据流程
     * @param type 类型
     * @param strStartDate 起始日期
     * @param strEndDate 结束日期
     * @return 处理的记录数
     */
    public int runMakeupData(Integer type, String strStartDate, String strEndDate) {
        int num = 0;
        try{
            Date start = DateUtil.asDate(LocalDate.parse(strStartDate,DateUtil.dateFormatter));
            Date end = DateUtil.asDate(LocalDate.parse(strEndDate,DateUtil.dateFormatter));
            if(start.after(end)){
                log.error("传参错误，起始日期:{}不应大于结束日期:{}",strStartDate,strEndDate);
                return num;
            }else{
                if(type.equals(1)){
                    num = dealCommentTag(null,type,start,end);
                }else{
                    num = dealCommentTag(null,type,start,end);
                }
            }

          }catch(Exception e){
            log.error("处理历史数据出错,错误信息：{}" ,e.getMessage());
        }
        return num;

    }
}
