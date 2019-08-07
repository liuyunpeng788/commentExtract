package com.fosun.data.cleanup.comment.tag.repository;

import com.fosun.data.cleanup.comment.tag.dto.po.db.MallFocusPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/1 20:40
 * @description: TODO
 * @modified: TODO
 * @version: 1.0
 **/
@Repository
public interface MallFocusRepository extends JpaRepository<MallFocusPo,Long> {
    @Query("select s from MallFocusPo s where s.srcMallId = ?1 and s.status = 1")
    List<MallFocusPo> findAllBySrcMallId(Long mallId);

    @Query("select s.mallId from MallFocusPo s where s.srcMallId = ?1 and s.status = 1")
    List<Long> findAllFocusMallIdBySrcMallId(Long srcMallId);

    @Query("select s.mallId from MallFocusPo s where s.status = 1")
    List<Long> findAllFocusMallIds(Long srcMallId);
}
