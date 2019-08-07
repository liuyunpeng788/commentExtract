package com.fosun.data.cleanup.comment.tag.repository;

import com.fosun.data.cleanup.comment.tag.dto.po.db.UserMallBindInfoPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author: liumch
 * @create: 2019/7/27 10:03
 **/
@Repository
public interface UserMallBindRepository extends JpaRepository<UserMallBindInfoPo,Long> {

    @Query("select s.mallId from UserMallBindInfoPo s ")
    Set<Long> queryBindMallIds();
}
