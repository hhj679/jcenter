package com.hw.bbs.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hw.bbs.entity.Topic;
import com.hw.bbs.entity.TopicFocus;

@Repository
public interface TopicDao extends BaseDao<Topic, Long>{
	
	@Query("select f from TopicFocus f where f.uid=:uid")
	List<TopicFocus> getFocusTopicIdsByUid(@Param("uid") Long uid);
}
