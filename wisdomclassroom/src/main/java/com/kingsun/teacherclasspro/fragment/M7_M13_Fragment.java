package com.kingsun.teacherclasspro.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.bean.teacherBean;


/***
 * 听音判断
 * @author hai.huang
 *
 */
public class M7_M13_Fragment extends BaseFragment implements OnClickListener{
	private View layout_View;
	private TextView tv_title;
	private ImageView img_content,img_right,img_error;
	private boolean isTrue,isChoose;
	private  Question bean;
	public M7_M13_Fragment(){}
	private loadCallback callback;
	private ArrayList<teacherBean> pList = new ArrayList<teacherBean>();
	public M7_M13_Fragment(Question bean,loadCallback callback){
		this.bean = bean;
		this.callback = callback;
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.listen_and_judge, container, false);
		initView();
		return layout_View;
	}

	private void initView(){
		//内容
		tv_title = (TextView) layout_View.findViewById(R.id.tv_title);

		img_content = (ImageView) layout_View.findViewById(R.id.img_content);

		img_right = (ImageView) layout_View.findViewById(R.id.img_right);
		img_right.setBackgroundResource(R.drawable.true_unselect);
		img_right.setOnClickListener(this);

		img_error = (ImageView) layout_View.findViewById(R.id.img_error);
		img_error.setBackgroundResource(R.drawable.error_unselect);
		img_error.setOnClickListener(this);
		if (isChoose) {
			if (isTrue) {
				img_right.setBackgroundResource(R.drawable.true_select);
				img_error.setBackgroundResource(R.drawable.error_unselect);
			}else {
				img_right.setBackgroundResource(R.drawable.true_unselect);
				img_error.setBackgroundResource(R.drawable.error_select);
			}
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//对
		case R.id.img_right:
			isTrue = true;
			isChoose = true;
			img_right.setBackgroundResource(R.drawable.true_select);
			img_error.setBackgroundResource(R.drawable.error_unselect);
			break;

		case R.id.img_error:
			//错
			isTrue = false;
			isChoose = true;
			img_right.setBackgroundResource(R.drawable.true_unselect);
			img_error.setBackgroundResource(R.drawable.error_select);
			break;	
		default:
			break;
		}
	}
}