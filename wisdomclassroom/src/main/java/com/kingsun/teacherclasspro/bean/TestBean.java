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
	private  String name;
	private  String zimu;//字幕
	private  String type;//类型 fragment/sentence/word"（片段、句子、单词）
	private  int    wordIndex;//当前配音的顺序
	private  boolean isAgain;//是否是重录
	private  String mp3Url;//本地MP3路径
	private  String urlPath;//上传到云平台的地址
	private  boolean isUpload;//是否上传
	private  boolean isMarge;//录音文件合成是否成功
	private  int score;

	public String getCaptions() {
		return captions;
	}

	public void setCaptions(String captions) {
		this.captions = captions;
	}

	private String captions;
	private String resourceName;
	private String recordType;

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getCbId() {
		return cbId;
	}

	public void setCbId(String cbId) {
		this.cbId = cbId;
	}

	private  String cbId;

	public int getWordIndex() {
		return wordIndex;
	}

	public void setWordIndex(int wordIndex) {
		this.wordIndex = wordIndex;
	}

	public boolean isAgain() {
		return isAgain;
	}

	public void setAgain(boolean again) {
		isAgain = again;
	}

	public String getMp3Url() {
		return mp3Url;
	}

	public void setMp3Url(String mp3Url) {
		this.mp3Url = mp3Url;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public boolean isUpload() {
		return isUpload;
	}

	public void setUpload(boolean upload) {
		isUpload = upload;
	}

	public boolean isMarge() {
		return isMarge;
	}

	public void setMarge(boolean marge) {
		isMarge = marge;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZimu() {
		return zimu;
	}

	public void setZimu(String zimu) {
		this.zimu = zimu;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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
