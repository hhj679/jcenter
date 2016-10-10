package com.hw.bbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bbs_topic")
@Entity
public class Topic extends BaseEntity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	@Id
//    @GeneratedValue(generator = "idGenerator")
//    @GenericGenerator(name = "idGenerator", strategy = "native")
//	@Transient
//    private Long id;
	
	@Id
	@Column(name = "topic_id")
	private Long topicId;
	
	@Column(name = "topic_title")
	private String topicTitle;
	
	@Column(name = "add_time")
	private Long addTime;
	
	@Column(name = "discuss_count")
	private Integer discussCount;
	
	@Column(name = "topic_description")
	private String topicDescription;
	
	@Column(name = "topic_pic")
	private String topicPic;
	
	@Column(name = "topic_lock")
	private Integer topicLock;
	
//	@Column(name = "topic_top")
//	private Integer topicTop;
	
	@Column(name = "focus_count")
	private Integer focusCount;
	
	@Column(name = "user_related")
	private Integer userRelated;
	
	@Column(name = "url_token")
	private String urlToken;
	
	@Column(name = "merged_id")
	private Integer mergedId;
	
	@Column(name = "seo_title")
	private String seoTitle;
	
	@Column(name = "parent_id")
	private Integer parentId;
	
	@Column(name = "is_parent")
	private Integer isParent;
	
	@Column(name = "discuss_count_last_week")
	private Long discussCountLastWeek;
	
	@Column(name = "discuss_count_last_month")
	private Long discussCountLastMonth;
	
	@Column(name = "discuss_count_update")
	private Long discussCountUpdate;
	
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public String getTopicTitle() {
		return topicTitle;
	}
	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}
	public Long getAddTime() {
		return addTime;
	}
	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}
	public Integer getDiscussCount() {
		return discussCount;
	}
	public void setDiscussCount(Integer discussCount) {
		this.discussCount = discussCount;
	}
	public String getTopicDescription() {
		return topicDescription;
	}
	public void setTopicDescription(String topicDescription) {
		this.topicDescription = topicDescription;
	}
	public String getTopicPic() {
		return topicPic;
	}
	public void setTopicPic(String topicPic) {
		this.topicPic = topicPic;
	}
	public Integer getTopicLock() {
		return topicLock;
	}
	public void setTopicLock(Integer topicLock) {
		this.topicLock = topicLock;
	}
//	public Integer getTopicTop() {
//		return topicTop;
//	}
//	public void setTopicTop(Integer topicTop) {
//		this.topicTop = topicTop;
//	}
	public Integer getFocusCount() {
		return focusCount;
	}
	public void setFocusCount(Integer focusCount) {
		this.focusCount = focusCount;
	}
	public Integer getUserRelated() {
		return userRelated;
	}
	public void setUserRelated(Integer userRelated) {
		this.userRelated = userRelated;
	}
	public String getUrlToken() {
		return urlToken;
	}
	public void setUrlToken(String urlToken) {
		this.urlToken = urlToken;
	}
	public Integer getMergedId() {
		return mergedId;
	}
	public void setMergedId(Integer mergedId) {
		this.mergedId = mergedId;
	}
	public String getSeoTitle() {
		return seoTitle;
	}
	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getIsParent() {
		return isParent;
	}
	public void setIsParent(Integer isParent) {
		this.isParent = isParent;
	}
	public Long getDiscussCountLastWeek() {
		return discussCountLastWeek;
	}
	public void setDiscussCountLastWeek(Long discussCountLastWeek) {
		this.discussCountLastWeek = discussCountLastWeek;
	}
	public Long getDiscussCountLastMonth() {
		return discussCountLastMonth;
	}
	public void setDiscussCountLastMonth(Long discussCountLastMonth) {
		this.discussCountLastMonth = discussCountLastMonth;
	}
	public Long getDiscussCountUpdate() {
		return discussCountUpdate;
	}
	public void setDiscussCountUpdate(Long discussCountUpdate) {
		this.discussCountUpdate = discussCountUpdate;
	}
}
