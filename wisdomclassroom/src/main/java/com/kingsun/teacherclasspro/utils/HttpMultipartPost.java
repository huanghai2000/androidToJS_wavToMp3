package com.kingsun.teacherclasspro.utils;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kingsun.teacherclasspro.utils.CustomMultipartEntity.ProgressListener;

public class HttpMultipartPost extends AsyncTask<String, Integer, String> {

	private Context context;
	private String filePath;
//	private ProgressDialog pd;
	private long totalSize;
	private Handler handler;

	public HttpMultipartPost(Context context, String filePath,Handler handler) {
		this.context = context;
		this.filePath = filePath;
		this.handler = handler;
	}

	@Override
	protected void onPreExecute() {
		//显示dialog
	}

	@Override
	protected String doInBackground(String... params) {
		String serverResponse = null;
		Log.i("MainActivity", "params: " + params[0]);
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(params[0]);
		try {
			CustomMultipartEntity multipartContent = new CustomMultipartEntity(
					new ProgressListener() {
						@Override
						public void transferred(long num) {
							publishProgress((int) ((num / (float) totalSize) * 100));
						}
					});
			
//			CustomMultipartEntity multipartContent = new CustomMultipartEntity(
//					HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName("gbk"),
//					new ProgressListener() {
//						@Override
//						public void transferred(long num) {
//							publishProgress((int) ((num / (float) totalSize) * 100));
//						}
//					});

			// We use FileBody to transfer an image
			multipartContent.addPart("file", new FileBody(new File(filePath)));
			totalSize = multipartContent.getContentLength();

			// Send it
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			serverResponse = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serverResponse;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
//		pd.setProgress((int) (progress[0]));
		//更新进度条
		Message msg = new Message();
		msg.what = 4;
		msg.arg2 = (int) (progress[0]);
		handler.sendMessage(msg);
	}

	@Override
	protected void onPostExecute(String result) {
//		Log.i("MainActivity", "result: " + result);
//		pd.dismiss();
		Message msg = new Message();
		msg.what = 2;
		msg.obj = result;
		handler.sendMessage(msg);
	}

	@Override
	protected void onCancelled() {
		handler.sendEmptyMessage(3);
	}
}
