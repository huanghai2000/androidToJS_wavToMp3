package com.kingsun.teacherclasspro.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.adpter.MyFragmentPagerAdapter;
import com.kingsun.teacherclasspro.bean.KingSoftResultBean;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.config.Config;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.dialog.S_DialogUtil;
import com.kingsun.teacherclasspro.fragment.M10_Fragment;
import com.kingsun.teacherclasspro.fragment.M12_Fragment;
import com.kingsun.teacherclasspro.fragment.M18_Fragment;
import com.kingsun.teacherclasspro.fragment.M1_Fragment;
import com.kingsun.teacherclasspro.fragment.M2_Fragment;
import com.kingsun.teacherclasspro.fragment.M3_Fragment;
import com.kingsun.teacherclasspro.fragment.M4_M15_Fragment;
import com.kingsun.teacherclasspro.fragment.M5_Fragment;
import com.kingsun.teacherclasspro.fragment.M6_M14_Fragment;
import com.kingsun.teacherclasspro.fragment.M7_M13_Fragment;
import com.kingsun.teacherclasspro.fragment.M8_Fragment;
import com.kingsun.teacherclasspro.fragment.M9_Fragment;
import com.kingsun.teacherclasspro.utils.KingSoftHttpManager;
import com.kingsun.teacherclasspro.utils.KingSoftHttpManager.OnQueueComplete;
import com.kingsun.teacherclasspro.utils.KingSoftParasJson;
import com.kingsun.teacherclasspro.utils.MediaPlayerUtil;
import com.kingsun.teacherclasspro.utils.QuestionUtil;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.kingsun.teacherclasspro.widgets.MyPager;

/***
 * 作业题型
 * @author hai.huang
 *
 */
public class JobActivity extends BaseActivity implements OnClickListener{
	protected static final String TAG = "JobActivity";
	private TextView tv_title,tv_up,tv_down;
	private ImageView img_record,img_play;
	private MyPager viewpager;
	private KingSoftHttpManager mHttpClient;
	private int currentIndex = 0;
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	private ArrayList<Question> questionList = new ArrayList<Question>(); ;
	public ArrayList<Question> SortquestionList;

	public loadCallback callback = new  loadCallback() {
		@Override
		public void loadData(int  po,Question qus) {
//			BaseActivity.Ilog(TAG, "index == "+currentIndex);
//			Gson gson = new Gson();
//			final String jsonString = gson.toJson(qus);
//			BaseActivity.Ilog(TAG, jsonString);
			//保存数据
			SortquestionList.set(po, qus);
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate( Bundle arg0) {
		super.onCreate(arg0);
		BaseActivity.Ilog(TAG, "onCreate == ");
		setContentView(R.layout.job_activity);
		initView();
		if (arg0 != null) {
			BaseActivity.Ilog(TAG, "onCreate == 111");
			SortquestionList = (ArrayList<Question>) arg0.getSerializable("Bean");
//			fragmentList = (ArrayList<Fragment>) arg0.getSerializable("Fragment");
			currentIndex = arg0.getInt("index");
			if (SortquestionList != null) {
				Gson gson = new Gson();
				final String jsonString = gson.toJson(SortquestionList.get(currentIndex));
//				BaseActivity.Ilog(TAG, jsonString);
				BaseActivity.Ilog(TAG, "onCreate =222=  +"+SortquestionList.size()+"; frgemt "+fragmentList.size());
				notifyDate(currentIndex);
			}
		}else{
			if (SortquestionList == null || SortquestionList.size() == 0) {
				BaseActivity.Ilog(TAG, "onCreate == 重新请求数据");
				initData();
				ToastUtils.showToast(getApplicationContext(), "开始请求数据");
			}
		}
	}

	private void initView(){
		mHttpClient = new KingSoftHttpManager(this);
		mHttpClient.setOnQueueCompleteListener(onComplete);
		tv_title = (TextView) findViewById(R.id.tv_title);

		tv_up = (TextView) findViewById(R.id.tv_up);
		tv_up.setOnClickListener(this);

		tv_down = (TextView) findViewById(R.id.tv_down);
		tv_down.setOnClickListener(this);

		img_record = (ImageView) findViewById(R.id.img_record);
		img_record.setOnClickListener(this);

		img_play = (ImageView) findViewById(R.id.img_play);
		img_play.setOnClickListener(this);

		viewpager = (MyPager) findViewById(R.id.viewpager);
	}

	private  void initData(){
		S_DialogUtil.showProgressDialog(this);
		//模拟测试数据
		Map<String, String> mapPost = new HashMap<String, String>();
		//课程id
//		mapPost.put("StuTaskID", "0DD2A1A9-C8AC-48EB-BF7A-01C53C7FCC9E");
//		mapPost.put("StuTaskID", "CBF9888B-FE45-4085-B1E8-BFF3BB3C0291");
//		mapPost.put("StuTaskID", "3035CFEF-082D-43EF-86AA-D255C802317C");
//		mapPost.put("StuTaskID", "80020089-556A-458F-A977-B25A2B23665D");
//		mapPost.put("StuTaskID", "0501E1EC-46CA-490A-97C8-FEEA8111ABC4");
		mapPost.put("StuTaskID", "99D42930-2A91-44B1-816E-103379DD9CC6");

		String url = Config.GetStuTaskDetails;
		mHttpClient.startQueuePost(url,mapPost,1);
	}

	/**
	 * 数据请求成功
	 * 
	 * @date 2014-12-4 下午5:12:17
	 */
	OnQueueComplete onComplete = new OnQueueComplete() {
		// 数据请求成功,根据tag来判断是哪个数据请求
		@Override
		public void onCompleteSu(KingSoftResultBean result, int tag) {
			S_DialogUtil.dismissDialog();
			if (result != null) {
				if (result.Success) {
					switch (tag) {
					case 1:
//						BaseActivity.Ilog(TAG, "result = "+result.Data);
						BaseActivity.Ilog(TAG, "result = 22222222222222");
						if (result.Data != null) {
							//拿到混合数据
							questionList = KingSoftParasJson.getListByJson(result.Data, Question.class);
							if (questionList != null  && questionList.size() >0) {
								for (int i = 0; i < questionList.size(); i++) {
									if (questionList.get(i).getQuestionModel().equals("M21")) {
										questionList.get(i).setQuestionModel("Y21");
									}
								}
								QuestionUtil questionUtil = new QuestionUtil();
								//拿到排列后的数据
								SortquestionList = questionUtil.SortQuestion(questionList);
								notifyDate(0);
							}
						}
						break;
					default:
						break;
					}
				}else {
					ToastUtils.showToast(JobActivity.this, ""+result.Message);
				}
			} else {
				//服务器返回失败
				ToastUtils.showToast(JobActivity.this,"获取数据失败");
			}
		}

		// 数据请求失败
		@Override
		public void onCompleteFail(VolleyError volleryError, int tag) {
			S_DialogUtil.dismissDialog();
			if (volleryError.networkResponse == null) {
				ToastUtils.showToast(JobActivity.this, "网络请求失败");
				if (SortquestionList != null && fragmentList != null && fragmentList.size() >0 ) {
					notifyDate(currentIndex);
				}
				return;
			}
		}

		// 开始数据请求
		@Override
		public void onQueueStart(int tag) {
		}
	};
	/**
	 * 卡页监听器
	 * @author Administrator
	 *
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener{  
		@Override  
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}  

		@Override  
		public void onPageScrollStateChanged(int arg0) {  
		}  

		@Override  
		public void onPageSelected(int arg0) {
			MediaPlayerUtil.stop();
			viewpager.setCurrentItem(arg0);
			currentIndex = arg0;
			if (arg0 == fragmentList.size() - 1) {
				//最后一页
				tv_up.setVisibility(View.VISIBLE);
				tv_down.setVisibility(View.INVISIBLE);
			}else{
				tv_down.setVisibility(View.VISIBLE);
				if (arg0 == 0) {
					tv_up.setVisibility(View.INVISIBLE);
				}else{
					tv_up.setVisibility(View.VISIBLE);
				}
			}
			//			VoiceBoxFragment fragment1 =(VoiceBoxFragment) fragmentList.get(currIndex);
			//			if (fragment1 != null) {
			//				fragment1.setCode(currIndex+1);
			//			}
			String  tString = SortquestionList.get(arg0).getQuestionTitle()+
					SortquestionList.get(arg0).getQuestionModel()+"（ "+(currentIndex+1)+"/"+
					SortquestionList.size()+")";
			tv_title.setText(tString);
		}  
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//上一题
		case R.id.tv_up:
			if (currentIndex > 0) {
				viewpager.setCurrentItem(currentIndex-1);
			}
			break;

		case R.id.tv_down:
			//下一题
			if (currentIndex < fragmentList.size() -1 ) {
				viewpager.setCurrentItem(currentIndex+1);
			}
			break;

		case R.id.img_record:
			//录音
			Gson gson = new Gson();
			final String jsonString = gson.toJson(SortquestionList.get(currentIndex));
			BaseActivity.Ilog(TAG, jsonString);
			String urlString =  SortquestionList.get(currentIndex).getMp3Url();
			urlString = getUrl(urlString);
			MediaPlayerUtil.playFromIntenet(JobActivity.this,urlString,myHandler);
			break;

		case R.id.img_play:
			//试听
			break;
		default:
			break;
		}
	}
	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case  Constant.LISTEN_EVER_PLAY_FAILED:
				String sp = (String) msg.obj;
				ToastUtils.showToast(getApplicationContext(), sp);
				break;
			}
		};
	};

	/**
	 * url国际化
	 * @param url
	 * @return
	 */
	public  String getUrl(String url) {
		String urlStr = null;
		try {
			urlStr = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20");
			urlStr = urlStr.replaceAll("%3A", ":").replaceAll("%2F", "/");
			Log.e("decodeUrl", "url: " + url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return urlStr;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MediaPlayerUtil.stop();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (SortquestionList != null) {
			outState.putSerializable("Bean", SortquestionList);
		}
		outState.putInt("index", currentIndex);
	}

	//鍙栨暟鎹?
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		BaseActivity.Ilog(TAG, "onRestoreInstanceState == ");
//		if (SortquestionList == null) {
//			SortquestionList = (ArrayList<Question>) savedInstanceState.getSerializable("Bean");
//		}
	}
	
	/***
	 * 填充数据
	 */
	public void notifyDate(int page){
		fragmentList.clear();
		if (SortquestionList != null && SortquestionList.size() > 0) {
			BaseActivity.Ilog(TAG, "notifyDate ==come ");
			for (int j = 0; j < SortquestionList.size() ; j++) {
				if (SortquestionList.get(j) == null) {
					BaseActivity.Ilog(TAG, "notifyDate ==come  shit "+j);
				}
				if (SortquestionList.get(j).getQuestionModel().equals("M1")) {
					M1_Fragment fragment = new M1_Fragment(SortquestionList.get(j),callback,j);
					fragmentList.add(fragment);
				}else if (SortquestionList.get(j).getQuestionModel().equals("M2")) {
					M2_Fragment fragment = new M2_Fragment(SortquestionList.get(j),callback,j);
					fragmentList.add(fragment);
				}else if (SortquestionList.get(j).getQuestionModel().equals("M3")) {
					M3_Fragment fragment = new M3_Fragment(SortquestionList.get(j),callback);
					fragmentList.add(fragment);
				}else if (SortquestionList.get(j).getQuestionModel().equals("M4")||
						SortquestionList.get(j).getQuestionModel().equals("M15")) {
					M4_M15_Fragment fragment = new M4_M15_Fragment(SortquestionList.get(j),callback);
					fragmentList.add(fragment);
				}else if(SortquestionList.get(j).getQuestionModel().equals("M5")){
					M5_Fragment fragment = new M5_Fragment(SortquestionList.get(j),callback);
					fragmentList.add(fragment);
				}else if(SortquestionList.get(j).getQuestionModel().equals("M6")||
						SortquestionList.get(j).getQuestionModel().equals("M14")){
					M6_M14_Fragment fragment = new M6_M14_Fragment(SortquestionList.get(j),callback);
					fragmentList.add(fragment);
				}else if(SortquestionList.get(j).getQuestionModel().equals("M7")||
						SortquestionList.get(j).getQuestionModel().equals("M13")){
					M7_M13_Fragment fragment = new M7_M13_Fragment(SortquestionList.get(j),callback);
					fragmentList.add(fragment);
				}else if(SortquestionList.get(j).getQuestionModel().equals("M8")){
					M8_Fragment fragment = new M8_Fragment(SortquestionList.get(j),callback);
					fragmentList.add(fragment);
				}else if(SortquestionList.get(j).getQuestionModel().equals("M9")){
					M9_Fragment fragment = new M9_Fragment(SortquestionList.get(j),callback);
					fragmentList.add(fragment);
				}else if(SortquestionList.get(j).getQuestionModel().equals("M10")){
					M10_Fragment fragment = new M10_Fragment(SortquestionList.get(j),callback);
					fragmentList.add(fragment);
				}else if(SortquestionList.get(j).getQuestionModel().equals("M12")){
					M12_Fragment fragment = new M12_Fragment(SortquestionList.get(j),callback,j);
					fragmentList.add(fragment);
				}else if(SortquestionList.get(j).getQuestionModel().equals("M18")){
					M18_Fragment fragment = new M18_Fragment(SortquestionList.get(j),callback,j);
					fragmentList.add(fragment);
				}
			}
			viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));  
			viewpager.setCurrentItem(page);//设置当前显示标签页为第一页  
			tv_up.setVisibility(View.INVISIBLE);
			viewpager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器  
			String  tString = SortquestionList.get(page).getQuestionTitle()+
					SortquestionList.get(page).getQuestionModel()+"（ "+(page+1)+"/"+
					SortquestionList.size()+")";
			tv_title.setText(tString);
			BaseActivity.Ilog(TAG, "notifyDate ==end ");
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BaseActivity.Ilog(TAG, "onDestroy == ");
	}
}