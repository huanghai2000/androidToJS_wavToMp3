package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/**
 * @author 黄海
 * 
 */
public class Question implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String QuestionID;
	private String QuestionTitle; // 题目标题
	private String QuestionModel; // 题目模板 M1
	private String QuestionContent; // 题目内容
	private String Mp3Url;
	private String ImgUrl;
	private String QuestionAnswer; //新增题型模板D1答案存储图片位置
	private String ParentID; // 大题时，ParentID：Null；小题时，ParentID：大题ID
	private String ParentName; // 大题名字
	private String UnitID; // 单元ID
	private String Section; // Part （A、B、C、D…），Lesson（1、2、3、4…）
	private int Sort;
	private int QuestionTime;
	private String Difficulty;
	private String IsSplit;
	private int TaskQueSort; // 作业题目排序
	private int Round; // 作业题目次数
	private String WrongRate; // 本题错误率
	private ArrayList<Answer> AnswerList; // 选择题选项及答案列表
	private ArrayList<BlankAnswerInfo> BlankAnswer; // //填空题答案
	private StuAnswerInfo StuAnswer;
	private ArrayList<ReadRecord> ReadRecordList; // 学生跟读记录，跟读题未做完时读取此字段
	private boolean isLayoutClickable;// 多选题的时候判断控件是否能点击 张路
	private String type;// 判断是否点击 黄浩
	private String M2Score;// 学生分数 黄浩
	private String M2YzsUrl;// 云之声url 黄浩
	private String M2Time;// 每道小题的用时
	private String SecondContent;
	private int MinQueCount;
	private boolean isFinish; // 判断是否已完成 陈景坤设置
	private boolean isRecording;//是否正在录音
	private boolean isPlaying;//是否正在播放
	private String recordUrl;//录音保存地址
	
	
	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public String getRecordUrl() {
		return recordUrl;
	}

	public void setRecordUrl(String recordUrl) {
		this.recordUrl = recordUrl;
	}

	public int getIsRight() {
		return isRight;
	}

	public void setIsRight(int isRight) {
		this.isRight = isRight;
	}

	public boolean isRecording() {
		return isRecording;
	}

	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}

	public int getMinQueCount() {
		return MinQueCount;
	}

	public void setMinQueCount(int minQueCount) {
		MinQueCount = minQueCount;
	}

	public String getSecondContent() {
		return SecondContent;
	}

	public void setSecondContent(String secondContent) {
		SecondContent = secondContent;
	}

	public String getM2Time() {
		return M2Time;
	}

	public void setM2Time(String m2Time) {
		M2Time = m2Time;
	}

	public String getM2Score() {
		return M2Score;
	}

	public void setM2Score(String m2Score) {
		M2Score = m2Score;
	}

	public String getM2YzsUrl() {
		return M2YzsUrl;
	}

	public void setM2YzsUrl(String m2YzsUrl) {
		M2YzsUrl = m2YzsUrl;
	}

	/**
	 * SelectList:TODO（选择题选项及答案）
	 * 
	 * @since Ver 1.1
	 */
	private ArrayList<SelectList> SelectList;
	// --------------------------------------------------------------------------------
	private float score;// 此题分数
	private float onceTimeScore;// 评测类型的单词分数
	private float totalScore;// 累加加分数
	private float highScore;// 最高分
	private int doTimes;// 跟读次数
	private String highScoreUrl;// 云之声在线评测最高分录音地址
	private String currScoreUrl;// 云之声在线评测当次分录音地址
	private boolean isDone;// 是否做完
	private boolean isSave;// 是否提交到后台保存
	private int isRight;// 是否正确
	private boolean textColor;
	private boolean displayScore;// 显示分数
	private int curVoiceIndex;
	private ArrayList<Question> smallQuestions;

	public ArrayList<Question> getSmallQuestions() {
		return smallQuestions;
	}

	public void setSmallQuestions(ArrayList<Question> smallQuestions) {
		this.smallQuestions = smallQuestions;
	}

	public ArrayList<SelectList> getSelectList() {
		return SelectList;
	}

	public void setSelectList(ArrayList<SelectList> selectList) {
		SelectList = selectList;
	}

	public boolean isSave() {
		return isSave;
	}

	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	public int isRight() {
		return isRight;
	}

	public void setRight(int isRight) {
		this.isRight = isRight;
	}

	private float smallScore;
	private int Award;

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public boolean isQuestionWorkOK() {
		return isQuestionWorkOK;
	}

	public void setQuestionWorkOK(boolean isQuestionWorkOK) {
		this.isQuestionWorkOK = isQuestionWorkOK;
	}

	private boolean isQuestionWorkOK;

	public boolean add(Answer object) {
		return AnswerList.add(object);
	}

	public void add(int index, Answer object) {
		AnswerList.add(index, object);
	}

	public boolean containsAll(Collection<?> collection) {
		return AnswerList.containsAll(collection);
	}

	public boolean addAll(Collection<? extends Answer> collection) {
		return AnswerList.addAll(collection);
	}

	public boolean addAll(int index, Collection<? extends Answer> collection) {
		return AnswerList.addAll(index, collection);
	}

	public void clear() {
		AnswerList.clear();
	}

	public Object clone() {
		return AnswerList.clone();
	}

	public boolean removeAll(Collection<?> collection) {
		return AnswerList.removeAll(collection);
	}

	public void ensureCapacity(int minimumCapacity) {
		AnswerList.ensureCapacity(minimumCapacity);
	}

	public Answer get(int index) {
		return AnswerList.get(index);
	}

	public int size() {
		return AnswerList.size();
	}

	public boolean isEmpty() {
		return AnswerList.isEmpty();
	}

	public boolean contains(Object object) {
		return AnswerList.contains(object);
	}

	public boolean retainAll(Collection<?> collection) {
		return AnswerList.retainAll(collection);
	}

	public int indexOf(Object object) {
		return AnswerList.indexOf(object);
	}

	public int lastIndexOf(Object object) {
		return AnswerList.lastIndexOf(object);
	}

	public Answer remove(int index) {
		return AnswerList.remove(index);
	}

	public boolean remove(Object object) {
		return AnswerList.remove(object);
	}

	public Answer set(int index, Answer object) {
		return AnswerList.set(index, object);
	}

	public Object[] toArray() {
		return AnswerList.toArray();
	}

	public <T> T[] toArray(T[] contents) {
		return AnswerList.toArray(contents);
	}

	public void trimToSize() {
		AnswerList.trimToSize();
	}

	public Iterator<Answer> iterator() {
		return AnswerList.iterator();
	}

	public ListIterator<Answer> listIterator() {
		return AnswerList.listIterator();
	}

	public ListIterator<Answer> listIterator(int location) {
		return AnswerList.listIterator(location);
	}

	public int hashCode() {
		return AnswerList.hashCode();
	}

	public boolean equals(Object o) {
		return AnswerList.equals(o);
	}

	public List<Answer> subList(int start, int end) {
		return AnswerList.subList(start, end);
	}

	public Question() {
		super();
	}

	public int getAward() {
		return Award;
	}

	public boolean isLayoutClickable() {
		return isLayoutClickable;
	}

	public void setLayoutClickable(boolean isLayoutClickable) {
		this.isLayoutClickable = isLayoutClickable;
	}

	public void setAward(int award) {
		Award = award;
	}

	public float getSmallScore() {
		return smallScore;
	}

	public void setSmallScore(float smallScore) {
		this.smallScore = smallScore;
	}

	public void setHighScore(float highScore) {
		this.highScore = highScore;
	}

	public String getQuestionID() {
		return QuestionID;
	}

	public void setQuestionID(String questionID) {
		QuestionID = questionID;
	}

	public String getQuestionTitle() {
		return QuestionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		QuestionTitle = questionTitle;
	}

	public String getQuestionModel() {
		return QuestionModel;
	}

	public void setQuestionModel(String questionModel) {
		QuestionModel = questionModel;
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

	public String getQuestionAnswer() {
		return QuestionAnswer;
	}

	public void setQuestionAnswer(String questionAnswer) {
		QuestionAnswer = questionAnswer;
	}

	public String getParentID() {
		return ParentID;
	}

	public void setParentID(String parentID) {
		ParentID = parentID;
	}

	public String getParentName() {
		return ParentName;
	}

	public void setParentName(String parentName) {
		ParentName = parentName;
	}

	public String getUnitID() {
		return UnitID;
	}

	public void setUnitID(String unitID) {
		UnitID = unitID;
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

	public String getDifficulty() {
		return Difficulty;
	}

	public void setDifficulty(String difficulty) {
		Difficulty = difficulty;
	}

	public String getIsSplit() {
		return IsSplit;
	}

	public void setIsSplit(String isSplit) {
		IsSplit = isSplit;
	}

	public int getTaskQueSort() {
		return TaskQueSort;
	}

	public void setTaskQueSort(int taskQueSort) {
		TaskQueSort = taskQueSort;
	}

	public int getRound() {
		return Round;
	}

	public void setRound(int round) {
		Round = round;
	}

	public String getWrongRate() {
		return WrongRate;
	}

	public void setWrongRate(String wrongRate) {
		WrongRate = wrongRate;
	}

	public int getDoTimes() {
		return doTimes;
	}

	public void setDoTimes(int doTimes) {
		this.doTimes = doTimes;
	}

	public String getHighScoreUrl() {
		return highScoreUrl;
	}

	public void setHighScoreUrl(String highScoreUrl) {
		this.highScoreUrl = highScoreUrl;
	}

	public String getCurrScoreUrl() {
		return currScoreUrl;
	}

	public void setCurrScoreUrl(String currScoreUrl) {
		this.currScoreUrl = currScoreUrl;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public boolean isTextColor() {
		return textColor;
	}

	public void setTextColor(boolean textColor) {
		this.textColor = textColor;
	}

	public boolean isDisplayScore() {
		return displayScore;
	}

	public void setDisplayScore(boolean displayScore) {
		this.displayScore = displayScore;
	}

	public int getCurVoiceIndex() {
		return curVoiceIndex;
	}

	public void setCurVoiceIndex(int curVoiceIndex) {
		this.curVoiceIndex = curVoiceIndex;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getOnceTimeScore() {
		return onceTimeScore;
	}

	public void setOnceTimeScore(float onceTimeScore) {
		this.onceTimeScore = onceTimeScore;
	}

	public float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}

	public float getHighScore() {
		return highScore;
	}

	public boolean setHighScore(float highScore, String highScoreUrl) {
		if (highScore >= getHighScore()) {
			this.highScore = highScore;
			this.highScoreUrl = highScoreUrl;
			return true;
		}
		return false;
	}

	public List<Answer> getAnswerList() {
		return AnswerList;
	}

	public void setAnswerList(ArrayList<Answer> answerList) {
		AnswerList = answerList;
	}

	public ArrayList<BlankAnswerInfo> getBlankAnswer() {
		return BlankAnswer;
	}

	public void setBlankAnswer(ArrayList<BlankAnswerInfo> blankAnswer) {
		BlankAnswer = blankAnswer;
	}

	public StuAnswerInfo getStuAnswer() {
		return StuAnswer;
	}

	public void setStuAnswer(StuAnswerInfo stuAnswer) {
		StuAnswer = stuAnswer;
	}

	public List<ReadRecord> getReadRecordList() {
		return ReadRecordList;
	}

	public void setReadRecordList(ArrayList<ReadRecord> readRecordList) {
		ReadRecordList = readRecordList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Question(String questionID, String questionTitle,
			String questionModel, String questionContent, String mp3Url,
			String imgUrl, String questionAnswer, String parentID,
			String parentName, String unitID, String section, int sort,
			int questionTime, String difficulty, String isSplit,
			int taskQueSort, int round, String wrongRate,
			ArrayList<Answer> answerList,
			ArrayList<BlankAnswerInfo> blankAnswer, StuAnswerInfo stuAnswer,
			ArrayList<ReadRecord> readRecordList, boolean isLayoutClickable,
			ArrayList<Question.SelectList> selectList, float score,
			float onceTimeScore, float totalScore, float highScore,
			int doTimes, String highScoreUrl, String currScoreUrl,
			boolean isDone, boolean textColor, boolean displayScore,
			int curVoiceIndex, ArrayList<Question> smallQuestions, float smallScore,
			int award, boolean isQuestionWorkOK) {
		super();
		QuestionID = questionID;
		QuestionTitle = questionTitle;
		QuestionModel = questionModel;
		QuestionContent = questionContent;
		Mp3Url = mp3Url;
		ImgUrl = imgUrl;
		QuestionAnswer = questionAnswer;
		ParentID = parentID;
		ParentName = parentName;
		UnitID = unitID;
		Section = section;
		Sort = sort;
		QuestionTime = questionTime;
		Difficulty = difficulty;
		IsSplit = isSplit;
		TaskQueSort = taskQueSort;
		Round = round;
		WrongRate = wrongRate;
		AnswerList = answerList;
		BlankAnswer = blankAnswer;
		StuAnswer = stuAnswer;
		ReadRecordList = readRecordList;
		this.isLayoutClickable = isLayoutClickable;
		SelectList = selectList;
		this.score = score;
		this.onceTimeScore = onceTimeScore;
		this.totalScore = totalScore;
		this.highScore = highScore;
		this.doTimes = doTimes;
		this.highScoreUrl = highScoreUrl;
		this.currScoreUrl = currScoreUrl;
		this.isDone = isDone;
		this.textColor = textColor;
		this.displayScore = displayScore;
		this.curVoiceIndex = curVoiceIndex;
		this.smallQuestions = smallQuestions;
		this.smallScore = smallScore;
		Award = award;
		this.isQuestionWorkOK = isQuestionWorkOK;
	}

	@Override
	public String toString() {
		return "Question [QuestionID=" + QuestionID + ", QuestionTitle="
				+ QuestionTitle + ", QuestionModel=" + QuestionModel
				+ ", QuestionContent=" + QuestionContent + ", Mp3Url=" + Mp3Url
				+ ", ImgUrl=" + ImgUrl + ", QuestionAnswer=" + QuestionAnswer
				+ ", ParentID=" + ParentID + ", ParentName=" + ParentName
				+ ", UnitID=" + UnitID + ", Section=" + Section + ", Sort="
				+ Sort + ", QuestionTime=" + QuestionTime + ", Difficulty="
				+ Difficulty + ", IsSplit=" + IsSplit + ", TaskQueSort="
				+ TaskQueSort + ", Round=" + Round + ", WrongRate=" + WrongRate
				+ ", AnswerList=" + AnswerList + ", BlankAnswer=" + BlankAnswer
				+ ", StuAnswer=" + StuAnswer + ", ReadRecordList="
				+ ReadRecordList + ", isLayoutClickable=" + isLayoutClickable
				+ ", SelectList=" + SelectList + ", score=" + score
				+ ", onceTimeScore=" + onceTimeScore + ", totalScore="
				+ totalScore + ", highScore=" + highScore + ", doTimes="
				+ doTimes + ", highScoreUrl=" + highScoreUrl
				+ ", currScoreUrl=" + currScoreUrl + ", isDone=" + isDone
				+ ", textColor=" + textColor + ", displayScore=" + displayScore
				+ ", curVoiceIndex=" + curVoiceIndex + ", smallQuestions="
				+ smallQuestions + ", smallScore=" + smallScore + ", Award="
				+ Award + ", isQuestionWorkOK=" + isQuestionWorkOK + "]";
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 选择题选项及答案
	 * 
	 */
	public class Answer implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String QuestionID;
		private String SelectItem;
		private String ImgUrl;
		private String Mp3Url;
		private String Sort; // 每题的几个选项按此字段排序
		private String IsAnswer; // 标记选项是否为答案

		public Answer(String questionID, String selectItem, String imgUrl,
				String mp3Url, String sort, String isAnswer) {
			super();
			QuestionID = questionID;
			SelectItem = selectItem;
			ImgUrl = imgUrl;
			Mp3Url = mp3Url;
			Sort = sort;
			IsAnswer = isAnswer;
		}

		public String getQuestionID() {
			return QuestionID;
		}

		public void setQuestionID(String questionID) {
			QuestionID = questionID;
		}

		public String getSelectItem() {
			return SelectItem;
		}

		public void setSelectItem(String selectItem) {
			SelectItem = selectItem;
		}

		public String getImgUrl() {
			return ImgUrl;
		}

		public void setImgUrl(String imgUrl) {
			ImgUrl = imgUrl;
		}

		public String getMp3Url() {
			return Mp3Url;
		}

		public void setMp3Url(String mp3Url) {
			Mp3Url = mp3Url;
		}

		public String getSort() {
			return Sort;
		}

		public void setSort(String sort) {
			Sort = sort;
		}

		public String getIsAnswer() {
			return IsAnswer;
		}

		public void setIsAnswer(String isAnswer) {
			IsAnswer = isAnswer;
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * //填空题答案
	 * 
	 */
	public class BlankAnswerInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String QuestionID;
		private String Answer; // 答案文本
		private String AnswerType; // 答案类型：1-正确答案（通常），2-参考答案

		public BlankAnswerInfo(String questionID, String answer,
				String answerType) {
			super();
			QuestionID = questionID;
			Answer = answer;
			AnswerType = answerType;
		}

		public String getQuestionID() {
			return QuestionID;
		}

		public void setQuestionID(String questionID) {
			QuestionID = questionID;
		}

		public String getAnswer() {
			return Answer;
		}

		public void setAnswer(String answer) {
			Answer = answer;
		}

		public String getAnswerType() {
			return AnswerType;
		}

		public void setAnswerType(String answerType) {
			AnswerType = answerType;
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * 项目名称：SunnyTask 类名称：StuAnswerInfo 类描述： 保存非跟读题答案 创建人：zhanglu 创建时间：2016-4-7
	 * 下午7:42:33 修改人：zhanglu 修改时间：2016-4-7 下午7:42:33 修改备注：
	 * 
	 * @version
	 * 
	 */
	public static class StuAnswerInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String StudentID;// 学生id
		private String StuTaskID;
		private String ParentID; // M1时不为空，M2时为空
		private String QuestionID;// M1时为小题ID，M2时为大题
		private String SpendTime;// 用时，以分为单位
		private String Answer;// 学生答案 跟question sort一样
		private int IsRight;
		private String StuScore;
		private int Award;
		private String Remark;
		private String AnswerDate;
		private String AnswerSystem;// 跟读平台，ios、android
		private String isDone; //为1时没有做过，为0时有做过需要保存 






		public StuAnswerInfo() {
			super();
		}




		public String getIsDone() {
			return isDone;
		}




		public void setIsDone(String isDone) {
			this.isDone = isDone;
		}




		public String getStudentID() {
			return StudentID;
		}

		public void setStudentID(String studentID) {
			StudentID = studentID;
		}

		public String getStuTaskID() {
			return StuTaskID;
		}

		public void setStuTaskID(String stuTaskID) {
			StuTaskID = stuTaskID;
		}

		public String getParentID() {
			return ParentID;
		}

		public void setParentID(String parentID) {
			ParentID = parentID;
		}

		public String getQuestionID() {
			return QuestionID;
		}

		public void setQuestionID(String questionID) {
			QuestionID = questionID;
		}

		public String getSpendTime() {
			return SpendTime;
		}

		public void setSpendTime(String spendTime) {
			SpendTime = spendTime;
		}

		public String getAnswer() {
			return Answer;
		}

		public void setAnswer(String answer) {
			Answer = answer;
		}

		public int getIsRight() {
			return IsRight;
		}

		public void setIsRight(int isRight) {
			IsRight = isRight;
		}

		public String getStuScore() {
			return StuScore;
		}


		public void setStuScore(String stuScore) {
			StuScore = stuScore;
		}

		public int getAward() {
			return Award;
		}

		public void setAward(int award) {
			Award = award;
		}

		public String getRemark() {
			return Remark;
		}

		public void setRemark(String remark) {
			Remark = remark;
		}

		public String getAnswerSystem() {
			return AnswerSystem;
		}

		public void setAnswerSystem(String answerSystem) {
			AnswerSystem = answerSystem;
		}



		public String getAnswerDate() {
			return AnswerDate;
		}

		public void setAnswerDate(String answerDate) {
			AnswerDate = answerDate;
		}

		@Override
		public String toString() {
			return "StuAnswerInfo [StudentID=" + StudentID + ", StuTaskID="
					+ StuTaskID + ", ParentID=" + ParentID + ", QuestionID="
					+ QuestionID + ", SpendTime=" + SpendTime + ", Answer="
					+ Answer + ", IsRight=" + IsRight + ", StuScore="
					+ StuScore + ", Award=" + Award + ", Remark=" + Remark
					+ ", AnswerSystem=" + AnswerSystem + "]";
		}

		public StuAnswerInfo(String studentID, String stuTaskID,
				String parentID, String questionID, String spendTime,
				String answer, int isRight, String stuScore, int award,
				String remark,String answerDate, String answerSystem,String isDone) {
			super();
			StudentID = studentID;
			StuTaskID = stuTaskID;
			ParentID = parentID;
			QuestionID = questionID;
			SpendTime = spendTime;
			Answer = answer;
			IsRight = isRight;
			StuScore = stuScore;
			Award = award;
			Remark = remark;
			AnswerDate = answerDate;
			AnswerSystem = answerSystem;
			this.isDone = isDone;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------



	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * 项目名称：SunnyTask 类名称：SelectList 类描述： 多选题的选项 创建人：zhanglu 创建时间：2016-4-9
	 * 下午4:01:43 修改人：zhanglu 修改时间：2016-4-9 下午4:01:43 修改备注：
	 * 
	 * @version
	 * 
	 */
	public class SelectList implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * Sort:TODO（每题的几个选项按此字段排序）
		 * 
		 * @since Ver 1.1
		 */
		private int Sort;// 序号
		/**
		 * IsAnswer:TODO（标记选项是否为答案）
		 * 
		 * @since Ver 1.1
		 */
		private int IsAnswer;// 是否是答案
		private String QuestionID;// 题目ID
		private String SelectItem;// 选项内容
		private String ImgUrl;// 图片地址
		private String Mp3Url;// 声音地址
		private int Type;// 类型
		private int selectAnswer;// 用戶选中答案
		private boolean isSelect;// 是否选择

		public int getSelectAnswer() {
			return selectAnswer;
		}

		public void setSelectAnswer(int selectAnswer) {
			this.selectAnswer = selectAnswer;
		}

		public boolean isSelect() {
			return isSelect;
		}

		public void setSelect(boolean isSelect) {
			this.isSelect = isSelect;
		}

		public int getSort() {
			return Sort;
		}

		public void setSort(int sort) {
			Sort = sort;
		}

		public int getIsAnswer() {
			return IsAnswer;
		}

		public void setIsAnswer(int isAnswer) {
			IsAnswer = isAnswer;
		}

		public String getQuestionID() {
			return QuestionID;
		}

		public void setQuestionID(String questionID) {
			QuestionID = questionID;
		}

		public String getSelectItem() {
			return SelectItem;
		}

		public void setSelectItem(String selectItem) {
			SelectItem = selectItem;
		}

		public String getImgUrl() {
			return ImgUrl;
		}

		public void setImgUrl(String imgUrl) {
			ImgUrl = imgUrl;
		}

		public String getMp3Url() {
			return Mp3Url;
		}

		public void setMp3Url(String mp3Url) {
			Mp3Url = mp3Url;
		}

		public int getType() {
			return Type;
		}

		public void setType(int type) {
			Type = type;
		}

		public SelectList(int sort, int isAnswer, String questionID,
				String selectItem, String imgUrl, String mp3Url, int type) {
			super();
			Sort = sort;
			IsAnswer = isAnswer;
			QuestionID = questionID;
			SelectItem = selectItem;
			ImgUrl = imgUrl;
			Mp3Url = mp3Url;
			Type = type;
		}
	}


}
