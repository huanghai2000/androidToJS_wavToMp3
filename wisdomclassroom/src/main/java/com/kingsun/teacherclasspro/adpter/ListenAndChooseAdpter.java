package com.kingsun.teacherclasspro.adpter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.Question.SelectList;
import com.kingsun.teacherclasspro.utils.QuestionUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListenAndChooseAdpter extends MyBaseAdpter{
	private LayoutInflater Container; 
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	public ArrayList<SelectList> beans = new ArrayList<SelectList>();

	public ListenAndChooseAdpter(Context context){
		this.context = context;
		this.Container =LayoutInflater.from(context);
		imageLoader = MyApplication.initImageLoader(context);
		options = new DisplayImageOptions.Builder().
				showImageForEmptyUri(R.drawable.kingsoft_defalt)  // 设置图片Uri为空或是错误的时候显示的图片  
				.showImageOnFail(R.drawable.kingsoft_defalt)       // 设置图片加载或解码过程中发生错误显示的图片      
				.cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中  
				.cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中  
				.build(); 
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
			convertView =Container.inflate(R.layout.listen_choose_adpter, null);
			//描述
			info.tv_content = (TextView)convertView.findViewById(R.id.tv_content);

			info.img_print = (ImageView)convertView.findViewById(R.id.img_print);

			info.imagebg_alpha_type_4 = (ImageView)convertView.findViewById(R.id.imagebg_alpha_type_4);

			convertView.setTag(info);   
		}else{
			info = (KingSoftInfo)convertView.getTag();
		}
		info.tv_content.setText(QuestionUtil.getChoose(position));
		if (beans.get(position) != null) {
			imageLoader.displayImage(beans.get(position).getImgUrl(),info.img_print, options);
			int color  ;
			Drawable drawable ;
			if (beans.get(position).isSelect()) {
				info.imagebg_alpha_type_4.setVisibility(View.VISIBLE);
				//表示选中
				color  = context.getResources().getColor(R.color.saffron_yellow);
				drawable = context.getResources().getDrawable(R.drawable.shape_text_select_bg);
			}else{
				info.imagebg_alpha_type_4.setVisibility(View.GONE);
				color  = context.getResources().getColor(R.color.white);
				drawable = context.getResources().getDrawable(R.drawable.shape_text_no_select_bg);
			}
			info.tv_content.setTextColor(color);
			info.tv_content.setBackground(drawable);
		}

		//		info.img_print.setImageResource(R.drawable.banner);
		return convertView;
	}

	public final class KingSoftInfo{      
		private TextView tv_content;//名称,时间，点赞数
		private ImageView img_print,imagebg_alpha_type_4 ;
	}
}

