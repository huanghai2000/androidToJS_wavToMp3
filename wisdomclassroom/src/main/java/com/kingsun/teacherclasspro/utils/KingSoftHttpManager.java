package com.kingsun.teacherclasspro.utils;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.bean.KingSoftResultBean;

/**
 * get 请求返回Str Vollery进一步封装 1.添加回调 OnQueueComplete 监听请求状态
 * onStartQueue,onCompleteSu,onCompleteFail 
 * 2.StartQueue方法添加tag 便于请求之后根据不同接口做不同的判断处理 
 * 3.StopQueue方法 在activity stop或者destory的时候调用关闭数据请求
 * 
 * 添加Post请求方法 StartQueuePost();
 * 
 * @author huanghai
 * 
 */
public class KingSoftHttpManager {
	private String TAG = "KingSoftHttpManager";
	private Context mContext;
	private RequestQueue mQueue;
	private OnQueueComplete mOnQueueComplete;

	public KingSoftHttpManager(Context context) {
		try {
			mContext = context;
			mQueue = Volley.newRequestQueue(mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopQueue() {
		if (mQueue != null) {
			mQueue.cancelAll(this);
		}
	}

	public void setOnQueueCompleteListener(OnQueueComplete onQueueComplete) {
		mOnQueueComplete = onQueueComplete;
	}

	public void startQueue(String url, int tag) {
		mQueue.add(new StringRequest(url, new compliteListener(tag),
				new errorListen(tag)));
		mQueue.start();
		BaseActivity.Ilog(TAG,"================strart queue==================");
		BaseActivity.Ilog(TAG,"tag-->" + tag + "-->" + url);
		if (mOnQueueComplete != null) {
			mOnQueueComplete.onQueueStart(tag);
		}
	}
	/**
	 * post 请求，不能用于文件，用于post字段提交过去
	 * @param url
	 * @param map
	 * @param tag
	 */
	public void startQueuePost(String url, Map<String, String> map, int tag) {
		final Map<String, String> mapPost = map;
		String urls = "";
		urls = url;
		BaseActivity.Ilog(TAG,"========strart queue  post===============");
		BaseActivity.Ilog(TAG,"tag-->" + tag + "-->" + url);
		//		System.out.println("请求url -》 "+urls);
		Iterator iter = map.entrySet().iterator();
//		while (iter.hasNext()) {
//			@SuppressWarnings("rawtypes")
//			Map.Entry entry = (Map.Entry) iter.next();
//			String str = (String) entry.getValue();
//			String key = (String) entry.getKey();
//			//			System.out.println("key --> "+key+" ;value --> "+str);
//		}
		StringRequest postRequest = new StringRequest(Request.Method.POST, urls,
				new compliteListener(tag), new errorListen(tag)) {
			@Override
			protected Map<String, String> getParams() {
				return mapPost;
			}
		};
		mQueue.add(postRequest);
	}

	public interface OnQueueComplete {
		void onCompleteSu(KingSoftResultBean result, int tag);

		void onCompleteFail(VolleyError volleryError, int tag);

		void onQueueStart(int tag);
	}

	private class compliteListener implements Listener<String> {
		int tag;

		public compliteListener(int tag) {
			this.tag = tag;
		}

		@Override
		public void onResponse(String arg0) {
			BaseActivity.Ilog(TAG,
					"================queue complete ==================");
			BaseActivity.Ilog(TAG,"tag-->" + tag + "-->" + arg0);
			if (mOnQueueComplete != null) {
				KingSoftResultBean result = KingSoftParasJson.getResult(arg0);
				mOnQueueComplete.onCompleteSu(result, tag);
			}
		}
	}

	private class errorListen implements ErrorListener {
		int tag;

		public errorListen(int tag) {
			this.tag = tag;
		}

		@Override
		public void onErrorResponse(VolleyError arg0) {
			BaseActivity.Ilog(TAG,
					"================queue complete error==================");
			if (arg0.networkResponse != null) {
				BaseActivity.Ilog(TAG,"tag-->" + tag + "-->"
						+ arg0.networkResponse.statusCode);
			} else {
				BaseActivity.Ilog(TAG,"tag-->" + tag + "-->网络请求失败");
			}
			if (mOnQueueComplete != null) {
				mOnQueueComplete.onCompleteFail(arg0, tag);
			}
		}
	}
}