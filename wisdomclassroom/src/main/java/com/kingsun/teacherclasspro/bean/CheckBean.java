package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

public class CheckBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private  otherBean result;
	
	public otherBean getResult() {
		return result;
	}


	public void setResult(otherBean result) {
		this.result = result;
	}


	public class otherBean {
		private int  overall;
		public int getOverall() {
			return overall;
		}
		public void setOverall(int overall) {
			this.overall = overall;
		}
	}
}
