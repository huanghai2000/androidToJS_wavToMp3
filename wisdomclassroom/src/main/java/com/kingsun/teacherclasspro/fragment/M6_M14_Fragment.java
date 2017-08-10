package com.kingsun.teacherclasspro.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.adpter.ListenChooseAdpter;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.bean.Question.SelectList;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.utils.Configure;
import com.kingsun.teacherclasspro.utils.MediaPlayerUtil;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.kingsun.teacherclasspro.widgets.Override_GridView;


/***
 * 听音选单词
 * @author hai.huang
 *
 */
public class M6_M14_Fragment extends BaseFragment implements OnClickListener{
	private String TAG = "M6_M14_Fragment";
	private View layout_View;
	private Override_GridView gridView;
	private  ListenChooseAdpter adpter;
	public M6_M14_Fragment(){}
	public  int width = 0;
	private  Question bean;
	private loadCallback callback;
	private ImageView img_record;
	private ArrayList<SelectList> pList = new ArrayList<SelectList>();
	public M6_M14_Fragment(Question bean,loadCallback callback){
		this.bean = bean;
		this.callback = callback;
		if (bean != null) {
			pList = bean.getSelectList();
		}
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.listen_circle, container, false);
		Configure.init(getActivity());
		width = Configure.screenWidth;
		initView();
		return layout_View;
	}

	private void initView(){
		gridView =  (Override_GridView)layout_View.findViewById(R.id.gridView_print);
		adpter = new ListenChooseAdpter(getActivity(),1);
		adpter.setCenter(true);
		gridView.setAdapter(adpter);

		img_record = (ImageView) layout_View.findViewById(R.id.img_record);
		img_record.setOnClickListener(this);

		gridView.setVerticalSpacing(40);
		adpter.setDate(pList);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < pList.size(); i++) {
					if (i== position) {
						pList.get(i).setSelect(true);
					}else{
						pList.get(i).setSelect(false);
					}
				}
				adpter.setDate(pList);
			}
		});
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
			String urlString = bean.getMp3Url();
			urlString = MediaPlayerUtil.getUrl(urlString);
			MediaPlayerUtil.playFromIntenet(getActivity(),urlString,myHandler);
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