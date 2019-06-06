package com.kingsun.teacherclasspro.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.BookMapBean;
import com.kingsun.teacherclasspro.bean.DownLoadBean;
import com.kingsun.teacherclasspro.bean.OperationRecordBean;
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
import java.util.Timer;
import java.util.TimerTask;

import static com.kingsun.teacherclasspro.activity.BaseActivity.Ilog;
import static com.kingsun.teacherclasspro.activity.BaseActivity.readLocalJson;

/****
 * 文件传输服务
 * @author Administrator
 *
 */
public class FileTransminssionService extends Service{
	public static String fileDownPath = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download";
	public static final String TAG = "download";
	private String ip ;//服务器IP
	private String bookID = "",userID = "",calssID = "";
	private boolean isRead = true;//线程标志位
	private Socket socket;//向服务器端主动发消息的
	//	private Socket msgSocket;//大屏主动给平板发消息的，因为没有做心跳包
	private int bufSize = 10000,port = 20000,msgSize = 100;//每个消息包的大小，不够的空格补全
	//上传文件总大小
	private long fileSize = 0;
	private int numContent = 1;
	private String prantPath = Environment.getExternalStorageDirectory().getPath() +
			"/KingSunSmart/Download";
	private static final int MSG_INIT = 0;
	private static final int URL_ERROR = 1;
	private static final int NET_ERROR = 2;
	private static final int DOWNLOAD_SUCCESS = 3;
	private InitThread msgThread;//发送文本消息线程。
	private getMsgThread thread;//监控服务器发过来的消息
	private ArrayList<DownLoadBean> sendList = new ArrayList<>();
	private Timer myTimer,sendTimer;
	private TimerTask myTask,sendTask;
	private ArrayList<String> dataList = new ArrayList<>();
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG,"onStartCommand COME "+flags);
		if(intent != null){
			ip = intent.getStringExtra("ip");
			bookID = intent.getStringExtra("BookID");
			userID = intent.getStringExtra("UserID");
			calssID = MyApplication.getInstance().getClassID();
			Log.e(TAG,"IP "+intent.getStringExtra("ip")+";bookID "+ MyApplication.getInstance().getBookID()
					+";ClassID "+calssID);
			isRead = true;
			MyApplication.getInstance().getMsgList().add("/data/"+userID+"/"+bookID+".zip");
			MyApplication.getInstance().getMsgList().add("/data/"+userID+"/"+bookID+"/catalog.json");
			MyApplication.getInstance().getMsgList().add("/data/"+userID+"/"+(userID+bookID+calssID)+".json");
			dataList.add("/data/"+userID+"/"+bookID+".zip");
			dataList.add("/data/"+userID+"/"+bookID+"/catalog.json");
			dataList.add("/data/"+userID+"/"+(userID+bookID+calssID)+".json");
			if(!BaseFragment.isEmty(ip)){
				if (myTimer != null) {
					myTimer.cancel();
					myTimer = null;
				}

				if (myTask != null) {
					myTask.cancel();
					myTask = null;
				}
				myTimer = new Timer();
				myTask = new TimerTask() {
					@Override
					public void run() {
						myHandler.sendEmptyMessage(6);
					}
				};
				myTimer.schedule(myTask, 5000);
			}else{
				myHandler.sendEmptyMessage(URL_ERROR);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG,"onCreate come ");
		MyApplication.getInstance().setMsgList(new ArrayList<String>());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//service销毁的时候执行
		//释放资源
		//关闭线程
		Log.e(TAG,"service onDestroy");
		try {
			if (myHandler != null){
				myHandler.removeMessages(1);
				myHandler.removeMessages(2);
				myHandler.removeMessages(3);
				myHandler.removeMessages(4);
				myHandler.removeMessages(6);
				myHandler.removeMessages(1916);
			}
			if (myTimer != null) {
				myTimer.cancel();
				myTimer = null;
			}

			if (myTask != null) {
				myTask.cancel();
				myTask = null;
			}

			if (sendTimer != null) {
				sendTimer.cancel();
				sendTimer = null;
			}

			if (sendTask != null) {
				sendTask.cancel();
				sendTask = null;
			}
			sendColseMsg();
			isRead = false;
			//释放线程
			if (thread != null){
				thread.stopThread();
			}

			if (msgThread != null){
				msgThread.interrupt();
				msgThread = null;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
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
			Log.e(TAG,"发送消息线程启动");
		}
		@Override
		public void run() {
			//定义消息
			Message msg = new Message();
			msg.what = 1;
			try {
				//连接服务器 并设置连接超时为1秒
				Log.e(TAG,"InitThread socket = "+socket+";ip "+ip);
				if (socket == null || socket.isClosed() || !socket.isConnected()) {
					socket = new Socket();
					socket.setReuseAddress(true);
					socket.connect(new InetSocketAddress(ip, port), 1000);
				}
				sendMsg();
			}catch (IOException e) {
				if (myTimer != null) {
					myTimer.cancel();
					myTimer = null;
				}

				if (myTask != null) {
					myTask.cancel();
					myTask = null;
				}
				myTimer = new Timer();
				myTask = new TimerTask() {
					@Override
					public void run() {
						myHandler.sendEmptyMessage(5);
					}
				};
				myTimer.schedule(myTask, 3000);
				e.printStackTrace();
			}
		}
	}

	/***
	 * 发送文本信息
	 * @param
	 */
	private void sendMsg(){
		Log.e(TAG,"发送文本开始 "+MyApplication.getInstance().getMsgList().size());
		try {
			if (MyApplication.getInstance().getMsgList() != null && MyApplication.getInstance().getMsgList().size() > 0){
				File file = new File(prantPath+MyApplication.getInstance().getMsgList().get(0));
				Log.e(TAG,"发送文本开始 "+prantPath+MyApplication.getInstance().getMsgList().get(0));
				if (file.exists()){
					fileSize = file.length();
					String con = "0X11:"+MyApplication.getInstance().getMsgList().get(0)+"&"+fileSize;
					if (con.length() < msgSize){
						String xp = "";
						for (int i = 0 ;i < msgSize-con.length() ;i++){
							//0X11后台默认取100个字节，不够100用空格补全
							xp += " ";
						}
						con  = con +xp;
						//获取输入输出流
						OutputStream ou ;
						Log.e(TAG,"发送文本开始 socket.isConnected() = "+socket.isConnected()+";" +
								"socket.isClosed() = "+socket.isClosed());
						if (socket == null || socket.isClosed() || !socket.isConnected()) {
							Log.e(TAG,"发送文本开始 重新创建 ");
							if (msgThread != null){
								msgThread.interrupt();
								msgThread = null;
							}
							msgThread = new InitThread("");
							msgThread.start();//主动链接大屏的线程
						}else{
							Log.e(TAG,"发送文本开始 socket2 = "+socket);
							ou = socket.getOutputStream();
							//向服务器发送信息
							Log.e(TAG, "开始发送数据 ："+con);
							ou.write(con.getBytes("UTF-8"));
							Log.e(TAG, "开始发送数据 ---：");
							ou.flush();
							Log.e(TAG, "传输文本结束");
							Log.e(TAG, "传输文本结束 dataList = "+MyApplication.getInstance().getMsgList().size());
						}
					}
				}else{
					Log.e(TAG,"发送文本失败，文件不存在");
					MyApplication.getInstance().getMsgList().remove(0);
					sendMsg();
				}
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
	private void sendFile(String file,boolean status){
		Log.e(TAG, "传输文件开始 "+file);
		//定义消息
		Message msg = new Message();
		msg.what = 1;
		try {
			File tFile = new File(file);
			if (tFile.exists()){
				Log.e(TAG,"存在");
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
					if (fileInput != null) {
						fileInput.close();
					}
					Log.e(TAG, "传输文件完毕---------《");
//					socket.close();
					numContent +=1;
					if (status){
						MyApplication.getInstance().getMsgList().remove(0);
						if (MyApplication.getInstance().getMsgList() != null && MyApplication.getInstance().getMsgList().size() >0){
							myHandler.sendEmptyMessage(4);
						}
					}
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
//				case URL_ERROR:
//					//IP地址为空
//					ip = "192.168.3.187";
//					break;
//
//				case NET_ERROR:
//					break;

				//表示可以传递文件了
				case 3:
					if (MyApplication.getInstance().getMsgList() != null &&
							MyApplication.getInstance().getMsgList().size() > 0){
						sendFile(prantPath+MyApplication.getInstance().getMsgList().get(0),true);
					}
					break;

				case 4://重复传输文件
					Log.e(TAG, "开始第"+numContent+"次传输");
					sendMsg();
					break;

				case 5:
					//开始启动发消息线程失败，服务器未开启
					if(msgThread != null){
						msgThread.interrupt();
						msgThread = null;
						msgThread = new InitThread("");
						msgThread.start();//主动链接大屏的线程
					}
					break;

				case 6:
					if (myTimer != null) {
						myTimer.cancel();
						myTimer = null;
					}

					if (myTask != null) {
						myTask.cancel();
						myTask = null;
					}
					//开始启动线程
					msgThread = new InitThread("");
					msgThread.start();//主动链接大屏的线程

					thread = new getMsgThread();
					thread.start();//大屏主动发消息给平板的线程
					break;

				case 8:
					//表示发送的消息服务器没有收到，重新创建socket，兼容.net后台收不到消息的情况
					try {
//						if (socket != null){
//							socket.close();
//						}
						if (msgThread != null){
							msgThread.interrupt();
							msgThread = null;
						}
						msgThread = new InitThread("");
						msgThread.start();//主动链接大屏的线程
					}catch (Exception e){
						e.printStackTrace();
					}
					break;

				case 10:
					String filePath  = (String) msg.obj;
					sendFile(filePath,true);
					break;

				case 1916:
					//接收大屏发过来的消息，被动接收
					String content = (String) msg.obj;
					Log.e(TAG,"content = "+content);
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
							}else if(cbId.equals("getFileStatus_n")){
								//读取指定文件发给PC
								String data = jsonObject.optString("data");
								data = data.replace("\\","/");
								String fileName = data.substring(data.lastIndexOf("/")+1, data.length());
								String fileAPath = (fileDownPath+data).replace(fileName,"");
								String xp = BaseActivity.getFileName(fileAPath,fileName);
								MyApplication.getInstance().getMsgList().add(data.replace(fileName,xp));
								sendMsg();
							}else if(cbId.equals("sendFileStatus_n")){
								//判断是否可以继续发文件
								String data = jsonObject.optString("data");
								boolean status = jsonObject.optBoolean("Status");
								Log.e(TAG,"data = "+data+";status = "+status+";dataList = "+
										MyApplication.getInstance().getMsgList());
								Log.e(TAG,"Socket.isConnected= "+socket.isConnected()+";socket.isClosed() = "+socket.isClosed()+";");
								if (status){
									//表示服务器有该文件，不需要发送直接发送下个文件
									if (MyApplication.getInstance().getMsgList() != null &&
											MyApplication.getInstance().getMsgList().size() > 0){
										for (String s : MyApplication.getInstance().getMsgList()){
											if (s.equalsIgnoreCase(data)){
												MyApplication.getInstance().getMsgList().remove(s);
												break;
											}
										}
										Log.e(TAG,"sendFileStatus_n = "+MyApplication.getInstance().getMsgList().size());
										if (MyApplication.getInstance().getMsgList() != null &&
												MyApplication.getInstance().getMsgList().size() > 0){
											sendMsg();//发送下一个文件
										}else {
											Log.e(TAG,"MyApplication.getInstance().getMsgList() = null");
										}
									}else {
										Log.e(TAG,"MyApplication.getInstance().getMsgList() = null ---------");
									}
								}else{
									//表示需要发送这个文件
									if (socket == null || socket.isClosed() || !socket.isConnected()) {
										//如果socket 关闭需要重新开启，重新发这个消息
										sendMsg();//发送下一个文件
									}else {
										Message message = new Message();
										message.what = 3;
										myHandler.sendMessage(message);
									}
								}
							}else if(cbId.equals("postTeachPage_n")){
								//保存授课页码
								String info = jsonObject.optString("pagedata");
								String path = fileDownPath+"/data/"+UserID+"/"+UserID+BookID+ClassID+".json";
								BaseFragment.saveFile(info,path);
							}else if(cbId.equals("userOperLog_n")){
								//保存用户操作记录
								String path = fileDownPath+"/data/"+UserID+"/userOperLog_n.json";
								String info = readLocalJson(path);
								ArrayList<OperationRecordBean> list = new ArrayList<>();
								OperationRecordBean op = (OperationRecordBean)KingSoftParasJson.getObjectByJson(content,OperationRecordBean.class);
								if(!BaseFragment.isEmty(info)){
									ArrayList<OperationRecordBean> beans = KingSoftParasJson.getListByJson(info,OperationRecordBean.class);
									if (beans != null && beans.size() >0 ){
										list.addAll(beans);
									}
								}
								list.add(op);
								BaseFragment.saveFile(new Gson().toJson(list),path);
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
		ServerSocket serverSocket;
		public getMsgThread() {
			Log.e(TAG,"接收线程启动");
		}

		public void stopThread(){
			isRead = false;
			if (serverSocket != null){
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while (isRead) {
				Message msg = new Message();
				msg.what = 1916;
				try {
					Socket  msgSocket = serverSocket.accept();
					Log.i(TAG,"接收线程 "+msgSocket.getInetAddress().getHostAddress());
					//获取输入信息
					BufferedReader bff = new BufferedReader(new InputStreamReader(msgSocket.getInputStream()));
					//读取信息
					String result = "";
					String buffer = "";
					while ((buffer = bff.readLine()) != null) {
						result = result + buffer;
					}
					msg.obj = result.toString();
					Log.i(TAG,"msg==XXX=====>"+result.toString());
					if(myHandler.hasMessages(8)){
						myHandler.removeMessages(8);
					}
					if (sendTimer != null) {
						sendTimer.cancel();
						sendTimer = null;
					}

					if (sendTask != null) {
						sendTask.cancel();
						sendTask = null;
					}
					myHandler.sendMessage(msg);
					bff.close();
					msgSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
						//获取输入输出流
						OutputStream ou;
						Ilog(TAG,"SOCKET "+socket.isConnected()+"; "+socket.isClosed());
						if (socket == null || socket.isClosed() || !socket.isConnected()) {
							//如果socket 关闭需要重新开启，重新发这个消息
							socket = new Socket();
							socket.setReuseAddress(true);
							socket.connect(new InetSocketAddress(ip, port), 1000);
						}
//						Socket mSocket= new Socket();
//						mSocket.connect(new InetSocketAddress(ip, port), 1000);
						ou = socket.getOutputStream();
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
						}
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
		if (MyApplication.getInstance().getMsgList() != null &&
				MyApplication.getInstance().getMsgList().size() >0){
			//表示原来消息列队中存在消息，现在只需要往里面添加消息即可，不需要重新发
			isSend = true;
		}
		if (been != null && been.size() >0 ){
			for (DownLoadBean b : been){
				MyApplication.getInstance().getMsgList().
						add(b.getFilePath().replace("\\","/")+"/"+getFileName(b));
			}
		}
		Log.e(TAG,"isSend = "+isSend);
		if (!isSend){
			numContent = 1;
			sendMsg();
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
						break;
					}
				}
			}
		}
		return fileName;
	}


	/***
	 * 销毁socket的时候告诉服务器关闭
	 */
	private void sendColseMsg(){
		try {
			if (socket == null || socket.isClosed() || !socket.isConnected()) {
				Log.e(TAG,"close 重新创建 ");
				socket.close();
				socket = null;
				socket= new Socket();
				socket.connect(new InetSocketAddress(ip, port), 1000);
			}
			String con = "0X13";
			Log.e(TAG,"close socket2 = "+socket);
			OutputStream ou = socket.getOutputStream();
			//向服务器发送信息
			Log.e(TAG, "close ："+con);
			ou.write(con.getBytes("UTF-8"));
			ou.flush();
			if (ou != null){
				ou.close();
			}
			socket.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
