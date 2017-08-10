package com.kingsun.teacherclasspro.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kingsun.teacherclasspro.config.Constant;

public class MediaPlayerUtil {
	private final static String TAG = "MediaPlayerUtil";
	/***
	 * playType = 1表示本地，0表示网络
	 */
	private static  int  playType = 0 ;
	public static MediaPlayer INSTANCE;

	public static MediaPlayer getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MediaPlayer();
		}
		return INSTANCE;
	}

	// 播放assets文件夹下的音频
	public static void play(final Context context, final String assetUrl) {
		playType = 1;
		AssetFileDescriptor descriptor = null;
		try {
			descriptor = context.getAssets().openFd(assetUrl);
			getInstance().reset();
			Log.e(TAG, "reset");
			getInstance().setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(),
					descriptor.getLength());
			Log.e(TAG, "setDataSource");
			getInstance().prepareAsync();
			Log.e(TAG, "prepare");
			getInstance().setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Log.d(TAG, "preparedfinished");
					mp.start();
				}
			});
			getInstance().setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					Log.e(TAG, "onError");
					mp.stop();
					return false;
				}
			});
		} catch (Exception e) {
			Log.e(TAG, "catch");
			e.printStackTrace();
		} finally {
			try {
				if (descriptor != null)
					descriptor.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 播放assets文件夹下的音频
	public static void play(final Context context, MediaPlayer player, final String assetUrl) {
		AssetFileDescriptor descriptor = null;
		try {
			descriptor = context.getAssets().openFd(assetUrl);
			player.reset();
			Log.e(TAG, "reset");
			player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			player.setLooping(true);
			Log.e(TAG, "setDataSource");
			player.prepareAsync();
			Log.e(TAG, "prepare");
			player.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Log.d(TAG, "preparedfinished");
					mp.start();
				}
			});
			player.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					Log.e(TAG, "onError");
					mp.stop();
					return false;
				}
			});
		} catch (Exception e) {
			Log.e(TAG, "catch");
			e.printStackTrace();
		} finally {
			try {
				if (descriptor != null)
					descriptor.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 播放SD卡文件夹下的音频
	public static void playFromSdCard(Context context, final String SdUrl) {
		playType = 1;
		try {
			String soundPath = SdUrl;
			Log.e(TAG, "soundPath:" + soundPath);
			File sound = new File(soundPath);
			if (sound.exists()) {
				getInstance().reset();
				Log.e(TAG, "reset");
				getInstance().setDataSource(soundPath);
				Log.e(TAG, "setDataSource");
				getInstance().prepare();
				// getInstance().prepareAsync();
				Log.e(TAG, "prepare");
				// getInstance().start();
				getInstance().setOnPreparedListener(new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
						// TODO Auto-generated method stub
						Log.d(TAG, "preparedfinished");
						mp.start();
					}
				});
				getInstance().setOnErrorListener(new OnErrorListener() {
					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						// TODO Auto-generated method stub
						Log.e(TAG, "onError");
						mp.stop();
						return false;
					}
				});
				getInstance().setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
					}
				});
			}

		} catch (Exception e) {
			Log.e(TAG, "catch");
			e.printStackTrace();
		}
	}
	private static long startTime=0;
	private final static int playTimeOut=5000;
	// 播放网络音频
	public static void playFromIntenet(final Context context, final String url,final Handler handler) {
		playType = 0;
		try {
			startTime=System.currentTimeMillis();
			Log.e(TAG, "url:" + url);
			getInstance().reset();
			Log.e(TAG, "reset");
			getInstance().setDataSource(url);
			Log.e(TAG, "setDataSource");
			getInstance().prepareAsync();
			Log.e(TAG, "prepare");
			getInstance().setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Log.d(TAG, "preparedfinished");
					Message msg=new Message();
					msg.what=Constant.RETURN_PALY_TIME;
					msg.arg1=mp.getDuration();
					if (playType ==0) {
						handler.sendMessage(msg);
					}
					mp.start();
				}
			});
			
			getInstance().setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					Log.d(TAG, "preparedfinished");
					Message msg=new Message();
					msg.what=Constant.RETURN_PALY_END;
					if (playType == 0) {
						handler.sendMessage(msg);
					}
				}
			});
			
			getInstance().setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
				@Override
				public void onBufferingUpdate(MediaPlayer mp, int percent) {
					// TODO Auto-generated method stub
					if(percent<100&&System.currentTimeMillis()-startTime>playTimeOut){
						stop();
						Message msg=new Message();
						msg.what=Constant.LISTEN_EVER_PLAY_FAILED;
						msg.arg1 = 1;
						msg.obj="播放超时,请检查网络~";
						if (playType ==0) {
							handler.sendMessage(msg);
						}
					}
				}
			});
			getInstance().setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					Log.e(TAG, "onError +"+what);
					stop();
					Message msg=new Message();
					msg.what=Constant.LISTEN_EVER_PLAY_FAILED;
					msg.obj="播放出错,请检查网络~";
					if (playType ==0) {
						handler.sendMessage(msg);
					}
					return false;
				}
			});

		} catch (Exception e) {
			Log.e(TAG, "catch");
			stop();
			Message msg=new Message();
			msg.what=Constant.LISTEN_EVER_PLAY_FAILED;
			msg.obj="播放器错误~";
			handler.sendMessage(msg);
			e.printStackTrace();
		}
	}

	public static void start() {
		if (getInstance() != null) {
			if (!getInstance().isPlaying())
				getInstance().start();
		}
	}

	public static void pause() {
		if (getInstance() != null) {
			if (getInstance().isPlaying())
				getInstance().pause();
		}
	}

	// 停止播放
	public static void stop() {
		if (getInstance() != null) {
			if (getInstance().isPlaying())
				getInstance().stop();
			getInstance().setOnCompletionListener(null);
		}
	}

	// // 获取跟读课文音频文件的总播放时长
	// public static int getMP3DurationFromPath(List<Question> list) {
	// Log.e(TAG, "获取跟读课文音频文件的总播放时长");
	// int duration = 0;
	// for (int i = 0; i < list.size(); i++) {
	// duration += getMP3Duration(list.get(i).getMp3Url());
	// }
	// return duration;
	// }

	// 获取指定音频文件时长
	public static int getMP3Duration(String path) {
		Log.e(TAG, "获取指定音频文件时长" + path);
		int duration = 0;
		File sound = new File(path);
		if (sound.exists()) {
			MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.reset();
			try {
				mediaPlayer.setDataSource(path);
				mediaPlayer.prepare();
				duration = mediaPlayer.getDuration();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
			}
		}
		return duration;
	}

	/**
	 * url国际化
	 * @param url
	 * @return
	 */
	public static String getUrl(String url) {
		String urlStr = null;
		try {
			urlStr = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20");
			urlStr = urlStr.replaceAll("%3A", ":").replaceAll("%2F", "/");
			Log.e("decodeUrl", "url: " + url);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urlStr;
	}
}
