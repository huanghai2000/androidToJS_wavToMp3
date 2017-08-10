package com.kingsun.teacherclasspro.adpter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.bean.Question;
/***
 * 跟读课文Adpter
 * @author hai.huang
 *
 */
public class RepeatTextAdpter extends MyBaseAdpter{
	private LayoutInflater Container; 
	private Context context;
	public ArrayList<Question> beans = new ArrayList<>();
	public RepeatTextAdpter(Context context){
		this.context = context;
		this.Container =LayoutInflater.from(context);
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
			convertView =Container.inflate(R.layout.repeat_the_text_adpter, null);
			
			//tile
			info.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			//描述
			info.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
			//分数
			info.tv_result = (TextView)convertView.findViewById(R.id.tv_result);
			
			convertView.setTag(info);   
		}else{
			info = (KingSoftInfo)convertView.getTag();
		}
		
		Question bean = beans.get(position);
		if (bean != null) {
			String sp = bean.getQuestionContent();
			if (sp.contains(":")) {
				sp = "【"+sp.replace(":", "】\n     ");
			}
			info.tv_content.setText(sp+"");
			if (bean.isFinish()) {
				if (sp.contains("】")) {
					SpannableStringBuilder style = new SpannableStringBuilder(sp);
					style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.white)),0, 
							sp.lastIndexOf("】"),Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.saffron_yellow)),
							sp.lastIndexOf("】")+1,sp.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					info.tv_content.setText(style);
				}else{
					info.tv_content.setTextColor(context.getResources().getColor(R.color.saffron_yellow));
				}
			}else{
				info.tv_content.setTextColor(context.getResources().getColor(R.color.white));
			}
		}
		return convertView;
	}

	public final class KingSoftInfo{      
		private TextView tv_name,tv_content,tv_result;//名称,时间，点赞数
	}
}
