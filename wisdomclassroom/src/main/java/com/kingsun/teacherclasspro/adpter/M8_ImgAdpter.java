package com.kingsun.teacherclasspro.adpter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.widgets.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class M8_ImgAdpter  extends MyBaseAdpter{
	private LayoutInflater Container; 
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	public ArrayList<Question> beans = new ArrayList<>();

	public M8_ImgAdpter(Context context){
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


	public void setDate(ArrayList<Question> tbean){
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
			convertView =Container.inflate(R.layout.m8_img_adpter, null);
			//描述
			info.tv_content = (TextView)convertView.findViewById(R.id.tv_content);

			info.imageView = (RoundImageView)convertView.findViewById(R.id.img_print);
			convertView.setTag(info);   
		}else{
			info = (KingSoftInfo)convertView.getTag();
		}
		if (beans.get(position) != null) {
			imageLoader.displayImage(beans.get(position).getImgUrl(),info.imageView, options);
		}
		info.tv_content.setText((position+1)+"");
		return convertView;
	}

	public final class KingSoftInfo{      
		private TextView tv_content;//名称,时间，点赞数
		private RoundImageView imageView ;
	}
}