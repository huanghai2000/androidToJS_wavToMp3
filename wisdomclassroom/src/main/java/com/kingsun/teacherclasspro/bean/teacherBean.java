package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class teacherBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String content;

	private String result;

	private ArrayList<TestBean> answerList;

	private boolean isRead;

	public ArrayList<TestBean> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(ArrayList<TestBean> answerList) {
		this.answerList = answerList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}



}
