package com.kingsun.teacherclasspro.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.king.percent.support.PercentRelativeLayout;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.adpter.BookListAdp;
import com.kingsun.teacherclasspro.adpter.MyFragmentPagerAdapter;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.BookInfoBean;
import com.kingsun.teacherclasspro.bean.LoginBean;
import com.kingsun.teacherclasspro.dialog.S_DialogUtil;
import com.kingsun.teacherclasspro.fragment.BaseFragment;
import com.kingsun.teacherclasspro.fragment.BookListFragment;
import com.kingsun.teacherclasspro.utils.AppConfig;
import com.kingsun.teacherclasspro.utils.FileTransminssionService;
import com.kingsun.teacherclasspro.utils.KingSoftParasJson;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.kingsun.teacherclasspro.utils.UnzipFromAssets;
import com.kingsun.teacherclasspro.widgets.MyPager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

import static com.kingsun.teacherclasspro.activity.BaseActivity.Elog;
import static com.kingsun.teacherclasspro.activity.BaseActivity.Ilog;
import static com.kingsun.teacherclasspro.fragment.BaseFragment.fileDownPath;
import static com.kingsun.teacherclasspro.fragment.BaseFragment.isEmty;
import static com.kingsun.teacherclasspro.fragment.BaseFragment.readCacheDate;

/**
 * Created by hai.huang on 2018/3/15.
 */

public class BookListAcivity extends FragmentActivity implements AdapterView.OnItemClickListener {
    @InjectView(R.id.img_back)
    ImageView imgBack;
    @InjectView(R.id.back_layout)
    PercentRelativeLayout backLayout;
    @InjectView(R.id.title_layout)
    PercentRelativeLayout titleLayout;
    @InjectView(R.id.choose_layout)
    PercentRelativeLayout chooseLayout;
    @InjectView(R.id.viewpager)
    MyPager viewpager;
    private static String TAG = "BookListAcivity";
    public ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    @InjectView(R.id.tv_book)
    TextView tvBook;
    @InjectView(R.id.book_layout)
    PercentRelativeLayout bookLayout;
    @InjectView(R.id.tv_Down)
    TextView tvDown;
    @InjectView(R.id.down_Layout)
    PercentRelativeLayout downLayout;
    @InjectView(R.id.right_layout)
    PercentRelativeLayout rightLayout;
    @InjectView(R.id.img_notic)
    ImageView imgNotic;//图片
    @InjectView(R.id.tv_name)
    TextView tvName;//教材版本文字
    @InjectView(R.id.bookType_layout)
    PercentRelativeLayout bookTypeLayout;
    @InjectView(R.id.mTogBtn)
    ToggleButton mTogBtn;
    private int currIndex = 0;
    private View popView, typePop, downPop;
    private PopupWindow pop, downPopWindow, versionWindow;
    private ListView lv_pop_preferential;
    private BookListAdp bookListAdp, downLoadAdp,versionAdp;
    private ArrayList<LoginBean.ClassBean> classBeen, loadBeans;
    private int type = 1;
    private ArrayList<BookInfoBean> bookInfoList = new ArrayList<>();//保存全部教材的书本信息
    private ArrayList<BookInfoBean> TBList = new ArrayList<>();//保存同步教材所有的书本信息
    private ArrayList<BookInfoBean> JFList = new ArrayList<>();//保存教辅教材所有的书本信息
    private ArrayList<BookInfoBean> TSList = new ArrayList<>();//保存特色教材所有的书本信息
    private HashMap<Integer, ArrayList<LoginBean.ClassBean>> loadMap = new HashMap<>();
    private HashMap<Integer, ArrayList<BookInfoBean>> jsonDataMap = new HashMap<>();//存放每个教材的本地缓存数据
    public static final String ACTION_UPLOAD = "com.clovsoft.ik.RemoteRecorder.MEDIA_IP";
    private Intent socketService;//启动的socket服务R
    private BaseFragment.dataChangeListener listener = new BaseFragment.dataChangeListener() {
        @Override
        public void changeData(Intent intent) {
            getJsonData(intent.getStringExtra("data"));
            for (int i = 0; i < fragmentList.size(); i++) {
                BookListFragment fragment = (BookListFragment) fragmentList.get(i);
                if (fragment != null) {
                    fragment.setBookInfoList(jsonDataMap.get(i));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_book_list_new);
        ButterKnife.inject(this);
        if (MyApplication.getInstance().getLoginBean() != null) {
            classBeen = MyApplication.getInstance().getLoginBean().getClasses();
            if (classBeen != null && classBeen.size() > 0) {
                tvBook.setText(classBeen.get(0).getClassName());
                MyApplication.getInstance().setClassID(classBeen.get(0).getClassID());
                MyApplication.getInstance().setGradeID(classBeen.get(0).getGradeID());
                MyApplication.getInstance().setSubjectID(classBeen.get(0).getSubjectID());
                MyApplication.getInstance().setClassName(classBeen.get(0).getClassName());
            }
        }
        getDate();
        Glide.get(this).clearMemory();
        getJsonData(readCacheDate(fileDownPath+"/data/"+
                MyApplication.getInstance().getUserID()+"book.json"));
        for (int i = 0; i < 4; i++) {
            BookListFragment fragment = new BookListFragment(i, jsonDataMap.get(i), listener);
            fragmentList.add(fragment);
        }
        viewpager.setAdapter(new MyFragmentPagerAdapter(this.getSupportFragmentManager(), fragmentList));
        viewpager.setCurrentItem(0);//设置当前显示标签页为第一页
        MyApplication.getInstance().setCurrentIndex(0);
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
        try {
            UnzipFromAssets.unZip(this, "html.zip", fileDownPath, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPLOAD);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(myReceiver, filter);
        Elog(TAG,"CHECK = "+MyApplication.getInstance().isOpenIK());
        mTogBtn.setChecked(MyApplication.getInstance().isOpenIK());
        mTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选中
                    MyApplication.getInstance().setOpenIK(true);
                }else{
                    //未选中
                    MyApplication.getInstance().setOpenIK(false);
                }
            }
        });
        imgNotic.setImageResource(BookListAdp.imgSelectList[0]);//图片
        tvName.setText(BookListAdp.name[0]);//教材版本文字
    }

    /**
     * Activity被系统杀死时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死.
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态.
     * 在onPause之前被调用.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Bean", MyApplication.getInstance().getLoginBean());
    }

    /**
     * Activity被系统杀死后再重建时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity.
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        LoginBean bean_n = (LoginBean) savedState.getSerializable("Bean");
        if (bean_n != null) {
            MyApplication.getInstance().setLoginBean(bean_n);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Elog(TAG, "onDestroy come");
        loadMap.clear();
        loadMap = null;
        jsonDataMap.clear();
        jsonDataMap = null;
        Glide.get(this).clearMemory();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
        if (socketService != null) {
            stopService(socketService);
            socketService = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Ilog(TAG, "回到主界面，onResume");
        if (MyApplication.getInstance().isGoToIK()) {
            //销毁IK回到主界面，需要关闭启动的socket服务
            Ilog(TAG, "回到主界面，关闭IK");
            if (socketService != null) {
                stopService(socketService);
                socketService = null;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            BookListFragment fragment = (BookListFragment) fragmentList.get(currIndex);
            if (fragment != null ) {
                if (fragment.isDel()){
                    fragment.setDel(false);
                }else {
                    finish();
                }
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.back_layout
            , R.id.book_layout, R.id.down_Layout, R.id.right_layout,
            R.id.bookType_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                //返回
                BookListFragment fragment = (BookListFragment) fragmentList.get(currIndex);
                if (fragment != null ) {
                    if (fragment.isDel()){
                        fragment.setDel(false);
                    }else {
                        finish();
                    }
                }else {
                    finish();
                }
                break;
            case R.id.book_layout:
                //班级
                type = 1;
                if (pop == null) {
                    initPopupWindow();
                } else {
                    final WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.7f;
                    getWindow().setAttributes(lp);
                    bookListAdp.setDate(classBeen);
                }
                pop.showAsDropDown(bookLayout);
                break;
            case R.id.down_Layout:
                //下载列表
                type = 2;
                if (loadMap.get(currIndex) != null) {
                    ArrayList<LoginBean.ClassBean> lists = changeData(loadMap.get(currIndex));
                    if (downPopWindow == null) {
                        initDownLoadPopWindow(lists);
//                        initDownLoadPopWindow(loadMap.get(currIndex));
                    } else {
                        final WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 0.7f;
                        getWindow().setAttributes(lp);
                        downLoadAdp.setDate(lists);
//                        downLoadAdp.setDate(loadMap.get(currIndex));
                    }
                    downPopWindow.showAsDropDown(downLayout);
                } else {
                    Elog(TAG, "没获取到数据，开始获取");
                    getDate();
                }
                break;

            case R.id.bookType_layout:
                //教材选择
                type = 3;
                if (versionWindow == null) {
                    initVersionPopWindow();
                } else {
                    final WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.7f;
                    getWindow().setAttributes(lp);
                }
                versionWindow.showAsDropDown(bookTypeLayout);
                break;

            case R.id.right_layout:
                //去备课
                Intent intent = new Intent(BookListAcivity.this, MainActivity.class);
                Elog(TAG,"TOKENT = "+MyApplication.getInstance().getToken());
                intent.putExtra("url",AppConfig.BEIKEURL+MyApplication.getInstance().getToken());
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (type) {
            case 1:
                //表示点击年级
                pop.dismiss();
                if (bookListAdp != null){
                    bookListAdp.setCurrentIndex(position);
                }
                if (classBeen != null && classBeen.get(position) != null) {
                    tvBook.setText(classBeen.get(position).getClassName());
                    MyApplication.getInstance().setClassID(classBeen.get(position).getClassID());
                    MyApplication.getInstance().setGradeID(classBeen.get(position).getGradeID());
                    MyApplication.getInstance().setSubjectID(classBeen.get(position).getSubjectID());
                    MyApplication.getInstance().setClassName(classBeen.get(position).getClassName());
                }
                break;
            case 2:
                //点击下载列表
                downPopWindow.dismiss();
                if (downLoadAdp != null){
                    downLoadAdp.setCurrentIndex(position);
                }
                if (loadMap != null && loadMap.get(currIndex) != null) {
                    tvDown.setText(loadMap.get(currIndex).get(position).getBookName());
                    getDownLoadData(loadMap.get(currIndex).get(position));
                }
                break;

            case 3:
                versionWindow.dismiss();
                if (versionAdp != null){
                    versionAdp.setCurrentIndex(position);
                }
                imgNotic.setImageResource(BookListAdp.imgSelectList[position]);//图片
                tvName.setText(BookListAdp.name[position]);//教材版本文字
                viewpager.setCurrentItem(position);
                break;

            default:
                break;
        }
    }


    /**
     * 卡页监听器
     *
     * @author Administrator
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
//            float tagerX = arg0 * (screenW/numSize) + arg2 / numSize;
//            ViewPropertyAnimator.animate(image).translationX(tagerX).setDuration(0);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageSelected(int arg0) {
            currIndex = arg0;
            MyApplication.getInstance().setCurrentIndex(currIndex);
            if (loadMap != null && loadMap.get(currIndex) != null) {
                if (loadMap.get(currIndex).get(0) != null)
                    tvDown.setText(loadMap.get(currIndex).get(0).getBookName());
            } else {
                getDate();
            }
            if (versionAdp != null){
                versionAdp.setCurrentIndex(arg0);
            }
            imgNotic.setImageResource(BookListAdp.imgSelectList[arg0]);//图片
            tvName.setText(BookListAdp.name[arg0]);//教材版本文字
        }
    }

    /**
     * popwindow
     **/
    private void initPopupWindow() {
        popView = View.inflate(BookListAcivity.this, R.layout.layout_order_pop_preferential, null);
        // PopupWindow初始化
        pop = new PopupWindow(popView, bookLayout.getWidth(), PercentRelativeLayout.LayoutParams.WRAP_CONTENT);
        pop.setAnimationStyle(R.style.popwin_anim_style_test);
        lv_pop_preferential = (ListView) popView.findViewById(R.id.lv_pop_preferential);
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
//        lv_pop_preferential.setBackgroundResource(R.drawable.shape_pop_book);
        //表示使用集合测试数据
        bookListAdp = new BookListAdp(BookListAcivity.this, classBeen, 1);
        lv_pop_preferential.setAdapter(bookListAdp);
        lv_pop_preferential.setOnItemClickListener(this);
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                pop.dismiss();
            }
        });
    }

    /**
     * popwindow
     **/
    private void initDownLoadPopWindow(ArrayList<LoginBean.ClassBean> lists) {
        downPop = View.inflate(BookListAcivity.this, R.layout.layout_order_pop_preferential, null);
        // PopupWindow初始化
        downPopWindow = new PopupWindow(downPop, downLayout.getWidth(), PercentRelativeLayout.LayoutParams.WRAP_CONTENT);
        downPopWindow.setAnimationStyle(R.style.popwin_anim_style_test);
        ListView listView = (ListView) downPop.findViewById(R.id.lv_pop_preferential);
//        lv_pop_preferential.setBackgroundResource(R.drawable.shape_pop_book);
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        //表示使用集合测试数据
        downLoadAdp = new BookListAdp(BookListAcivity.this, lists, 2);
        listView.setAdapter(downLoadAdp);
        listView.setOnItemClickListener(this);
        // 设置点击窗口外边窗口消失
        downPopWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        downPopWindow.setFocusable(true);
        downPopWindow.setBackgroundDrawable(new BitmapDrawable());
        downPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                downPopWindow.dismiss();
            }
        });
    }


    /**
     * popwindow
     **/
    private void initVersionPopWindow() {
        typePop = View.inflate(BookListAcivity.this, R.layout.layout_order_pop_preferential, null);
        // PopupWindow初始化
        versionWindow = new PopupWindow(typePop, bookTypeLayout.getWidth(), PercentRelativeLayout.LayoutParams.WRAP_CONTENT);
        versionWindow.setAnimationStyle(R.style.popwin_anim_style_test);
        ListView listView = (ListView) typePop.findViewById(R.id.lv_pop_preferential);
//        lv_pop_preferential.setBackgroundResource(R.drawable.shape_pop_book);
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        //表示使用集合测试数据
        versionAdp = new BookListAdp(BookListAcivity.this, null, 3);
        versionAdp.setCurrentIndex(0);
        listView.setAdapter(versionAdp);
        listView.setOnItemClickListener(this);
        // 设置点击窗口外边窗口消失
        versionWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        versionWindow.setFocusable(true);
        versionWindow.setBackgroundDrawable(new BitmapDrawable());
        versionWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                versionWindow.dismiss();
            }
        });
    }

    /***
     * 获取数据
     */
    public void getDate() {
        //获取书本列表
        String url = AppConfig.AppGetBookList
                + "?UserID=" + MyApplication.getInstance().getLoginBean().getId() +
                "&BookType=" + currIndex;
        Elog(TAG, "url " + url);
        if (MyApplication.getInstance().getCurrentIndex() == currIndex) {
            S_DialogUtil.showProgressDialog(this);
        }
        OkHttpUtils.get()//
                .url(url)//
                .id(100)
                .build()//
                .connTimeOut(5000)
                .execute(new MyStringCallback());
    }

    /***
     * 获取下载列表
     */
    public void getDownLoadData(LoginBean.ClassBean bean) {
        //获取书本列表
        if (bean != null) {
            String url = AppConfig.AppDownList
                    + MyApplication.getInstance().getLoginBean().getId() +
                    "?BookInfo=" + bean.getZipName() + "_" + bean.getBookID();
            Elog(TAG, "down url " + url);
            S_DialogUtil.showProgressDialog(this);
            OkHttpUtils.get()//
                    .url(url)//
                    .id(101)
                    .build()//
                    .connTimeOut(5000)
                    .execute(new MyStringCallback());
        }
    }

    /****
     * 拿到易课发过来的IP地址
     */
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Elog(TAG, "收到广播，" + intent.getAction());
            ToastUtils.showToast(BookListAcivity.this, "收到广播 " + intent.getStringExtra("ip"));
            if (intent != null) {
                if (intent.getAction().equals(ACTION_UPLOAD)) {
                    //表示收到创建链接
                    //拿到了IP 开始启动文件传输服务
                    socketService = new Intent(context, FileTransminssionService.class);
                    socketService.putExtra("ip", intent.getStringExtra("ip"));
                    socketService.putExtra("BookID", MyApplication.getInstance().getBookID());
                    socketService.putExtra("UserID", MyApplication.getInstance().getLoginBean().getId());
                    socketService.putExtra("ClassID", MyApplication.getInstance().getClassID());
                    context.startService(socketService);
                }
            }
        }
    };

    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id) {
            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            S_DialogUtil.dismissDialog();
            Log.e(TAG, "onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：complete " + id);
            Log.e(TAG, "onResponse： " + response);
            S_DialogUtil.dismissDialog();
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                boolean status = jsonObject.optBoolean("Success");
                switch (id) {
                    case 100:
                        if (status) {
                            //表示解析成功
                            String Data = jsonObject.optString("Data");
                            if (BaseFragment.isEmty(Data)) {
                                ToastUtils.showToast(BookListAcivity.this, "没有更多数据");
                                tvDown.setText("暂无数据");
                                loadMap.put(currIndex, null);
                                return;
                            } else {
                                ArrayList<LoginBean.ClassBean> list = KingSoftParasJson.getListByJson(Data, LoginBean.ClassBean.class);
                                if (list != null && list.size() > 0) {
                                    loadMap.put(currIndex, list);
                                    if (list.get(0) != null) {
                                        tvDown.setText(list.get(0).getBookName());
                                    }
                                } else {
                                    tvDown.setText("暂无数据");
                                    loadMap.put(currIndex, null);
                                }
                            }
                        } else {
                            Toast.makeText(BookListAcivity.this, jsonObject.optString("Message"),
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 101:
                        if (status) {
                            //表示解析成功
                            String Data = jsonObject.optString("Data");
                            if (BaseFragment.isEmty(Data)) {
                                ToastUtils.showToast(BookListAcivity.this, "没有更多数据");
                                return;
                            } else {
                                if (fragmentList != null) {
                                    BookListFragment fragment = (BookListFragment) fragmentList.get(currIndex);
                                    if (fragment != null && fragment.isVisi()) {
                                        fragment.downStart(Data);
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(BookListAcivity.this, jsonObject.optString("Message"),
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e(TAG, "inProgress:" + progress);
        }
    }

    /***
     * 读取本地缓存json数据
     */
    public void getJsonData(String bookInfo) {
        Elog(TAG, "getJsonData COME = " + bookInfo);
        if (!isEmty(bookInfo)) {
            bookInfoList.clear();
            jsonDataMap.clear();
            bookInfoList = KingSoftParasJson.getListByJson(bookInfo, BookInfoBean.class);
            if (bookInfoList != null && bookInfoList.size() > 0) {
                jsonDataMap.put(0, bookInfoList);
                TBList.clear();
                JFList.clear();
                TSList.clear();
                //表示读取到了数据
                for (BookInfoBean book : bookInfoList) {
                    if (book != null) {
                        //对书本进行分类,1是同步，2是教辅，3是特色
                        if (book.getBookType().equals("1")) {
                            TBList.add(book);
                        } else if (book.getBookType().equals("2")) {
                            JFList.add(book);
                        } else {
                            TSList.add(book);
                        }
                    }
                }
                Elog(TAG, "size0 = " + bookInfoList.size() + ";11 = " + TBList.size() + ";22 = " + JFList.size() + ";33 = " + TSList.size());
                jsonDataMap.put(1, TBList);
                jsonDataMap.put(2, JFList);
                jsonDataMap.put(3, TSList);
            }
        }
    }

    /***
     * 检查是否需要更新
     * @param lists
     */
    private ArrayList<LoginBean.ClassBean> changeData(ArrayList<LoginBean.ClassBean> lists){
        String json = readCacheDate(fileDownPath +"/data/"+
                MyApplication.getInstance().getUserID()+"book.json");
        if (!isEmty(json)) {
            //infoBeanArrayList本地缓存数据，lists线上数据
            ArrayList<BookInfoBean> infoBeanArrayList = KingSoftParasJson.getListByJson(json, BookInfoBean.class);
            if (infoBeanArrayList != null && infoBeanArrayList.size() > 0) {
                for (int i = 0; i < infoBeanArrayList.size() ;i ++){
                    for (int j = 0; j < lists.size() ;j ++){
                        if (infoBeanArrayList.get(i).getID().equals(lists.get(j).getBookID())){
                            //先找相同的书，再来比较时间
                            if (!isEmty(infoBeanArrayList.get(i).getCreateDateTime()) &&
                                    !isEmty(lists.get(j).getPreTime())&&
                                    infoBeanArrayList.get(i).getCreateDateTime().
                                            equals(lists.get(j).getPreTime())){
                                    //表示不需要更新,默认都是需要更新的
                                lists.get(j).setUpdata(true);
                                lists.set(j,lists.get(j));
                            }
                            break;
                        }
                    }
                }
            }
        }
        return lists;
    }
}
