package com.kingsun.teacherclasspro.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;

import com.kingsun.teacherclasspro.bean.AllUnitInfo.QuestionInfo;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.bean.QuestionTypes;

/**
 * @author 黄海
 * 
 */
// 排序工具
public class QuestionUtil {

	public static String[] spContent = {"A","B","C","D","E","F","G","H","I"};
	// 筛选每个单元的Part字段
	public ArrayList<String> filterTypeBySelectionAndSort(ArrayList<QuestionInfo> arrayList) {
		ArrayList<String> questionType = new ArrayList<>();
		for (int i = 0; i < arrayList.size(); i++) {
			if (i == 0) {
				questionType.add(arrayList.get(i).getSection());
			}else if (SortByPartInUnit(questionType, arrayList.get(i).getSection())) {
				questionType.add(arrayList.get(i).getSection());
			}
		}
		return questionType;
	}

	private boolean SortByPartInUnit(ArrayList<String> questionType, String type) {
		for (int i = 0; i < questionType.size(); i++) {
			if (questionType.get(i).equals(type)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据 unitID 、PartType、sort字段排序
	 * 
	 * @param unitID
	 *            单元ID
	 * @param questionList
	 *            所有作业列表
	 * @param PartType
	 *            作业part类型
	 * @return
	 */
	public ArrayList<QuestionInfo> filterQueByUnitAndSelectionAndSort(int munitID, ArrayList<QuestionInfo> questionList, String PartType) {
		String unitID = String.valueOf(munitID);
		ArrayList<QuestionInfo> quetionForSort = new ArrayList<>();
		ArrayList<QuestionInfo> quetionTemList = new ArrayList<>();
		for (int i = 0; i < questionList.size(); i++) {
			if (questionList.get(i).getUnitID().equals(unitID)) {
				if (questionList.get(i).getSection().equals(PartType)) {
					quetionTemList.add(questionList.get(i));
				}
			}
		}
		quetionForSort.addAll(quetionTemList);
		return quetionForSort;
	}

	// 根据sort字段排序
	private ArrayList<QuestionInfo> SortQuesBySort(ArrayList<QuestionInfo> quetionTemList) {
		ArrayList<QuestionInfo> quetionForSort = new ArrayList<>();
		for (int i = 0; i < quetionTemList.size(); i++) {
			QuestionInfo sortByMin;
			for (int j = i + 1; j < quetionTemList.size() - 1; j++) {
				if (quetionTemList.get(i).getSort() < quetionTemList.get(j).getSort()) {
					sortByMin = quetionTemList.get(i);
				} else {
					sortByMin = quetionTemList.get(j);
					i = j;
				}
				quetionTemList.add(sortByMin);
			}
		}
		return quetionForSort;
	}

	// 拼接TaskTitle
	public String appendAllSelectUnit(String currentTime, ArrayList<String> selectUnitStrings) {
		StringBuilder sb = new StringBuilder();
		sb.append(currentTime);
		sb.append(" ");
		for (int i = 0; i < selectUnitStrings.size(); i++) {
			sb.append(selectUnitStrings.get(i));
			sb.append(",");
		}
		sb.delete(sb.length() - 1, sb.length());
		Log.e("AssignWorkActivity", "拼接的单元字符串" + sb.toString());
		return sb.toString();
	}


	/**
	 * 题目排序
	 * 
	 * @param questionList
	 * @return
	 */
	public ArrayList<Question> SortQuestion(ArrayList<Question> questionList) {
		ArrayList<Question> SortquestionInfos = new ArrayList<Question>();
		ArrayList<Question> SortParentquestionInfos = new ArrayList<Question>();
		ArrayList<Question> temParentquestionInfos = new ArrayList<Question>();
		ArrayList<Question> temChildquestionInfos = new ArrayList<Question>();
		ArrayList<Question> realquestionInfos = new ArrayList<Question>();

		// 查找大题和小题
		for (int i = 0; i < questionList.size(); i++) {
			// 大题时，ParentID：Null；小题时，ParentID：大题ID
			if (StringUtils.isEmpty(questionList.get(i).getParentID())) {
				temParentquestionInfos.add(questionList.get(i));
			} else {
				temChildquestionInfos.add(questionList.get(i));
			}
		}

		// 大题排序
		SortParentquestionInfos = SortParent(temParentquestionInfos);

		// 构造每种真正的题目
		for (int i = 0; i < temChildquestionInfos.size(); i++) {
			for (int j = 0; j < SortParentquestionInfos.size(); j++) {
				if (temChildquestionInfos.get(i).getParentID().equals(SortParentquestionInfos.get(j).getQuestionID())) {
					temChildquestionInfos.get(i).setParentName(SortParentquestionInfos.get(j).getQuestionTitle());
					temChildquestionInfos.get(i).setTaskQueSort(SortParentquestionInfos.get(j).getTaskQueSort());
					realquestionInfos.add(temChildquestionInfos.get(i));
				}
			}
		}
		// 小题排序
		SortquestionInfos = SortChildQuestion(SortParentquestionInfos, realquestionInfos);
		return SortquestionInfos;
	}

	private ArrayList<Question> SortChildQuestion(ArrayList<Question> sortParentquestionInfos, ArrayList<Question> realquestionInfos) {
		ArrayList<Question> SortquestionInfos = new ArrayList<Question>();
		for (int i = 0; i < sortParentquestionInfos.size(); i++) {
			ArrayList<Question> ChildForParentQues = new ArrayList<Question>();
			if (isBigQuestion(sortParentquestionInfos.get(i).getQuestionModel())) {  //装小题
				for (int j = 0; j < realquestionInfos.size(); j++) {
					if (sortParentquestionInfos.get(i).getQuestionID().equals(realquestionInfos.get(j).getParentID())) {				
						ChildForParentQues.add(realquestionInfos.get(j));
					}
				}
				ArrayList<Question> SortChildList = SortChildQuesInParrent(ChildForParentQues);
				sortParentquestionInfos.get(i).setSmallQuestions(SortChildList);
				SortquestionInfos.add(sortParentquestionInfos.get(i));
			}else {
				//装大题
				for (int j = 0; j < realquestionInfos.size(); j++) {
					if (sortParentquestionInfos.get(i).getQuestionID().equals(realquestionInfos.get(j).getParentID())) {	
						if (    ("S12".equals(realquestionInfos.get(j).getQuestionModel())
								||"S13".equals(realquestionInfos.get(j).getQuestionModel())    //产品经理要求这样
								||"S15".equals(realquestionInfos.get(j).getQuestionModel())
								||"S23".equals(realquestionInfos.get(j).getQuestionModel()))) { 
							if (StringUtils.isEmpty(realquestionInfos.get(j).getQuestionContent())) {
								realquestionInfos.get(j).setQuestionContent(sortParentquestionInfos.get(i).getQuestionContent());
							}else {
								realquestionInfos.get(j).setQuestionContent(sortParentquestionInfos.get(i).getQuestionContent().trim()
										+"<br/>"+realquestionInfos.get(j).getQuestionContent());
							}
						}
						SortquestionInfos.add(realquestionInfos.get(j));				
					}
				}
			}
		}
		return SortquestionInfos;
	}

	private boolean isBigQuestion(String questionModel) {
		String[]bigQuestion = QuestionTypes.getBigQuestion();
		for (int i = 0; i < bigQuestion.length; i++) {
			if (bigQuestion[i].equals(questionModel)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据sort字段排序 (选择排序法)
	 * 
	 * @param childForParentQues
	 * @return
	 */
	private ArrayList<Question> SortChildQuesInParrent(ArrayList<Question> childForParentQues) {
		for (int i = 0; i < childForParentQues.size() - 1; i++) {
			int TempMin = i;
			for (int j = i + 1; j < childForParentQues.size(); j++) {
				if (childForParentQues.get(i).getSort() > childForParentQues.get(j).getSort()) {
					TempMin = j;
					if (TempMin != i) {
						Question temQuestion;
						temQuestion = childForParentQues.get(i);
						childForParentQues.set(i, childForParentQues.get(TempMin));
						childForParentQues.set(TempMin, temQuestion);
					}
				}
			}
		}
		//计算小题分数;
		float score = 100/childForParentQues.size();
		for (int i = 0; i < childForParentQues.size(); i++) {
			childForParentQues.get(i).setSmallScore(score);
		}
		return childForParentQues;
	}

	/**
	 * @param temParentquestionInfos
	 * @return 大题排序(选择排序法)
	 */
	private ArrayList<Question> SortParent(ArrayList<Question> temParentquestionInfos) {
		for (int i = 0; i < temParentquestionInfos.size(); i++) {
			for (int j = i; j < temParentquestionInfos.size(); j++) {
				if (temParentquestionInfos.get(i).getTaskQueSort() > temParentquestionInfos.get(j).getTaskQueSort()) {
					Question TemBean = temParentquestionInfos.get(i);
					temParentquestionInfos.set(i, temParentquestionInfos.get(j));
					temParentquestionInfos.set(j, TemBean);
				}
			}
		}
		return temParentquestionInfos;
	}

	/**
	 * 根据quetionId 查找 questionI
	 * 
	 * @param questionInfo
	 * @param questionInfos
	 * @return
	 */
	public QuestionInfo findQuesFromList(QuestionInfo questionInfo, ArrayList<QuestionInfo> questionInfos) {
		for (int i = 0; i < questionInfos.size(); i++) {
			if (questionInfo.getQuestionID().equals(questionInfos.get(i).getQuestionID())) {
				return questionInfos.get(i);
			}
		}
		return null;
	}

	// 禁止连续点击按钮
	private static long lastClickTime;

	public static boolean isFastDoubleClick(long period) {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < period) {
			Log.e("ButtonUtil", "连续点击按钮间隔时间太短");
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static String transfationTime(int secound) {
		StringBuilder sb =  new StringBuilder();

		if(secound/(60)!=0) {  //超过一分钟
			if (secound%(60)==0) {
				sb.append(secound/60+"分钟");
			}else {
				sb.append(1+secound/60+"分钟");
			}
			return sb.toString();
		}else if(secound%(60)==0){   //0秒情况
			sb.append(0+"分钟");
			return sb.toString();
		}else if(secound%(60)!=0) {//0至1分钟情况
			sb.append(1+"分钟");
			return sb.toString();
		}
		return null;
	}

	public static CharSequence textHandle(final Context context,String text) {
		ImageGetter imageGetter = new ImageGetter()  
		{  
			@Override  
			public Drawable getDrawable(String source)  
			{  
				int id = Integer.parseInt(source);  
				Drawable d = context.getResources().getDrawable(id);  
				d.setBounds(0, 0, d.getIntrinsicWidth(), d .getIntrinsicHeight());  
				return d;  
			}  
		}; 
		CharSequence charSequence = Html.fromHtml(text,imageGetter,null); 
		return charSequence;
	}	

	public static String setQuestionScore(String questionScore) {
		float mQuestionScore = Float.parseFloat(questionScore);
		int mmQueString  = Math.round(mQuestionScore);
		String mmmQueString = String.valueOf(mmQueString);
		return mmmQueString;
	}

	public static String getChoose(int  index){
		return spContent[index];
	}
}