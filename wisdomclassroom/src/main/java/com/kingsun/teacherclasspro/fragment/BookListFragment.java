package com.kingsun.teacherclasspro.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.king.percent.support.PercentRelativeLayout;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.activity.MainActivity;
import com.kingsun.teacherclasspro.adpter.DoanLoadBookAdp;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.BookBean;
import com.kingsun.teacherclasspro.bean.BookInfoBean;
import com.kingsun.teacherclasspro.bean.DownLoadBean;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.dialog.DownLoadDialog;
import com.kingsun.teacherclasspro.dialog.ExitDialog;
import com.kingsun.teacherclasspro.utils.AppConfig;
import com.kingsun.teacherclasspro.utils.Check_UnRar;
import com.kingsun.teacherclasspro.utils.KingSoftParasJson;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.kingsun.teacherclasspro.utils.Utils;
import com.kingsun.teacherclasspro.utils.ZipExtractorTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

import static com.kingsun.teacherclasspro.R.id.noData_Layout;
import static com.kingsun.teacherclasspro.activity.BaseActivity.Elog;
import static com.kingsun.teacherclasspro.activity.BaseActivity.Ilog;

/**
 * Created by hai.huang on 2018/3/15.
 */

@SuppressLint("ValidFragment")
public class BookListFragment extends BaseFragment implements
        AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private String TAG = "Fragment";
    private GridView mGridview;
    private View layout_View;
    private int index;
    private ArrayList<BookBean> bookList = new ArrayList<>();
    private String zipPath = "";
    private ProgressDialog dlgMixing;
    private DoanLoadBookAdp adp;
    private DownLoadDialog downDialog;
    private PercentRelativeLayout noDataLayout;
    private TextView tv_noData;
    private dataChangeListener listener;

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
        if (adp != null){
            adp.setDel(false);
        }
    }

    private boolean isDel = false;//是否是删除状态
    public boolean isVisi() {
        return isVisi;
    }

    private boolean isVisi,isDown;
    public BookListFragment() {
    }

    public BookListFragment(int po,ArrayList<BookInfoBean> list,dataChangeListener mListener) {
        this.index = po;
        this.bookInfoList = list;
        this.listener = mListener;
    }

    private ArrayList<DownLoadBean> downLoadBeen = new ArrayList<>();//备课数据下载集合
    private ArrayList<DownLoadBean> dBeen = new ArrayList<>();//需要下载的备课数据下载集合
    private ArrayList<DownLoadBean> rBeen = new ArrayList<>();//需要下载的备课数据下载集合
    private ExitDialog dialog;
    private BaseActivity.PostOrCanser postOrCanser  = new BaseActivity.PostOrCanser() {
        @Override
        public void Post() {

        }

        @Override
        public void upLoad(Intent intent) {
            if (intent != null){
                int  index  = intent.getIntExtra("position",0);
                String info = delBookFile(bookInfoList.get(index).getID());//删除当前页面数据，
                boolean sp = delFile(fileDownPath+"/data/"+MyApplication.getInstance().getUserID(),bookInfoList.get(index).getID());
                if (sp){
                    if (listener != null){
                        intent.putExtra("data",info);
                        listener.changeData(intent);
                    }
                    bookInfoList.remove(index);
                    if (adp != null){
                        adp.setDate(bookInfoList);
                    }
                }
            }
        }

        @Override
        public void Canser() {

        }
    };

    public void setBookInfoList(ArrayList<BookInfoBean> bookInfoList) {
        this.bookInfoList = bookInfoList;
        if (bookInfoList != null && bookInfoList.size() > 0){
            if (noDataLayout != null){
                noDataLayout.setVisibility(View.GONE);
            }
            if (mGridview != null){
                mGridview.setVisibility(View.VISIBLE);
            }
            if (adp != null){
                adp.setDate(bookInfoList);
                adp.setDel(isDel);
            }
        }
    }

    private ArrayList<BookInfoBean> bookInfoList ;//保存所有的书本信息
    private int currentIndex = 0,allSize = 0;
    private DownLoadBean bookBean;//书本信息Bean,isDownload = 2;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisi = true;
            Ilog(TAG, "setUserVisibleHint =显示== " + index );
            if (adp != null){
                adp.setDel(isDel);
            }
        } else {
            Ilog(TAG, "setUserVisibleHint =隐藏== " + index);
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout_View = inflater.inflate(R.layout.book_list_fragment_layout, container, false);
        initView();
        dlgMixing = new ProgressDialog(getActivity());
        dlgMixing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgMixing.setCancelable(false);
        dlgMixing.setCanceledOnTouchOutside(false);
        downDialog = new DownLoadDialog(getActivity(),"");
        return layout_View;
    }

    private void initView() {
        mGridview = (GridView) layout_View.findViewById(R.id._gridview);
        adp = new DoanLoadBookAdp(getActivity());
        mGridview.setAdapter(adp);
        mGridview.setOnItemClickListener(this);

        noDataLayout = (PercentRelativeLayout) layout_View.findViewById(noData_Layout);
        tv_noData = (TextView) layout_View.findViewById(R.id.tv_noData);

        if (bookInfoList == null || bookInfoList.size() == 0){
            //表示没有数据
            noDataLayout.setVisibility(View.VISIBLE);
        }else{
            //展示数据
            mGridview.setVisibility(View.VISIBLE);
            adp.setDate(bookInfoList);
        }
        mGridview.setOnItemLongClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dlgMixing != null ){
            dlgMixing.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (downDialog != null && downDialog.isShowing()){
            downDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookInfoBean bean = (BookInfoBean) parent.getAdapter().getItem(position);
        if (bean != null){
            if (isDel){
                String toastName = "确定要删除:"+bean.getBooKName()+"吗？";
                //表示当前正在展开删除
                if (dialog == null){
                    dialog = new ExitDialog(getActivity(),postOrCanser,toastName);
                }else {
                    dialog.setText(toastName);
                }
                dialog.showDialog(6,position);
            }else {
                /******************************需要保留************************************/
                String BookName = new File(bean.getImgPath()).getParent();
                BookName = BookName.substring(BookName.lastIndexOf("/")+1);
                MyApplication.getInstance().setBookID(bean.getID().toString());
                /******************************************************************/
                if(MyApplication.getInstance().isOpenIK()){
                    //表示打开易课
                    HashMap<String,String> prams = new HashMap<>();
                    prams.put("account",MyApplication.getInstance().getUserName());
                    prams.put("password",MyApplication.getInstance().getUserPWD());
                    prams.put("apiName","Students");
                    prams.put("webUrl",AppConfig.API);
                    prams.put("classID",""+MyApplication.getInstance().getClassID());
                    prams.put("param","BookID="+bean.getID()
                            +"&UserID="+MyApplication.getInstance().getLoginBean().getId()
                            +"&EditionID="+bean.getEdition()
                            +"&SubjectID="+bean.getSubject()
                            +"&BookName="+BookName
                            +"&GradeID="+MyApplication.getInstance().getGradeID()
                            +"&ClassID="+MyApplication.getInstance().getClassID()
                            +"&BookType="+bean.getBookType()+"#/Lesson");
                    String ClassName = MyApplication.getInstance().getClassName();
                    String clsName = ClassName.substring(ClassName.indexOf("年级")+2);
                    String gradeName = ClassName.replace(clsName,"");
                    prams.put("ClassName",""+ClassName);
                    prams.put("gradeName",""+gradeName);
                    prams.put("clsName",""+clsName);
                    Log.e(TAG,"json = "+new Gson().toJson(prams));
                    Utils.gotoXKJ(getActivity(),"com.kingsunedu.ik",new Gson().toJson(prams),"com.clovsoft.ik.MainActivity");
                }else {
                    String dbPath = bean.getImgPath().substring(0, bean.getImgPath().lastIndexOf("/"))+"/db.js";
                    Elog(TAG,"path "+dbPath);
                    Intent inten = new Intent();
                    inten.setClass(getActivity(), MainActivity.class);
                    inten.putExtra("DBPath ",dbPath+"");
                    inten.putExtra("BookID ",bean.getID()+"");
                    inten.putExtra("UserID ",MyApplication.getInstance().getLoginBean().getId()+"");
                    inten.putExtra("EditionID ",bean.getEdition()+"");
                    inten.putExtra("SubjectID ",bean.getSubject()+"");
                    inten.putExtra("GradeID ",MyApplication.getInstance().getGradeID()+"");
                    inten.putExtra("ClassID ",MyApplication.getInstance().getClassID()+"");
                    inten.putExtra("BookType ",bean.getBookType()+"");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Bean", bean);
                    inten.putExtra("url","file:"+ fileDownPath+"/index.html?BookID="+bean.getID()
                            +"&UserID="+MyApplication.getInstance().getLoginBean().getId()
                            +"&EditionID="+bean.getEdition()
                            +"&SubjectID="+bean.getSubject()
                            +"&BookName="+BookName
                            +"&GradeID="+MyApplication.getInstance().getGradeID()
                            +"&ClassID="+MyApplication.getInstance().getClassID()
                            +"&BookType="+bean.getBookType()+"#/Lesson");
                    inten.putExtras(bundle);
                    getActivity().startActivity(inten);
                }
            }

            //模拟发广播
//            Intent intent = new Intent();
//            intent.setAction(BookListAcivity.ACTION_UPLOAD);
//            intent.putExtra("ip","192.168.40.70");
//            intent.putExtra("BookID",bean.getID().toString());
//            intent.putExtra("UserID",MyApplication.getInstance().getLoginBean().getId()+"");
//            //发送广播
//            getActivity().sendBroadcast(intent);
        }
    }
    /**
     * 下载更新
     */
    @SuppressLint("HandlerLeak")
    private Handler downloadHandle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.UNZIP_START:
                    //正在解压
                    if (dlgMixing == null){
                        dlgMixing = new ProgressDialog(getActivity());
                        dlgMixing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dlgMixing.setCancelable(false);
                        dlgMixing.setCanceledOnTouchOutside(false);
                    }
                    dlgMixing.setMessage("正在解压文件");
                    dlgMixing.show();
                    break;

                case Constant.DOWNLOAD_UNZIP_RESULT:
                    //解压完成
                    if (dlgMixing != null) {
                        dlgMixing.cancel();
                    }
                    String openName = (String) msg.obj;
                    Elog(TAG,"解压成功 "+openName);
                    if (bookBean != null){
                        Elog(TAG,"解压成功:==xx  "+ bookBean.getOther());
                        BookInfoBean infoBean = (BookInfoBean)KingSoftParasJson.getObjectByJson(bookBean.getOther(),BookInfoBean.class);
                        if (infoBean != null){
                            Elog(TAG,"解压成功==> "+infoBean.getBooKName());
                            infoBean.setImgPath(openName);
                            if (bookInfoList != null && bookInfoList.size() >0){
                                for (int i = 0 ; i < bookInfoList.size() ; i ++  ){
                                    if (bookInfoList.get(i) != null && bookInfoList.get(i).getID().equals(infoBean.getID())){
                                        //表示有相同的数据
                                        bookInfoList.set(i,infoBean);
                                        break;
                                    }else{
                                        if (i == bookInfoList.size() -1){
                                            //表示没有，需要添加
                                            bookInfoList.add(infoBean);
                                        }
                                    }
                                }
                            }else{
                                bookInfoList = new ArrayList<>();
                                bookInfoList.add(infoBean);
                            }
                            //展示数据
                            noDataLayout.setVisibility(View.GONE);
                            mGridview.setVisibility(View.VISIBLE);
                            adp.setDate(bookInfoList);

                            //把文件写入SD卡
                            String bookInfo = BaseFragment.readCacheDate(fileDownPath+"/data/"+
                                    MyApplication.getInstance().getUserID()+"book.json");
                            String sendInfo = "";
                            if (!isEmty(bookInfo)){
                                ArrayList<BookInfoBean> list = KingSoftParasJson.getListByJson(bookInfo,BookInfoBean.class);
                                if (list != null && list.size() > 0){
                                    //表示读取到了数据
                                    for (int i = 0 ; i < list.size() ; i ++  ){
                                        if (list.get(i) != null && list.get(i).getID().equals(infoBean.getID())){
                                            list.set(i,infoBean);
                                            break;
                                        }
                                        if (i == list.size() -1){
                                            //表示没有，需要添加
                                            list.add(infoBean);
                                        }
                                    }
                                    sendInfo = new Gson().toJson(list);
                                    saveFile(sendInfo,fileDownPath+"/data/"+
                                            MyApplication.getInstance().getUserID()+"book.json");
                                }
                            }else{
                                sendInfo = new Gson().toJson(bookInfoList);
                                saveFile(sendInfo,fileDownPath+"/data/"+
                                        MyApplication.getInstance().getUserID()+"book.json");
                            }
                            //更新数据
                            if (listener != null){
                                Intent intent = new Intent();
                                intent.putExtra("data",sendInfo);
                                listener.changeData(intent);
                            }
                        }
                    }
                    break;

                case Constant.UNZIP_NO_SPACE:
                    //解压空间不足
                    if (dlgMixing != null) {
                        dlgMixing.cancel();
                    }
                    ToastUtils.showToast(getActivity(),(String) msg.obj);
                    break;

                case Constant.UNZIP_ERROR:
                    //解压出错
                    if (dlgMixing != null) {
                        dlgMixing.cancel();
                    }
                    ToastUtils.showToast(getActivity(),(String) msg.obj);
                    break;

                case 1001:
                    long total1 = (Long) msg.obj;
//                    Elog("DOWNLOAD","下载进度 "+total1+"%");
                    if (downDialog != null){
                        downDialog.setTextNotic(total1+"%");
                        downDialog.setProgress((int)total1);
                        downDialog.setTextPro(currentIndex+"/"+allSize);
                    }
                    if (total1 == 100 ) {
                        Elog(TAG,"当前下载完毕");
                        if (downDialog != null){
                            downDialog.setProgress(0);
                        }
                        if (dBeen != null && dBeen.size() > 0){
                            dBeen.remove(0);
                            if (dBeen != null && dBeen.size() > 0){
                                if (dBeen.get(0) != null){
                                    currentIndex += 1;
                                    if (downDialog != null){
                                        downDialog.setProgressMax();
                                        downDialog.setTextPro(currentIndex+"/"+allSize);
                                    }
                                    downFile(dBeen.get(0),downloadHandle);
                                }
                            }else{
                                //表示所有的下载任务已经完成了,开始解压文件
                                if (downDialog != null && downDialog.isShowing()){
                                    downDialog.dismiss();
                                }
                                if (zipPath.endsWith(".zip")) {
                                    String  pptUrl =zipPath.replace(".zip", "");
//                                    try {
//                                        dlgMixing.setMessage("正在解压文件");
//                                        dlgMixing.show();
//                                        //解压PPT
//                                        new Check_UnZip(zipPath ,downloadHandle,pptUrl).start();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
                                    ZipExtractorTask task = new ZipExtractorTask(zipPath,
                                            pptUrl, getActivity(), true,downloadHandle);
                                    task.execute();
                                }else if(zipPath.endsWith(".rar")){
                                    //解压rar文件
                                    String  pptUrl =zipPath.replace(".rar", "");
                                    try {
                                        dlgMixing.setMessage("正在解压文件");
                                        dlgMixing.show();
                                        //解压PPT
                                        new Check_UnRar(zipPath ,downloadHandle,pptUrl).start();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }else {
                            Elog(TAG,"yyyyyyyyy");
                        }
                    }
                    break;

                case 1002:
                    //
                    zipPath = (String) msg.obj;
                    Elog(TAG,"当前需要解压的路径 = "+zipPath);
                    break;
                default:
                    break;
            }
        }
    };

    /***
     * 准备开始下载
     * @param info
     */
    public void downStart(String info){
        if (!isEmty(info)){
            downLoadBeen = KingSoftParasJson.getListByJson(info,DownLoadBean.class);
            if (downLoadBeen != null){
                String path = fileDownPath+"/data/"+MyApplication.getInstance().getLoginBean()
                        .getId()+"/"+downLoadBeen.get(0).getBookID()+"_data.json";
                saveFile(info,path);
                for (DownLoadBean loadBean : downLoadBeen){
                    if (loadBean.getIsDownLoad() == 1 || loadBean.getIsDownLoad() == 2){//表示需要下载的数据
                        if (loadBean.getIsDownLoad() == 2){
                            bookBean = loadBean;//保存书本信息Bean
                        }
                        dBeen.add(loadBean);
                    }if (loadBean.getIsDownLoad() == 0 || loadBean.getIsDownLoad() == 2){//需要保存json文件的数据
                        rBeen.add(loadBean);
                    }
                }
                /*************************开始资源下载********************************/
                if (dBeen != null && dBeen.size() > 0){
                    if (dBeen.get(0) != null){
                        currentIndex = 1;
                        allSize = dBeen.size();
                        downFile(dBeen.get(0),downloadHandle);
                        isDown = true;
                        if (downDialog == null){
                            downDialog = new DownLoadDialog(getActivity(),"");
                        }
                        downDialog.setProgressMax();
                        downDialog.setTextPro(currentIndex+"/"+allSize);
                        downDialog.setTextNotic("0%");
                        downDialog.showDialog(0,1);
                    }
                }
                /*************************开始保存JSON资源********************************/
                if (rBeen != null && rBeen.size() > 0){
                    SaveFileList(rBeen);
                }
            }
        }
    }

    private ArrayList<BookInfoBean>  readData(){
        String bookInfo = BaseFragment.readCacheDate(fileDownPath+"/data/"+
                MyApplication.getInstance().getUserID()+"book.json");
        ArrayList<BookInfoBean> list = new ArrayList<>();//保存所有书本信息
        ArrayList<BookInfoBean> TBList = new ArrayList<>();//保存同步教材所有的书本信息
        ArrayList<BookInfoBean> JFList = new ArrayList<>();//保存教辅教材所有的书本信息
        ArrayList<BookInfoBean> TSList = new ArrayList<>();//保存特色教材所有的书本信息
        if (!isEmty(bookInfo)){
            list = KingSoftParasJson.getListByJson(bookInfo,BookInfoBean.class);
            if (list != null && list.size() > 0){
                if (MyApplication.getInstance().getCurrentIndex() == 0){
                    //表示返回所有数据
                    return list;
                }else{
                    //表示读取到了数据
                    for (BookInfoBean book : list){
                        if (book != null){
                            //对书本进行分类,1是同步，2是教辅，3是特色
                            if (book.getBookType().equals("1")){
                                TBList.add(book);
                            }else if(book.getBookType().equals("2")){
                                JFList.add(book);
                            }else{
                                TSList.add(book);
                            }
                        }
                    }
                    switch (index){
                        case 1://1是同步
                            return TBList;
                        case 2://2是教辅
                            return JFList;
                        case 3://3是特色
                            return TSList;
                    }
                }
            }
        }
        return  list;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isDel){
            //表示展开删除
            if (adp != null){
                adp.setDel(true);
                isDel = true;
            }
        }
        return true;
    }
}
