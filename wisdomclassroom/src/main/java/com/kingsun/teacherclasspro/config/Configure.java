package com.kingsun.teacherclasspro.config;

import java.io.File;

import com.kingsun.teacherclasspro.utils.FileUtils;

public class Configure {
	public final static String ipAddress = "http://zrpd.kingsun.cn/";// 账户相关的请求地址（外网正式）
	//	 public final static String ipAddress = "http://119.145.5.77:8108/";// 账户相关的请求地址（内网测试）
	public final static String ipAddress5 = "http://192.168.3.187:1112/";// （ 陈中意电脑）
	public final static String ipAddress6 = "http://192.168.3.89:8044/";// （ 陈中意电脑）
	public final static String ipAddress3 = "http://183.47.42.221:4322/";// 宾老师电脑机子
	public final static String ipAddress2 = "http://183.47.42.221:4321/";// 宾老师电脑机子
	public final static String ipAddressTest = "http://192.168.7.148:8000/";// 测试
	public final static String ipAddress4 ="http://192.168.3.2:4322/";//测试机
	public final static String domeIndexPage = ipAddress2+"login.html";// 账户相关web请求名称
	public final static String queryAccount = "Account.ashx";// 账户相关web请求名称
	public final static String queryContent = "Query.ashx";// 资源相关web请求名称
	public final static String queryPay = "Pay.ashx";// 支付相关web请求名称
	public final static String queryState = "UserState.ashx?unum=";// 验证用户在线状态相关web请求名称

	// 自动登陆成功后返回 true , 挤下线 、密码错误、账号禁用、登陆过期等返回false 并清空缓存数据
	public static boolean isLoginType = false;

	// 拼接后的URL
	public final static String urlAccount = ipAddress + queryAccount;
	public final static String urlContent = ipAddress + queryContent;
	public final static String urlPay = ipAddress + queryPay;
	public final static String urlState = ipAddress + queryState;

	public final static String H5Frame = "H5Frame";
	public final static String zip = ".zip";

	// 请求方法
	public final static String QueryCourseData = "QueryCourseData";// 请求课本最新下载地址信息
	// app 存在sd 中的文件夹名
	public final static String NaturallySpell = "SmartClass";
	public final static String AssetRoot = "file:///android_asset/";
	public final static String AssetWebPage = AssetRoot+"webpage/";
	public final static String indexPage = Configure.folder_Res+"webpage"+ File.separator+"dome.html";
	public final static String domePage = "file://"+indexPage;

	public final static String folder_Root = FileUtils.getDir();// 本应用的根路径
	public final static String folder_Res = Configure.folder_Root + "Res" + File.separator;// 本应用的资源存放路径
	public final static String folder_Temp = Configure.folder_Root + "Temp" + File.separator;// 本应用的临时资源（如下载等）存放路径
	public final static String folder_Source_Detele = folder_Res + H5Frame + File.separator + "source" + File.separator;
	public final static String folder_Source = folder_Res + H5Frame + File.separator;// 更新多媒体资源解压路径
	public final static String CRASH_BUG_LOG = "KingSunSmart/Logs";// 异常退出时bug日志记录
	public final static String FOLDER_RECORD = folder_Res + "record" + File.separator;// 录音文件保存路径

	// 是否开启H5 网页调试 ,打开过后外部的都可以对js 进行调试
	public static boolean isH5WebContentsDebuggingEnabled = true;

	// 是否开启免下载开关，需要配合HTML_INDEX 使用
	public static boolean downloadOff = false;
	// 框架默认打开的主页地址
	//	 public final static String HTML_INDEX="http://192.168.3.115:8082/index.html";// 宾老师机子
	//	public final static String HTML_INDEX="http://www.baidu.com";// 宾老师机子
	//	 public final static String HTML_INDEX="file:///storage/emulated/0/.NaturallySpell/Res/H5Frame/source/1/P_1/U_1/A/11AS3.html";
	public final static String HTML_INDEX = "file://" + Configure.folder_Res + Configure.H5Frame + File.separator + "dome.html";
	public final static String HTML_FILE = "file://" + Configure.folder_Res + Configure.H5Frame + File.separator;
	public final static String HTML_LIST = "file://" + Configure.folder_Res + Configure.H5Frame + File.separator + "list.html";
	public final static String HTML_SETTING = "file://" + Configure.folder_Res + Configure.H5Frame + File.separator + "setting.html";
	public final static String HTML_LOGIN = "file://" + Configure.folder_Res + Configure.H5Frame + File.separator + "login.html";
	public final static String HTML_404 = "file://" + Configure.folder_Res + Configure.H5Frame + File.separator + "404.html";

	// 微信APPID
	public final static String AppId_WX = "wxbbf5d6430bacdefa";

	// 资源下载包是否即时删除
	public final static boolean isDeletDownloadZipFile = false;// 默认不删除，如果需要以后打开
	// 免登陆时长，这里设置指的是具体多少天，以天为单位，精确到秒
	public final static int LOGIN_OUT_TIME = 7;// 天

	// 心跳广播 发送时间
	public final static int interval = 60;// 心跳包发送间隔，单位为秒s
	// 心跳 广播注册名称
	public final static String ACTION_RELOGIN = "com.king.naturallyspell.relogin";
	public final static String ACTION_WRONGPSW = "com.king.naturallySpell.wrongpsw";
	public final static String ACTION_NOUSER = "com.king.naturallySpell.nouser";

	// 与框架协商好，使用source/表示在做题的页面逻辑中
	public final static String HTML_SOURCE = "source" + File.separator;

	//	public static String gradeID = "";// 课本id分 低年级和高年级

	// SharedPreferences name 一般性，普通
	public final static String preNaturallySpell = "preNaturallySpell";

	// 是否覆盖解压 SharedPreferences name
	public final static String H5_CDR_SharedName = "H5_CDR_SharedName";


	// 支付系统请求重发 支付单号
	public final static String pay_OrderId = "OrderID";
	// 支付系统请求重发 支付方式
	public final static String pay_ErrCode = "pay_ErrCode";
	// 支付系统请求重发 支付课本
	public final static String pay_GradeID = "pay_GradeID";




	// 统一使用的保存名戳
	public final static String userID = "userID";
	public final static String userName = "userName";
	public final static String userPassword = "userPassword";
	public final static String userNum = "userNum";
	public final static String userNickName = "userNickName";
	public final static String userLoginTime = "userLoginTime";
	public final static String firstLoginTime = "firstLoginTime";
	public final static String byEditionID = "byEditionID";
	public final static String userStatus = "userStatus";
	public final static String registerDate = "registerDate";
	public final static String userCourses = "userCourses";
	public final static String isVipBefore = "isVipBefore";
	public final static String userGrade = "userGrade";
	public final static String userEmail = "userEmail";
	public final static String useInfo = "useInfo";
	public final static String courseName = "courseName";
	public final static String editionName = "editionName";
	public final static String userSchool = "userSchool";
	public final static String userClass = "userClass";
	public final static String userPhone = "userPhone";
	public final static String useremail = "useremail";
	public final static String long_out_time = "long_out_time";
	public final static String CourseID = "CourseID";
	public final static String H5_ZIP_OK = "H5_ZIP_OK";// 框架是否已经解压成功过
	public final static String H5_CDR_ZIP_OK = "H5_CDR_ZIP_OK";// 资源是否已经解压成功过
	public final static String userinfoBase64 = "userinfoBase64";// 用户信息
}
