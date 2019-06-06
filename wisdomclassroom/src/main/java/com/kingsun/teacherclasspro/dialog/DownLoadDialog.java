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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.utils.Configure;

/***
 * 操作过程提示对话框
 * @author Administrator
 *
 */
public class DownLoadDialog extends Dialog {
	private String TAG = "DownLoadDialog";
	private Context context;
	private int type = 0;
	//用来判断相关操作
	private int typeY;
	private TextView tipTextView,tv_notice;
	private String content;//提示内容
	private ProgressBar progressBar;
	public DownLoadDialog(Context context, String toast) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.content = toast ;
	}

	public void showDialog(int x, int y){
		Configure.init((Activity) context);
		View view = View.inflate(context, R.layout.layout_down_book_dialog,null);
		int width = Configure.screenWidth;
		Log.i(TAG, "width = "+width+";h = "+Configure.screenHeight);
		this.typeY = y;
		int height = width * 2/3;
		if (x == 0) {
			//标示退出账号提示框
			width = width * 50 / 100;
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
		window.setAttributes(wl);
	}

	@SuppressLint("CutPasteId")
	public void  initView(){
		progressBar = (ProgressBar) findViewById(R.id.pb_dub_startdub);

		tipTextView = (TextView) findViewById(R.id.tv_size);

		tv_notice = (TextView) findViewById(R.id.tv_content);
	}

	//更新总数下载记录
	public void setTextPro(String pro){
		Log.i(TAG, "setTextPro = "+pro);
		if (tipTextView != null) {
			Log.i(TAG, "setTextPro end = "+pro);
			tipTextView.setText(pro);
		}
	}

	//更新当前下载进度
	public void setTextNotic(String pro){
		Log.i(TAG, "setTextNotic = "+pro);
		if (tv_notice != null) {
			Log.i(TAG, "setTextNotic end= "+pro);
			tv_notice.setText(pro);
		}
	}

	//更新当前下载进度
	public void setProgress(int pro){
		if (progressBar != null) {
			progressBar.setProgress(pro);
		}
	}

	public void setProgressMax(){
		if (progressBar != null) {
			progressBar.setMax(100);
		}
	}

	protected DownLoadDialog(Context context, boolean cancelable,
							 OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public DownLoadDialog(Context context) {
		super(context);
	}

	public DownLoadDialog(Context context, AttributeSet attrs) {
		super(context);
	}

}
