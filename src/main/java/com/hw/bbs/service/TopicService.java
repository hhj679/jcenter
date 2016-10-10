package com.hw.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hw.bbs.dao.BaseDao;
import com.hw.bbs.dao.TopicDao;
import com.hw.bbs.entity.Topic;
import com.hw.bbs.entity.TopicFocus;

@Service
@Transactional
public class TopicService extends BaseService<Topic, Long> {
	
	@Autowired
    private TopicDao topicDao;

	@Override
	protected BaseDao<Topic, Long> getEntityDao() {
		// TODO Auto-generated method stub
		return topicDao;
	}
	
	public List<TopicFocus> getFocusTopicIdsByUid(Long uid) {
		return topicDao.getFocusTopicIdsByUid(uid);
	}
}
