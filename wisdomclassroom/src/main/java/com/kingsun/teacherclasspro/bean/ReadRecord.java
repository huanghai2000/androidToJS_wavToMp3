package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;


/**
 * @author kings
 * 跟读记录
 */
public class ReadRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String ReadRecordID;
	private String StuID;
	private String StuTaskID;
	private String QuestionID;
	private String ParentID;
	private String StuAnswer;
	private String StuScore;
	private String SpendTime;
	private int Round;
	private String ReadDate;
	private String ReadSystem;
	private int isSave;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsSave() {
		return isSave;
	}

	public void setIsSave(int isSave) {
		this.isSave = isSave;
	}

	public String getReadRecordID() {
		return ReadRecordID;
	}

	public void setReadRecordID(String readRecordID) {
		ReadRecordID = readRecordID;
	}

	public String getStuID() {
		return StuID;
	}

	public void setStuID(String stuID) {
		StuID = stuID;
	}

	public String getStuTaskID() {
		return StuTaskID;
	}

	public void setStuTaskID(String stuTaskID) {
		StuTaskID = stuTaskID;
	}

	public String getQuestionID() {
		return QuestionID;
	}

	public void setQuestionID(String questionID) {
		QuestionID = questionID;
	}

	public String getParentID() {
		return ParentID;
	}

	public void setParentID(String parentID) {
		ParentID = parentID;
	}

	public String getStuAnswer() {
		return StuAnswer;
	}

	public void setStuAnswer(String stuAnswer) {
		StuAnswer = stuAnswer;
	}

	public String getStuScore() {
		return StuScore;
	}

	public void setStuScore(String stuScore) {
		StuScore = stuScore;
	}

	public String getSpendTime() {
		return SpendTime;
	}

	public void setSpendTime(String spendTime) {
		SpendTime = spendTime;
	}

	public int getRound() {
		return Round;
	}

	public void setRound(int round) {
		Round = round;
	}

	public String getReadDate() {
		return ReadDate;
	}

	public void setReadDate(String readDate) {
		ReadDate = readDate;
	}

	public String getReadSystem() {
		return ReadSystem;
	}

	public void setReadSystem(String readSystem) {
		ReadSystem = readSystem;
	}

	@Override
	public String toString() {
		return "ReadRecord [ReadRecordID=" + ReadRecordID + ", StuID="
				+ StuID + ", StuTaskID=" + StuTaskID + ", QuestionID="
				+ QuestionID + ", ParentID=" + ParentID + ", StuAnswer="
				+ StuAnswer + ", StuScore=" + StuScore + ", SpendTime="
				+ SpendTime + ", Round=" + Round + ", ReadDate=" + ReadDate
				+ ", ReadSystem=" + ReadSystem + "]";
	}

	public ReadRecord(String readRecordID, String stuID, String stuTaskID,
			String questionID, String parentID, String stuAnswer,
			String stuScore, String spendTime, int round, String readDate,
			String readSystem) {
		super();
		ReadRecordID = readRecordID;
		StuID = stuID;
		StuTaskID = stuTaskID;
		QuestionID = questionID;
		ParentID = parentID;
		StuAnswer = stuAnswer;
		StuScore = stuScore;
		SpendTime = spendTime;
		Round = round;
		ReadDate = readDate;
		ReadSystem = readSystem;
	}

	public ReadRecord(String readRecordID, String stuID, String stuTaskID,
			String questionID, String parentID, String stuAnswer,
			int stuScore, int spendTime, int round, String readDate,
			String readSystem) {
		super();
		ReadRecordID = readRecordID;
		StuID = stuID;
		StuTaskID = stuTaskID;
		QuestionID = questionID;
		ParentID = parentID;
		StuAnswer = stuAnswer;
		StuScore = String.valueOf(stuScore);
		SpendTime = String.valueOf(spendTime);
		Round = round;
		ReadDate = readDate;
		ReadSystem = readSystem;
	}

	public ReadRecord(int id, String readRecordID, String stuID,
			String stuTaskID, String questionID, String parentID,
			String stuAnswer, String stuScore, String spendTime, int round,
			String readDate, String readSystem, int isSave) {
		super();
		this.id = id;
		ReadRecordID = readRecordID;
		StuID = stuID;
		StuTaskID = stuTaskID;
		QuestionID = questionID;
		ParentID = parentID;
		StuAnswer = stuAnswer;
		StuScore = stuScore;
		SpendTime = spendTime;
		Round = round;
		ReadDate = readDate;
		ReadSystem = readSystem;
		this.isSave = isSave;
	}

	public ReadRecord(int id, String readRecordID, String stuID,
			String stuTaskID, String questionID, String parentID,
			String stuAnswer, int stuScore, int spendTime, int round,
			String readDate, String readSystem, int isSave) {
		super();
		this.id = id;
		ReadRecordID = readRecordID;
		StuID = stuID;
		StuTaskID = stuTaskID;
		QuestionID = questionID;
		ParentID = parentID;
		StuAnswer = stuAnswer;
		StuScore = String.valueOf(stuScore);
		SpendTime = String.valueOf(spendTime);
		Round = round;
		ReadDate = readDate;
		ReadSystem = readSystem;
		this.isSave = isSave;
	}

	public ReadRecord() {
		super();
	}
}