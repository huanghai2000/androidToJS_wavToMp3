package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

public class UPBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String FilePath;
	private String FileID;
	private String ResourceStyle;
	private String UserName;
	public String getFilePath() {
		return FilePath;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	public String getFileID() {
		return FileID;
	}
	public void setFileID(String fileID) {
		FileID = fileID;
	}
	public String getResourceStyle() {
		return ResourceStyle;
	}
	public void setResourceStyle(String resourceStyle) {
		ResourceStyle = resourceStyle;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
}
