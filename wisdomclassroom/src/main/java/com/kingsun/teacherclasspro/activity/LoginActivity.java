package com.kingsun.teacherclasspro.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.king.percent.support.PercentRelativeLayout;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.LoginBean;
import com.kingsun.teacherclasspro.dialog.S_DialogUtil;
import com.kingsun.teacherclasspro.fragment.BaseFragment;
import com.kingsun.teacherclasspro.fragment.BookListFragment;
import com.kingsun.teacherclasspro.utils.AppConfig;
import com.kingsun.teacherclasspro.utils.KingSoftParasJson;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by hai.huang on 2018/3/14.
 */

public class LoginActivity extends BaseActivity {
    private String TAG = "LoginActivity";
    @InjectView(R.id.ed_name)
    EditText edName;
    @InjectView(R.id.ed_pwd)
    EditText edPwd;
    @InjectView(R.id.btn_login)
    ImageView btnLogin;
    @InjectView(R.id.img_name)
    ImageView imgName;
    @InjectView(R.id.view1)
    View view1;
    @InjectView(R.id.name_layout)
    PercentRelativeLayout nameLayout;
    @InjectView(R.id.img_pwd)
    ImageView imgPwd;
    public SharedPreferences sp;
    private LoginBean loginBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.inject(this);
        MyApplication.getInstance().setOpenIK(true);//默认打开易课
        initView();
//        initData();
    }

    private void initView() {
        sp = getSharedPreferences("KINGSUNSOFT",MODE_PRIVATE);
        if (sp != null) {
            String name = sp.getString("userName","");
            String pwd = sp.getString("userPwd","");
            if (!isNULL(name)){
                edName.setText(name);
            }
            if (!isNULL(pwd)){
                edPwd.setText(pwd);
            }
        }
        if (!isNULL(edName.getText().toString()) && !isNULL(edPwd.getText().toString())){
            Ilog(TAG,"开始登录");
            loginApp();
        }
    }

    private void initData(){
        String url = "http://zhongyou.zhoil.cn/irsapi/Selectorder.aspx";
        String url1 = "http://zhongyou.zhoil.cn/irsapi/Selectorder.aspx?posid=N9NL10339588&ShiftID=99&Station=79%20&Companyid=%E6%9F%90%E6%9F%90%E5%8A%A0%E6%B2%B9%E7%AB%99&md5=EAEAFDB035FDCDB8&star=1530613380&end=1530615360";
        Map<String, String> params = new HashMap<String, String>();
//        params.put("POSID","N9NL10339588");
//        params.put("md5","EAEAFDB035FDCDB8");
//        params.put("ShiftID","99");
//        params.put("Station","79");
//        params.put("start","1530613380");
//        params.put("end","1530615360");
        params.put("UserName",edName.getText().toString());
        OkHttpUtils.get()//
                .url(url1)//
                .id(101)
                .params(params)//
//                .headers(headers)//
                .build()//
                .connTimeOut(50)
                .execute(new MyStringCallback());
    }

    @OnClick({R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //登录
                if (isNULL(edName.getText().toString().trim())){
                    ToastUtils.showToast(LoginActivity.this,"请输入用户名");
                    return;
                }
                if (isNULL(edPwd.getText().toString().trim())){
                    ToastUtils.showToast(LoginActivity.this,"请输入密码");
                    return;
                }
                loginApp();
                break;
        }
    }

    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request, int id)
        {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id)
        {
            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
            Log.e(TAG,"onError:" + e.getMessage());
            btnLogin.setEnabled(true);
        }

        @Override
        public void onResponse(String response, int id)
        {
            Log.e(TAG, "onResponse：complete "+id);
            Log.e(TAG, "onResponse： "+response);
            btnLogin.setEnabled(true);
            switch (id)
            {
                case 100:
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        boolean status = jsonObject.optBoolean("Success");
                        if (status){
                            //表示解析成功
                            String Data = jsonObject.optString("Data");
                            if (!isNULL(Data)){
                                loginBean = (LoginBean) KingSoftParasJson.getObjectByJson(Data,LoginBean.class);
                                MyApplication.getInstance().setUserName(edName.getText().toString());
                                MyApplication.getInstance().setUserPWD(edPwd.getText().toString());
                                SharedPreferences.Editor e = sp.edit();
                                e.putString("userName",edName.getText().toString());//用户名密码
                                e.putString("userPwd",edPwd.getText().toString());//访问信息
                                e.commit();
                                if (loginBean != null){
                                    MyApplication.getInstance().setToken(loginBean.getToken());
                                    MyApplication.getInstance().setUserID(loginBean.getId());
                                    MyApplication.getInstance().setLoginBean(loginBean);
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this,BookListAcivity.class);
                                    intent.putExtra("id",loginBean.getId());
                                    startActivity(intent);
//                                    finish();
                                }
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, jsonObject.optString("Message"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 101:
                    Toast.makeText(LoginActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
        }
    }

    private void loginApp(){
        btnLogin.setEnabled(false);
        String url = AppConfig.AppLogin
                +"?UserName="+edName.getText().toString()+
                "&Password="+edPwd.getText().toString();
        Elog(TAG,"url "+url);

        Map<String, String> params = new HashMap<String, String>();
        params.put("UserName",edName.getText().toString());
        params.put("Password",edPwd.getText().toString());
//        Map<String, String> headers = new HashMap<>();
//        headers.put("APP-Key", "APP-Secret222");
//        headers.put("APP-Secret", "APP-Secret111");
        params.put("time",System.currentTimeMillis()+"");
        OkHttpUtils.get()//
                .url(url)//
                .id(100)
                .params(params)//
//                .headers(headers)//
                .build()//
                .connTimeOut(50)
                .execute(new MyStringCallback());
    }
}
