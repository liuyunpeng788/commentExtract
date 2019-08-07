package com.fosun.data.cleanup.comment.tag.repository;

import com.fosun.data.cleanup.comment.tag.dto.po.db.ShopClassPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/3 18:30
 * @description: TODO
 * @modified: TODO
 * @version: 1.0
 **/
@Repository
public interface ShopClassRepository extends JpaRepository<ShopClassPo,Integer> {
    @Query("select s from ShopClassPo s where s.id in ?1")
    List<ShopClassPo> findAllByIds(Set<Integer> classIds);

    @Query("select s from ShopClassPo s where s.id  = ?1")
    ShopClassPo findByClsId(Integer slevelTypeId);
}
