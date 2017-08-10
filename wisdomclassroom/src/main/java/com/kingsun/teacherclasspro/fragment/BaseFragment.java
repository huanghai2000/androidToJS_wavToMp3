package com.kingsun.teacherclasspro.fragment;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment{

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 
	 * @param sp
	 * @return true表示是空 false不是空
	 */
	public  boolean isEmty(String sp){
		boolean result = false;
		if (sp == null || sp.equals("") || sp.equals("null")) {
			result = true;
		}
		return result;
	}

	//英语键盘字符
	//小写键盘
	public static ArrayList<String> otherList = new ArrayList<>(Arrays.asList(
			"a", "b", "c", "d", "e","f", "g", "h",
			"i", "j", "k", "l", "m", "n", "o", "p",
			"q", "r", "s", "t","u", "v", "w", "x", "y", "z",
			"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D",
			"F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M",
			"0", "1", "2", "3", "4","5", "6", "7","8","9"));

	//小写键盘
	public static ArrayList<String> KBLowlerArrayList = new ArrayList<>(Arrays.asList("q", "w", "e", "r", "t",
			"y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l","del","大写", "z",
			"x", "c", "v", "b", "n", "m","符号","确定"));
	//大写键盘
	public static ArrayList<String> KBCapitalData = new ArrayList<>(Arrays.asList("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D",
			"F", "G", "H", "J", "K", "L","del","小写", "Z", "X", "C", "V", "B", "N", "M","符号","确定"));
	//符号键盘
	public static ArrayList<String> KBSymbolArrayList = new ArrayList<>(Arrays.asList("0", "1", "2", "3","4","5", "6", "7", "8", "9", ".", ",",
			"…", "?", "!", ":", ";", "\"", "\'","del", " ","(", ")", "空格", "-","—", "/","字母","确定"," "));
}
