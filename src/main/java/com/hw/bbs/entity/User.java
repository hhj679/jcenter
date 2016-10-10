package com.hw.bbs.entity;

import java.util.List;


public class User {

    private Long uid;
    private String account;
    private String realName;
    private Integer sex;
    private String province;
    private String city;
    private String signature;
    private String job;
    private Long jobId;
    private String url_token;
    private Long url_token_update;
    private String default_timezone;
    private String qq;
    private String mobile;
    private String common_email;
    private String homepage;
    private String user_name;
    //private List<EduExperienceMapper> eduExperience;
    //private List<WorkExperienceMapper> workExperience;

    public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getUrl_token() {
		return url_token;
	}

	public void setUrl_token(String url_token) {
		this.url_token = url_token;
	}

	public Long getUrl_token_update() {
		return url_token_update;
	}

	public void setUrl_token_update(Long url_token_update) {
		this.url_token_update = url_token_update;
	}

	public String getDefault_timezone() {
		return default_timezone;
	}

	public void setDefault_timezone(String default_timezone) {
		this.default_timezone = default_timezone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getCommon_email() {
		return common_email;
	}

	public void setCommon_email(String common_email) {
		this.common_email = common_email;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

//	public List<EduExperienceMapper> getEduExperience() {
//		return eduExperience;
//	}
//
//	public void setEduExperience(List<EduExperienceMapper> eduExperience) {
//		this.eduExperience = eduExperience;
//	}
//
//	public List<WorkExperienceMapper> getWorkExperience() {
//		return workExperience;
//	}
//
//	public void setWorkExperience(List<WorkExperienceMapper> workExperience) {
//		this.workExperience = workExperience;
//	}

	public Long getUid() {
        return uid;
    }

    public void setUid(Long id) {
        this.uid = id;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String userName) {
        this.user_name = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

}
