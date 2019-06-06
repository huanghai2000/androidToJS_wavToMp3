package com.kingsun.teacherclasspro.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.myjson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.BookInfoBean;
import com.kingsun.teacherclasspro.bean.CheckBean;
import com.kingsun.teacherclasspro.bean.DownLoadBean;
import com.kingsun.teacherclasspro.bean.NewProBean;
import com.kingsun.teacherclasspro.bean.OperationRecordBean;
import com.kingsun.teacherclasspro.bean.SendBean;
import com.kingsun.teacherclasspro.bean.SendBeanByH5;
import com.kingsun.teacherclasspro.bean.SendBeanByH5_PageData;
import com.kingsun.teacherclasspro.bean.TestBean;
import com.kingsun.teacherclasspro.bean.UPBean;
import com.kingsun.teacherclasspro.bean.UpLoadBean;
import com.kingsun.teacherclasspro.bean.UpLoadBean.otherBean;
import com.kingsun.teacherclasspro.callback.MyHandlerCallBack;
import com.kingsun.teacherclasspro.config.Configure;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.config.HandlerStrings;
import com.kingsun.teacherclasspro.dialog.ExitDialog;
import com.kingsun.teacherclasspro.dialog.setIpAndPortDialog;
import com.kingsun.teacherclasspro.dialog.upLoadProDialog;
import com.kingsun.teacherclasspro.fragment.BaseFragment;
import com.kingsun.teacherclasspro.receiver.NetBroadcastReceiver;
import com.kingsun.teacherclasspro.utils.AppConfig;
import com.kingsun.teacherclasspro.utils.CheckPermissionUtils;
import com.kingsun.teacherclasspro.utils.Check_UnRar;
import com.kingsun.teacherclasspro.utils.Check_UnZip;
import com.kingsun.teacherclasspro.utils.HttpMultipartPost;
import com.kingsun.teacherclasspro.utils.KeyBoardListener;
import com.kingsun.teacherclasspro.utils.KingSoftParasJson;
import com.kingsun.teacherclasspro.utils.PhotoSelUtil;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.kingsun.teacherclasspro.utils.Utils;
import com.kingsun.teacherclasspro.widgets.MyHandler;
import com.kingsun.teacherclasspro.widgets.MyWebView;
import com.kingsun.teacherclasspro.widgets.WVJBWebViewClient;
import com.kingsun.teacherclasspro.widgets.WVJBWebViewClient.WVJBResponseCallback;
import com.torment.lame.LameUtils;
import com.xs.SingEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements  MyHandlerCallBack ,OnKeyListener{
    MyHandler myHandler = new MyHandler(this);
    static MainActivity mManager = null;
    private static String TAG ="MainActivity";
    //下载文件名
    private String UPDATE_SERVERAPK = "";
    private String fileName = "";
    private static final int  REQUEST_FILE_PICKER = 100;
    private String  upLoadUrl = "";
    private upLoadProDialog upDialog ;
    private MediaRecorder mRecorder;
    private CheckPermissionUtils permissionUtils;
    private  String path = "";
    private NetBroadcastReceiver receiver;
    private int  upIndex = 0;
    private  setIpAndPortDialog portDialog;
    public  SharedPreferences sp;
    private String url = "";
    private Button btn;
    private ProgressDialog dlgMixing;
    private boolean isDown = true;
    private HttpMultipartPost post;
    private String upLoadName = "";
    private Timer myTimer;
    private TimerTask myTask;
    private PhotoSelUtil photoSelUtil;
    private String loadPhotoUrl = "";
    private String ModeInfo = "HUAWEI";//设备类型
    private String zimu = "",recordType;
    private boolean isOld = true,isSend ;//是否是老版本
    private int index = 1 ;
    private MediaPlayer player;
    private int score = 0,screenHeight = 0;
    private MyWebView webView;
    Gson gson = new Gson();
    private TestBean testBean;
    protected WVJBWebViewClient webViewClient;
    private String mp3File,wavFile;
    private SingEngine engine;
    private String UpLoadUrl;
    private Map<Integer,TestBean> map = new HashMap<>();
    private boolean isNewType;//是否是新课件
    private String backType;//前端通过此字段判断消息是哪个函数处理
    public static final String ACTION_UPLOAD = "com.clovsoft.ik.RemoteRecorder.MEDIA_FILE";
    public static final String OPEN_READ = "open.classroom.read";
    public static final String OPEN_XUNFEI_API = "open.xuefei_api";
    public static final String OPEN_ANIMATION = "open.calssroom.animation";//打开动画
    public static final String OPEN_IMAGE = "open.calssroom.image";//打开图片
    public static final String EXIT_CLASSROOM = "exit.calssroom";//退出电子书
    public static final String NEXIT_CLASSROOM = "next.calssroom";//下一页
    public static final String UP_CLASSROOM = "UP.calssroom";//上一页
    public static final String OPEN_EBOOKE = "open.calssroom.ebook";//打开电子书
    public static final String CLASSROOM_DICTATION = "calssroom.dictation";//报听写
    private int currentIndex = 0;
    private String UserID,DBJSPath;
    /***************************************测试路数*******************************************/
    private String testPath2 = Environment.getExternalStorageDirectory().getPath()+
            "/KingSunSmart/Page/data/49a92e48-1f39-4382-9273-0a0d521162c6/332/" +
            "resource/page002/950baed5-b7de-48dc-b00b-1a14713f6628/ren_mang/";
    private String checkName = "zy",checkPWD = "123456";
    private BookInfoBean bean ;
    /***********************************************************************************/

    private ArrayList<DownLoadBean> downLoadBeen = new ArrayList<>();//备课数据下载集合
    private ArrayList<DownLoadBean> dBeen = new ArrayList<>();//需要下载的备课数据下载集合
    private ArrayList<DownLoadBean> rBeen = new ArrayList<>();//需要下载的备课数据下载集合
    //修改IP地址
    public  doThing doThing = new doThing() {
        @Override
        public void changeIP(String head,String ip) {
            if (webView != null) {
                Editor e = sp.edit();
                e.putString("HEAD", ip);
                e.commit();
                MyApplication.getInstance().setServer_head(ip);
                url = head+ip;
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
                        downloadHandle.sendEmptyMessage(11);
                    }
                };
                myTimer.schedule(myTask, 3000);
                checkURL(url,downloadHandle);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("KINGSUNTEACHER",MODE_PRIVATE);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        if (Build.VERSION.SDK_INT <= 19){
            if (Build.MODEL.equals("RS2")){
                url = AppConfig.MathineUrl;
            }else {
                url = AppConfig.MathineUrl_Nomarl;
            }
        }
        Elog(TAG,"url "+url);
        if (MyApplication.getInstance().getLoginBean() != null){
            UserID = MyApplication.getInstance().getLoginBean().getId();
        }
        bean = (BookInfoBean) intent.getSerializableExtra("Bean");
        if (bean != null){
            DBJSPath = bean.getImgPath().substring(0, bean.getImgPath().lastIndexOf("/"))+"/db.js";
        }
        photoSelUtil = new PhotoSelUtil();
        ModeInfo = Build.MANUFACTURER;//用来区别各个厂商

        com.kingsun.teacherclasspro.utils.Configure.init(this);
        screenHeight = com.kingsun.teacherclasspro.utils.Configure.screenHeight;
//        Ilog(TAG, "w = "+com.kingsun.teacherclasspro.utils.Configure.screenWidth+";h = "+
//                com.kingsun.teacherclasspro.utils.Configure.screenHeight);

//        url = "file:"+Environment.getExternalStorageDirectory().getPath()+
//                "/KingSunSmart/Page/index.html";

        intView(); //初始化界面
        permissionUtils = CheckPermissionUtils.getinstance();
        String sp = getVersion();
        portDialog = new setIpAndPortDialog(MainActivity.this,doThing,sp);
        IntentFilter filter = new IntentFilter();
        filter.addDataScheme("file");
        filter.addAction(ACTION_UPLOAD);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(myReceiver, filter);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(OPEN_READ);
        filter1.addAction(OPEN_XUNFEI_API);

        filter1.addAction(OPEN_ANIMATION);//打开动画
        filter1.addAction(OPEN_IMAGE);//打开图片
        filter1.addAction(EXIT_CLASSROOM);//退出电子书
        filter1.addAction(NEXIT_CLASSROOM);//下一页
        filter1.addAction(UP_CLASSROOM);//上一页
        filter1.addAction(OPEN_EBOOKE);//打开电子书
        filter1.addAction(CLASSROOM_DICTATION);//报听写

        filter1.setPriority(Integer.MAX_VALUE);
        registerReceiver(robotReceiver, filter1);

        receiver = new NetBroadcastReceiver();
        IntentFilter filters = new IntentFilter();
        filters.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filters);

        dlgMixing = new ProgressDialog(this);
        dlgMixing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgMixing.setCancelable(false);
        dlgMixing.setCanceledOnTouchOutside(false);
    }

    @SuppressLint("NewApi")
    private void intView() {
        initSingEnge();
        setContentView(R.layout.activity_main);
        KeyBoardListener.getInstance(this).init();
        btn = (Button) findViewById(R.id.login);
        webView = (MyWebView) findViewById(R.id.webView_main);
        webView.setBackgroundColor(getResources().getColor(R.color.app_bg));
        Ilog(TAG,"系统版本 "+Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
            // 开启app内调试的开关，开启后将可以在谷歌浏览器中调试js
            webView.setWebContentsDebuggingEnabled(Configure.isH5WebContentsDebuggingEnabled);
//            }
        }
        webViewClient = new WVJBWebViewClient(webView);
        webViewClient.webViewLoadFinished(myHandler);
        webViewClient.enableLogging();
        webView.addJavascriptInterface(this,"jswritecard");
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int progress){
            }
        });

        webView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setWebViewClient(webViewClient);

//        url = "http://192.168.3.187:2014/FrontWeb/xbzsb/index.html?phone=15601662752";
//        url = "http://192.168.3.115:2004/";
        webView.loadUrl(url);// 加载H5主页


        /**
         * 支付失败
         */
        webViewClient.registerHandler("payFail", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"payFail =x==>"+datas);
            }
        });

        /**
         * 解密json
         */
        webViewClient.registerHandler("aesDecryption", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"aesDecryption =x==>"+datas);
            }
        });

        /**
         * 解密json
         */
        webViewClient.registerHandler("aesDecryption_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"aesDecryption_n =x==>"+datas);
            }
        });

        /**
         * 支付成功
         */
        webViewClient.registerHandler("paySuccess", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"paySuccess =x==>"+datas);
            }
        });

        /**
         * 启动易课+接口
         */
        webViewClient.registerHandler("startAPP", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"startAPP =x==>"+datas);
                Utils.gotoXKJ(MainActivity.this,"com.kingsunedu.ik",datas.toString(),"com.clovsoft.ik.MainActivity");
            }
        });


        /**
         * 'Islogin表示回到了登录页面，需要注册输入法监听
         */
        webViewClient.registerHandler("Islogin", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"Islogin =x==>"+datas);
                KeyBoardListener.getInstance(MainActivity.this).init();
            }
        });


        /**
         * '保存用户名密码
         */
        webViewClient.registerHandler("getInfo_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"getInfo_n =x==>"+datas);
                if (sp != null) {
                    String info = sp.getString("userInfo", "");
                    sendMsgToNewH5("getInfo_n",info);
                }
            }
        });

        /****************************************1.3新增加接口**********************************/
        /**
         * 检测录音权限
         */
        webViewClient.registerHandler("recordAuthority_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"recordAuthority_n =x==>"+datas);
                backType = "recordAuthority_n";
                isNewType = true;
                stopPlayRecord();
                if (datas != null && !datas.equals("")){
                    isOld = false;
                    testBean = (TestBean) KingSoftParasJson.getObjectByJson(datas.toString(),TestBean.class);
                    if (testBean != null){
                        zimu = testBean.getCaptions();
                        recordType = testBean.getRecordType();
                        index = testBean.getWordIndex();
                        fileName = testBean.getResourceName()+testBean.getWordIndex();
                        wavFile = Environment.getExternalStorageDirectory().getPath() + "/KingSunSmart/Download/" +
                                "recordCache/" +MyApplication.getInstance().getLoginName()+"/"+ fileName + ".wav";
//                        Log.i(TAG, "checkRcord wavFile = " + wavFile);
                        testBean.setMp3Url(Environment.getExternalStorageDirectory().getPath() + "/KingSunSmart/Download/" +
                                "recordCache/"  +MyApplication.getInstance().getLoginName()+"/"+ fileName + ".mp3");
                        testBean.setUpload(false);
                        map.put(index,testBean);
                        if (engine != null) {
                            engine.setWavPath(wavFile);
                        }
                    }
                }
                checkPrimiss();
            }
        });

/********************************************TEST*************************************************/
        /**
         * 读取本地JS文件
         */
        webViewClient.registerHandler("getTeachPage_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Elog(TAG,"getTeachPage_n come ---》"+datas);
                try {
                    JSONObject jsonObject = new JSONObject(datas.toString());
                    String UserID = jsonObject.optString("UserID");
                    String BookID = jsonObject.optString("BookID");
                    String ClassID = jsonObject.optString("ClassID");
                    String path = fileDownPath+"/data/"+UserID+"/"+(UserID+BookID+ClassID)+".json";
                    String info = readLocalJson(path);
//                    TeacheBean beanByH5 = new TeacheBean();
                    SendBeanByH5 beanByH5 = new SendBeanByH5();
                    beanByH5.setCode("0");
                    beanByH5.setMessage("");
                    beanByH5.setSuccess(true);
                    beanByH5.setData(info);
//                    if (isNULL(info)){
//                        beanByH5.setData(new ArrayList<TeacheBean.otherBean>());
//                    }else{
//                        ArrayList<TeacheBean.otherBean> been = KingSoftParasJson.getListByJson(info,
//                                TeacheBean.otherBean.class);
//                        beanByH5.setData(been);
//                    }
                    String tt = gson.toJson(beanByH5).toString();
                    sendMsgToNewH5("getTeachPage_n",tt);
                } catch (JSONException e) {
                    Elog(TAG,"getTeachPage_n 解析错误");
                    e.printStackTrace();
                }
            }
        });


        /**
         * 读取本地JS文件
         */
        webViewClient.registerHandler("postTeachPage_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Elog(TAG,"postTeachPage_n come ---》"+datas);
                try {
                    JSONObject jsonObject = new JSONObject(datas.toString());
                    String UserID = jsonObject.optString("UserID");
                    String BookID = jsonObject.optString("BookID");
                    String ClassID = jsonObject.optString("ClassID");
                    String info = jsonObject.optString("pagedata");
                    String path = fileDownPath+"/data/"+UserID+"/"+UserID+BookID+ClassID+".json";
                    BaseFragment.saveFile(info,path);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * 读取本地JS文件
         */
        webViewClient.registerHandler("userOperLog_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Elog(TAG,"userOperLog_n come ---》"+datas);
                try {
                    //保存用户操作记录
                    String path = fileDownPath+"/data/"+UserID+"/userOperLog_n.json";
                    String info = readLocalJson(path);
                    ArrayList<OperationRecordBean> list = new ArrayList<>();
                    OperationRecordBean op = (OperationRecordBean)KingSoftParasJson.getObjectByJson(datas.toString(),
                            OperationRecordBean.class);
                    if(!BaseFragment.isEmty(info)){
                        ArrayList<OperationRecordBean> beans = KingSoftParasJson.getListByJson(info,OperationRecordBean.class);
                        if (beans != null && beans.size() >0 ){
                            list.addAll(beans);
                        }
                    }
                    list.add(op);
                    BaseFragment.saveFile(new com.google.gson.Gson().toJson(list),path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        /**
         * 获取电子书dbjs
         */
        webViewClient.registerHandler("getDbJsById_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Elog(TAG,"getDbJsById_n come ---》"+datas);
                String info = readLocalJson(DBJSPath);
                SendBeanByH5 beanByH5 = new SendBeanByH5();
                beanByH5.setCode("0");
                beanByH5.setMessage("");
                beanByH5.setSuccess(true);
                beanByH5.setData(info);
                String tt = gson.toJson(beanByH5).toString();
                sendMsgToNewH5("getDbJsById_n",tt);
            }
        });


        /**
         * 读取本地JS文件
         */
        webViewClient.registerHandler("selBookPageData_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Elog(TAG,"selBookPageData_n come "+datas);
                try {
                    JSONObject jsonObject = new JSONObject(datas.toString());
                    String UserID = jsonObject.optString("UserID");
                    String BookID = jsonObject.optString("BookID");
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
                        SendBeanByH5_PageData beanByH5 = new SendBeanByH5_PageData();
                        beanByH5.setCode("0");
                        beanByH5.setMessage("");
                        beanByH5.setSuccess(true);
                        beanByH5.setData(list);
                        String tt = gson.toJson(beanByH5).toString();
                        sendMsgToNewH5("selBookPageData_n",tt);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * 读取本地JS文件
         */
        webViewClient.registerHandler("getCatalogByBookId_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Elog(TAG,"getCatalogByBookId_n come "+datas);
                try {
                    JSONObject jsonObject = new JSONObject(datas.toString());
                    String UID = jsonObject.optString("UserID");
                    String BID = jsonObject.optString("BookID");
                    String bookPath4 = fileDownPath+"/data/"+UID+"/"+BID+"/catalog.json";
                    String info = readLocalJson(bookPath4);
                    SendBeanByH5 beanByH5 = new SendBeanByH5();
                    beanByH5.setCode("0");
                    beanByH5.setMessage("");
                    beanByH5.setSuccess(true);
                    beanByH5.setData(info);
                    String tt = gson.toJson(beanByH5).toString();
                    sendMsgToNewH5("getCatalogByBookId_n",tt);
                } catch (JSONException e) {
                    Elog(TAG,"getCatalogByBookId_n 解析错误");
                    e.printStackTrace();
                }
            }
        });

        /**
         * 读取本地JS文件
         */
        webViewClient.registerHandler("getCurTeachMap_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Elog(TAG,"getCurTeachMap_n come "+datas);
                try {
                    JSONObject jsonObject = new JSONObject(datas.toString());
                    String UID = jsonObject.optString("UserID");
                    String BID = jsonObject.optString("BookID");
                    String UnitID = jsonObject.optString("UnitID");
                    String bookPath4 = fileDownPath+"/data/"+UID+"/"+BID+"/resource/"+UnitID+".json";
                    String info = readLocalJson(bookPath4);
                    SendBeanByH5 beanByH5 = new SendBeanByH5();
                    beanByH5.setCode("0");
                    beanByH5.setMessage("");
                    beanByH5.setSuccess(true);
                    beanByH5.setData(info);
                    String tt = gson.toJson(beanByH5).toString();
                    sendMsgToNewH5("getCurTeachMap_n",tt);
                } catch (JSONException e) {
                    Elog(TAG,"getCurTeachMap_n 解析错误");
                    e.printStackTrace();
                }
            }
        });


        /**
         * 获取互动课件配置首页html
         */
        webViewClient.registerHandler("wareEntry_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Elog(TAG,"wareEntry_n come "+datas);
                try {
                    JSONObject jsonObject = new JSONObject(datas.toString());
                    String UID = jsonObject.optString("UserID");
                    String BID = jsonObject.optString("BookID");
                    int PageID = jsonObject.optInt("PageID");
                    String FileID = jsonObject.optString("FileID");
                    String pageNum = "page";
                    if (PageID < 10){
                        pageNum +="00"+PageID;
                    }else if (PageID < 100){
                        pageNum +="0"+PageID;
                    }else{
                        pageNum +=PageID;
                    }
                    String bookPath4 = fileDownPath+"/data/"+UID+"/"+BID+"/resource/"+pageNum;
                    File file = new File(bookPath4+"/"+FileID);
                    String name = "";
                    String data = "";
                    SendBean send = new SendBean();
                    SendBean.JsonBean jsonB = new SendBean.JsonBean();
                    send.setCode("0");
                    send.setMessage("");
                    send.setSuccess(true);
                    if (file.exists() && file.isDirectory()){
                        //表示是目录,遍历下级目录，默认取第一个，如果没有就是资源打包错误
                        File[] files = file.listFiles();
                        if (files != null && files.length > 0 ){
                            String xp = bookPath4+"/"+FileID+"/"+files[0].getName()+"/config.json";//默认读取这个文件
                            name = "data/"+UID+"/"+BID+"/resource/"+pageNum+"/"+FileID+"/"+files[0].getName()+"/"+files[0].getName()+".html";
                            data = readLocalJson(xp);
                        }
                    }else{
                        File[] files = new File(bookPath4).listFiles();
                        if (files != null && files.length > 0 ){
                            for (File f : files){
                                if (f.getName().startsWith(FileID)){
                                    name = "data/"+UID+"/"+BID+"/resource/"+pageNum+"/"+f.getName();
                                    break;
                                }
                            }
                        }
                    }
                    jsonB.setJson(data);
                    jsonB.setUrl(name);
                    send.setData(jsonB);
                    String tt = gson.toJson(send).toString();
                    sendMsgToNewH5("wareEntry_n",tt);
                } catch (JSONException e) {
                    Elog(TAG,"getCurTeachMap_n 解析错误");
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        /**
         * 离线判断用户名密码
         */
        webViewClient.registerHandler("checkUser_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Elog(TAG,"checkUser_n come "+datas);
                try{
                    JSONObject jsonObject = new JSONObject(datas.toString());
                    String name = jsonObject.getString("name");
                    String pwd = jsonObject.getString("pwd");
                    boolean isCheck = false;
                    if (!isNULL(name) && !isNULL(pwd)){
                        if (name.equals(checkName) && pwd.equals(checkPWD)){
                            isCheck = true;
                        }
                    }
                    SendBean send = new SendBean();
                    send.setCode("0");
                    send.setMessage("");
                    send.setSuccess(isCheck);
                    String tt = gson.toJson(send).toString();
                    sendMsgToNewH5("checkUser_n",tt);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


/******************************************TEST***************************************************/

        /**
         * 1.3开始录音
         */
        webViewClient.registerHandler("startRecord_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"startRecord_n =x==>"+datas);
                start();
            }
        });

        /**
         * 1.3结束录音
         *
         * */
        webViewClient.registerHandler("endRecord_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"endRecord_n =x==>"+datas);
                backType = "endRecord_n";
                //表示是新版本
                stop();
                if (datas.toString() != null && !datas.toString() .equals("")){
                    //表示需要上传
                    isSend = true;
                }
            }
        });


        /**
         * 1.3强制结束录音
         *
         * */
        webViewClient.registerHandler("stopRecord_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"stopRecord_n =x==>"+datas);
                backType = "stopRecord_n";
                stopPlayRecord();
                //表示是新版本
                stop();
                isSend = false;//表示强制停止
            }
        });

        /**
         * 1.3播放录音
         *
         * */
        webViewClient.registerHandler("playRecording_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"playRecording_n =x==>"+datas);
                backType = "playRecording_n";
                stopPlayRecord();
                if (datas != null){
                    TestBean ben =  (TestBean) KingSoftParasJson.getObjectByJson(datas.toString(),TestBean.class);
                    if (ben != null){
                        int sp = ben.getWordIndex();
                        TestBean bean = map.get(sp );
                        String mp3File = "";
                        if (bean != null){
                            mp3File = bean.getMp3Url();
                        }else{
                            Ilog(TAG, "playRecord come url is null " );
                            return;
                        }
//                        Ilog(TAG, "playRecord sp = " + sp + ";mp3File = " + mp3File);
                        try {
                            player = buildVedioPlayer(mp3File);
                            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                }
                            });
                            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    player.release();
                                    player = null;
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            Ilog(TAG, "playRecord come error ");
                        }
                    }
                }
            }
        });

/*********************************************1.3接口结束**********************************************************/



        /**
         * 角色扮演
         */
        webViewClient.registerHandler("startRcord", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"startRcord =x==>"+datas);
                if (isOld){
                    //表示是老版本的
                    onStartDub();
                }else{
                    //添加了评测的版本
                    start();
                }
            }
        });

        /**
         * 停止播放录音文件
         */
        webViewClient.registerHandler("stopPlayRecord", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"stopPlayRecord =x==>"+datas);
                stopPlayRecord();
            }
        });

        /**
         * 角色扮演
         */
        webViewClient.registerHandler("checkRcord", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"checkRcord =x==>"+datas);
                stopPlayRecord();
                isNewType = false;
                if (datas != null && !datas.equals("")){
                    isOld = false;
                    testBean = (TestBean) KingSoftParasJson.getObjectByJson(datas.toString(),TestBean.class);
                    if (testBean != null){
                        zimu = testBean.getZimu();
                        recordType = testBean.getType();
                        index = testBean.getWordIndex();
                        fileName = testBean.getName()+testBean.getWordIndex();
                        wavFile = Environment.getExternalStorageDirectory().getPath() + "/KingSunSmart/Download/" +
                                "recordCache/" +MyApplication.getInstance().getLoginName()+"/"+ fileName + ".wav";
                        Log.i(TAG, "checkRcord wavFile = " + wavFile);
                        testBean.setMp3Url(Environment.getExternalStorageDirectory().getPath() + "/KingSunSmart/Download/" +
                                "recordCache/"  +MyApplication.getInstance().getLoginName()+"/"+ fileName + ".mp3");
                        testBean.setUpload(false);
                        map.put(index,testBean);
                        if (engine != null) {
                            engine.setWavPath(wavFile);
                        }
                    }
                }else{
                    isOld = true;
                }
                checkPrimiss();
            }
        });

        /**
         * 设置IP端口号
         */
        webViewClient.registerHandler("setIpAndPort", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"setIpAndPort =x==>"+datas);
                portDialog.showDialog(1, 1);
            }
        });

        /**
         * 设置保存用户信息
         */
        webViewClient.registerHandler("loginSet", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"loginSet =x==>"+datas);
                if (datas != null && !datas.equals("")){
                    String[] xp =  datas.toString().split(",");
                    if (xp!= null && xp.length>=2){
                        //存储信息
                        Editor e = sp.edit();
                        e.putString("userInfo",xp[0]);//用户名密码
//                        e.putString("HEAD", xp[1]);//访问信息
                        e.commit();
                    }
                }
            }
        });

        /**
         * 重新登录
         */
        webViewClient.registerHandler("reLogin", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"reLogin =x==>"+datas);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /**
         * upLoadPhoto
         */
        webViewClient.registerHandler("upLoadPhoto", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"upLoadPhoto =x==>"+datas);
                if (datas != null) {
                    loadPhotoUrl = (String) datas;
                    photoSelUtil.showPhoto(MainActivity.this, "修改头像");
                }
            }
        });

        /**
         * 保存登录用户名
         */
        webViewClient.registerHandler("goBack_n", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"goBack =x==>"+datas);
                webView.destroy();
                finish();
            }
        });

        /**
         * 保存登录用户名
         */
        webViewClient.registerHandler("goBack", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"goBack =x==>"+datas);
                webView.destroy();
                finish();
            }
        });

        /**
         * 保存登录用户名
         */
        webViewClient.registerHandler("loginName", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"loginName =x==>"+datas);
                if (datas != null) {
                    MyApplication.getInstance().setLoginName((String) datas);
                }
            }
        });

        /**
         * 输入法监听
         */
        webViewClient.registerHandler("isOpenSoftInput", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                if (datas != null) {
                    if((int)datas == 1){
                        //打开输入法监听
                        KeyBoardListener.getInstance(MainActivity.this).init();
                    }else if((int)datas == 2){
                        //关闭输入法监听
                        KeyBoardListener.getInstance(MainActivity.this).
                                clearOnGlobalLayoutListener(screenHeight);
                        stopPlayRecord();
                        if (engine != null){
                            isSend = false;
                            stop();
                        }
                    }
                }
            }
        });

        /**
         * 设置软硬件加速，主要兼容MTK平台
         */
        webViewClient.registerHandler("setSoftMode", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                if (!ModeInfo.equalsIgnoreCase("HUAWEI")) {
                    //表示不是华为平台
                    if (datas != null) {
                        if ((int)datas == 1) {
                            //表示使用软件加速
                            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                        }else if ((int)datas == 2) {
                            //硬件加速
                            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                        }
                    }
                }
            }
        });

        /**
         * 角色扮演
         */
        webViewClient.registerHandler("stopRcord", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                upIndex = 1;
                Ilog(TAG,"stopRcord =x==>"+datas);
                isSend = false;
                if (!isOld){
                    //表示是新版本
                    if (datas.toString() != null && !datas.toString() .equals("")
                            &&!datas.toString() .equals("null")){
                        //表示需要上传
                        isSend = true;
                    }else{
                        isSend = false;
                    }
                    stop();
                }else{
                    //表示是老版本角色扮演
                    if (mRecorder != null) {
                        mRecorder.release();
                        mRecorder = null;
                    }
                    if (datas.toString() != null && !datas.toString() .equals("")){
                        try {
                            UPBean upBean = (UPBean) KingSoftParasJson.getObjectByJson(datas.toString(), UPBean.class);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("FileID", upBean.getFileID());
                            map.put("ResourceStyle", upBean.getResourceStyle());
                            map.put("UserName", upBean.getUserName());
                            String dateUrl  = gson.toJson(map);
                            String  teString = upBean.getFilePath()+"?JsonFile="+dateUrl;
                            if (teString != null && !teString.equals("")) {
                                if(upDialog != null){
                                    if(!upDialog.isShowing()){
                                        upDialog.setTextPro("上传中");
                                        upDialog.setTextNotic("");
                                        upDialog.showDialog(1, 1);
                                    }
                                }
                                Ilog(TAG,"path = "+path);
                                Ilog(TAG,"teString = "+teString);
                                upLoadFile(path,teString,downloadHandle);
                                //上传角色扮演录音
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        /**
         * 播放录音
         */
        webViewClient.registerHandler("playRecord", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG,"playRecord =x==>"+datas);
                stopPlayRecord();
                if (datas != null){
                    TestBean ben =  (TestBean) KingSoftParasJson.getObjectByJson(datas.toString(),TestBean.class);
                    if (ben != null){
                        int sp = ben.getWordIndex();
                        TestBean bean = map.get(sp );
                        String mp3File = "";
                        if (bean != null){
                            mp3File = bean.getMp3Url();
                        }else{
                            Ilog(TAG, "playRecord come url is null " );
                            return;
                        }
                        Ilog(TAG, "playRecord sp = " + sp + ";mp3File = " + mp3File);
                        try {
                            player = buildVedioPlayer(mp3File);
                            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                }
                            });
                            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    player.release();
                                    player = null;
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            Ilog(TAG, "playRecord come error ");
                        }
                    }
                }
            }
        });

        /**
         * 下载文件
         */
        webViewClient.registerHandler("downLoadFile", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                Ilog(TAG, "downLoadFile =test=>"+datas.toString());
                String urlString = datas.toString().replace("?","/");
                String[] ttStrings = urlString.split("/");
                UPDATE_SERVERAPK = ttStrings[ttStrings.length -1];
                fileName = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" +
                        MyApplication.getInstance().getLoginName()+"/ppt/"+UPDATE_SERVERAPK;
                //表明是PPT文件
                File file = new File(fileName);
                if (file.exists()) {
                    long s ;
                    FileInputStream fis;
                    try {
                        fis = new FileInputStream(file);
                        s= fis.available();
                        isTureFile(urlString, s,downloadHandle);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }else{
                    if (upDialog != null && !upDialog.isShowing()) {
                        upDialog.setTextNotic("下载中.....");
                        upDialog.setTextPro(0+"%");
                        upDialog.showDialog(1,1);
                    }
                    isDown = true;
                    downFile(urlString,downloadHandle,fileName);
                }
            }
        });

        /**
         * 上传载文件
         */
        webViewClient.registerHandler("upLoadFile", new WVJBWebViewClient.WVJBHandler() {
            @Override
            public void request(Object datas, WVJBResponseCallback callbacks) {
                upLoadUrl = datas.toString();
                Ilog(TAG, "upLoadUrl ==> "+upLoadUrl);
                if (upLoadUrl.contains("?type=1")) {
                    upIndex = 2;
                    //表示上传图片文件
                    upLoadUrl = upLoadUrl.replace("?type=1", "");
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        startActivityForResult(Intent.createChooser(intent,"File Chooser"),REQUEST_FILE_PICKER);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showToast(getApplicationContext(), "请先安装资源管理器");
                    }
                }else if (upLoadUrl.contains("?type=2")) {
                    upIndex = 3;
                    //打开微课
                    upLoadUrl = upLoadUrl.replace("?type=2", "");
                    String recordName = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" +
                            MyApplication.getInstance().getLoginName()+"/weike.mp4";
                    Utils.startAty(MainActivity.this,"com.kingsunedu.ik","com.clovsoft.ik" +
                            ".RemoteRecorder",recordName);
                }
            }
        });
        upDialog = new upLoadProDialog(MainActivity.this,"");
        upDialog.setOnKeyListener(this);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCache();
            }
        });
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case HandlerStrings.MAIN_WEBVIEW_STARTAPP:// 检查更新请求成功
                break;
            case HandlerStrings.MAIN_WEBVIEW_404:// 网络连接失败
//                ToastUtils.showToast(getApplicationContext(), "链接地址无法打开");
//                Intent mainIntent = new Intent(MainActivity.this, TestMainAcitity.class);
//                startActivity(mainIntent);
//                CheckActivityIn();
//                finish();
                break;
            case 100:
//                webView.loadUrl("http://192.168.3.89:8079/demo.html");// 加载本地H5主页
                break;
            case -200:
                ToastUtils.showToast(getApplicationContext(), "初始化失败");
                break;
            case 101:
                //设置H5页面显示可以打开EK
                webView.loadUrl("javascript:openEK()");
                KeyBoardListener.getInstance(this).clearOnGlobalLayoutListener(screenHeight);
                break;

            case 102:
//                if (!ModeInfo.equalsIgnoreCase("HUAWEI")) {
//                    Ilog(TAG, "兼容修改密码界面1");
//                    //兼容华瑞安修改密码界面
//                    if (myTimer != null) {
//                        myTimer.cancel();
//                        myTimer = null;
//                    }
//
//                    if (myTask != null) {
//                        myTask.cancel();
//                        myTask = null;
//                    }
//                    myTimer = new Timer();
//                    myTask = new TimerTask() {
//                        @Override
//                        public void run() {
//                            downloadHandle.sendEmptyMessage(14);
//                        }
//                    };
//                    myTimer.schedule(myTask, 500);
//                }
                KeyBoardListener.getInstance(this).init();
//                btn.setVisibility(View.VISIBLE);
                break;
            case 103:
                //离开登录页面需要注销输入法监听
//                btn.setVisibility(View.GONE);
//                KeyBoardListener.getInstance(this).clearOnGlobalLayoutListener(screenHeight);
//                String filePa = Environment.getExternalStorageDirectory().getPath() + "/KingSunSmart/Download/" +
//                        "recordCache/" +MyApplication.getInstance().getLoginName();
////                Ilog(TAG,"filePa = "+filePa);
//                deleteFile(new File(filePa));
                break;
            case 10000:
                Log.e(TAG,"页面加载完毕");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String upString1 = "";
                        if (sp != null) {
                            upString1 = sp.getString("userInfo", "");
                        }
                        Log.e(TAG,"页面加载完毕 -- "+upString1);
                        webView.loadUrl("javascript:webViewVersion('"+ ""+Build.VERSION.SDK_INT + "')");
                        webView.loadUrl("javascript:getLoginSetting('"+ upString1 + "')");
                        //告诉页面Android系统版本，然后做兼容处理
                    }
                });
                break;

            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendMsgToH5("release","release");//释放页面资源
//        clearCache();
        webView.clearHistory();
        if(upDialog != null && upDialog.isShowing()){
            upDialog.dismiss();
        }

        if (portDialog != null  && portDialog.isShowing()) {
            portDialog.dismiss();
        }
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }

        if (robotReceiver != null) {
            unregisterReceiver(robotReceiver);
        }

        if (receiver != null) {
            unregisterReceiver(receiver);
        }

        if (myTimer != null) {
            myTimer.cancel();
            myTimer = null;
        }

        if (myTask != null) {
            myTask.cancel();
            myTask = null;
        }
        webView.destroy();
        stopPlayRecord();
    }

    long exitTime =0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                ToastUtils.showToast(getApplicationContext(), "再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                webView.destroy();
                finish();
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 下载更新
     */
    @SuppressLint("HandlerLeak")
    private Handler downloadHandle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    long total = (Long) msg.obj;
                    long progress = (long) msg.arg2;
                    float p = ((float) progress / (float) total);
                    BigDecimal b = new BigDecimal(p);
                    float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                    int pro = (int) (f1 * 100);
                    if(upDialog != null && upDialog.isShowing()){
                        upDialog.setTextNotic("下载中.....");
                        upDialog.setTextPro(pro+"%");
                    }
                    if (pro == 100 && isDown ) {
                        isDown = false;
                        //					ToastUtils.showToast(MainActivity.this,"下载成功");
                        if(upDialog != null){
                            upDialog.setTextPro("100%");
                            upDialog.dismiss();
                        }
                        pro = 0;
                        if (fileName.endsWith(".zip")) {
                            String  pptUrl =fileName.replace(".zip", "");
                            try {
                                dlgMixing.setMessage("正在解压PPT文件");
                                dlgMixing.show();
                                //解压PPT
                                new Check_UnZip(fileName ,downloadHandle,pptUrl).start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(fileName.endsWith(".rar")){
                            //解压rar文件
                            String  pptUrl =fileName.replace(".rar", "");
                            try {
                                dlgMixing.setMessage("正在解压PPT文件");
                                dlgMixing.show();
                                //解压PPT
                                new Check_UnRar(fileName ,downloadHandle,pptUrl).start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else  {
                            try {
                                Intent intent = Utils.openFile(fileName);
                                if(intent == null){
                                    ToastUtils.showToast(getApplicationContext(), "无法打开该文件");
                                    return;
                                }
                                startActivity(intent);
                            } catch (Exception e) {
                                ToastUtils.showToast(getApplicationContext(), "安装相应应用后再打开");
                            }
                        }
                    }
                    break;

                case 2:
                    if (upDialog != null && upDialog.isShowing() ) {
                        upDialog.dismiss();
                    }
                    final String  data = (String) msg.obj;
                    Ilog(TAG, "obj ==> "+data);
                    if (upIndex >1) {
                        UpLoadBean bean = (UpLoadBean) KingSoftParasJson.getObjectByJson(data, UpLoadBean.class);
                        if (bean != null && bean.isSuccess()) {
                            ToastUtils.showToast(MainActivity.this,"上传成功");
                            if (upIndex ==2) {
                                //上传图片
                                otherBean beans = bean.getData();
                                if (beans != null) {
                                    beans.setType("1");
                                    beans.setFileName(upLoadName);
                                    bean.setData(beans);
                                }
                            }else if (upIndex == 3) {
                                //上传微课
                                otherBean beans = bean.getData();
                                if (beans != null) {
                                    beans.setType("2");
                                    beans.setFileName(upLoadName);
                                    bean.setData(beans);
                                }
                            }
                        }else {
                            sendMsgToH5("error", "error");
                            if(photoSelUtil != null){
                                photoSelUtil.setImgString(null);
                                photoSelUtil.delFile();
                            }
                            ToastUtils.showToast(MainActivity.this,"上传失败");
                            return;
                        }
                        Gson gson = new Gson();
                        final String upString = gson.toJson(bean);
                        final String upString1 = gson.toJson(bean.getData());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (upIndex == 5) {
                                    if(photoSelUtil != null){
                                        photoSelUtil.setImgString(null);
                                        photoSelUtil.delFile();
                                    }
                                    webView.loadUrl("javascript:getAvatarUrl('"+ upString1 + "')");
                                }else{
                                    webView.loadUrl("javascript:uploadfile('"+ upString + "')");
                                }
                            }
                        });
                    }else if(upIndex == 1){
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONObject js = jsonObject.optJSONObject("Data");
                            if(js != null){
                                String pathString = js.optString("FilePath");
                                webView.loadUrl("javascript:recordMessageHandler('"+ pathString + "')");
                            }else{
                                sendMsgToH5("error", "error");
                                ToastUtils.showToast(MainActivity.this,"上传失败:"+jsonObject.optString("ErrorMsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case 3:
                    sendMsgToH5("error", "error");
                    if(upDialog != null && upDialog.isShowing() ){
                        upDialog.dismiss();
                    }
                    break;

                case 4:
                    //上传进度
                    int pros = msg.arg2;
                    if(upDialog != null && upDialog.isShowing()){
                        upDialog.setTextNotic("上传中.....");
                        upDialog.setTextPro(pros+"%");
                    }
                    break;

                case 5:
                    final boolean  xp = (boolean) msg.obj;
                    if (!isNewType){
                        Ilog(TAG,"检查录音权限 =x==>"+xp);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl("javascript:recordMessageHandler('"+ xp + "')");
                            }
                        });
                    }else{
                        //表示是1.3新课件
                        NewProBean proBean = new NewProBean();
                        if (xp){
                            proBean.setCode(0);
                            proBean.setMsg("");
                        }else{
                            proBean.setCode(500);
                            proBean.setMsg("录音权限未开启");
                        }
                        final  String tt = gson.toJson(proBean).toString();
                        Ilog(TAG,"检查录音权限 =x==>"+xp);
                        sendMsgToNewH5(tt);
                    }
                    break;

                case 6:
                    //文件相同
                    if (fileName.endsWith(".zip")) {
                        String  pptUrl =fileName.replace(".zip", "");
                        try {
                            dlgMixing.setMessage("正在解压PPT文件");
                            dlgMixing.show();
                            //解压PPT
                            new Check_UnZip(fileName,downloadHandle,pptUrl).start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(fileName.endsWith(".rar")){
                        //解压rar文件
                        String  pptUrl =fileName.replace(".rar", "");
                        try {
                            dlgMixing.setMessage("正在解压PPT文件");
                            dlgMixing.show();
                            //解压PPT
                            new Check_UnRar(fileName ,downloadHandle,pptUrl).start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {
                            Intent intent = Utils.openFile(fileName);
                            if(intent == null){
                                ToastUtils.showToast(getApplicationContext(), "无法打开该文件");
                                return;
                            }
                            startActivity(intent);
                        } catch (Exception e) {
                            ToastUtils.showToast(getApplicationContext(), "安装相应应用后再打开");
                        }
                    }
                    break;
                case 7:
                    //文件不相同
                    Ilog(TAG, "7777");
                    final String  urlString = (String) msg.obj;
                    if (upDialog != null && !upDialog.isShowing()) {
                        upDialog.showDialog(1,1);
                    }
                    isDown = true;
                    downFile(urlString,downloadHandle,fileName);
                    break;

                case Constant.UNZIP_START:
                    //正在解压
                    //				dlgMixing.setMessage("正在解压PPT文件");
                    //				dlgMixing.show();
                    break;

                case Constant.DOWNLOAD_UNZIP_RESULT:
                    //解压完成
                    if (dlgMixing != null) {
                        dlgMixing.cancel();
                    }
                    String openName = (String) msg.obj;
                    try {
                        Intent intent2 = Utils.openFile(openName);
                        if(intent2 == null){
                            ToastUtils.showToast(getApplicationContext(), "无法打开该文件");
                            return;
                        }
                        startActivity(intent2);
                    } catch (Exception e) {
                        ToastUtils.showToast(getApplicationContext(), "安装相应应用后再打开");
                    }
                    break;

                case Constant.UNZIP_NO_SPACE:
                    //解压空间不足
                    if (dlgMixing != null) {
                        dlgMixing.cancel();
                    }
                    ToastUtils.showToast(MainActivity.this,(String) msg.obj);
                    break;

                case Constant.UNZIP_ERROR:
                    //解压出错
                    if (dlgMixing != null) {
                        dlgMixing.cancel();
                    }
                    ToastUtils.showToast(MainActivity.this,(String) msg.obj);
                    break;

                case 11:
                    if (myTimer != null) {
                        myTimer.cancel();
                        myTimer = null;
                    }

                    if (myTask != null) {
                        myTask.cancel();
                        myTask = null;
                    }
                    ToastUtils.showToast(MainActivity.this,"输入地址无法访问");
                    break;

                case 12:
                    if (myTimer != null) {
                        myTimer.cancel();
                        myTimer = null;
                    }

                    if (myTask != null) {
                        myTask.cancel();
                        myTask = null;
                    }
				/*
				 * 清除缓存
				 *
				 * */
                    clearCache();
                    webView.loadUrl(url);// 加载H5主页
                    break;

                case 13:
                    if(photoSelUtil != null){
                        photoSelUtil.setImgString(null);
                        photoSelUtil.delFile();
                    }
                    ToastUtils.showToast(MainActivity.this,"取消上传");
                    break;

                case 14 :
                    if (myTimer != null) {
                        myTimer.cancel();
                        myTimer = null;
                    }

                    if (myTask != null) {
                        myTask.cancel();
                        myTask = null;
                    }
                    Ilog(TAG, "兼容修改密码界面2");
                    webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    break;
                case 15:
                    Ilog(TAG,"testLoad UpLoadUrl = "+UpLoadUrl);
                    if (UpLoadUrl != null && !UpLoadUrl.equals("")) {
                        if(upDialog != null){
                            if(!upDialog.isShowing()){
                                upDialog.setTextPro("上传中");
                                upDialog.setTextNotic("");
                                upDialog.showDialog(1, 1);
                            }
                        }
                        File file = new File(mp3File);
                        if (file.exists() && file.isFile()){
                            upLoadFile(mp3File,UpLoadUrl,downloadHandle);
                        }else{
                            Ilog(TAG,"file is error");
                        }
                    }else{
                        Ilog(TAG,"testLoad 1");
                    }
                    break;

                case 16:
                    //更新UI，告诉页面评测成功
                    deleteFile(new File(wavFile));
                    if (!isNewType){
                        sendMsgToH5("score", score + "");
                    }else{
                        NewProBean proBean = new NewProBean();
                        proBean.setCode(0);
                        proBean.setMsg("");
                        NewProBean.sendBean sendBean = new NewProBean.sendBean();
                        sendBean.setScore(score);
                        proBean.setData(sendBean);
                        final  String tt = gson.toJson(proBean).toString();
                        sendMsgToNewH5(tt);
                    }
                    break;

                case 100:
                    //读取本地文件
                    String str = msg.getData().getString("str");
                    int arg = msg.arg1;
                    backType = (String)msg.obj;
//                    Elog(TAG,"str " +str);
                    if (str != null) {
                        String tt = "";
                        if (arg == 2){
                            SendBean send = new SendBean();
                            SendBean.JsonBean jsonB = new SendBean.JsonBean();
                            send.setCode("0");
                            send.setMessage("");
                            send.setSuccess(true);
                            jsonB.setJson(str);
                            jsonB.setUrl(testPath2+"ren_mang.html");
                            send.setData(jsonB);
                            tt = gson.toJson(send).toString();
                            sendMsgToNewH5(tt);
                        }else if(arg == 1000){
                            //测试读取的是离线下载数据
                            if (!isNULL(str)){
                                downLoadBeen = KingSoftParasJson.getListByJson(str,DownLoadBean.class);
                                if (downLoadBeen != null){
//                                    SaveFile(gson.toJson(downLoadBeen),fileDownPath+"/dataBean.json");//保存下载的数据
                                    Elog(TAG,"downLoadBeen - "+downLoadBeen.size());
                                    for (DownLoadBean loadBean : downLoadBeen){
                                        if (loadBean.getIsDownLoad() == 1 || loadBean.getIsDownLoad() == 2){//表示需要下载的数据
                                            dBeen.add(loadBean);
                                        }if (loadBean.getIsDownLoad() == 0 || loadBean.getIsDownLoad() == 2){//需要保存json文件的数据
                                            rBeen.add(loadBean);
                                        }
                                    }
                                    /*************************开始资源下载********************************/
                                    if (dBeen != null && dBeen.size() > 0){
                                        if (dBeen.get(0) != null){
                                            Elog(TAG,"开始第一个下载，总共有 "+dBeen.size());
                                            currentIndex = 1;
                                            downFile(dBeen.get(0),downloadHandle);
//                                            String filePath = dBeen.get(0).getFilePath().replace("\\","/");
//                                            downFile(dBeen.get(0).getFileURL(),downloadHandle,
//                                                    filePath,"/test.zip");
                                        }
                                    }

                                    /*************************开始保存JSON资源********************************/
                                    if (rBeen != null && rBeen.size() > 0){
                                        SaveFileList(rBeen);
                                    }
                                }
                            }
                        }else {
                            SendBeanByH5 beanByH5 = new SendBeanByH5();
                            beanByH5.setCode("0");
                            beanByH5.setMessage("");
                            beanByH5.setSuccess(true);
                            beanByH5.setData(str);
                            tt = gson.toJson(beanByH5).toString();
                            sendMsgToNewH5(tt);
                        }
                    }
                    break;

                case 1001:
                    long total1 = (Long) msg.obj;
                    Elog("DOWNLOAD","下载进度 "+total1+"%");
                    if (total1 == 100 ) {
                        Elog(TAG,"下载完毕");
                        if (dBeen != null && dBeen.size() > 0){
                            dBeen.remove(0);
                            if (dBeen != null && dBeen.size() > 0){
                                if (dBeen.get(0) != null){
                                    currentIndex += 1;
                                    Elog(TAG,"下载完毕 ,开始下载第 "+currentIndex);
                                    downFile(dBeen.get(0),downloadHandle);
//                                    String filePath = dBeen.get(0).getFilePath().replace("\\","/");
//                                    Elog(TAG,"getFilePath = "+filePath);
//                                    downFile(dBeen.get(0).getFileURL(),downloadHandle,
//                                            filePath,"/test.zip");
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        };
    };

    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        Ilog(TAG, "onActivityResult come = "+arg0);
        if (arg0 == REQUEST_FILE_PICKER) {
            if (arg2 != null && arg2.getData() != null) {
                String xp = getPath(getApplicationContext(), arg2.getData());
                Ilog(TAG, "xp = "+xp);
                File file = new File(xp);
                if (file.exists()) {
                    String[] jkString = xp.split("/");
                    String okString = jkString[jkString.length-1];
                    if (!okString.contains(".")) {
                        ToastUtils.showToast(MainActivity.this,"该文件类型不能被上传");
                        return;
                    }
                    //保存文件名
                    upLoadName= okString.substring(0, okString.indexOf("."));
                    String  ttString = okString.substring(okString.indexOf(".")+1);
                    if (ttString.contains(".")) {
                        ttString = ttString.substring(ttString.indexOf(".")+1);
                    }
                    if (Utils.isCanUpload(ttString)) {
                        try {
                            long size = getFileSizes(file);
                            if(size > 1024 *1024*500){
                                //标示以G的形式显示
                                ToastUtils.showToast(MainActivity.this,"上传文件不能超过500M");
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(upDialog != null){
                            if(!upDialog.isShowing()){
                                upDialog.setTextPro("上传中");
                                upDialog.setTextNotic("");
                                upDialog.showDialog(1, 1);
                            }
                        }
                        //上传用户选的课件
                        post = new HttpMultipartPost(this, xp,downloadHandle);
                        post.execute(upLoadUrl);
                    }else{
                        ToastUtils.showToast(MainActivity.this,"该文件类型不能被上传");
                        return;
                    }
                }
            }
        }else if (arg0 == 1 || arg0 == 2 || arg0 ==0 ) {
            if (photoSelUtil==null) {
                photoSelUtil = new PhotoSelUtil();
            }
            photoSelUtil.onActivityResult(this,  arg0,  arg1,  arg2);
            //表示是截取后的
            if (photoSelUtil.RESIZE_REQUEST_CODE==arg0 ) {
                Ilog(TAG, "imgURL = "+photoSelUtil.getImgString());
                if (photoSelUtil.getImgString() == null || photoSelUtil.getImgString().equals("")) {
                    //上传取消
                    downloadHandle.sendEmptyMessage(13);
                }else{
                    File file = new File(photoSelUtil.getImgString());
                    if (file.exists()) {
                        //开始上传
                        upIndex = 5;
                        post = new HttpMultipartPost(MainActivity.this, photoSelUtil.getImgString(),downloadHandle);
                        post.execute(loadPhotoUrl);
                    }else{
                        Ilog(TAG, "文件不存在 ");
                        downloadHandle.sendEmptyMessage(13);
                    }
                }
            }
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
        {
            return true;
        }
        return false;
    };

    public void onStartDub() {
        try {
            if (!isFileExist(MyApplication.getInstance().getLoginName()+"/recordCache")) {
                createSDDir(MyApplication.getInstance().getLoginName()+"/recordCache");
            }
            path = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" +
                    MyApplication.getInstance().getLoginName()+ "/recordCache/record.mp3";
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
            mRecorder = new MediaRecorder();
            mRecorder.reset();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//定义音频来源
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//定义输出格式
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);//定义音频编码
            mRecorder.setAudioEncodingBitRate(96000);
            mRecorder.setAudioChannels(2);
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setOutputFile(path);
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();
    }

    /**
     * 初始化评测SDK
     */
    private void initSingEnge() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //  获取引擎实例,设置测评监听对象
                    engine = SingEngine.newInstance(MainActivity.this);
                    engine.setListener(mResultListener);
                    if (!isFileExist(MyApplication.getInstance().getLoginName()+"/recordCache")) {
                        createSDDir(MyApplication.getInstance().getLoginName()+"/recordCache");
                    }
                    //引擎类型（在线 、 离线 、混合）,默认使用在线引擎
                    //备注:   在线: "cloud"; 离线:"native"; 混合:"auto"
                    engine.setServerType("native");
                    //  设置是否开启VAD功能
                    engine.setOpenVad(false, null);
                    engine.setLogLevel(4);
//					engine.setOpenVad(true, "vad.0.1.bin");
//					engine.setFrontVadTime(3000);
//					engine.setBackVadTime(3000);
                    //   构建引擎初始化参数
                    //测试参数
                    JSONObject cfg_init = engine.buildInitJson("t135", "1eb07a38f3834b7ea666934cb0ce3085");
                    //正式的APPkeyko
//                    JSONObject cfg_init = engine.buildInitJson("a135", "5ceb63b8a5124854a92d046dca1e54a3");
                    //   设置引擎初始化参数
                    engine.setNewCfg(cfg_init);
                    //   引擎初始化
                    engine.newEngine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void start() {
        if (engine != null) {
            try {
                JSONObject request = new JSONObject();
                //段落评测
                request.put("coreType", "en.pred.score");
                request.put("refText", zimu);
                request.put("rank", 100);
                //构建评测请求参数
                JSONObject startCfg = engine.buildStartJson("guest", request);
                //设置评测请求参数
                engine.setStartCfg(startCfg);
                //开始测评
                engine.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    SingEngine.ResultListener mResultListener = new SingEngine.ResultListener() {
        @Override
        public void onBegin() {}

        @Override
        public void onResult(JSONObject jsonObject) {
            Ilog(TAG,"onResult come  = "+jsonObject.toString());
            if (!isSend){
                //表示强制停止
                return;
            }
            //回调评测结果
            CheckBean bean = (CheckBean) KingSoftParasJson.getObjectByJson(jsonObject.toString(), CheckBean.class);
            if (bean != null) {
                CheckBean.otherBean oBean = bean.getResult();
                if (oBean != null) {
                    score = oBean.getOverall();
                    Ilog(TAG,"core = "+oBean.getOverall() +";index = "+index);
                    TestBean testBean = map.get(index);
                    if(testBean != null){
                        testBean.setScore(score);
                        //保存分数
                        map.put(index,testBean);
                    }else{
                        Ilog(TAG,"评测 bean is null ");
                    }
                    if (isSend){
                        //开始转码MP3
                        wavToMp3();
                    }
                }else {
                    Ilog(TAG,"oBean is null ");
                }
            }else{
                Ilog(TAG,"bean is null ");
            }
        }

        @Override
        public void onEnd(int i, String s) {

        }

        @Override
        public void onUpdateVolume(int i) {

        }

        @Override
        public void onFrontVadTimeOut() {

        }

        @Override
        public void onBackVadTimeOut() {

        }

        @Override
        public void onRecordingBuffer(byte[] bytes) {

        }

        @Override
        public void onRecordLengthOut() {

        }

        @Override
        public void onReady() {

        }

        @Override
        public void onPlayCompeleted() {

        }
    };

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_UPLOAD)) {//录屏保存文件
                final Uri uri = intent.getData();
                String path = uri.getPath();
                KeyBoardListener.getInstance(MainActivity.this).
                        clearOnGlobalLayoutListener(screenHeight);//注销输入法监听，使得输入法不占用高度
                //弹出提示框
                ExitDialog dialog =new ExitDialog(MainActivity.this, postOrCanser, path);
                dialog.showDialog(1, 0);
                dialog.setOnKeyListener(new OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
                            return true;
                        else
                            return false;
                    }
                });
            }
        }
    };


    /***
     * 机器人指令类
     */
    private BroadcastReceiver robotReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Elog("ParseAlice","智慧教室收到广播 === 》"+intent.getAction());
            if (intent.getAction().equals(OPEN_XUNFEI_API)){
                //JS暂停页面动作
                sendMsgRobot("fct","0");
            }else if (intent.getAction().equals(OPEN_READ)){
                //JS全文阅读
                sendMsgRobot("fct","1");
            }else if (intent.getAction().equals(OPEN_ANIMATION)){
                //JS打开动画
                sendMsgRobot("fct","2");
            }else if (intent.getAction().equals(OPEN_IMAGE)){
                //JS打开图片
                sendMsgRobot("fct","3");
            }else if (intent.getAction().equals(UP_CLASSROOM)){
                //JS上一页
                sendMsgRobot("fct","4");
            }else if (intent.getAction().equals(NEXIT_CLASSROOM)){
                //JS下一页
                sendMsgRobot("fct","5");
            }else if (intent.getAction().equals(EXIT_CLASSROOM)){
                //JS退出电子书
                sendMsgRobot("fct","6");
            }else if (intent.getAction().equals(OPEN_EBOOKE)){
                //JS打开电子书
                sendMsgRobot("fct","7");
            }else if(intent.getAction().equals(CLASSROOM_DICTATION)){
                //JS报听写
                sendMsgRobot("fct","8");
            }
        }
    };

    public void checkPrimiss(){
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
//        if (!isOld){
        //中途强行停止
        isSend = false;
        stop();
//        }
        try{
            Thread.sleep(500);
        }catch (Exception e){
            e.printStackTrace();
        }
        boolean xp = permissionUtils.isHasAudioRecordingPermission(MainActivity.this);
        Message msg = new Message();
        msg.what = 5;
        msg.obj = xp;
        downloadHandle.sendMessage(msg);
        return;
    }

    /**
     * 清楚webView  缓存
     */
    public void clearCache(){
        try {
            webView.clearCache(true);
            deleteFile(new File(getCacheDir().getAbsolutePath()+"/databases"));
            deleteFile(new File(getFilesDir().getParent()));
            CookieSyncManager.createInstance(this);
            CookieManager.getInstance().removeAllCookie();
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
        webView.clearHistory();
    }

    private PostOrCanser postOrCanser = new PostOrCanser() {
        @Override
        public void upLoad(Intent intent) {
            if (intent != null) {
                String name = intent.getStringExtra("name");
                path = intent.getStringExtra("path");
                upLoadName = name;
                if(upDialog != null){
                    if(!upDialog.isShowing()){
                        upDialog.setTextPro("上传中");
                        upDialog.setTextNotic("");
                        upDialog.showDialog(1, 1);
                    }
                }
                //上传微课
                post = new HttpMultipartPost(MainActivity.this, path,downloadHandle);
                post.execute(upLoadUrl);
            }
        }

        @Override
        public void Post() {

        }

        @Override
        public void Canser() {
            ToastUtils.showToast(MainActivity.this,"取消上传微课视频");
        }
    };

    public void audioCheckResult(int  result){
        Ilog(TAG, "audioCheckResult come "+result);
    }

    public static MainActivity getInstance() {
        if (mManager == null) {
            mManager = new MainActivity();
        }
        Ilog(TAG,"mManager=" + mManager);
        return mManager;
    }

    /**
     *停止录音
     */
    private void stop() {
        if (engine != null) {
            engine.stop();
        }
    }

    /**
     * 将wav转Mp3
     */
    private void wavToMp3(){
        new Thread() {
            @Override
            public void run() {
                LameUtils utils = new LameUtils();
                utils.initLame(16000, 16000, 1);
                TestBean bean = map.get(index);
                if ( bean != null){
                    String mp3url = bean.getMp3Url();
                    Elog(TAG,"wavToMp3 mp3url = "+mp3url);
                    utils.wavTomp3(wavFile, mp3url);
                    utils.closeLame();
                    downloadHandle.sendEmptyMessage(16);
                }else{
                    Elog(TAG,"bean is null");
                }
            }
        }.start();
    }

    /**
     * 给H5页面发消息
     */
    private void sendMsgToH5(final String key, final String value) {
        Ilog(TAG, "sendMsgToH5 key = " + key + ";value = " + value);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject request = new JSONObject();
                    request.put(key, value);
                    webView.loadUrl("javascript:exitRecord('" + request.toString() + "')");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 给H5页面发消息,机器人项目使用
     */
    private void sendMsgRobot(final String key, final String value) {
        Ilog("ParseAlice", "sendMsgToH5 key = " + key + ";value = " + value);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject request = new JSONObject();
                    request.put(key, value);
                    webView.loadUrl("javascript:robotAct('" + request.toString() + "')");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 给H5页面发消息
     */
    private void sendMsgToNewH5(final String value) {
        Ilog(TAG, "sendMsgToH5 value = " + value);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:sClassJSBridge.handleMessage('"+ backType+"',"+ value + ");");
            }
        });
    }

    private void sendMsgToNewH5(final String name,final String value) {
        Ilog(TAG, "sendMsgToH5 name = " + name);
        Ilog(TAG, "sendMsgToH5 value = " + value);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:sClassJSBridge.handleMessage('"+ name+"',"+ value + ");");
            }
        });
    }

    /**
     * @param pathString
     * @return
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalStateException
     * @throws IOException
     */
    public MediaPlayer buildVedioPlayer(String pathString) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(pathString);
        mediaPlayer.prepareAsync();
        return mediaPlayer;
    }

    /**
     * 停止播放录音
     */
    private void stopPlayRecord(){
        try{
            if (player != null && player.isPlaying()) {
                player.stop();
                player.release();
                player = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @android.webkit.JavascriptInterface
    public void writeCard(final String indata){
        //js调用Java 有参
        Ilog(TAG, "js 调用 Java de writeCard ,indata : "+indata);
    }
}