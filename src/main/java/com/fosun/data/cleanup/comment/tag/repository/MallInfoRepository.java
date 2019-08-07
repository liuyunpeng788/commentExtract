package com.fosun.data.cleanup.comment.tag.repository;

import com.fosun.data.cleanup.comment.tag.dto.po.db.MallInfoPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**  商场信息jpa接口
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/3/28 15:52
 * @description: 查询商场信息
 * @modified: TODO
 * @version: 1.0
 **/
@Repository
public interface MallInfoRepository extends JpaRepository<MallInfoPo,Long> {
    @Query("select count(s)  from MallInfoPo s where s.cityId = ?1 and s.createTime < ?2  and s.businessStatus = ?3")
    int countAllByCityIdAndTime(String cityId, Date datetime, Integer status);

    @Query("select s from MallInfoPo s where s.cityId = ?1 and s.createTime between ?2 and ?3 and s.businessStatus = ?4 order by s.createTime desc")
    List<MallInfoPo> findAllByCityId(String cityId, Date start, Date end, Integer status);

    @Query("select count(s) from MallInfoPo s where s.cityId = ?1 and s.businessStatus = ?2 ")
    int countAllByCityId(String cityId, Integer status);

    @Query("select max(s.updateTime) from MallInfoPo s where s.cityId = ?1 and s.businessStatus = ?2 ")
    Date findLatestUpdateTime(String cityId, Integer status);


    @Query(nativeQuery = true,value = "select A.* from  t_mall_info A " +
            " LEFT JOIN t_distance_mall_info B " +
            " ON A.city_id = ?2 AND business_status = ?3 AND A.create_time <?5 " +
            " AND B.target_mall_id = A.mall_id " +
            " WHERE B.source_mall_id = ?1  " +
            " AND  B.distance <= ?4 ")
    List<MallInfoPo> findAllByDistanceAndDate(Long mallId, String cityId, Integer value, BigDecimal dist, Date date);

    @Query(nativeQuery = true,value = "select A.* from  t_mall_info A " +
            " LEFT JOIN t_distance_mall_info B " +
            " ON A.city_id = ?2 AND business_status = ?3 AND A.create_time <?5 " +
            " AND B.target_mall_id = A.mall_id " +
            " WHERE B.source_mall_id = ?1  " +
            " AND  B.distance > ?4 ")
    List<MallInfoPo> findAllByBiggerDistanceAndDate(Long mallId, String cityId, Integer value, BigDecimal dist, Date date);

    //查询距离小于等于20KM 的所有商场信息
    @Query(nativeQuery = true,value = "SELECT A.*  FROM  t_mall_info A " +
            " LEFT JOIN t_distance_mall_info B " +
            " ON A.city_id = ?2 AND business_status = ?3  " +
            " AND B.target_mall_id = A.mall_id " +
            " WHERE B.source_mall_id = ?1  " +
            " AND B.distance<= ?4 ")
    List<MallInfoPo> findAllByDistance(Long mallId, String cityId, Integer value, BigDecimal dist);

    //查询距离大于20KM 的所有商场信息
    @Query(nativeQuery = true,value = "SELECT A.*  FROM  t_mall_info A " +
            " LEFT JOIN t_distance_mall_info B " +
            " ON A.city_id = ?2 AND business_status = ?3  " +
            " AND B.target_mall_id = A.mall_id " +
            " WHERE B.source_mall_id = ?1  " +
            " AND B.distance > ?4 ")
    List<MallInfoPo>  findAllByBiggerDistance(Long mallId, String cityId, Integer value, BigDecimal dist);

   @Query("select s from MallInfoPo s where s.mallName like  %?1%")
    List<MallInfoPo> findByMallNameLike(String search);

    @Query("select s from MallInfoPo s where s.mallId in ?2 and s.mallName like  %?1%")
    List<MallInfoPo> findByMallIdAndMallNameLike(String search, List<Long> mallIds);

    @Query("select s from MallInfoPo s where s.citySubId in ?1 and s.createTime between ?2 and ?3 and s.businessStatus = ?4")
    List<MallInfoPo> findAllBySubCityId(Set<String> subCityId, Date start, Date end, Integer value);

    @Query("select s.mallName from MallInfoPo s where s.mallId = ?1")
    String findMallNameByMallId(Long mallId);

    @Query("select s from MallInfoPo s where s.mallId in ?1")
    List<MallInfoPo> findAllByMallId(Set<Long> mallIds);
}
