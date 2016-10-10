package com.hw.bbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Table(name = "bbs_topic_focus")
@Entity
public class TopicFocus extends BaseEntity<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "focus_id")
	private String focusId;
	
	@Column(name = "topic_id")
	private Long topicId;
	
	
	@Column(name = "uid")
	private Long uid;
	
	@Column(name = "add_time")
	private Integer addTime;
}
