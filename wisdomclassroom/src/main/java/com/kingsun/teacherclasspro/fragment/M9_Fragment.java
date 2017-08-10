package com.kingsun.teacherclasspro.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.adpter.ListenChooseAdpter;
import com.kingsun.teacherclasspro.adpter.ListenWriteAdpter;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.bean.TestBean;
import com.kingsun.teacherclasspro.utils.Configure;

/***
 * 听写单词
 * @author hai.huang
 *
 */
public class M9_Fragment extends BaseFragment implements OnClickListener{
	private View layout_View;
	private GridView gridView_print;
	private ListView content_listView;
	private  ListenWriteAdpter prWriteAdpter;
	private  ListenChooseAdpter adpter;
	public M9_Fragment(){}
	private ArrayList<String> pList = new ArrayList<>();
	private ArrayList<TestBean> writeList = new ArrayList<>();
	private int width = 0;
	private  Question bean;
	private loadCallback callback;
	public M9_Fragment(Question bean,loadCallback callback){
		this.bean = bean;
		this.callback = callback;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.m9_fragment_layout, container, false);
		Configure.init(getActivity());
		width = Configure.screenWidth;
		initView();
		return layout_View;
	}

	private void initView(){
		content_listView = (ListView) layout_View.findViewById(R.id.content_listView);
//		adpter = new ListenChooseAdpter(getActivity(),2);
		content_listView.setAdapter(adpter);
		if (writeList == null || writeList.size() == 0) {
			writeList.clear();
			for (int i = 0; i < 4; i++) {
				TestBean bean = new TestBean("where is  my  pad "+i);
				writeList.add(bean);
			}
		}
//		adpter.setDate(writeList);
		content_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//点击图片
				String sp = (position+1)+"";
				if (!pList.contains(sp)) {
					for (int i = 0; i < pList.size(); i++) {
						if (pList.get(i).equals("")) {
							pList.set(i, sp);
							break;
						}
					}
//					prWriteAdpter.setDate(pList);
				}
			}
		});

		gridView_print = (GridView) layout_View.findViewById(R.id.gridView_print);
		prWriteAdpter = new ListenWriteAdpter(getActivity(),3);
		gridView_print.setAdapter(prWriteAdpter);
		gridView_print.setVerticalSpacing(0);
		gridView_print.setHorizontalSpacing(15);
		if (pList == null || pList.size() == 0) {
			pList.clear();
			for (int i = 0; i < writeList.size(); i++) {
				pList.add("");
			}
		}
		int chooseWidth = width/19;
		int viewWidth = chooseWidth*writeList.size();
		gridView_print.setNumColumns(writeList.size());
		gridView_print.setBackground(null);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(viewWidth, chooseWidth);
		params1.setMargins(0,0,0,35);
		params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params1.addRule(RelativeLayout.BELOW, R.id.content_listView);
//		prWriteAdpter.setDate(pList);
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
		default:
			break;
		}
	}
}
