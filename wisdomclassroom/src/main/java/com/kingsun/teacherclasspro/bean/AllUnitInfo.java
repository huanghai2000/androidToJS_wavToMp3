package com.kingsun.teacherclasspro.bean;

import java.util.ArrayList;

public class AllUnitInfo {
	private int TopEditionID;
	private String TopEditionName;//父版本名称，如：人教PEP
	private ArrayList<GradeInfo>  GradeList;//�?��年级列表，默认�?中所选班级所在的年级
	private ArrayList<BookReelInfo>  BookReelList;//册别列表，（根据CurrentTerm）�?中当前学期册�?
	private String DefaultEditionID;  //默认选中的EditionList中的EditionID
	private ArrayList<EditionInfo>  EditionList;// //版本列表，�b：自主练习�?活动手册   
	private ArrayList<UnitInfo>  UnitList;//选中课本的单元列�?

	public String getDefaultEditionID() {
		return DefaultEditionID;
	}
	public void setDefaultEditionID(String defaultEditionID) {
		DefaultEditionID = defaultEditionID;
	}
	public class GradeInfo{//�?��年级列表，默认�?中所选班级所在的年级
		private String GradeID;
		private String GradeName;
		public GradeInfo(String gradeID2, String gradeName2) {
			this.GradeID = gradeID2;
			this.GradeName = gradeName2;
		}
		public String getGradeID() {
			return GradeID;
		}
		public void setGradeID(String gradeID) {
			GradeID = gradeID;
		}
		public String getGradeName() {
			return GradeName;
		}
		public void setGradeName(String gradeName) {
			GradeName = gradeName;
		}
	}

	public class BookReelInfo{ //册别，（根据CurrentTerm）�?中当前学期册�?
		private int BookReelID;
		private String BookReelName;

		public BookReelInfo(int bookReelID, String bookReelName) {
			super();
			BookReelID = bookReelID;
			BookReelName = bookReelName;
		}

		public int getBookReelID() {
			return BookReelID;
		}
		public void setBookReelID(int bookReelID) {
			BookReelID = bookReelID;
		}
		public String getBookReelName() {
			return BookReelName;
		}
		public void setBookReelName(String bookReelName) {
			BookReelName = bookReelName;
		}
	}

	public class EditionInfo{ //版本，�b：自主练习�?活动手册
		private int EditionID;
		private String EditionName;
		private int ParentID;
		private String MOD_ED;
		private int IsRemove;
		public int getEditionID() {
			return EditionID;
		}
		public void setEditionID(int editionID) {
			EditionID = editionID;
		}
		public String getEditionName() {
			return EditionName;
		}
		public void setEditionName(String editionName) {
			EditionName = editionName;
		}
		public int getParentID() {
			return ParentID;
		}
		public void setParentID(int parentID) {
			ParentID = parentID;
		}
		public String getMOD_ED() {
			return MOD_ED;
		}
		public void setMOD_ED(String mOD_ED) {
			MOD_ED = mOD_ED;
		}
		public int getIsRemove() {
			return IsRemove;
		}
		public void setIsRemove(int isRemove) {
			IsRemove = isRemove;
		}
	}
	public class UnitInfo{//选中课本的单�?
		private int UnitID;
		private String UnitName;
		private String KeyWord;//如：”Unit 1”�?”Unit 2”；显示时拼掤�eyWord+UnitName
		private String Sort;
		private ArrayList<QuestionInfo>  QuestionList;//单元题目列表（标粗为此�w可能要用的字段）
		public int getUnitID() {
			return UnitID;
		}
		public void setUnitID(int unitID) {
			UnitID = unitID;
		}
		public String getUnitName() {
			return UnitName;
		}
		public void setUnitName(String unitName) {
			UnitName = unitName;
		}
		public String getKeyWord() {
			return KeyWord;
		}
		public void setKeyWord(String keyWord) {
			KeyWord = keyWord;
		}
		public String getSort() {
			return Sort;
		}
		public void setSort(String sort) {
			Sort = sort;
		} 

		public ArrayList<QuestionInfo> getQuestionList() {
			return QuestionList;
		}
		public void setQuestionList(ArrayList<QuestionInfo> questionList) {
			QuestionList = questionList;
		}
	}
	public class QuestionInfo{
		private String QuestionID;
		private String QuestionNumber;
		private String QuestionTitle;
		private String QuestionContent;
		private String Mp3Url;
		private String ImgUrl;
		private String ParentID;
		private String QuestionAnswer;
		private String QuestionModel;//题目模板，M1和M2�?�a设定跟读次数
		private String QuestionTypeID;
		private String UnitID;    	    	
		private String Section;
		private int Sort;
		private int QuestionTime;//题目预计用时，分�?
		private int Difficulty;
		private String CreateDate;
		private String Remark;
		private int IsSplit;
		private String Resolve;

		private int QuesTimes = 1;
		private boolean isSelect = false;

		public boolean isSelect() {
			return isSelect;
		}
		public void setSelect(boolean isSelect) {
			this.isSelect = isSelect;
		}
		public int getQuesTimes() {
			return QuesTimes;
		}
		public void setQuesTimes(int quesTimes) {
			QuesTimes = quesTimes;
		}
		public String getUnitID() {
			return UnitID;
		}
		public void setUnitID(String unitID) {
			UnitID = unitID;
		}
		public String getQuestionID() {
			return QuestionID;
		}
		public void setQuestionID(String questionID) {
			QuestionID = questionID;
		}
		public String getQuestionNumber() {
			return QuestionNumber;
		}
		public void setQuestionNumber(String questionNumber) {
			QuestionNumber = questionNumber;
		}
		public String getQuestionTitle() {
			return QuestionTitle;
		}
		public void setQuestionTitle(String questionTitle) {
			QuestionTitle = questionTitle;
		}
		public String getQuestionContent() {
			return QuestionContent;
		}
		public void setQuestionContent(String questionContent) {
			QuestionContent = questionContent;
		}
		public String getMp3Url() {
			return Mp3Url;
		}
		public void setMp3Url(String mp3Url) {
			Mp3Url = mp3Url;
		}
		public String getImgUrl() {
			return ImgUrl;
		}
		public void setImgUrl(String imgUrl) {
			ImgUrl = imgUrl;
		}
		public String getParentID() {
			return ParentID;
		}
		public void setParentID(String parentID) {
			ParentID = parentID;
		}
		public String getQuestionAnswer() {
			return QuestionAnswer;
		}
		public void setQuestionAnswer(String questionAnswer) {
			QuestionAnswer = questionAnswer;
		}
		public String getQuestionModel() {
			return QuestionModel;
		}
		public void setQuestionModel(String questionModel) {
			QuestionModel = questionModel;
		}
		public String getQuestionTypeID() {
			return QuestionTypeID;
		}
		public void setQuestionTypeID(String questionTypeID) {
			QuestionTypeID = questionTypeID;
		}


		public String getSection() {
			return Section;
		}
		public void setSection(String section) {
			Section = section;
		}

		public int getSort() {
			return Sort;
		}
		public void setSort(int sort) {
			Sort = sort;
		}
		public int getQuestionTime() {
			return QuestionTime;
		}
		public void setQuestionTime(int questionTime) {
			QuestionTime = questionTime;
		}
		public int getDifficulty() {
			return Difficulty;
		}
		public void setDifficulty(int difficulty) {
			Difficulty = difficulty;
		}
		public String getCreateDate() {
			return CreateDate;
		}
		public void setCreateDate(String createDate) {
			CreateDate = createDate;
		}
		public String getRemark() {
			return Remark;
		}
		public void setRemark(String remark) {
			Remark = remark;
		}

		public int getIsSplit() {
			return IsSplit;
		}
		public void setIsSplit(int isSplit) {
			IsSplit = isSplit;
		}
		public String getResolve() {
			return Resolve;
		}
		public void setResolve(String resolve) {
			Resolve = resolve;
		}
		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub
			if(this.QuestionID.equals(((QuestionInfo)o).getQuestionID()))
				return true;
			return false;
		}

	}
	public int getTopEditionID() {
		return TopEditionID;
	}
	public void setTopEditionID(int topEditionID) {
		TopEditionID = topEditionID;
	}
	public String getTopEditionName() {
		return TopEditionName;
	}
	public void setTopEditionName(String topEditionName) {
		TopEditionName = topEditionName;
	}
	public ArrayList<GradeInfo> getGradeList() {
		return GradeList;
	}
	public void setGradeList(ArrayList<GradeInfo> gradeList) {
		GradeList = gradeList;
	}
	public ArrayList<BookReelInfo> getBookReelList() {
		return BookReelList;
	}
	public void setBookReelList(ArrayList<BookReelInfo> bookReelList) {
		BookReelList = bookReelList;
	}
	public ArrayList<EditionInfo> getEditionList() {
		return EditionList;
	}
	public void setEditionList(ArrayList<EditionInfo> editionList) {
		EditionList = editionList;
	}
	public ArrayList<UnitInfo> getUnitList() {
		return UnitList;
	}
	public void setUnitList(ArrayList<UnitInfo> unitList) {
		UnitList = unitList;
	}
	@Override
	public String toString() {
		return "AllUnitInfo [TopEditionID=" + TopEditionID
				+ ", TopEditionName=" + TopEditionName + ", GradeList="
				+ GradeList + ", BookReelList=" + BookReelList
				+ ", DefaultEditionID=" + DefaultEditionID + ", EditionList="
				+ EditionList + ", UnitList=" + UnitList + "]";
	}



}
