package com.hw.bbs.entity;

public class WorkExperience {
	private Long work_id;
	private Long uid;
	private Integer start_year;
	private Integer end_year;
	private String company_name;
	private Long job_id;
	private Long add_time;
	
	
	public Long getWork_id() {
		return work_id;
	}
	public void setWork_id(Long work_id) {
		this.work_id = work_id;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public Integer getStart_year() {
		return start_year;
	}
	public void setStart_year(Integer start_year) {
		this.start_year = start_year;
	}
	public Integer getEnd_year() {
		return end_year;
	}
	public void setEnd_year(Integer end_year) {
		this.end_year = end_year;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public Long getJob_id() {
		return job_id;
	}
	public void setJob_id(Long job_id) {
		this.job_id = job_id;
	}
	public Long getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Long add_time) {
		this.add_time = add_time;
	}
}
