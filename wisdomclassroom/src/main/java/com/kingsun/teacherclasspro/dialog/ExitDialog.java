package com.kingsun.teacherclasspro.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity.PostOrCanser;
import com.kingsun.teacherclasspro.utils.Configure;
import com.kingsun.teacherclasspro.utils.ToastUtils;

public class ExitDialog extends Dialog implements View.OnClickListener{

	private TextView tv_content,tv_title,sure,canser;//确定取消;//提示性文字
	private Context context;
	private PostOrCanser postOrCanser;
	private View view_line;
	private int type = 0;
	//用来判断相关操作
	private int typeY;
	private String content;//提示内容
	//传递的数据
	private Intent intent;

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	private LinearLayout title_layout;
	private RelativeLayout file_layout;
	private EditText ed_fileName;
	public ExitDialog(Context context,PostOrCanser pCanser,String toast) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.postOrCanser = pCanser;
		this.content = toast ;
		//		setCanceledOnTouchOutside(false);
		//		setCancelable(false);// 设置点击屏幕Dialog不消失
	}
	public void showDialog(int x, int y){  
		Configure.init((Activity) context);
		View view = View.inflate(context, R.layout.post_dialog,null);
		int width = Configure.screenWidth;
		this.typeY = y;
		int height = width * 2/3;
		if (x == 0) {
			//设置提示框
			width = width * 45 / 100;
			height = width *25 /80;
			//设置触摸对话框意外的地方取消对话框  
			setCanceledOnTouchOutside(true); 
		}else if (x == 1 ) {
			//表示用户录入微课文件名
			width = width * 45 / 100;
			height = width *3 / 8;
			//设置触摸对话框意外的地方取消对话框  
			setCanceledOnTouchOutside(false); 
		}
		setContentView(view, new ViewGroup.LayoutParams(width, height));
		this.type = x ;//就只是在扫二维码的时候使用的，其它时候都是代表x的坐标
		initView();
		windowDeploy(0, 0);  
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
		title_layout = (LinearLayout) findViewById(R.id.title_layout);
		view_line = findViewById(R.id.view_line);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView)findViewById(R.id.tv_content);
		sure = (TextView)findViewById(R.id.btn_post);
		canser = (TextView)findViewById(R.id.btn_cancle);
		
		/**
		 * 设置文件名
		 */
		file_layout = (RelativeLayout) findViewById(R.id.file_layout);
		ed_fileName = (EditText) findViewById(R.id.ed_fileName);
		
		sure.setOnClickListener(this);
		canser.setOnClickListener(this);
		tv_title.setText(content+"");
		if (type == 0) {
			//设置网络
			file_layout.setVisibility(View.GONE);
			title_layout.setVisibility(View.VISIBLE);
		}else if (type == 1) {
			//输入文件名
			file_layout.setVisibility(View.VISIBLE);
			title_layout.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_post:
			if (type == 0) {
				//表示设置网络
				postOrCanser.Post();
			}else if (type == 1) {
				//设置上传微课文件名
				intent = new Intent();
				String nameString = ed_fileName.getText().toString().trim();
				if (nameString == null || nameString.equals("")) {
					ToastUtils.showToast(context, "请输入录制微课的名字！");
					return;
				}
				intent.putExtra("name", nameString);
				intent.putExtra("path",content);
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.hideSoftInputFromWindow(ed_fileName.getWindowToken(), 0);//从控件所在的窗口中隐藏
				postOrCanser.upLoad(intent);
			}
			dismiss();
			break;

		case R.id.btn_cancle:
			postOrCanser.Canser();
			dismiss();
			break;
		default:
			break;
		}
	}
}
