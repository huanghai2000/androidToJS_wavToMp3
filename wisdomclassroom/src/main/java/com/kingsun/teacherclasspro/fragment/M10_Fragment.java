package com.kingsun.teacherclasspro.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.adpter.ListenChooseAdpter;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.bean.TestBean;
import com.kingsun.teacherclasspro.bean.teacherBean;
import com.kingsun.teacherclasspro.utils.Configure;
import com.kingsun.teacherclasspro.widgets.Override_GridView;


/***
 * 听音选单词
 * @author hai.huang
 *
 */
public class M10_Fragment extends BaseFragment implements OnClickListener{
	private View layout_View;
	private Override_GridView gridView;
	private  ListenChooseAdpter adpter;
	private TextView tv_title;
	private  Question bean;
	public M10_Fragment(){}
	public  int width = 0;
	private loadCallback callback;
	private ArrayList<teacherBean> pList = new ArrayList<teacherBean>();
	public M10_Fragment(Question bean,loadCallback callback){
		this.bean = bean;
		this.callback = callback;
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
		tv_title = (TextView) layout_View.findViewById(R.id.tv_title);
		tv_title.setVisibility(View.VISIBLE);

		gridView =  (Override_GridView)layout_View.findViewById(R.id.gridView_print);
		adpter = new ListenChooseAdpter(getActivity(),1);
		gridView.setAdapter(adpter);
		ArrayList<TestBean> beans = new ArrayList<>();
		for (int j = 0; j <4; j++) {
			TestBean testBean = new TestBean("where is  my  pad "+j) ;
			beans.add(testBean);
		}
		gridView.setVerticalSpacing(20);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//上一题
		case R.id.tv_up:
			break;
		default:
			break;
		}
	}
}