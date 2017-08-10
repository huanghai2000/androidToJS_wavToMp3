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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.utils.Configure;

/***
 * 操作过程提示对话框
 * @author Administrator
 *
 */
public class upLoadProDialog extends Dialog {

	private ImageView imageView;
	private Context context;
	private int type = 0;
	//用来判断相关操作
	private int typeY;
	private TextView tipTextView,tv_notice;
	private String content;//提示内容

	public upLoadProDialog(Context context,String toast) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.content = toast ;
	}

	public void showDialog(int x, int y){  
		Configure.init((Activity) context);
		View view = View.inflate(context, R.layout.up_load_progress,null);
		int width = Configure.screenWidth;
		Log.i("upLoadProDialog", "width = "+width+";h = "+Configure.screenHeight);
		this.typeY = y;
		int height = width * 2/3;
		if (x == 0) {
			//标示退出账号提示框
			width = width * 85 / 100;
			height = width *3 /8;
		}else if (x == 1 ) {
			//表示提示到现场
			width = width * 30 / 100;
			height = width *3 / 5;
		}
		setContentView(view, new ViewGroup.LayoutParams(width, height));
		//		setContentView(view, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.type = x ;//就只是在扫二维码的时候使用的，其它时候都是代表x的坐标
		initView();
		windowDeploy(0, 0);  
		//设置触摸对话框意外的地方取消对话框  
		setCanceledOnTouchOutside(false);  
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
		imageView = (ImageView) findViewById(R.id.img);

		tipTextView = (TextView) findViewById(R.id.tipTextView);

		tv_notice = (TextView) findViewById(R.id.tv_notice);
		// 加载动画  
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
				context, R.anim.loading_animation);  
		// 使用ImageView显示动画  
		imageView.startAnimation(hyperspaceJumpAnimation); 
	}

	//更新进度
	public void setTextPro(String pro){
		if (tipTextView != null) {
			tipTextView.setText(pro);
		}
	}

	//更新显示
	public void setTextNotic(String pro){
		if (tv_notice != null) {
			tv_notice.setText(pro);
		}
	}

	protected upLoadProDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public upLoadProDialog(Context context) {
		super(context);
	}

	public upLoadProDialog(Context context, AttributeSet attrs) {
		super(context);
	}

}
