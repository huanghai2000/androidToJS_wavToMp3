package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

public class TestBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private  String content;
	private  String account;
	private  String password;
	private  String webUrl;
	private  String ClassID;

	public TestBean(String  cString){
		this.content = cString;
	}

	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getWebUrl() {
		return webUrl;
	}


	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}


	public String getClassID() {
		return ClassID;
	}


	public void setClassID(String classID) {
		ClassID = classID;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
