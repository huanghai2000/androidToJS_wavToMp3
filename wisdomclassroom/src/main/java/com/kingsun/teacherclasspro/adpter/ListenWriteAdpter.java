package com.kingsun.teacherclasspro.adpter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.bean.M3_Bean;
import com.kingsun.teacherclasspro.utils.Configure;

public class ListenWriteAdpter extends MyBaseAdpter{
	private LayoutInflater Container; 
	private Context context;
	private int width = 0;
	private int type;
	public boolean isClear;
	public boolean isClear() {
		return isClear;
	}

	public void setClear(boolean isClear) {
		this.isClear = isClear;
	}

	public ArrayList<M3_Bean> beans = new ArrayList<>();
	public ArrayList<M3_Bean> getBeans() {
		return beans;
	}

	public ListenWriteAdpter(Context context,int  type){
		this.context = context;
		this.type = type;
		this.Container =LayoutInflater.from(context);
		width = Configure.screenWidth;
	}


	public void setDate(ArrayList<M3_Bean> tbean){
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
			if (type == 0) {
				//表示听写
				convertView =Container.inflate(R.layout.listen_writer_adpter, null);
			}else {
				convertView =Container.inflate(R.layout.print_adpter, null);
			}
			//描述
			info.tv_content = (TextView)convertView.findViewById(R.id.tv_content);

			info.img_print = (ImageView)convertView.findViewById(R.id.img_print);
			convertView.setTag(info);   
		}else{
			info = (KingSoftInfo)convertView.getTag();
		}

		info.tv_content.setText(beans.get(position).getContent());
		if (type == 3) {
//			int  itemWidth = width/15;
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(itemWidth,itemWidth);
//			info.tv_content.setLayoutParams(params);
			info.tv_content.setTextSize(context.getResources().getDimension(R.dimen.text_size_18));
			info.tv_content.setBackground(context.getResources().getDrawable(R.drawable.shape_text_no_select_bg));
		}else if (type == 0) {
			info.tv_content.setTextSize(context.getResources().getDimension(R.dimen.text_size_18));
			if (beans.get(position).isInput()) {
				if (beans.get(position).isChoose()) {
					info.tv_content.setBackground(context.getResources().getDrawable(R.drawable.shape_text_select_bg));
				}else{
					info.tv_content.setBackground(context.getResources().getDrawable(R.drawable.shape_text_no_select_bg));
				}
			}else{
				info.tv_content.setBackground(null);
			}
		}else if (type == 1) {
			info.tv_content.setTextSize(context.getResources().getDimension(R.dimen.text_size_28));
			info.tv_content.setBackground(context.getResources().getDrawable(R.drawable.shape_input_text_bg));
			if (position == 19) {
				//表示删除
			}else if (position == 20) {
				//大小写切换
				info.tv_content.setTextSize(context.getResources().getDimension(R.dimen.text_size_18));
			}else if (position == 28) {
				//符号
				info.tv_content.setTextSize(context.getResources().getDimension(R.dimen.text_size_18));
			}else if (position == 29) {
				//确定
				info.tv_content.setTextSize(context.getResources().getDimension(R.dimen.text_size_18));
			}
		}
		return convertView;
	}

	public final class KingSoftInfo{      
		private TextView tv_content;//名称,时间，点赞数
		private ImageView img_print ;
	}
}

