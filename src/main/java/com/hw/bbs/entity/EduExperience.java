package com.hw.bbs.entity;

public class EduExperience {
	private Long education_id;
	private Long uid;
	private Integer education_years;
	private String school_name;
	private String departments;
	private Long add_time;
	
	
	public Long getEducation_id() {
		return education_id;
	}
	public void setEducation_id(Long education_id) {
		this.education_id = education_id;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public Integer getEducation_years() {
		return education_years;
	}
	public void setEducation_years(Integer education_years) {
		this.education_years = education_years;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	public String getDepartments() {
		return departments;
	}
	public void setDepartments(String departments) {
		this.departments = departments;
	}
	public Long getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Long add_time) {
		this.add_time = add_time;
	}
}
