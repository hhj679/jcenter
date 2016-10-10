package com.hw.bbs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hw.bbs.entity.TopicFocus;
import com.hw.bbs.service.TopicService;

@RestController
@RequestMapping(value="/topic")
public class TopicController {
	
	@Autowired
	private TopicService topicService;

	@RequestMapping(value="/focus/{uid}", method=RequestMethod.GET)
    public List<TopicFocus> index(@PathVariable Long uid) {
		return topicService.getFocusTopicIdsByUid(uid);
    }
}
