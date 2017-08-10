package com.kingsun.teacherclasspro.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.activity.BaseActivity.doThing;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.utils.Configure;
import com.kingsun.teacherclasspro.widgets.AtuoCompleteTextView;

/***
 * 操作过程提示对话框
 * @author Administrator
 *
 */
public class setIpAndPortDialog extends Dialog implements View.OnClickListener {
	private Context context;
	private  AtuoCompleteTextView  text,ed_yuming;
	private TextView  tv_login,version_tv;
	private String TAG = "setIpAndPortDialog";
	private  doThing doThing;
	private String version;
	public setIpAndPortDialog(Context context,doThing doThing,String po) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.doThing = doThing;
		this.version = po;
	}

	public void showDialog(int x, int y){  
		Configure.init((Activity) context);
		View view = View.inflate(context, R.layout.set_ip_port_layout,null);
		int width = Configure.screenWidth;
		Log.i(TAG, "width = "+width+";h = "+Configure.screenHeight);
		int height = width * 2/3;
		//表示提示到现场
		width = width * 40/ 100;
		height = width *3 / 5;
		setContentView(view, new ViewGroup.LayoutParams(width, height));
		//		setContentView(view, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		initView();
		windowDeploy(0, 0);  
		//设置触摸对话框意外的地方取消对话框  
		setCanceledOnTouchOutside(true);  
		show();  
	}  

	//设置窗口显示  
	public void windowDeploy(int x, int y){  
		Window window = getWindow(); //得到对话框  
		window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画  
		window.setBackgroundDrawableResource(R.color.vifrification); //设置对话框背景为透明  
		WindowManager.LayoutParams wl = window.getAttributes();  
		//根据x，y坐标设置窗口需要显示的位置  
		wl.x = x; //x小于0左移，大于0右移  
		wl.y = y; //y小于0上移，大于0下移    
		//        wl.alpha = 0.6f; //设置透明度  
		//        wl.gravity = Gravity.BOTTOM; //设置重力  
		window.setAttributes(wl);  
	}  

	@SuppressLint("CutPasteId")
	public void  initView(){
		text = (AtuoCompleteTextView) findViewById(R.id.ed_ip);
		ed_yuming = (AtuoCompleteTextView) findViewById(R.id.ed_yuming);
		version_tv = (TextView) findViewById(R.id.version_tv);
		tv_login = (TextView) findViewById(R.id.login);
		tv_login.setOnClickListener(this);
		String spString = MyApplication.getInstance().getServer_head();
//		spString =spString.replace("http://", "");
		text.setText(spString);
		text.setSelection(spString.length());
		version_tv.setText("当前版本："+version);
	}

	protected setIpAndPortDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public setIpAndPortDialog(Context context) {
		super(context);
	}

	public setIpAndPortDialog(Context context, AttributeSet attrs) {
		super(context);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			String xp = text.getText().toString();
			BaseActivity.Ilog(TAG, "xp = "+xp);
			if (!xp.trim().toString().equals("")){
				doThing.changeIP(ed_yuming.getText()+"",xp);
				dismiss();
//				if (xp.contains(":")) {
//					String [] tStrings = xp.split(":");
//					if (tStrings != null && tStrings.length ==2) {
//						if (xp.startsWith("192.")) {
//							MyApplication.getInstance().setServer_head("http://"+xp);
//						}else{
//							MyApplication.getInstance().setServer_head(xp);
//						}
//						doThing.changeIP(MyApplication.getInstance().getServer_head());
//						dismiss();
//					}else{
//						ToastUtils.showToast(context, "请输入正确的地址");
//					}
//				}else{
//					ToastUtils.showToast(context, "请输入正确的地址");
//				}
			}else{
				dismiss();
			}
			break;
		default:
			break;
		}
	}
}
