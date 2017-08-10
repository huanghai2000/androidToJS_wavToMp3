package com.kingsun.teacherclasspro.fragment;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.utils.MediaPlayerUtil;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.kingsun.teacherclasspro.widgets.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import static com.kingsun.teacherclasspro.application.MyApplication.getContext;

/**
 * 跟读单词
 * @author hai.huang
 *
 */
public class M1_Fragment extends BaseFragment implements OnClickListener{
	private String TAG = "Fragment";
	private View layout_View;
	private TextView tv_title,tv_dub_startdub_time;
	private RoundImageView img_pho;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageView img_result;
	private ProgressBar pb_dub_startdub;
	private Question bean;
	private loadCallback callback;
	private int  index ;
	private ImageView img_record,img_play;
	private MediaRecorder mRecorder;
	private int  playTime = 0,progressIndex = 0;
	private String path;
	//播放定时器，录音定时器
	private Timer myTimer,progressTimer;
	private TimerTask myTask,progressTask;
	public M1_Fragment(){}
	public M1_Fragment(Question bean,loadCallback callback,int  po){
		this.bean = bean;
		this.callback = callback;
		this.index = po;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {

		}else{
			BaseActivity.Ilog(TAG, "setUserVisibleHint === "+index);
			if (bean != null && callback != null) {
				callback.loadData(index,bean);
			}
		}
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.repeat_the_word, container, false);
		initView();
		return layout_View;
	}

	private void initView(){
		imageLoader = MyApplication.initImageLoader(getActivity());
		options = new DisplayImageOptions.Builder().
				showImageForEmptyUri(R.drawable.kingsoft_defalt)  // 设置图片Uri为空或是错误的时候显示的图片  
				.showImageOnFail(R.drawable.kingsoft_defalt)       // 设置图片加载或解码过程中发生错误显示的图片      
				.cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中  
				.cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中  
				.build(); 

		tv_title = (TextView) layout_View.findViewById(R.id.tv_title);
		
		tv_dub_startdub_time = (TextView) layout_View.findViewById(R.id.tv_dub_startdub_time);

		img_pho = (RoundImageView) layout_View.findViewById(R.id.img_pho);

		//评测结果
		img_result = (ImageView) layout_View.findViewById(R.id.img_result);

		pb_dub_startdub = (ProgressBar) layout_View.findViewById(R.id.pb_dub_startdub);

		img_record = (ImageView) layout_View.findViewById(R.id.img_record);
		img_record.setOnClickListener(this);

		img_play = (ImageView) layout_View.findViewById(R.id.img_play);
		img_play.setOnClickListener(this);

		if (bean != null) {
			//图片
			imageLoader.displayImage(bean.getImgUrl(),img_pho, options);

			tv_title.setText(bean.getQuestionContent()+"");

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//上一题
		case R.id.tv_up:
			break;

		case R.id.tv_down:
			//下一题
			break;

		case R.id.img_record:
			//录音
			if (bean != null) {
				Gson gson = new Gson();
				final String jsonString = gson.toJson(bean);
				BaseActivity.Ilog(TAG, jsonString);
				if (bean.isPlaying()) {
					//表示真正播放录音
					MediaPlayerUtil.stop();
					String urlString =  bean.getMp3Url();
					if (isEmty(bean.getRecordUrl())) {
						pb_dub_startdub.setProgress(0);
						progressIndex = 0;
						tv_dub_startdub_time.setText("录音完  ");
					}
					
					urlString = MediaPlayerUtil.getUrl(urlString);
					MediaPlayerUtil.playFromIntenet(getActivity(),urlString,myHandler);
				}else{
					//pad是否在录音
					if (bean.isRecording()) {
						ToastUtils.showToast(getActivity(), "正在录音，请等待录音结束");
					}else{
						progressIndex =0;
						pb_dub_startdub.setProgress(0);
						tv_dub_startdub_time.setText("录音  ");
						String urlString =  bean.getMp3Url();
						urlString = MediaPlayerUtil.getUrl(urlString);
						MediaPlayerUtil.playFromIntenet(getActivity(),urlString,myHandler);
					}
				}
			}else{
				BaseActivity.Ilog(TAG, "bean  is null");
			}
			break;

		case R.id.img_play:
			BaseActivity.Ilog(TAG, "bean.isPlaying()  = "+bean.isPlaying() +
					";bean.isRecording() = "+bean.isRecording()+"; bean.getRecordUrl() = "+bean.getRecordUrl());
			//试听
			if (bean != null &&!bean.isPlaying()  && !bean.isRecording() && !isEmty(bean.getRecordUrl())) {
//				MediaPlayerUtil.setPlayType
				MediaPlayerUtil.playFromSdCard(getActivity(), path);
			}
			break;
		default:
			break;
		}
	}

	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case  Constant.LISTEN_EVER_PLAY_FAILED:
				String sp = (String) msg.obj;
				if (msg.arg1 == 1) {
					playTime = 0;
					ToastUtils.showToast(getActivity(), sp);
				}
				break;
			case Constant.RETURN_PALY_TIME:
				//获取播放时长0..0
				BaseActivity.Ilog(TAG, "paly == > "+msg.arg1);
				if (bean != null && pb_dub_startdub != null) {
					playTime = msg.arg1*3/2;
					pb_dub_startdub.setMax(playTime);
					bean.setPlaying(true);
				}
				break;

			case  Constant.RETURN_PALY_END:
				bean.setPlaying(false);
				//播放结束 就开始录音
				ToastUtils.showToast(getContext(), "准备开始录音");
				bean.setRecording(true);
				if (myHandler.hasMessages(Constant.UPDATE_PROGRESS)) {
					myHandler.removeMessages(Constant.UPDATE_PROGRESS);
				}
				if (myHandler.hasMessages(Constant.STOP_DUB)) {
					myHandler.removeMessages(Constant.STOP_DUB);
				}
				if (progressTimer != null ) {
					progressTimer.cancel();
					progressTimer = null;
				}
				
				if (progressTask != null) {
					progressTask.cancel();
					progressTask = null;
				}
				
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
						myHandler.sendEmptyMessage(Constant.STOP_DUB);
					}
				};
				myTimer.schedule(myTask, playTime);

				//更新progress
				progressTimer = new Timer();
				progressTask = new TimerTask() {
					@Override
					public void run() {
						myHandler.sendEmptyMessage(Constant.UPDATE_PROGRESS);
					}
				};
				progressTimer.schedule(progressTask, 0, 10);
				tv_dub_startdub_time.setText("录音中  ");
				progressIndex = 0;
				pb_dub_startdub.setProgress(0);
				onStartDub(bean.getQuestionID());
				break;

			case  Constant.STOP_DUB:
				//结束录制
				if (myTimer != null) {
					myTimer.cancel();
					myTimer = null;
				}

				if (myTask != null) {
					myTask.cancel();
					myTask = null;
				}

				if (progressTimer != null && progressTask != null) {
					progressTimer.cancel();
					progressTimer = null;
					progressTask.cancel();
					progressTask = null;
				}
				//录音结束
				bean.setRecording(false);
				if (mRecorder != null) {
					mRecorder.stop();
					mRecorder = null;
				}
				bean.setRecordUrl(path);
				tv_dub_startdub_time.setText("录音完成  ");
				pb_dub_startdub.setProgress(playTime);
				//播放结束 就开始录音
				ToastUtils.showToast(getContext(), "录音完成");
				break;

			case  Constant.UPDATE_PROGRESS:
				//更新progress
				progressIndex +=10;
				if (progressIndex < playTime) {
					pb_dub_startdub.setProgress(progressIndex);
				} else {
					progressIndex = 0;
					pb_dub_startdub.setProgress(playTime);
				}
				break;
			}
		};
	};

	public void onStartDub(String  name) {
		BaseActivity.Ilog(TAG, "onStartDub");
		try {
			if (!BaseActivity.isFileExist("recordCache")) {
				BaseActivity.createSDDir("recordCache");
			}
			path = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/recordCache/" +name+".mp3";
			BaseActivity.Ilog(TAG, "onStartDub -path "+path);
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
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;	
		}
		
		if (myTimer != null) {
			myTimer.cancel();
			myTimer = null;
		}

		if (myTask != null) {
			myTask.cancel();
			myTask = null;
		}
		
		if (progressTimer != null && progressTask != null) {
			progressTimer.cancel();
			progressTimer = null;
			progressTask.cancel();
			progressTask = null;
		}
		
		if (bean != null && callback != null) {
			callback.loadData(index,bean);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		BaseActivity.Ilog(TAG, "onPause === ");
//		if (bean != null && callback != null) {
//			callback.loadData(index,bean);
//		}
	}
}
