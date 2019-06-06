package com.kingsun.teacherclasspro.adpter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

public class MyBaseAdpter  extends BaseAdapter{

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getFileSizes(File f) throws Exception{//取得文件大小
		long s=0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s= fis.available();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	//double四舍五入保留2位小数
	public static String  getSize(double size){
		double get_double ;
		DecimalFormat df = new DecimalFormat("#.##");
		get_double = Double.parseDouble(df.format(size));
		return get_double+"";
	}

	public boolean isNull(String info){
		boolean sp = false;
		if (info == null || info .equals("") || info.equals("null")){
			sp = true;
		}
		return  sp;
	}
}
