package com.kingsun.teacherclasspro.config;
/**
 * Created by 陈景坤on 2016-2-26.
 * 
 * @des 用于保存服务器端相关资源URL
 */
public class Config {
	// public static final String AppID = "TKS0213";//教师端
	public static final String AppID = "SKS0213";// 学生端
	public static final String IP = "http://yjx.kingsunedu.com/";// 外网地址2(正式站点)
	public static final String APIUrl =IP+"/SunnyTask/Api/"; //
	public static final String LoginByName = APIUrl + "Account/LoginByUserName";
	public static final String FeedBackUrl = APIUrl + "Account/SendFeedback";// 反馈URL
	public static final String ExtiUrl = APIUrl + "Account/Logout";// 退出登录URL
	public static final String ResetPsdUrl = APIUrl + "Account/ModifyPassword";// 重置密码URL
	public static final String HeadImageUploadUrl = IP+"/SunnyTask/FileUploadHandler.ashx";// 头像上传路径
	public static final String saveImageUrl = APIUrl + "Account/UploadAvatar";// 获取头像路径
	public static final String SendVerifyCodeUrl = APIUrl
			+ "Account/SendVerifyCode";// 发送验证码路径
	public static final String VerifyAndBindUrl = APIUrl
			+ "Account/VerifyAndBind";// 修改并绑定手机
	// ----------------------------------------------------------
	public static final String GetClassesByTeaID = APIUrl
			+ "TeacherTask/GetClassesByTeaID";// 获取班级列表并返回系统时间GetClassesByTeaID
	// 5.1
	public static final String GetAreaListByGPS = APIUrl
			+ "Account/GetAreaListByGPS";// 1.3 根据定位获取省市区学校
	public static final String GetCities = APIUrl + "Account/GetCities";// 1.4
	// 获取市GetCities
	public static final String GetDistricts = APIUrl + "Account/GetDistricts";// 1.5
	// 获取区GetDistricts
	public static final String GetSchools = APIUrl + "Account/GetSchools";// 1.6
	// 获取学校GetSchools
	public static final String LoginByTrueName = APIUrl
			+ "Account/LoginByTrueName";// 1.7 姓名登录LoginByTrueName
	public static final String SendSchoolMessage = APIUrl
			+ "Account/AndroidSendSchoolMessage";// 7.2 发送校讯通SendSchoolMessage
	public static final String GetSchoolMessages = APIUrl
			+ "Account/GetSchoolMessages";// 7.1 获取已发消息列表GetSchoolMessages
	public static final String GetClassTasks = APIUrl
			+ "TeacherTask/GetClassTasks";// 5.2 获取班级作业列表GetClassTasks
	public static final String CheckBeforeUndoTask = APIUrl
			+ "TeacherTask/CheckBeforeUndoTask";// 5.3
	// 撤销作业前校验CheckBeforeUndoTask
	public static final String ConfirmUndoTask = APIUrl
			+ "TeacherTask/ConfirmUndoTask";// 5.4 确认撤销作业ConfirmUndoTask
	public static final String GetTaskDetails = APIUrl
			+ "TeacherTask/GetTaskDetails";// 5.5 获取班级作业详情GetTaskDetails
	public static final String GetStuTaskReport = APIUrl
			+ "TeacherTask/GetStuTaskReport";// 5.6 获取学生作业报告GetStuTaskReport
	public static String APP_FOLDER = "/kingSun_sunnytask/";
	public static String MAIN_FOLDER_URL = "/kingSun_sunnytask/QuestionFiles/";
	public static String IMG_FOLDER = "/Img/";
	public static String MP3_FOLDER = "/Mp3/";
	// ----------------------------------------------------------

	// ----------------------------------------------------------听说作业学生端开始--------------------------------------

	public static String[] phone_nunber = new String[] { "134", "135", "136",
		"137", "138", "139", "150", "151", "152", "157", "158", "159",
		"182", "183", "184", "187", "188", "178", "147", "130", "131",
		"132", "155", "156", "185", "186", "176", "145", "133", "153",
		"180", "181", "189", "177", "170" };
	public static final String SaveStuAnswer = APIUrl
			+ "StudentTask/SaveStuAnswer";// 保存非跟读题答案SaveStuAnswer
	public static final String SubmitTask = APIUrl + "StudentTask/SubmitTask";// 提交作业SubmitTask
	public static final String StuGetStuTaskReport = APIUrl
			+ "StudentTask/GetStuTaskReport";// 获取学生作业报告GetStuTaskReport
	public static final String GetTeachersByStuID = APIUrl
			+ "Account/GetTeachersByStuID";// 4.5
	// 获取学生的教师信息GetTeachersByStuID（仅学生使用）
	public static final String SaveWrongTwo = APIUrl
			+ "StudentTask/AndroidSaveStuWrongQue"; // 错题重做-保存做题记录AndroidSaveStuWrongQue
	public static final String AndroidSaveStuSplitWrongQue = APIUrl
			+ "StudentTask/AndroidSaveStuSplitWrongQue"; // 错题重做-保存做题M18记录SaveStuSplitWrongQue
	public static final String GetWrongQuestions = APIUrl
			+ "TeacherTask/GetWrongQuestions"; // 获取高频错题GetWrongQuestions
	public static final String GetStuWrongQuestions = APIUrl
			+ "StudentTask/GetStuWrongQuestions"; // 获取学生作业错题信息GetStuWrongQuestions
	// ----------------------------------------------------------学生端

	public static final String ConfirmBind = APIUrl + "Account/ConfirmBind"; // 确认绑定ConfirmBind（仅教师可用）
	public static final String VerifyAndGetUser = APIUrl
			+ "Account/VerifyAndGetUser";// 核对验证码并获取用户信息VerifyAndGetUser
	public static final String ResetPassword = APIUrl + "Account/ResetPassword";// 重置密码ResetPassword
	public static final String GetUserByUserName = APIUrl
			+ "Account/GetUserByUserName";// 输入用户名，获取信息GetUserByUserName
	//	public static String AppUpdateUrl = "http://app.szjxst.com/AppLibrary/Query.ashx";// 获取更新地址
	public static String AppUpdateUrl = APIUrl+"Account/GetLatestAppVersion";// 获取更新地址
	public static final String GetUserByTrueName = APIUrl
			+ "Account/GetUserByTrueName";// 输入学校和姓名，获取信息GetUserByTrueName
	public static String UPDATEVERSIONAPPID =
			"ec3f9596-c471-42e5-bf7a-0ea25c4f9e6e";//外网更新app版本专用appid(PEP)
	/** 获取学生学期列表 **/
	public static String GetSemester = APIUrl + "StudentTask/GetSemester";

	/** 获取学习任务列表 **/
	public static String GetStuTasks = APIUrl + "StudentTask/GetStuTasks";

	/** 获取学生作业详情 **/
	public static String GetStuTaskDetails = APIUrl
			+ "StudentTask/GetStuTaskDetails";

	/** 保存跟读题答案8.4 **/
	public static String SaveReadRecord = APIUrl
			+ "StudentTask/AndroidSaveReadRecord";

	/** 保存错题作答记录 **/
	public static String SaveStuWrongQue = APIUrl
			+ "StudentTask/AndroidSaveStuWrongQue";

	/** 预习 **/
	public static String GetStuPreview = APIUrl + "StudentTask/GetStuPreview";

	public static String AndroidSaveStuAnswerList = APIUrl + "StudentTask/AndroidSaveStuAnswerList";//保存一屏多题作答记录SaveStuAnswerList
	public static String GetUnitsByBookID = APIUrl + "StudentTask/GetUnitsByBookID";//通过课本ID获取所有单元GetUnitsByBookID
	public static String GetUnitsByMOD = APIUrl + "TeacherTask/GetUnitsByMOD";//6.2 根据MOD版本获取单元列表GetUnitsByMOD
	public static String GetUnitsByWhere = APIUrl + "StudentTask/GetUnitsByWhere";//筛选年级和册别获取所有单元GetUnitsByWhere
	public static String GetQuestionInfo = APIUrl + "StudentTask/GetQuestionInfo";// 根据大题ID获取题目信息GetQuestionInfo
	public static String CorrectQuestion = APIUrl + "TeacherTask/CorrectQuestion";// 5.8 批改分数CorrectQuestion
	public static String AndroidUploadStuAnswerList = APIUrl + "StudentTask/AndroidUploadStuAnswerList";//一键上传学生答案UploadStuAnswerList
	public static String AndroidUploadAudios = APIUrl + "StudentTask/AndroidUploadAudios";//一键上传学生上传音频UploadAudios
	public static String ConfirmSubmitTask = APIUrl + "StudentTask/ConfirmSubmitTask";//确认提交作业ConfirmSubmitTask
	public static String GetUnitResourceList = APIUrl + "StudentTask/GetUnitResourceList";//获取单元资源列表GetUnitResourceList
	// ----------------------------------------------------------听说作业学生端结束--------------------------------------
}
