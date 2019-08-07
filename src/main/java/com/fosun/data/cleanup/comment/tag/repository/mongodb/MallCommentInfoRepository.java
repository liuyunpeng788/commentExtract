package com.fosun.data.cleanup.comment.tag.repository.mongodb;

import com.fosun.data.cleanup.comment.tag.dto.po.mongodb.MallCommentInfoPo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/9 16:53
 * @description: TODO
 * @modified: TODO
 * @version: 1.0
 **/
@Repository
public interface MallCommentInfoRepository extends MongoRepository<MallCommentInfoPo,Long> {

}
