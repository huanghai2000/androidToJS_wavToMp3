package com.kingsun.teacherclasspro.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.myjson.Gson;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.bean.KingSoftResultBean;

public class KingSoftParasJson {
	private static String TAG = "KingSoftParasJson";
	public static KingSoftResultBean getResult(String result) {
 		KingSoftResultBean resultBean = new KingSoftResultBean();
		try {
			JSONObject obj = new JSONObject(result);
			resultBean.Success = obj.getBoolean("Success");
			if (obj.getString("Message") == null || obj.getString("Message").equals("")) {
				resultBean.Message = "";
			}else {
				resultBean.Message = obj.getString("Message");
			}
			resultBean.Data = obj.getString("Data");
//			resultBean.page_total = obj.getInt("page_total");
		} catch (Exception e) {
			BaseActivity.Elog(TAG,"====================== Paras LYResult ======================");
			BaseActivity.Elog(TAG,e.toString());
		}
		return resultBean;
	}
	


	public static <T> Object getObjectByJson(String pData, Class<T> pClass) {
		Object msg = new Object();
		try {
			Gson gson = new Gson();
//			BaseActivity.Elog(TAG,"pData ----   "+pData);
			msg = gson.fromJson(pData, pClass);
		} catch (Exception e) {
			BaseActivity.Elog(TAG,"====================== Paras obj ======================");
			BaseActivity.Elog(TAG,e.toString());
			msg = null;
		}
		return msg;
	}
	public static String[] getArrByJson(String pData) {
		String[] arrTemp =null;
		try {
			JSONArray jsonarray = new JSONArray(pData);
			arrTemp = new String[jsonarray.length()];
			for (int i = 0; i < jsonarray.length(); i++) {
				String data = jsonarray.getString(i);
				arrTemp[i] = data;
			}
		} catch (Exception e) {
			BaseActivity.Elog(TAG,"====================== Paras List ======================");
			BaseActivity.Elog(TAG,e.toString());
		}
		return arrTemp;
	}
	
	@SuppressWarnings("unchecked")
	public static <T, E> ArrayList<E> getListByJson(String pData,
			Class<T> pClass) {
		ArrayList<E> msg = new ArrayList<E>();
		try {
//			JSONObject obj = new JSONObject(pData);
//			String info = obj.getString("Date");
//			BaseActivity.Elog(TAG,"pData => "+pData);
			JSONArray jsonarray = new JSONArray(pData);
//			BaseActivity.Elog(TAG,"jsonarray.leng ==> "+jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				String data = jsonarray.getString(i);
				Object temp = getObjectByJson(data, pClass);
				msg.add((E) temp);
			}
		} catch (Exception e) {
			BaseActivity.Elog(TAG,"====================== Paras List ======================");
			BaseActivity.Elog(TAG,e.toString());
			msg = null;
		}
		return msg;
	}
}
