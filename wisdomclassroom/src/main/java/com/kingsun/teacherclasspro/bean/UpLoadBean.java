package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

public class UpLoadBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private  String  RequestID ;
	private boolean Success;
	private  String  ErrorMsg ;
	private  otherBean Data;

	public String getRequestID() {
		return RequestID;
	}

	public void setRequestID(String requestID) {
		RequestID = requestID;
	}

	public boolean isSuccess() {
		return Success;
	}

	public void setSuccess(boolean success) {
		Success = success;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}

	public otherBean getData() {
		return Data;
	}

	public void setData(otherBean data) {
		Data = data;
	}

	public class  otherBean {
		private  String  ID;
		private  String FileName;
		private  String FileExtension;
		private  String FileSize;
		private  String FileType;
		private  String FilePath;
		private String type;
		public String getID() {
			return ID;
		}
		public void setID(String iD) {
			ID = iD;
		}
		public String getFileName() {
			return FileName;
		}
		public void setFileName(String fileName) {
			FileName = fileName;
		}
		public String getFileExtension() {
			return FileExtension;
		}
		public void setFileExtension(String fileExtension) {
			FileExtension = fileExtension;
		}
		public String getFileSize() {
			return FileSize;
		}
		public void setFileSize(String fileSize) {
			FileSize = fileSize;
		}
		public String getFileType() {
			return FileType;
		}
		public void setFileType(String fileType) {
			FileType = fileType;
		}
		public String getFilePath() {
			return FilePath;
		}
		public void setFilePath(String filePath) {
			FilePath = filePath;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
}
