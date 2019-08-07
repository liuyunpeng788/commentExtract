package com.fosun.data.cleanup.comment.tag.repository;

import com.fosun.data.cleanup.comment.tag.dto.po.db.BaiduBizClsPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: liumch
 * @create: 2019/7/17 18:16
 **/
@Repository
public interface BaiduBizClsRepository extends JpaRepository<BaiduBizClsPo,Integer> {
}