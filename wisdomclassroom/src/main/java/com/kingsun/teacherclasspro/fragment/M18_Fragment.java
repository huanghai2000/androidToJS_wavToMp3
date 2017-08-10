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
import android.widget.TextView;

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.adpter.ListenChoose_F18_TextAdpter;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.utils.ToastUtils;


/***
 * 看题选择正确答案
 * @author hai.huang
 *
 */
public class M18_Fragment extends BaseFragment implements OnClickListener{
	private String  TAG = "M12_Fragment";
	private View layout_View;
	private ListView content_grid;
	private TextView tv_title;
	private ListenChoose_F18_TextAdpter adpter;
	private Question bean;
	private int  index;
	private ArrayList<Question> smallQuestions;
	public M18_Fragment(){}
	private loadCallback callback;
	private ImageView img_record,img_play;
	public M18_Fragment(Question bean,loadCallback callback,int  po){
		this.bean = bean;
		this.callback = callback;
		this.index = po;
		if (bean != null) {
			smallQuestions = bean.getSmallQuestions();
		}
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {
			//可见

		}else{
			//隐藏
			BaseActivity.Ilog(TAG, "setUserVisibleHint === "+index);
			if (adpter != null) {
				smallQuestions = adpter.getBeans();
				if (smallQuestions != null && smallQuestions.size() >0 && callback != null) {
					bean.setSmallQuestions(smallQuestions);
					callback.loadData(index,bean);
				}
			}
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.f18_listen_choose_text, container, false);
		initView();
		return layout_View;
	}

	private void initView(){
		tv_title = (TextView) layout_View.findViewById(R.id.tv_title);
		tv_title.setVisibility(View.VISIBLE);

		content_grid = (ListView) layout_View.findViewById(R.id.my_listView);
		adpter = new ListenChoose_F18_TextAdpter(getActivity(),1);

		img_record = (ImageView) layout_View.findViewById(R.id.img_record);
		img_record.setOnClickListener(this);

		img_play = (ImageView) layout_View.findViewById(R.id.img_play);
		img_play.setOnClickListener(this);

		content_grid.setAdapter(adpter);
		if (smallQuestions != null) {
			adpter.setDate(smallQuestions);
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
			//			String urlString =  bean.getMp3Url();
			//			urlString = MediaPlayerUtil.getUrl(urlString);
			//			MediaPlayerUtil.playFromIntenet(getActivity(),urlString,myHandler);
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