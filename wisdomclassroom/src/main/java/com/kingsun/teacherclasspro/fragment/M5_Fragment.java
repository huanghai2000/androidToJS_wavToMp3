package com.kingsun.teacherclasspro.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.adpter.ListenChooseTextAdpter;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.bean.teacherBean;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.utils.Configure;
import com.kingsun.teacherclasspro.utils.MediaPlayerUtil;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/***
 * 听音选句子
 * @author hai.huang
 *大题
 */
public class M5_Fragment extends BaseFragment implements OnClickListener{
	private String TAG = "Fragment";
	private View layout_View;
	private ListView content_grid;
	private ImageView img_print;
	private ListenChooseTextAdpter adpter;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	public M5_Fragment(){}
	public  int width = 0;
	private  Question bean;
	private ImageView img_record,img_play;
	private loadCallback callback;
	public M5_Fragment(Question bean,loadCallback callback){
		this.bean = bean;
		this.callback = callback;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.listen_choose_text, container, false);
		Configure.init(getActivity());
		width = Configure.screenWidth;
		if (bean != null) {
			initView();
		}
		return layout_View;
	}

	private void initView(){
		imageLoader = MyApplication.initImageLoader(getActivity());
		img_print = (ImageView) layout_View.findViewById(R.id.img_print);
		imageLoader.displayImage(bean.getImgUrl(),img_print, options);
		
		img_record = (ImageView) layout_View.findViewById(R.id.img_record);
		img_record.setOnClickListener(this);

		img_play = (ImageView) layout_View.findViewById(R.id.img_play);
		img_play.setOnClickListener(this);
		
		content_grid = (ListView) layout_View.findViewById(R.id.my_listView);
		adpter = new ListenChooseTextAdpter(getActivity(),0);
		content_grid.setAdapter(adpter);
		if (bean .getSmallQuestions() != null) {
			adpter.setDate(bean .getSmallQuestions());
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//上一题
		case R.id.tv_up:
			break;
		case R.id.img_record:
			//录音
			Gson gson = new Gson();
			final String jsonString = gson.toJson(bean);
			BaseActivity.Ilog(TAG, jsonString);
			String urlString =  bean.getMp3Url();
			urlString = MediaPlayerUtil.getUrl(urlString);
			MediaPlayerUtil.playFromIntenet(getActivity(),urlString,myHandler);
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
				ToastUtils.showToast(getActivity(), sp);
				break;
			}
		};
	};
}