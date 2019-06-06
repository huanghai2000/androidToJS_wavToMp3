package com.kingsun.teacherclasspro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.BookInfoBean;
import com.kingsun.teacherclasspro.bean.DownLoadBean;
import com.kingsun.teacherclasspro.utils.KingSoftParasJson;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import static com.kingsun.teacherclasspro.activity.BaseActivity.Elog;
import static com.kingsun.teacherclasspro.activity.BaseActivity.createSDDir;

public class BaseFragment extends Fragment{
	public static String fileDownPath = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download";
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 *
	 * @param sp
	 * @return true表示是空 false不是空
	 */
	public static boolean isEmty(String sp){
		boolean result = false;
		if (sp == null || sp.equals("") || sp.equals("null")|| sp.equals("[]")) {
			result = true;
		}
		return result;
	}

	//英语键盘字符
	//小写键盘
	public static ArrayList<String> otherList = new ArrayList<>(Arrays.asList(
			"a", "b", "c", "d", "e","f", "g", "h",
			"i", "j", "k", "l", "m", "n", "o", "p",
			"q", "r", "s", "t","u", "v", "w", "x", "y", "z",
			"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D",
			"F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M",
			"0", "1", "2", "3", "4","5", "6", "7","8","9"));

	//小写键盘
	public static ArrayList<String> KBLowlerArrayList = new ArrayList<>(Arrays.asList("q", "w", "e", "r", "t",
			"y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l","del","大写", "z",
			"x", "c", "v", "b", "n", "m","符号","确定"));
	//大写键盘
	public static ArrayList<String> KBCapitalData = new ArrayList<>(Arrays.asList("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D",
			"F", "G", "H", "J", "K", "L","del","小写", "Z", "X", "C", "V", "B", "N", "M","符号","确定"));
	//符号键盘
	public static ArrayList<String> KBSymbolArrayList = new ArrayList<>(Arrays.asList("0", "1", "2", "3","4","5", "6", "7", "8", "9", ".", ",",
			"…", "?", "!", ":", ";", "\"", "\'","del", " ","(", ")", "空格", "-","—", "/","字母","确定"," "));


	/**
	 * 下载
	 */
	public void downFile(final DownLoadBean bean , final Handler handler) {
//		Elog("MainActivity","downFile RUL = "+bean.getFileURL());
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(bean.getFileURL());
				HttpResponse response;
				try {
					response = client.execute(get);
					Header[] headers = response.getAllHeaders();
					String name = "";//文件后缀名
					String path = "",filePath = "";
					String fileLength = "";
					for (Header x :headers){
//						Elog("MainActivity","name "+x.getName()+";value "+x.getValue());
//						if (x.getName().equals("Content-Length")){
//							fileLength = x.getValue();
////							continue;
//						}
//						if (x.getName().equals("Content-Type")){
//							name = x.getValue().replace("application/",".");
//							break;
//						}
						if (x.getName().equals("content-disposition")){
							name = x.getValue();
							name = name.substring(name.indexOf("filename="), name.length())
									.replace("filename=","")
									.replace("%20"," ");
							name = name.substring(name.lastIndexOf("."));
							break;
						}
					}
//					Elog("MainActivity","fileLength = "+fileLength);
					path = bean.getFilePath().replace("\\","/").substring(1);
					if (!BaseActivity.isFileExist(path)) {
						createSDDir(path);
					}
					File downFile;
					if (isEmty(name)){
						filePath = fileDownPath+"/"+path+"/"+bean.getFileID()+".zip";
					}else{
						filePath = fileDownPath+"/"+path+"/"+bean.getFileID()+name;
					}
					bean.setFileName(path+"/"+bean.getFileID()+name);
					if (bean.getIsDownLoad() == 2){
						//表示下载书本资料，需要解压
						Message msg = new Message();
						msg.what = 1002;
						msg.obj = filePath;
						handler.sendMessage(msg);
					}
					downFile = new File(filePath);
					if (!downFile.exists()){//不存在就下载
						HttpEntity entity = response.getEntity();
						long length = entity.getContentLength();
						InputStream is = entity.getContent();
						FileOutputStream fileOutputStream = null;
						if (is != null) {
							fileOutputStream = new FileOutputStream(downFile);
							byte[] b = new byte[10240];
							int charb = -1;
							int count = 0;
							while ((charb = is.read(b)) != -1) {
								fileOutputStream.write(b, 0, charb);
								count += charb;
//								Elog("MainActivity","下载进度 "+count+" ; size =" +
//										length+"; ==> "+(count/(length/100)));
								Message msg = new Message();
								msg.what = 1001;
								if (length == -1){
									msg.obj =  -1l;
								}else{
									msg.obj = (count/(length/100));
								}
								msg.arg2 = count;
								handler.sendMessage(msg);
							}
						}
						fileOutputStream.flush();
						if (fileOutputStream != null) {
							fileOutputStream.close();
							if (length == -1){
								//表示未知大小的文件下载完毕
								Message msg = new Message();
								msg.what = 1001;
								msg.obj = 100L;
								handler.sendMessage(msg);
								Elog("MainActivity","获取大小异常，下载完成");
							}
						}
						if (is != null){
							is.close();
						}
					}else{
						//存在就直接跳过
						Message msg = new Message();
						msg.what = 1001;
						msg.obj = 100L;
						msg.arg2 = 100;
						Elog("MainActivity","存在不需要重复下载");
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					Elog("MainActivity","下载出错");
					e.printStackTrace();
				}
			}
		}.start();
	}


	/***
	 * 字符串保存本地文件
	 * @param been
	 */
	public void SaveFileList(final ArrayList<DownLoadBean> been){
		new Thread() {
			public void run() {
				if (been != null && been.size() > 0){
					for (DownLoadBean loadBean : been){
						String fileName = "";
						String str = "";
						if (loadBean != null ){
							if (loadBean.getIsDownLoad() == 0){
								fileName = fileDownPath+"/"+loadBean.getFilePath().replace("\\","/");
								str = loadBean.getFileURL();
							}else if (loadBean.getIsDownLoad() == 2){
								fileName = fileDownPath+"/"+loadBean.getBookData().replace("\\","/");
								str = loadBean.getOther();
							}
						}
						File dir = new File(fileName.substring(0,fileName.lastIndexOf("/")));
						if (!dir.exists()) {
							dir.mkdirs();
						}
						File myjson = new File(fileName);
						BufferedWriter bufferedWriter = null;
						try {
							bufferedWriter = new BufferedWriter(new FileWriter(myjson));
							for (int i = 0; i < str.length(); i++) {
								bufferedWriter.write(str.charAt(i));
							}
							bufferedWriter.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}

	/***
	 *
	 * @param info 需要保存的数据
	 * @param savaPath  保存的绝对路径
	 */
	public static void saveFile(final String info,final String savaPath){
		new Thread() {
			public void run() {
				String path = savaPath.substring(0, savaPath.lastIndexOf("/"));
				if (!new File(path).exists()){
					new File(path).mkdirs();
				}
				File myjson = new File(savaPath);
				BufferedWriter bufferedWriter = null;
				try {
					bufferedWriter = new BufferedWriter(new FileWriter(myjson));
					for (int i = 0; i < info.length(); i++) {
						bufferedWriter.write(info.charAt(i));
					}
					bufferedWriter.close();
				} catch (Exception e) {
					Elog("Fragment","写入文件出错");
					e.printStackTrace();
				}
			}
		}.start();
	}

	//读取缓存数据
	public static String readCacheDate(final String readPath){
		File myjson = new File(readPath);
		String str = "";
		if (myjson.exists() && myjson.length() > 0) {
			//加载以前的数据
			InputStreamReader inputReader = null;
			BufferedReader bufferReader = null;
			try {
				InputStream inputStream = new FileInputStream(myjson);
				inputReader = new InputStreamReader(inputStream);
				bufferReader = new BufferedReader(inputReader);
				String line = null;
				StringBuffer stringBuffer = new StringBuffer();
				while ((line = bufferReader.readLine()) != null) {
					stringBuffer.append(line);
				}
				str = stringBuffer.toString();
				bufferReader.close();
				inputReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	public interface dataChangeListener{
		void changeData(Intent intent);
	}

	/*
   * 函数名：getFile
   * 作用：使用递归，输出指定文件夹内的所有文件
   * 参数：path：文件夹路径   deep：表示文件的层次深度，控制前置空格的个数
   * 前置空格缩进，显示文件层次结构
   */
	public static boolean delFile(String path, String id){
		// 获得指定文件对象
		File file = new File(path);
		// 获得该文件夹内的所有文件
		File[] array = file.listFiles();
		if (array != null && array.length > 0){
			try {
				for(int i=0;i<array.length;i++)
				{
					if(array[i].isFile())//如果是文件
					{
						if (array[i].getName().startsWith(id) ||
								array[i].getName().startsWith(MyApplication.getInstance().getUserID()+id)){
							array[i].delete();
						}
					}
				}
			}catch (Exception e){
				e.printStackTrace();
				return  false;
			}
		}
		return true;
	}

	/***
	 * 删除那本教材，
	 * @param id
	 */
	public String delBookFile(String id){
		String bookInfo = readCacheDate(fileDownPath+"/data/book.json");
		String info = "";
		if (!isEmty(bookInfo)){
			ArrayList<BookInfoBean> list = KingSoftParasJson.getListByJson(bookInfo,BookInfoBean.class);
			if (list != null && list.size() > 0){
				for (BookInfoBean bean : list){
					if (bean != null && bean.getID().equals(id)){
						list.remove(bean);
						break;
					}
				}
				if (list != null && list.size() > 0){
					info = new Gson().toJson(list);
				}
				saveFile(info,fileDownPath+"/data/book.json");
			}
		}
		return  info;
	}
}
