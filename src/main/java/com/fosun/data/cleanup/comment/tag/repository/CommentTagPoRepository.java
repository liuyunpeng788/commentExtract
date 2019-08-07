package com.fosun.data.cleanup.comment.tag.repository;

import com.fosun.data.cleanup.comment.tag.dto.po.db.CommentTagPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: liumch
 * @create: 2019/7/17 15:49
 **/
@Repository
public interface CommentTagPoRepository extends JpaRepository<CommentTagPo,Long> {
    @Modifying
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ)
    @Query("delete from CommentTagPo s where s.subjectId = ?1 and s.subjectType = ?2 and s.commentType = ?3")
    int deleteBySubjectIdAndTypes(Long id, Short value, Short commentType);

    @Query("from CommentTagPo s where s.subjectId = ?1 and s.subjectType = ?2 and s.commentType = ?3")
    List<CommentTagPo> queryBySubjectIdAndTypes(Long subjectId, Short subjectType, Short commentType);
}
