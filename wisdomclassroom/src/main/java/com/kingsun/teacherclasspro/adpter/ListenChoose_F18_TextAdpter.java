package com.kingsun.teacherclasspro.adpter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.bean.Question.SelectList;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.kingsun.teacherclasspro.widgets.Override_GridView;

public class ListenChoose_F18_TextAdpter extends MyBaseAdpter{
	private LayoutInflater Container; 
	private Context context;
	public ArrayList<Question> beans = new ArrayList<>();
	public ArrayList<Question> getBeans() {
		return beans;
	}
	private int  type ;
	public ListenChoose_F18_TextAdpter(Context context,int  type){
		this.context = context;
		this.type = type;
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
			convertView =Container.inflate(R.layout.listen_choose_text_f18__adpter, null);
			//描述
			info.tv_content = (TextView)convertView.findViewById(R.id.tv_name);

			info.gridView = (Override_GridView)convertView.findViewById(R.id.gridView_print);
			convertView.setTag(info);   
		}else{
			info = (KingSoftInfo)convertView.getTag();
		}
		final ListenChooseAdpter adpter = new ListenChooseAdpter(context,0);
		info.gridView.setAdapter(adpter);
		info.gridView.setVerticalSpacing(20);
		adpter.setCenter(true);
		info.tv_content.setText("("+(position+1)+")"+
		beans.get(position).getQuestionContent().replace("/n","\n     ")+"");
		adpter.setDate(beans.get(position).getSelectList());
		info.gridView.setOnItemClickListener(new MyItemClick(adpter,beans.get(position),position));
		return convertView;
	}

	class MyItemClick implements OnItemClickListener{
		ListenChooseAdpter adpter;
		Question question;
		int  po;
		private MyItemClick(ListenChooseAdpter adpter1,Question question,int  po){
			this.adpter = adpter1;
			this.question = question;
			this.po = po;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SelectList selectBean = (SelectList) parent.getAdapter().getItem(position);
			for (int i = 0; i <question.getSelectList().size(); i++) {
				if (i ==position ) {
					question.getSelectList().get(i).setSelect(true);
				}else{
					question.getSelectList().get(i).setSelect(false);
				}
			}
			beans.set(po, question);
			notifyDataSetChanged();
			ToastUtils.showToast(context,selectBean.getSelectItem());
		}
	}
	public final class KingSoftInfo{      
		private TextView tv_content;//名称,时间，点赞数
		private Override_GridView gridView ;
	}
}