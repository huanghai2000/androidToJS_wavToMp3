package com.kingsun.teacherclasspro.utils;

/**
 * Created by hai.huang on 2018/3/14.
 * 常用数据配置
 */

public class AppConfig {
    public static final String AppID = "SKS1916";// 智慧教室，SKS2000表示智慧教室，SKS1916表示新开发测试APP

    public static final String AppIP = "http://192.168.40.70:1213";
//    public static final String AppIP = "http://192.168.3.2:1030";//

    public static final String API =AppIP+"/api/";//

    public static final String BEIKEIP = "http://192.168.3.2";//备课地址不能加端口

    //备课地址
    public static final String BEIKEURL =BEIKEIP+":1031/PreLesson/Page/UserStandBook.aspx?token=";

    public static final String AppLogin = AppIP+"/api/getUserByLogin";//登录接口

    public static final String AppGetBookInfo = AppIP+"/api/getselBookInfo";//

    public static final String AppDownList = AppIP+"/api/GetDownList/";//下载列表

    public static final String AppGetBookList = AppIP+"/api/getBookList";//下载列表

    public static final String MathineUrl_Nomarl = "http://183.47.42.218:1036/AttLesson/Page/SelectBook.aspx";//4.4机器人测试地址

    public static final String MathineUrl =  "http://183.47.42.218:1036/AttLesson/Page/Robot.aspx";
}
