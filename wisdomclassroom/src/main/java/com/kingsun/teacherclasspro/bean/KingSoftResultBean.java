package com.kingsun.teacherclasspro.bean;

public class KingSoftResultBean {
	public boolean Success;
	public String Data;
	public String Message;
	public boolean isSuccess() {
		return Success;
	}
	public void setSuccess(boolean success) {
		Success = success;
	}
	
	public String getData() {
		return Data;
	}
	public void setData(String data) {
		Data = data;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
}
