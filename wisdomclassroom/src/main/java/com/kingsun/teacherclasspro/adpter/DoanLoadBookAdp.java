package com.kingsun.teacherclasspro.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.BookInfoBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by hai.huang on 2018/3/15.
 * 离线下载数据
 */

public class DoanLoadBookAdp extends  MyBaseAdpter {
    private LayoutInflater listContainer;
    private Context context;
    private ArrayList<BookInfoBean> beans;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
        notifyDataSetChanged();
    }

    private boolean isDel;//
    public DoanLoadBookAdp(Context tcontext){
        this.context = tcontext;
        this.listContainer = LayoutInflater.from(context);
        imageLoader = MyApplication.initImageLoader(context);
        options = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.kingsoft_defalt)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.kingsoft_defalt)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();
    }

    public void setDate(ArrayList<BookInfoBean> tbean){
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
       KingSoftInfo info = null;
        if (convertView == null) {
            info = new KingSoftInfo();
            convertView =listContainer.inflate(R.layout.layout_down_book_adp, null);
            info.tv_content = (TextView)convertView.findViewById(R.id.tv_name);
            info.tv_Age = (TextView)convertView.findViewById(R.id.tv_age);
            info.tvType = (TextView)convertView.findViewById(R.id.tv_type);
            info.img_connect = (ImageView) convertView.findViewById(R.id.img_content);
            info.img_del = (ImageView) convertView.findViewById(R.id.img_del);
            convertView.setTag(info);
        }else{
            info = (KingSoftInfo)convertView.getTag();
        }
        if (beans != null && beans.get(position) != null){
//            imageLoader.displayImage("file://"+beans.get(position).getImgPath(), info.img_connect, options);
            Glide.with(context).load("file://"+beans.get(position).getImgPath())
                    .skipMemoryCache(true)//跳过内存缓存。
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不要在disk硬盘中缓存。
                    .into(new GlideDrawableImageViewTarget(info.img_connect, 1));//设置播放次数
            if(!isNull(beans.get(position).getBooKName())){
                if (beans.get(position).getBooKName().contains("年级")){
                    String tvClass = beans.get(position).getBooKName().substring(
                            beans.get(position).getBooKName().indexOf("年级")-1);
                    //教材
                    info.tv_content.setText(beans.get(position).getBooKName().replace(tvClass,""));
                    //年级
                    info.tv_Age.setText(tvClass);
                }else {
                    //教材
                    info.tv_content.setText(beans.get(position).getBooKName());
                    //年级
                    info.tv_Age.setText(beans.get(position).getAgeRange()+"岁");
                }
                //版本 1是同步，2是教辅，3是特色
                if (beans.get(position).getBookType().equals("1")){
                    info.tvType.setText("同步教材");
                }else if(beans.get(position).getBookType().equals("2")){
                    info.tvType.setText("教辅教材");
                }else {
                    info.tvType.setText("特色教材");
                }
            }
            if (isDel){
                info.img_del.setVisibility(View.VISIBLE);
            }else {
                info.img_del.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
    public final class KingSoftInfo{
        private ImageView img_connect,img_del;
        private TextView tv_content,tv_Age,tvType;//教材,年级，版本号
    }
}
