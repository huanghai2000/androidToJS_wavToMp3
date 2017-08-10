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
import com.kingsun.teacherclasspro.adpter.ListenChooseTextAdpter;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/***
 * 看题选择正确答案
 * @author hai.huang
 *
 */
public class M12_Fragment extends BaseFragment implements OnClickListener{
	private String  TAG = "M12_Fragment";
	private View layout_View;
	private ListView content_grid;
	private ImageView img_print;
	private TextView tv_title;
	private ListenChooseTextAdpter adpter;
	private Question bean;
	private int  index;
	public  M12_Fragment(){}
	private loadCallback callback;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageView img_record,img_play;
	private ArrayList<Question> pList = new ArrayList<Question>();
	public M12_Fragment(Question bean,loadCallback callback,int  po){
		this.bean = bean;
		this.callback = callback;
		this.index = po;
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
				ArrayList<Question> list = adpter.getBeans();
				if (list != null && list.size() >0 && callback != null) {
					callback.loadData(index,list.get(0));
				}
			}
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.listen_choose_text, container, false);
		initView();
		return layout_View;
	}

	private void initView(){
		imageLoader = MyApplication.initImageLoader(getActivity());
		options = new DisplayImageOptions.Builder().
				showImageForEmptyUri(R.drawable.kingsoft_defalt)  // 设置图片Uri为空或是错误的时候显示的图片  
				.showImageOnFail(R.drawable.kingsoft_defalt)       // 设置图片加载或解码过程中发生错误显示的图片      
				.cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中  
				.cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中  
				.build(); 

		img_print = (ImageView) layout_View.findViewById(R.id.img_print);

		tv_title = (TextView) layout_View.findViewById(R.id.tv_title);
		tv_title.setVisibility(View.VISIBLE);

		content_grid = (ListView) layout_View.findViewById(R.id.my_listView);
		adpter = new ListenChooseTextAdpter(getActivity(),1);

		img_record = (ImageView) layout_View.findViewById(R.id.img_record);
		img_record.setOnClickListener(this);

		img_play = (ImageView) layout_View.findViewById(R.id.img_play);
		img_play.setOnClickListener(this);

		content_grid.setAdapter(adpter);
		if (bean != null) {
			tv_title.setText(bean.getQuestionContent().replace("/n", "\n")+"");
			
			imageLoader.displayImage(bean.getImgUrl(),img_print, options);
			if (pList == null || pList.size() == 0) {
				pList.add(bean);
				adpter.setDate(pList);
			}else{
				adpter.setDate(pList);
			}
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