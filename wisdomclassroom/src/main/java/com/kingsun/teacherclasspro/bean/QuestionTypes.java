package com.kingsun.teacherclasspro.bean;

import java.util.ArrayList;
import java.util.Arrays;



public enum QuestionTypes {

	M1,//Follow me. 跟读单词
	M2, //Follow me.跟读课文
	M3,
	M4,
	M5,
	M6,
	M7,
	M8,
	M9,
	M10,
	M11,
	M12,
	M13,
	M14,
	M15,
	M16,
	M17,
	M18,
	M19, 
	D1,
	
	
//数学
	//下面数学模板跟英语的一样
		S1,
		S2,
		S3,
		S4,
		S5,
	    S6,
	    S7,
	    S8,
		S9,
		S10,
		S11,
		S12,
	    S13,
	    S14,
	    S15,
		S16,
		S17,
		S18,
		S19,
    //新模板
	//写一写
	S23,
	//填一填
	S24,
	//算一算
	S25,
	
	
    
    //语文
	//下面数学模板跟英语的一样
	Y1,
	Y2,
	Y3,
	Y4,
	Y5,
    Y6,
    Y7,
    Y8,
	Y9,
	Y10,
	Y11,
	Y12,
    Y13,
    Y14,
    Y15,
	Y16,
	Y17,
	Y18,
	Y19,
	//新模板
	//写一写
	Y20,
	//我会读
	Y21,
	//我会背
	Y22;
	
	public static QuestionTypes getType(String type){
		return valueOf(type);
	}
	public static String[] getBigQuestion( ){
		return bigQuestion;
	}
	public static String[] bigQuestion ={"M2","S2","Y2","M5","M8","S8","Y8","M9","S9","Y9","M10","S10","Y10","M11",
		"S11","Y12","M16","S16","Y16","M17","S17","Y17","M18","S18","Y18","M19","S19","Y19","Y21","Y22","Y23"};

	
	
	//英语键盘字符
	
	//小写键盘
	public static ArrayList<String> KBLowlerArrayList = new ArrayList<>(Arrays.asList("q", "w", "e", "r", "t",
			"y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l","delete","大写", "z",
			"x", "c", "v", "b", "n", "m","符号","确定"));
	//大写键盘
	public static ArrayList<String> KBCapitalData = new ArrayList<>(Arrays.asList("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D",
			"F", "G", "H", "J", "K", "L","delete","小写", "Z", "X", "C", "V", "B", "N", "M","符号","确定"));
	//符号键盘
	public static ArrayList<String> KBSymbolArrayList = new ArrayList<>(Arrays.asList("0", "1", "2", "3","4","5", "6", "7", "8", "9", ".", ",",
			"…", "?", "!", ":", ";", "\"", "\'","delete", " ","(", ")", "空格", "-","—", "/","字母","确定"," "));
	
	//数学键盘字符
	
	public static ArrayList<String> KBNumberArrayList = new ArrayList<>(Arrays.asList( "0","1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "＋", "－", "×", "÷", "＞", "＜", "＝", ":","delete", "(", ")", "…", "%", "≈", "x²", "x³", "°","空格","确定"));
	
	//大题保存大题信息的题型
	
	public static ArrayList<String> SaveBigQueList = new ArrayList<>(Arrays.asList("Y21","Y22"));
	
}