package com.kingsun.teacherclasspro.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.bean.BookMapBean;
import com.kingsun.teacherclasspro.bean.DownLoadBean;
import com.kingsun.teacherclasspro.bean.SocketBean;
import com.kingsun.teacherclasspro.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static com.kingsun.teacherclasspro.activity.BaseActivity.readLocalJson;

/****
 * 文件传输服务
 * @author Administrator
 *
 */
public class FileTransminssionService0330 extends Service{
	public static String fileDownPath = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download";
	public static final String TAG = "download";
	private String fileName=null;//文件名
	private String ip ;//服务器IP
	private String bookID = "",userID = "";
	private String content = "";
	private Socket socket;//向服务器端主动发消息的
	private Socket msgSocket;//大屏主动给平板发消息的，因为没有做心跳包
	private int bufSize = 10000,currentIndex = 0,port = 20000,msgSize = 100;//每个消息包的大小，不够的空格补全
	//上传文件总大小
	private long fileSize = 0;
	private int numContent = 1;
	private String prantPath = Environment.getExternalStorageDirectory().getPath() +
			"/KingSunSmart/Download/";
	private static final int MSG_INIT = 0;
	private static final int URL_ERROR = 1;
	private static final int NET_ERROR = 2;
	private static final int DOWNLOAD_SUCCESS = 3;
	private ArrayList<DownLoadBean> sendList = new ArrayList<>();
	private ArrayList<String> dataList = new ArrayList<>();
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			ip = intent.getStringExtra("ip");
			bookID = intent.getStringExtra("BookID");
			userID = intent.getStringExtra("UserID");
			Log.e(TAG,"IP "+intent.getStringExtra("ip")+";bookID "+intent.getStringExtra("BookID"));
			if(!BaseFragment.isEmty(ip)){
				dataList.add("data/"+userID+"/"+bookID+"/catalog.json");
				dataList.add("data/"+userID+"/"+bookID+".zip");
				new InitThread("").start();//主动链接大屏的线程
			}else{
				myHandler.sendEmptyMessage(URL_ERROR);
			}
			new getMsgThread().start();//大屏主动发消息给平板的线程
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//service销毁的时候执行
		//释放资源
		if (myHandler != null){
			myHandler.removeMessages(1);
			myHandler.removeMessages(2);
			myHandler.removeMessages(3);
			myHandler.removeMessages(4);
			myHandler.removeMessages(1916);
			myHandler = null;
		}
		//关闭线程
	}

	/**
	 * 初始化子线程
	 * @author huanghai
	 *
	 */
	class InitThread extends Thread{
		String con = "";

		/***
		 * 需要发送的字符串
		 * @param content
		 */
		public InitThread(String content) {
			this.con = content;
		}
		@Override
		public void run() {
			//定义消息
			Message msg = new Message();
			msg.what = 1;
			try {
				//连接服务器 并设置连接超时为1秒
				if (socket == null || socket.isClosed()) {
					socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), 1000);
				}
				if (dataList != null && dataList.size() > 0){
					File file = new File(prantPath+dataList.get(0));
					if (file.exists()){
						fileSize = file.length();
						String con = "0X11:"+dataList.get(0)+"&"+fileSize;
						if (con.length() < msgSize){
							String xp = "";
							for (int i = 0 ;i < msgSize-con.length() ;i++){
								//0X11后台默认取100个字节，不够100用空格补全
								xp += " ";
							}
							con  = con +xp;
						}
						sendMsg(con,false,"");
					}else{
						Log.e(TAG,"发送文本失败，文件不存在");
					}
				}
			}catch (SocketTimeoutException aa) {
				//连接超时 在UI界面显示消息
				msg.obj =  "服务器连接失败！请检查网络是否打开";
				//发送消息 修改UI线程中的组件
				myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * 发送文本信息
	 * @param con
	 */
	private void sendMsg(String con ,boolean sp,String conbody){
		Log.e(TAG, "传输文本开始 "+con);
		try {
			//获取输入输出流
			OutputStream ou;
			if (socket == null || socket.isClosed()) {
				socket= new Socket();
				socket.connect(new InetSocketAddress(ip, port), 1000);
			}
			ou = socket.getOutputStream();
			//向服务器发送信息
			Log.e(TAG, "开始发送数据 ："+con);
			ou.write(con.getBytes("UTF-8"));
			ou.flush();
			Log.e(TAG, "传输文本结束");
			if (sp){
				if (ou != null){
					//向服务器发送信息
					Log.e(TAG, "开始发送数据2 ："+conbody);
					ou.write(conbody.getBytes("UTF-8"));
					ou.flush();
//					ou.close();
				}
//				socket.close();
			}else{
				Message message = new Message();
				message.what = 3;
				message.obj = con;
				myHandler.sendMessage(message);
			}
		} catch (IOException e) {
			Log.e(TAG, "传输文本异常");
			e.printStackTrace();
		}
	}

	/***
	 * 发送文件信息
	 * @param file
	 *
	 */
	private void sendFile(String file){
		Log.e(TAG, "传输文件开始 "+file);
		//定义消息
		Message msg = new Message();
		msg.what = 1;
		try {
//			if (socket == null || socket.isClosed()) {
//				socket= new Socket();
//				socket.connect(new InetSocketAddress(ip, port), 1000);
//			}
			File tFile = new File(file);
			if (tFile.exists()){
				Log.e(TAG,"存在");
			}
			if(socket != null){
				OutputStream outputData = socket.getOutputStream();
				FileInputStream fileInput = new FileInputStream(file);
				int size = -1;
				int num = 0;
				byte[] buffer = new byte[bufSize];
				while((size = fileInput.read(buffer, 0, bufSize)) != -1){
					num+=size;
					outputData.write(buffer, 0, size);
					Log.e("UnRar","num = "+num);
				}
				if (outputData != null) {
					outputData.close();
				}
				if (fileInput != null) {
					fileInput.close();
				}
				Log.e(TAG, "传输文件完毕---------《");
				numContent +=1;
				socket.close();
				dataList.remove(0);
				if (dataList != null && dataList.size() >0){
					myHandler.sendEmptyMessage(4);
				}
			}
		} catch (SocketTimeoutException aa) {
			Log.e(TAG, "服务器连接失败！请检查网络是否打开");
			//连接超时 在UI界面显示消息
			msg.what = NET_ERROR;
			msg.obj =  "服务器连接失败！请检查网络是否打开";
			//发送消息 修改UI线程中的组件
			myHandler.sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "sendFile error");
		}
	}

	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case URL_ERROR:
					//IP地址为空
					ip = "192.168.3.187";
					break;

				case NET_ERROR:
					break;

				//表示可以传递文件了
				case 3:
					if (dataList != null && dataList.size() > 0){
						sendFile(prantPath+dataList.get(0));
					}
					break;

				case 4://重复传输文件
					Log.e(TAG, "开始第"+numContent+"次传输");
					new InitThread(content).start();
					break;

				case 1916:
					//接收大屏发过来的消息，被动接收
					String content = (String) msg.obj;
					SocketBean bean = null;
					try {
						JSONObject jsonObject = new JSONObject(content);
						String UserID = jsonObject.optString("UserID");
						String BookID = jsonObject.optString("BookID");
						String ClassID = jsonObject.optString("ClassID");
						String cbId = jsonObject.optString("cbId");
						Log.e(TAG, "cbId "+cbId);
						if (!BaseFragment.isEmty(cbId)){
							if (cbId.equals("selBookPageData_n")){
								String Pages = jsonObject.optString("Pages");
								String []p = Pages.split(",");
								if (p != null){
									ArrayList<String> list = new ArrayList<>();
									for (int i = 0; i< p.length ; i++){
										String pageNum = "page";
										int num = Math.abs(Integer.parseInt(p[i]));
										if (num < 10){
											pageNum +="00"+num;
										}else if (num < 100){
											pageNum +="0"+num;
										}else{
											pageNum +=num;
										}
										String path = fileDownPath+"/data/"+UserID+"/"+BookID+"/resource/"+pageNum+"/"+pageNum+".json";
										String info = readLocalJson(path);
										if (!BaseFragment.isEmty(info)){
											list.add(info);
										}
									}
									bean = new SocketBean(cbId,new Gson().toJson(list));
									sendMsg(bean);
								}
							}else if(cbId.equals("postTeachPage_n")){
								//保存操作记录
								String info = jsonObject.optString("pagedata");
								String path = fileDownPath+"/data/"+UserID+"/"+UserID+BookID+ClassID+".json";
								BaseFragment.saveFile(info,path);
							}else if(cbId.equals("getCurTeachMap_n")){
								String UnitID = jsonObject.optString("UnitID");
								String bookPath4 = fileDownPath+"/data/"+UserID+"/"+BookID+"/resource/"+UnitID+".json";
								String info = readLocalJson(bookPath4);
								bean = new SocketBean(cbId,info);
								sendMsg(bean);
								/**
								 * 开始对比数据，看看那些数据是需要传给PC端的
								 */
								if (!BaseFragment.isEmty(info)){
									ArrayList<BookMapBean> bookMapBean = KingSoftParasJson.getListByJson(info,BookMapBean.class);
									ArrayList<BookMapBean.BookMapInfo> bInfoList = new ArrayList<>();
									if (bookMapBean != null && bookMapBean.size() > 0){
										for (BookMapBean bookMapBean1 : bookMapBean){
											bInfoList.addAll(bookMapBean1.getLiList());
										}
									}
									Log.e(TAG,"bInfoList = "+bInfoList.size());
									String bookPath5 = fileDownPath+"/data/"+UserID+"/"+BookID+"_data.json";
									String info2 = readLocalJson(bookPath5);
									ArrayList<DownLoadBean> downLoadBeen = KingSoftParasJson.getListByJson(info2,DownLoadBean.class);
									sendList.clear();
									if (downLoadBeen != null && bInfoList != null){
										for (int j = 0; j < downLoadBeen.size(); j++ ){
											if (downLoadBeen.get(j) != null && downLoadBeen.get(j).getIsDownLoad() == 1){
												//只需要IsDownLoad() = 1的数据
												if (!BaseFragment.isEmty(downLoadBeen.get(j).getFileID())){
													for (int i = 0; i < bInfoList.size(); i++ ){
														if (bInfoList.get(i)!= null && bInfoList.get(i).getSourceSrc().
																equals(downLoadBeen.get(j).getFileID())){
															//保存当前的对比数据
															sendList.add(downLoadBeen.get(j));
															break;
														}
													}
												}
											}
										}
									}
									Log.e(TAG,"sendList = "+sendList.size());
									if (sendList != null){
										addMsg(sendList);
									}
								}
							}
						}
					}catch (JSONException e) {
						Log.e(TAG,"json 解析异常111 ");
						e.printStackTrace();
					}catch (Exception e){
						Log.e(TAG,"json 异常222 ");
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		}
	};

	/***
	 * 接收服务端发送消息
	 * @author hai.huang
	 *
	 */
	class getMsgThread extends Thread {
		public getMsgThread() {
			Log.e(TAG,"接收线程启动");
		}

		@Override
		public void run() {
			OutputStream output;
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				while (true) {
					Message msg = new Message();
					msg.what = 1916;
					try {
						msgSocket = serverSocket.accept();
						//获取输入信息
						BufferedReader bff = new BufferedReader(new InputStreamReader(msgSocket.getInputStream()));
						//读取信息
						String result = "";
						String buffer = "";
						while ((buffer = bff.readLine()) != null) {
							result = result + buffer;
						}
						msg.obj = result.toString();
						Log.i(TAG, "IP XXX= "+msgSocket.getInetAddress().getHostAddress());
						Log.i(TAG,"msg==XXX=====>"+result.toString());
						myHandler.sendMessage(msg);
						bff.close();
						msgSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/***
	 * 与PC通信，发送读取的本地数据给大屏
	 * @param bean
	 */
	private void sendMsg(final SocketBean bean){
		if (bean != null){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String readContent = new Gson().toJson(bean);
						byte[] bytes = readContent.getBytes("UTF-8");
						String con = "0X12:"+bytes.length;
						if ( con.length() < msgSize){
							String xp = "";
							for (int i = 0 ;i < msgSize-con.length() ;i++){
								//0X11后台默认取100个字节，不够100用空格补全
								xp += " ";
							}
							con  = con +xp;
						}
//						sendMsg(con,true,readContent);
						//获取输入输出流
						OutputStream ou;
						Socket mSocket= new Socket();
						mSocket.connect(new InetSocketAddress(ip, port), 1000);
						ou = mSocket.getOutputStream();
						//向服务器发送信息
						Log.e(TAG, "开始发送数据 --- ："+con);
						ou.write(con.getBytes("UTF-8"));
						ou.flush();
						Log.e(TAG, "传输文本结束 --1>");
						if (ou != null){
							//向服务器发送信息
							Log.e(TAG, "开始发送数据 -- 2 ："+readContent);
							ou.write(readContent.getBytes("UTF-8"));
							ou.flush();
//					ou.close();
						}
//						ou.close();
//						mSocket.close();
					}catch (Exception e){
						Log.e(TAG, "单独发送文本异常");
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	/***
	 * 添加消息到列表中
	 *
	 * @param been
	 */
	private void addMsg(ArrayList<DownLoadBean> been){
		//需要先判断原来消息列队中是否还有消息
		boolean isSend = false;
		if (dataList != null && dataList.size() >0){
			//表示原来消息列队中存在消息，现在只需要往里面添加消息即可，不需要重新发
			isSend = true;
		}
		if (been != null && been.size() >0 ){
			for (DownLoadBean b : been){
				dataList.add(b.getFilePath().replace("\\","/")+"/"+getFileName(b));
			}
		}
		Log.e(TAG,"isSend = "+isSend);
		if (!isSend){
			numContent = 1;
			new InitThread("").start();
		}
	}

	/***
	 * 获取文件名
	 * @param bean
	 * @return
	 */
	private String getFileName(DownLoadBean bean){
		String fileName = "";
		if (bean != null){
			File file = new File(fileDownPath+bean.getFilePath().replace("\\","/"));
			if (file.exists() && file.isDirectory()){
				//开始遍历文件
				for (File f : file.listFiles()){
					if (f.getName().startsWith(bean.getFileID())){
						fileName = f.getName();
						Log.e(TAG,"NAME "+f.getAbsolutePath());
						break;
					}
				}
			}
		}
		return fileName;
	}
}
