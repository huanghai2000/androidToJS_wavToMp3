package com.kingsun.teacherclasspro.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.activity.BaseActivity.loadCallback;
import com.kingsun.teacherclasspro.adpter.RepeatTextAdpter;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.utils.MediaPlayerUtil;
import com.kingsun.teacherclasspro.utils.ToastUtils;

import static com.kingsun.teacherclasspro.application.MyApplication.getContext;

/***
 * 跟读课文
 * @author hai.huang
 *
 */
public class M2_Fragment extends BaseFragment implements OnClickListener{
	private String TAG ="M2_Fragment";
	private View layout_View;
	private ImageView img_result;
	private ProgressBar pb_dub_startdub;
	private ListView myListView;
	private RepeatTextAdpter adp;
	private Question bean;
	private loadCallback callback;
	private ImageView img_record,img_play;
	private ArrayList<Question> smallQuestions;
	private MediaRecorder mRecorder;
	private boolean isAgain = true;
	private TextView tv_dub_startdub_time;
	private int  playTime = 0,position;
	private int  index = 0,progressIndex = 0;
	private String path;
	//播放定时器，录音定时器
	private Timer myTimer,progressTimer,nextTimer;
	private TimerTask myTask,progressTask,nextTask;
	public M2_Fragment(){}
	public M2_Fragment(Question bean,loadCallback callback,int  position){
		this.bean = bean;
		this.callback = callback;
		this.position = position;
		if (bean != null) {
			smallQuestions = bean.getSmallQuestions();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {

		}else{
			BaseActivity.Ilog(TAG, "setUserVisibleHint === "+index);
			if (bean != null && callback != null && smallQuestions != null) { 
				bean.setSmallQuestions(smallQuestions);
				callback.loadData(position,bean);
			}
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_View = inflater.inflate(R.layout.repeat_the_text, container, false);
		initView();
		return layout_View;
	}

	private void initView(){
		myListView = (ListView) layout_View.findViewById(R.id.myListView);
		adp = new RepeatTextAdpter(getActivity());
		myListView.setAdapter(adp);

		//评测结果
		img_result = (ImageView) layout_View.findViewById(R.id.img_result);

		pb_dub_startdub = (ProgressBar) layout_View.findViewById(R.id.pb_dub_startdub);

		tv_dub_startdub_time = (TextView) layout_View.findViewById(R.id.tv_dub_startdub_time);

		img_record = (ImageView) layout_View.findViewById(R.id.img_record);
		img_record.setOnClickListener(this);

		img_play = (ImageView) layout_View.findViewById(R.id.img_play);
		img_play.setOnClickListener(this);

		if (smallQuestions != null) {
			adp.setDate(smallQuestions);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//上一题
		case R.id.tv_up:
			break;
		case R.id.img_record:
			//录音
			if (smallQuestions != null) {
				if (index >=smallQuestions.size()) {
					index = 0;
					isAgain = false;
				}
				if (smallQuestions.get(index)!= null) {
					if (smallQuestions.get(index).isPlaying()) {
						//正在播放
						ToastUtils.showToast(getActivity(), "正在播放课文");
					}else if (smallQuestions.get(index).isRecording()) {
						//正在录音
						ToastUtils.showToast(getActivity(), "正在录音");
					}else{
						//可以录音
						//						Gson gson = new Gson();
						//						final String jsonString = gson.toJson(smallQuestions.get(index));
						//						BaseActivity.Ilog(TAG, jsonString);
						smallQuestions.get(index).setFinish(false);
						adp.setDate(smallQuestions);
//						myListView.setSelection(index);
						String urlString =  smallQuestions.get(index).getMp3Url();
						urlString = MediaPlayerUtil.getUrl(urlString);
						MediaPlayerUtil.playFromIntenet(getActivity(),urlString,myHandler);
					}
				}
			}
			break;

		case R.id.img_play:
			//试听
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
				playTime = 0;
				ToastUtils.showToast(getActivity(), sp);
				break;

			case Constant.RETURN_PALY_TIME:
				playTime = msg.arg1;
				//获取播放时长0..0
				if (smallQuestions != null) {
					if (smallQuestions.get(index) != null && pb_dub_startdub != null) {
						playTime = msg.arg1*3/2;
						pb_dub_startdub.setMax(playTime);
						progressIndex = 0;
						tv_dub_startdub_time.setText("录音  ");
						pb_dub_startdub.setProgress(0);
						smallQuestions.get(index).setPlaying(true);
					}
				}
				break;

			case  Constant.RETURN_PALY_END:
				//播放结束 就开始录音
				if (smallQuestions == null || smallQuestions.get(index) == null) {
					return;
				}
				if (smallQuestions != null) {
					smallQuestions.get(index).setFinish(true);
					adp.setDate(smallQuestions);
				}
				smallQuestions.get(index).setPlaying(false);
				//播放结束 就开始录音
				ToastUtils.showToast(getContext(), "准备开始录音");
				smallQuestions.get(index).setRecording(true);
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
				onStartDub(smallQuestions.get(index).getQuestionID());
				break;

			case Constant.NEXT_PLAY:
				//继续播放
				if (myHandler.hasMessages(Constant.NEXT_PLAY)) {
					myHandler.removeMessages(Constant.NEXT_PLAY);
				}
				if (nextTimer != null) {
					nextTimer.cancel();
					nextTimer = null;
				}

				if (nextTask != null) {
					nextTask.cancel();
					nextTask = null;
				}
				if (smallQuestions.get(index)!= null) {
					//可以录音
					//					Gson gson = new Gson();
					//					final String jsonString = gson.toJson(smallQuestions.get(index));
					//					BaseActivity.Ilog(TAG, jsonString);
					smallQuestions.get(index).setFinish(false);
					adp.setDate(smallQuestions);
//					myListView.setSelection(index);
					String urlString =  smallQuestions.get(index).getMp3Url();
					urlString = MediaPlayerUtil.getUrl(urlString);
					MediaPlayerUtil.playFromIntenet(getActivity(),urlString,myHandler);
				}
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
				smallQuestions.get(index).setRecording(false);
				if (mRecorder != null) {
					mRecorder.stop();
					mRecorder = null;
				}
				smallQuestions.get(index).setRecordUrl(path);
				tv_dub_startdub_time.setText("录音完成  ");
				pb_dub_startdub.setProgress(playTime);
				//播放结束 就开始录音
				ToastUtils.showToast(getContext(), "录音完成");
				index +=1;
				//继续往下面播放
				if (index >=smallQuestions.size() || !isAgain) {
					return;
				}
				if (nextTimer != null) {
					nextTimer.cancel();
					nextTimer = null;
				}

				if (nextTask != null) {
					nextTask.cancel();
					nextTask = null;
				}
				nextTimer = new Timer();
				nextTask = new TimerTask() {
					@Override
					public void run() {
						myHandler.sendEmptyMessage(Constant.NEXT_PLAY);
					}
				};
				nextTimer.schedule(nextTask, 2000);

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
		tv_dub_startdub_time.setText("录音  ");
		progressIndex = 0;
		pb_dub_startdub.setProgress(0);
		try {
			if (!BaseActivity.isFileExist("recordCache")) {
				BaseActivity.createSDDir("recordCache");
			}
			path = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/recordCache/" +name+".mp3";
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

		if (nextTimer != null) {
			nextTimer.cancel();
			nextTimer = null;
		}

		if (nextTask != null) {
			nextTask.cancel();
			nextTask = null;
		}

		if (progressTimer != null && progressTask != null) {
			progressTimer.cancel();
			progressTimer = null;
			progressTask.cancel();
			progressTask = null;
		}

		//		if (bean != null && callback != null && smallQuestions != null) { 
		//			bean.setSmallQuestions(smallQuestions);
		//			callback.loadData(position,bean);
		//		}
	}
}
