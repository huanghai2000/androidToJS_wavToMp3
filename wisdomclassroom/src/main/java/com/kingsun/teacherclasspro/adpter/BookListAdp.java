package com.kingsun.teacherclasspro.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.bean.LoginBean;

import java.util.ArrayList;

/**
 * Created by hai.huang on 2018/3/15.
 */

public class BookListAdp extends  MyBaseAdpter {
    private LayoutInflater listContainer;
    private Context context;
    private ArrayList<LoginBean.ClassBean> beans;

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    private int currentIndex = 0;
    public static Integer[] imgList = {
            R.drawable.qb_1,R.drawable.tb_1,R.drawable.jf_1,R.drawable.ts_1};

    public static Integer[] imgSelectList = {
            R.drawable.qb_2,R.drawable.tb_2,R.drawable.jf_2,R.drawable.ts_2};

    public static String[] name = {"全部教材","同步教材","教辅教材","特色教材"};
    private int type ;//1表示班级，2表示下载
    public BookListAdp(Context tcontext,ArrayList<LoginBean.ClassBean> list ,int type){
        this.context = tcontext;
        this.listContainer = LayoutInflater.from(context);
        this.beans = list;
        this.type = type;
    }

    public void setDate(ArrayList<LoginBean.ClassBean> tbean){
        if (tbean != null) {
            this.beans = tbean;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (type == 3){
            return name.length;
        }else {
            return beans != null ? beans.size() : 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (type == 3){
            return name[position];
        }else {
            return beans!= null ? beans.get(position) : null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KingSoftInfo info = null;
        if (convertView == null) {
            info = new KingSoftInfo();
            convertView =listContainer.inflate(R.layout.pop_list_adp_layout, null);
            info.tv_content = (TextView)convertView.findViewById(R.id.tv_name);
            info.tv_status = (TextView)convertView.findViewById(R.id.tv_status);
            info.img_connect = (ImageView) convertView.findViewById(R.id.img_notic);
            convertView.setTag(info);
        }else{
            info = (KingSoftInfo)convertView.getTag();
        }
        if (type == 3){
            info.img_connect.setVisibility(View.VISIBLE);
            info.tv_content.setText(name[position]);
            if (currentIndex == position){
                info.img_connect.setImageResource(imgSelectList[position]);
                info.tv_content.setTextColor(context.getResources().getColor(R.color._3b5998));
            }else {
                info.img_connect.setImageResource(imgList[position]);
                info.tv_content.setTextColor(context.getResources().getColor(R.color._76777b));
            }
        }else {
            if (beans != null && beans.get(position) != null){
                if (type == 1){//班级
                    info.tv_content.setText(beans.get(position).getClassName()+"");
                }else if (type == 2){//下载
                    if (!beans.get(position).isUpdata()){
                        info.tv_status.setVisibility(View.VISIBLE);
                    }else{
                        info.tv_status.setVisibility(View.GONE);
                    }
                    info.tv_content.setText(beans.get(position).getBookName()+"");
                }
            }
            if (currentIndex == position){
                info.tv_content.setTextColor(context.getResources().getColor(R.color._3b5998));
            }else {
                info.tv_content.setTextColor(context.getResources().getColor(R.color._76777b));
            }
        }
        return convertView;
    }
    public final class KingSoftInfo{
        private ImageView img_connect;
        private TextView tv_content,tv_status;//名称,时间，点赞数
    }
}
