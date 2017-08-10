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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.adpter.ListenAndChooseAdpter;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.bean.Question.SelectList;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.utils.Configure;
import com.kingsun.teacherclasspro.utils.MediaPlayerUtil;
import com.kingsun.teacherclasspro.utils.ToastUtils;


/***
 * 听音选图
 * @author hai.huang
 *
 */
public class M4_M15_Fragment extends BaseFragment implements OnClickListener{
	private String TAG = "Fragment";
	private View layout_View;
	private GridView content_grid;
	private ListenAndChooseAdpter adpter;
	private Question bean;
	private TextView tv_title;
	public M4_M15_Fragment(){}
	private ArrayList<SelectList> SelectList = new ArrayList<Question.SelectList>();
	public  int width = 0;
	private loadCallback callback;
	private ImageView img_record,img_play;
	public M4_M15_Fragment(Question bean,loadCallback callback){
		this.bean = bean;
		this.callback = callback;
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.listen_choose, container, false);
		Configure.init(getActivity());
		width = Configure.screenWidth;
		if (bean != null ) {
			if (SelectList == null || SelectList.size() == 0) {
				SelectList =  bean.getSelectList();
			}
		}
		initView();
		return layout_View;
	}

	private void initView(){
		tv_title = (TextView) layout_View.findViewById(R.id.tv_title);
		img_record = (ImageView) layout_View.findViewById(R.id.img_record);
		img_record.setOnClickListener(this);

		img_play = (ImageView) layout_View.findViewById(R.id.img_play);
		img_play.setOnClickListener(this);

		content_grid = (GridView) layout_View.findViewById(R.id.gridView_print);
		adpter = new ListenAndChooseAdpter(getActivity());
		content_grid.setAdapter(adpter);
		if (bean != null ) {
			tv_title.setText(bean.getQuestionContent()+"");
		}
		if (SelectList != null && SelectList.size() >0) {
			//特定每一个item的宽度
			int itemWidth = width*20/100;
			int gridViewWidth = itemWidth*SelectList.size();
			content_grid.setNumColumns(SelectList.size());
			content_grid.setHorizontalSpacing(20);
			LayoutParams params = new LayoutParams(gridViewWidth,
					LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.addRule(RelativeLayout.BELOW,R.id.tv_title);
			content_grid.setLayoutParams(params);
			adpter.setDate(SelectList);
		}
		content_grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SelectList bean = SelectList.get(position);
				for (int i = 0; i < SelectList.size(); i++) {
					if (i== position) {
						bean.setSelect(true);
						SelectList.set(position,bean);
					}else{
						SelectList.get(i).setSelect(false);
					}
				}
				adpter.setDate(SelectList);
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