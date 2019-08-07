package com.fosun.data.cleanup.comment.tag.repository;

import com.fosun.data.cleanup.comment.tag.dto.po.db.ShopInfoPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/2 16:38
 * @description: TODO
 * @modified: TODO
 * @version: 1.0
 **/
@Repository("JpaShopInfoRepository")
public interface ShopInfoRepository extends JpaRepository<ShopInfoPo,Long> {
    @Query("select s from ShopInfoPo s where s.mallId in ?1 and s.createTime< ?2 and s.shopStatus = ?3 ")
    List<ShopInfoPo> findAllByMallIdAndEndTime(Set<Long> mallIds, Date end, Integer value);

    @Query("select count(s) from ShopInfoPo s where s.mallId in ?1 and  s.shopStatus = ?2 and s.createTime < ?3")
    Integer countByMallId(Set<Long> mallIds, Integer value, Date date);

    @Query("select s from ShopInfoPo s where s.mallId in ?1  and s.createTime <?2 and  s.shopStatus = ?3 and s.customerFlevelTypeId is not null and s.customerSlevelTypeId is not null")
    List<ShopInfoPo> findAllByMallId(Set<Long> mallIds, Date date, Integer value);

    @Query("select s from ShopInfoPo s where s.mallId in ?1  and s.customerFlevelTypeId = ?2  and s.createTime < ?3 and  s.shopStatus = ?4")
    List<ShopInfoPo> findAllByMallIdAndClassIdAndTime(Set<Long> mallIds, Long firstClassId, Date date, Integer value);

    @Query("select s from ShopInfoPo s where s.mallId in ?1  and s.customerFlevelTypeId = ?2  and  s.shopStatus = ?3 and s.createTime < ?4")
    List<ShopInfoPo> findAllByMallIdAndClassId(Set<Long> mallIds, Long firstClassId, Integer value, Date date);

    @Query("select s from ShopInfoPo s where s.mallId in ?1  and s.customerFlevelTypeId = ?2  and s.customerSlevelTypeId = ?3   and  s.shopStatus = ?4  and s.createTime < ?4")
    List<ShopInfoPo> findAllByMallIdAndMultiClassId(Set<Long> mallIds, Long firstClassId, Long secondClassId, Integer value, Date date);

    @Query("select max(s.updateTime) from ShopInfoPo s ")
    Date findLatestUpdateTime();

    @Query("select s.shopId from ShopInfoPo s where s.mallId = ?1 and s.shopStatus = ?2 and s.customerFlevelTypeId = ?3")
    Set<Long> findAllShopIdByMallIdAndClassId(Long mallId, Integer value, Long bizClassId);

    @Query("select s.shopId from ShopInfoPo s where s.mallId = ?1 and s.shopStatus = ?2")
    Set<Long> findAllShopIdByMallId(Long mallId, Integer value);

    @Query("select s from ShopInfoPo s where s.mallId = ?1 and s.shopStatus = ?2 ")
    List<ShopInfoPo> findAllShopInfoByMallId(Long mallId, Integer value);

    @Query("select s from ShopInfoPo s where s.createTime between ?2 and ?3 and s.mallId in ?1 and s.shopStatus = ?4  and s.customerFlevelTypeId is not null and s.customerSlevelTypeId is not null")
    List<ShopInfoPo> findAllByMallId(Set<Long> mallIds, Date start, Date end, Integer value);

    @Query("select s from ShopInfoPo s where s.shopId in ?1 ")
    List<ShopInfoPo> findAllByShopIds(Set<Long> shopIds);

    @Query( nativeQuery = true, value ="SELECT brand_id,customer_flevel_type_id,COUNT(*) as cnt FROM t_shop_info where brand_id > 0  and mall_id in ?1 and shop_status = ?2 GROUP BY brand_id ORDER BY cnt DESC limit ?3 ")
    List<Object[]> findAllByMallIdAndStatus(Set<Long> targetMallIds, Integer value, Integer topK);

    @Query(nativeQuery =  true,value = "SELECT * from t_shop_info where shop_id = ?1 limit 1")
    ShopInfoPo findByShopId(Long shopId);
}
