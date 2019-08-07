package com.fosun.data.cleanup.comment.tag.repository;

import com.fosun.data.cleanup.comment.tag.dto.po.db.CommentFocusPo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/10 14:26
 * @description: TODO
 * @modified: TODO
 * @version: 1.0
 **/
@Repository
public interface CommentFocusRepository extends JpaRepository<CommentFocusPo, Long> {

    @Query("select s from CommentFocusPo s where s.subjectType = ?1 and s.subjectId = ?2 and s.commentType = ?3 order by s.num desc ")
    List<CommentFocusPo> findAllByCommentType(Short subjectType, Long id, Short commentType, PageRequest request);

    /**
     * 查找评论关注点
     *
     * @param subjectType 数据类型
     * @param type        评论类型
     * @param id          subjectId
     * @return List
     */
    @Query(value = "SELECT distinct(s.keyword) FROM t_consumer_comment_focus s WHERE  s.subject_type = ?1 AND " +
            "s.comment_type = ?2 AND subject_id = ?3 ORDER BY s.num DESC LIMIT 10", nativeQuery = true)
    List<String> findKeywordBySubject(Short subjectType, Short type, Long id);
}


