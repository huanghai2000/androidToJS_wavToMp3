package com.kingsun.teacherclasspro.fragment;

import java.util.ArrayList;

import android.R.integer;
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

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.adpter.ListenWriteAdpter;
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
public class M3_Fragment extends BaseFragment implements OnClickListener{
	private String TAG = "M3_Fragment";
	private View layout_View;
	private GridView content_grid,gridView_print;
	private ListenWriteAdpter adpter,prWriteAdpter;
	private ImageView img_record;
	public  M3_Fragment(){}
	private ArrayList<M3_Bean> pList = new ArrayList<M3_Bean>();
	private ArrayList<M3_Bean> writeList = new ArrayList<M3_Bean>();
	private String answer = "";
	private int width = 0;
	private int chooseIndex = 0;
	private Question bean;
	private loadCallback callback;
	public M3_Fragment(Question bean,loadCallback callback){
		this.bean  = bean;
		this.callback = callback;
	}

	//大小写
	private boolean isUp = false;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.listen_write, container, false);
		Configure.init(getActivity());
		width = Configure.screenWidth;
		if (bean != null && isEmty(answer)) {
			answer = bean.getBlankAnswer().get(0).getAnswer();
			BaseActivity.Ilog(TAG, "answer =>"+answer+"; size = "+answer.length());
		}
		initView();
		return layout_View;
	}

	private void initView(){
		content_grid = (GridView) layout_View.findViewById(R.id.content_grid);
		adpter = new ListenWriteAdpter(getActivity(),0);
		content_grid.setAdapter(adpter);

		img_record = (ImageView) layout_View.findViewById(R.id.img_record);
		img_record.setOnClickListener(this);

		writeList.clear();
		for (int i = 0; i < answer.length(); i++) {
			M3_Bean bean = new M3_Bean();
			if (otherList.contains(answer.charAt(i)+"")) {
				bean.setContent("");
				bean.setInput(true);
				if (i == 0 ) {
					bean.setChoose(true);
				}
			}else{
				bean.setChoose(false);
				bean.setContent(answer.charAt(i)+"");
				bean.setInput(false);
			}
			writeList.add(bean);
		}
		//特定每一个item的宽度
		int itemWidth = width/15;
		int num  = writeList.size();
		if (writeList.size() > 8) {
			num = 8;
		}
		int gridViewWidth = itemWidth*num;
		content_grid.setNumColumns(num);
		LayoutParams params = new LayoutParams(gridViewWidth,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 35,0, 0);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		content_grid.setLayoutParams(params);
		adpter.setClear(true);
		adpter.setDate(writeList);

		gridView_print = (GridView) layout_View.findViewById(R.id.gridView_print);
		prWriteAdpter = new ListenWriteAdpter(getActivity(),1);
		gridView_print.setAdapter(prWriteAdpter);
		pList.clear();
		for (int i = 0; i < KBLowlerArrayList.size(); i++) {
			M3_Bean bean = new M3_Bean();
			bean.setContent(KBLowlerArrayList.get(i));
			pList.add(bean);
		}  
		prWriteAdpter.setDate(pList);
		gridView_print.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				M3_Bean m3_Bean = (M3_Bean) parent.getAdapter().getItem(position);
				if (position == 19) {
					//表示删除,判断是否需要更新数据
					if (chooseIndex < 0) {
						chooseIndex = 0;
						return;
					}
					M3_Bean bM3_Bean = writeList.get(chooseIndex);
					if (bM3_Bean.getContent().equals("")) {
						//需要移动光标
						for (int i = 0; i < 3; i++) {
							chooseIndex -= 1;
							if (chooseIndex < 0) {
								chooseIndex = 0;
							}
							if (writeList.get(chooseIndex).isInput()) {
								writeList.get(chooseIndex).setChoose(true);
								break;
							}
						}
						for (int i = 0; i < writeList.size(); i++) {
							if (i != chooseIndex) {
								writeList.get(i).setChoose(false);
							}
						}
						adpter.setDate(writeList);
					}else {
						bM3_Bean.setContent("");
						writeList.set(chooseIndex, bM3_Bean);
						adpter.setDate(writeList);
					}
				}else if (position == 20) {
					//大小写
					if (isUp) {
						//表示大写变成小写
						isUp = false;
						pList.clear();
						for (int i = 0; i < KBLowlerArrayList.size(); i++) {
							M3_Bean bean = new M3_Bean();
							bean.setContent(KBLowlerArrayList.get(i));
							pList.add(bean);
						}
					}else {
						isUp = true;
						pList.clear();
						for (int i = 0; i < KBCapitalData.size(); i++) {
							M3_Bean bean = new M3_Bean();
							bean.setContent(KBCapitalData.get(i));
							pList.add(bean);
						}
					}
					prWriteAdpter.setDate(pList);
				}else{
					//其它输入
					for (int i = 0; i < 3; i++) {
						if (chooseIndex >= writeList.size()) {
							chooseIndex = 0;
						}
						if (writeList.get(chooseIndex).isInput()) {
							//填充数据
							M3_Bean bean = writeList.get(chooseIndex);
							bean.setContent(m3_Bean.getContent());
							writeList.set(chooseIndex, bean);
							chooseIndex +=1;
							//移动光标
							for (int j = 0; i < 3;j++) {
								if (chooseIndex >= writeList.size()) {
									chooseIndex = 0;
								}
								if (writeList.get(chooseIndex).isInput()) {
									M3_Bean bean1 = writeList.get(chooseIndex);
									bean1.setChoose(true);
									writeList.set(chooseIndex, bean1);
									break;
								}else{
									chooseIndex += 1;
								}
							}
							break;
						}else{
							chooseIndex += 1;
						}
					}
					//刷新整个数据光标
					for (int i = 0; i < writeList.size(); i++) {
						if (i !=chooseIndex ) {
							writeList.get(i).setChoose(false);
						}
					}
					adpter.setDate(writeList);
				}
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
