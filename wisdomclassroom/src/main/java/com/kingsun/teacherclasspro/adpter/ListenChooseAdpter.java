package com.kingsun.teacherclasspro.adpter;

import java.util.ArrayList;

import org.xutils.http.RequestParams;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.percent.support.PercentRelativeLayout;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.bean.Question.SelectList;
import com.kingsun.teacherclasspro.utils.QuestionUtil;

public class ListenChooseAdpter extends MyBaseAdpter{
	private LayoutInflater Container; 
	private Context context;
	private int type;
	public ArrayList<SelectList> beans = new ArrayList<>();
	public boolean isCenter ;

	public boolean isCenter() {
		return isCenter;
	}

	public void setCenter(boolean isCenter) {
		this.isCenter = isCenter;
	}

	public ListenChooseAdpter(Context context,int  type){
		this.context = context;
		this.type = type;
		this.Container =LayoutInflater.from(context);
	}

	public void setDate(ArrayList<SelectList> tbean){
		if (tbean != null) {
			this.beans = tbean;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return beans != null ? beans.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return beans!= null ? beans.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		KingSoftInfo  info = null; 
		if (convertView == null) {
			info = new KingSoftInfo();
			convertView =Container.inflate(R.layout.listen_and_choose_adpter, null);
			//描述
			info.tv_content = (TextView)convertView.findViewById(R.id.tv_content);

			info.tv_title = (TextView)convertView.findViewById(R.id.tv_title);

			info.content_layout = (PercentRelativeLayout) convertView.findViewById(R.id.content_layout);
			convertView.setTag(info);   
		}else{
			info = (KingSoftInfo)convertView.getTag();
		}
		if (isCenter) {
			RelativeLayout.LayoutParams params = new RelativeLayout.
					LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			info.content_layout.setLayoutParams(params);
		}
		
		if (type != 2) {
			info.tv_title.setText(QuestionUtil.getChoose(position));
		}else{
			info.tv_title.setText((position+1)+"");
			info.tv_title.setTextSize(16);
			info.tv_content.setTextSize(14);
		}

		SelectList bean = beans.get(position);
		if (bean != null) {
			int color  ;
			Drawable drawable ;
			info.tv_content.setText(bean.getSelectItem()+"");
			if (bean.isSelect()) {
				//表示选中
				color  = context.getResources().getColor(R.color.saffron_yellow);
				drawable = context.getResources().getDrawable(R.drawable.shape_text_select_bg);
			}else{
				color  = context.getResources().getColor(R.color.white);
				drawable = context.getResources().getDrawable(R.drawable.shape_text_no_select_bg);
			}
			info.tv_content.setTextColor(color);
			info.tv_title.setTextColor(color);
			info.tv_title.setBackground(drawable);
		}
		if (type == 1) {
			info.content_layout.setPadding(0, 20, 0,20);
		}
		return convertView;
	}

	public final class KingSoftInfo{      
		private TextView tv_content,tv_title;//名称,时间，点赞数
		private PercentRelativeLayout content_layout;
	}
}

