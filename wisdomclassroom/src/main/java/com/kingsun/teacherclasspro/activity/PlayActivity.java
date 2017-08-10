package com.kingsun.teacherclasspro.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.bean.CheckBean;
import com.torment.lame.LameUtils;
import com.xs.SingEngine;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.os.Environment.getExternalStorageDirectory;
import static com.kingsun.teacherclasspro.activity.BaseActivity.Ilog;

public class PlayActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "PlayActivity";

    EditText edit;
    Button bt, button_play;
    TextView tv;

    Boolean running = false;
    Boolean playing = false;

    SingEngine engine;

    ProgressDialog mProgressDialog;
    public  PlayActivity(){}
    SimpleDateFormat sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd    hh:mm:ss:ms");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        edit = (EditText) findViewById(R.id.et);
        edit.setText("What are you doing? What are you doing?");
        bt = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.tv);
        button_play = (Button) findViewById(R.id.button_play);

        tv.setMovementMethod(ScrollingMovementMethod.getInstance());


        bt.setText("开始录音");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在加载...");


        //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/lingoes.ttf");
        //tv.setTypeface(face);
        //兼容6.0权限管理
        //        if (ContextCompat.checkSelfPermission(PlayActivity.this, Manifest.permission.RECORD_AUDIO)
        //                != PackageManager.PERMISSION_GRANTED) {
        //            //申请WRITE_EXTERNAL_STORAGE权限
        //            ActivityCompat.requestPermissions(PlayActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},
        //                    1);
        //        } else {
        initSingEnge();
        //        }

        bt.setOnClickListener(this);
        button_play.setOnClickListener(this);
    }


    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //        doNext(requestCode, grantResults);
    //    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                initSingEnge();
            } else {
                // Permission Denied
            }
        }
    }

    private void initSingEnge() {
        mProgressDialog.show();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //  获取引擎实例,设置测评监听对象
                    engine = SingEngine.newInstance(PlayActivity.this);
                    engine.setListener(mResultListener);
                    //设置wav保存的路径
                    engine.setWavPath(getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" +
                            "record.wav");
                    //  设置引擎类型
                    //引擎类型（在线 、 离线 、混合）,默认使用在线引擎
                    //备注:   在线: "cloud"; 离线:"native"; 混合:"auto"
                    engine.setServerType("native");
                    //  设置是否开启VAD功能
                    //engine.setOpenVad(true, null);
                    engine.setOpenVad(true, "vad.0.1.bin");
                    engine.setFrontVadTime(3000);
                    engine.setBackVadTime(3000);
                    //   构建引擎初始化参数
                    JSONObject cfg_init = engine.buildInitJson("t135", "1eb07a38f3834b7ea666934cb0ce3085");
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
        bt.setText("停止录音");
        String retext = edit.getText().toString().trim();
        if (engine != null) {
            try {
                JSONObject request = new JSONObject();
                //                if (retext.contains(" ")) {en.pred.score
                //英文句子评测
                //                    request.put("coreType", "en.sent.score");
                //                } else {
                //英文单词评测
                //                    request.put("coreType", "en.word.score");
                //                }
                //段落评测
                request.put("coreType", "en.pred.score");
//                request.put("coreType", "en.sent.score");
                request.put("refText", retext);
                request.put("rank", 100);
                //构建评测请求参数
                JSONObject startCfg = engine.buildStartJson("guest", request);
                //设置评测请求参数
                engine.setStartCfg(startCfg);
                //开始测评
                engine.start();
                running = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void stop() {
        bt.setText("开始录音");
        if (engine != null) {
            engine.stop();
            running = false;
        }
    }

    private void cancel() {
        if (engine != null) {
            engine.cancel();
            running = false;
        }
    }


    private void playBack() {
        if (engine != null) {
            engine.playback();
        }
        playing = true;

    }

    private void stopPlayBack() {
        if (engine != null) {
            engine.stopPlayBack();
        }
        playing = false;

    }

    SingEngine.ResultListener mResultListener = new SingEngine.ResultListener() {
        @Override
        public void onBegin() {
        }

        @Override
        public void onResult(final JSONObject result) {
            Log.w(TAG, "-----onResult()-----" + result.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CheckBean bean = (CheckBean) getObjectByJson(result.toString(), CheckBean.class);
                    if (bean != null) {
                        CheckBean.otherBean oBean = bean.getResult();
                        if (oBean != null) {
                            tv.setText("总得分： "+oBean.getOverall());
                        }else {
                            tv.setText("总得分：没取到数据 ");
                        }
                    }else{
                        Log.w(TAG,"bean is null ");
                        tv.setText(result.toString());
                    }
                }
            });
        }

        @Override
        public void onEnd(final int Code, final String msg) {
            Log.w(TAG, "-----onEnd()-----" + Code);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    running = false;
                    bt.setText("开始录音");
                    tv.setText(Code+", "+msg);

                }
            });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText(Code+", "+msg);
                }
            });
        }

        @Override
        public void onUpdateVolume(final int volume) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText("Volume:"+ volume);
                }
            });
        }

        @Override
        public void onFrontVadTimeOut() {
            Log.e(TAG, "前置超时");
            running = false;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stop();
                }
            });

        }

        @Override
        public void onBackVadTimeOut() {
            Log.e(TAG, "后置超时");
            running = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bt.setText("开始录音");
                }
            });
        }

        @Override
        public void onRecordingBuffer(byte[] data) {
        }

        @Override
        public void onRecordLengthOut() {
            Log.e(TAG, "录音超时");
            stop();
        }

        @Override
        public void onReady() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            });
            Log.e(TAG, "onReady()");
        }

        @Override
        public void onPlayCompeleted() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playing = false;
                    Toast.makeText(PlayActivity.this, "compelete", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button:
//                if (!running) {
//                    start();
//                } else {
//                    stop();
//                }
//                MediaPlayer player = new MediaPlayer();
//                try {
//                    player.reset();
//                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    player.setDataSource(getExternalStorageDirectory().getPath()+ "/KingSunSmart/lucifer.mp3");
//                    player.prepare();
//                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mp) {
//                            mp.start();
//                            Log.w(TAG,"start time: " + mp.getDuration());
//                        }
//                    });
//                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            Log.w(TAG,"play end: " + mp.getDuration());
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                new Thread() {
                    @Override
                    public void run() {
                        Date curDate    = new Date(System.currentTimeMillis());//获取当前时间
                        String    str    =    sDateFormat.format(curDate);
                        Log.w(TAG,"start time: " + str);
                        LameUtils utils = new LameUtils();
                        utils.initLame(16000, 16000, 1);
//                        utils.initLame(16000, 44100, 1);
                        String wavurl = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" +
                                "record.wav";
//                        String wavurl = getExternalStorageDirectory().getPath()+ "/KingSunSmart/test2.wav";
                        String mp3url = getExternalStorageDirectory().getPath()+ "/KingSunSmart/lucifer_new.mp3";
                        utils.wavTomp3(wavurl,mp3url);
                        utils.closeLame();
                        Date   curDate1    = new Date(System.currentTimeMillis());//获取当前时间
                        String    str1    =    sDateFormat.format(curDate1);
                        Log.w(TAG, "end time: " + str1);
                    }
                }.start();
                break;

            case R.id.button_play:
                if (!playing) {
                    playBack();
                    Log.e(TAG, "playback");
                } else {
                    stopPlayBack();
                    Log.e(TAG, "stopPlayBack");
                }
                break;
        }
    }

    public static <T, E> ArrayList<E> getListByJson(String pData,
                                                    Class<T> pClass) {
        ArrayList<E> msg = new ArrayList<E>();
        try {
            //			JSONObject obj = new JSONObject(pData);
            //			String info = obj.getString("Date");
            //			BaseActivity.Elog(TAG,"pData => "+pData);
            JSONArray jsonarray = new JSONArray(pData);
            //			BaseActivity.Elog(TAG,"jsonarray.leng ==> "+jsonarray.length());
            for (int i = 0; i < jsonarray.length(); i++) {
                String data = jsonarray.getString(i);
                Object temp = getObjectByJson(data, pClass);
                msg.add((E) temp);
            }
        } catch (Exception e) {
            Log.e(TAG, "====================== Paras List ======================");
            Log.e(TAG,e.toString());
            msg = null;
        }
        return msg;
    }

    public static <T> Object getObjectByJson(String pData, Class<T> pClass) {
        Object msg = new Object();
        try {
            Gson gson = new Gson();
            //			BaseActivity.Elog(TAG,"pData ----   "+pData);
            msg = gson.fromJson(pData, pClass);
        } catch (Exception e) {
            Log.e(TAG,"====================== Paras obj ======================");
            Log.e(TAG,e.toString());
            msg = null;
        }
        return msg;
    }
    public void audioCheckResult(int result){
        Ilog(TAG, "audioCheckResult come result = "+result);
    }

}