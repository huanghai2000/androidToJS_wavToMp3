package com.kingsun.teacherclasspro.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.adpter.ListenWriteAdpter;
import com.kingsun.teacherclasspro.adpter.M8_ImgAdpter;
import com.kingsun.teacherclasspro.bean.M3_Bean;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.utils.Configure;
import com.kingsun.teacherclasspro.utils.MediaPlayerUtil;
import com.kingsun.teacherclasspro.utils.ToastUtils;

/***
 * 听写单词
 * @author hai.huang
 *
 */
public class M8_Fragment extends BaseFragment implements OnClickListener{
	private String TAG = "M8_Fragment";
	private View layout_View;
	private GridView content_grid,gridView_print;
	private  ListenWriteAdpter prWriteAdpter;
	private M8_ImgAdpter adpter;
	private ImageView img_record;
	private ArrayList<Question> smallQuestions;
	private int chooseIndex = 0;
	public M8_Fragment(){}
	private ArrayList<M3_Bean> pList = new ArrayList<M3_Bean>();
	private int width = 0;
	private  Question bean;
	private loadCallback callback;
	public M8_Fragment(Question bean,loadCallback callback){
		this.bean = bean;
		this.callback = callback;
		if (bean != null) {
			smallQuestions = bean.getSmallQuestions();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {
			//			if (content_grid != null && prWriteAdpter != null ) {
			//				prWriteAdpter.setDate(pList);
			//			}
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.listen_write, container, false);
		Configure.init(getActivity());
		width = Configure.screenWidth;
		initView();
		return layout_View;
	}

	private void initView(){
		content_grid = (GridView) layout_View.findViewById(R.id.content_grid);
		adpter = new M8_ImgAdpter(getActivity());
		content_grid.setAdapter(adpter);

		img_record = (ImageView) layout_View.findViewById(R.id.img_record);
		img_record.setOnClickListener(this);

		//特定每一个item的宽度
		int itemWidth = width/5;
		int  height = itemWidth*4/5;
		int gridViewWidth = itemWidth*smallQuestions.size();
		content_grid.setNumColumns(smallQuestions.size());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(gridViewWidth,
				itemWidth);
		params.setMargins(0, 50,0, 0);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		content_grid.setLayoutParams(params);
		adpter.setDate(smallQuestions);
		content_grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (chooseIndex >= pList.size()) {
					chooseIndex = 0;
				}
				//点击图片
				String sp = (position+1)+"";
				M3_Bean bean = pList.get(chooseIndex);
				bean.setContent(sp);
				pList.set(chooseIndex, bean);
				prWriteAdpter.setDate(pList);
				chooseIndex +=1;
			}
		});

		gridView_print = (GridView) layout_View.findViewById(R.id.gridView_print);
		prWriteAdpter = new ListenWriteAdpter(getActivity(),3);
		gridView_print.setAdapter(prWriteAdpter);
		gridView_print.setVerticalSpacing(0);
		gridView_print.setHorizontalSpacing(15);
		gridView_print.setBackgroundColor(getActivity().getResources().getColor(R.color.red));
		if (pList == null || pList.size() == 0) {
			pList.clear();
			for (int i = 0; i < smallQuestions.size(); i++) {
				M3_Bean bean = new M3_Bean();
				pList.add(bean);
			}
		}
		int chooseWidth = width/15;
		int viewWidth = chooseWidth*smallQuestions.size();
		gridView_print.setNumColumns(smallQuestions.size());
		gridView_print.setBackground(null);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(viewWidth,chooseWidth);
		params1.setMargins(0, 70,0,0);
		params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params1.addRule(RelativeLayout.BELOW, R.id.content_grid);
		prWriteAdpter.setDate(pList);
		gridView_print.setLayoutParams(params1);
		gridView_print.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

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
