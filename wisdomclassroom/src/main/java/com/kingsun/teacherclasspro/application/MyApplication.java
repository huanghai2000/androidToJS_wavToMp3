package com.kingsun.teacherclasspro.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.utils.CrashHandler;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import static android.os.Process.myTid;

public class MyApplication extends Application  {
	private static MyApplication app;
	/** 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了 */
	private static Context mContext;
	private static Handler mHandler;
	private static long mMainThreadId;
	private static Typeface mSHtypeface = null;
	private static Typeface mPinyingtypeface = null;
	private String  loginName;//登录用户名
	private  int  mNetWorkState;//网络状态
	private boolean isLog = false;//是否开启错误日志
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public int getmNetWorkState() {
		return mNetWorkState;
	}

	public void setmNetWorkState(int mNetWorkState) {
		this.mNetWorkState = mNetWorkState;
	}

	/****请求头*****/
	public String server_head ;//网络测试环境

	public String getServer_head() {
		return server_head;
	}

	public void setServer_head(String server_head) {
		this.server_head = server_head;
	}

	public static MyApplication getInstance() {
		if (app == null)
			new Throwable("MyApplication is null");
		return app;
	}

	public static Context getContext() {
		return mContext;
	}

	public static Handler getHandler() {
		return mHandler;
	}

	public static Typeface getSHTypeface() {
		return mSHtypeface;
	}
	public static Typeface getPinyinTypeface() {
		return mPinyingtypeface;
	}

	public static long getMainThreadId() {
		return mMainThreadId;
	}

	public ArrayList<Activity> activities = new ArrayList<Activity>();

//	private RefWatcher refWatcher;
//	public static RefWatcher getRefWatcher(Context context) {
//		MyApplication application = (MyApplication) context.getApplicationContext();
//		return application.refWatcher;
//	}

	@Override
	public void onCreate() {
		if (isLog) {
			CrashHandler crashHandler = CrashHandler.getInstance();
			//		注册crashHandler
			crashHandler.init(this);
		}
		// 1.上下文
		mContext = getApplicationContext();
		// 2.主线程的Handler
		mHandler = new Handler();
		// 3.得到主线程的id
		mMainThreadId = myTid();
//		refWatcher = LeakCanary.install(this);
		/**
		 * myTid:thread myPid:process myUid:user
		 */
		super.onCreate();
		app = this;
		x.Ext.init(this);// 初始化xutil3配置
		// 初始化图片缓存
		initImageLoader(getApplicationContext());
	}


	// 初始化ImageLoader 矩形图片，默认 加在图
	@SuppressWarnings({ "static-access", "deprecation" })
	public static ImageLoader initImageLoader(Context context) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		// .showImageOnLoading(R.drawable.photo3)
		// .showImageForEmptyUri(R.drawable.photo3)
		// .showImageOnFail(R.drawable.photo3).cacheInMemory()
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT).displayer(new FadeInBitmapDisplayer(300)).cacheOnDisc()
		.bitmapConfig(Bitmap.Config.RGB_565).build();
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "Farben/Cache");// 缓存地址
		ImageLoaderConfiguration config = (new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 1).threadPoolSize(4).denyCacheImageMultipleSizesInMemory()
		.memoryCache(new WeakMemoryCache()).memoryCacheSize(1 * 1024 * 100)
		.discCacheFileNameGenerator(new Md5FileNameGenerator()).discCache(new UnlimitedDiscCache(cacheDir))
		.tasksProcessingOrder(QueueProcessingType.LIFO)).defaultDisplayImageOptions(options).build();
		imageLoader.getInstance().init(config);
		return imageLoader;
	}

	// 初始化ImageLoader的对象，默认 圆形图片
	@SuppressWarnings({ "static-access", "deprecation" })
	public static ImageLoader initImageLoader(Context context, int type) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.kingsoft_defalt)
				.showImageForEmptyUri(R.drawable.kingsoft_defalt).showImageOnFail(R.drawable.kingsoft_defalt)
				.cacheInMemory().imageScaleType(ImageScaleType.IN_SAMPLE_INT).displayer(new RoundedBitmapDisplayer(300))
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "KingSoft/Cache");// 缓存地址
		ImageLoaderConfiguration config = (new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 1).threadPoolSize(4).denyCacheImageMultipleSizesInMemory()
		.memoryCache(new WeakMemoryCache()).memoryCacheSize(1 * 1024 * 100)
		.discCacheFileNameGenerator(new Md5FileNameGenerator()).discCache(new UnlimitedDiscCache(cacheDir))
		.tasksProcessingOrder(QueueProcessingType.LIFO)).defaultDisplayImageOptions(options).build();
		imageLoader.getInstance().init(config);
		return imageLoader;
	}
	/**
	 * 判断网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

	public void popActivity() {
		Activity activity = activities.get(activities.size() - 1);
		if (activity != null) {
			activity.finish();
			activity = null;
		}
	}

	public void popActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activities.remove(activity);
			activity = null;
		}
	}

	public Activity currentActivity() {
		Activity activity = activities.get(activities.size() - 1);
		return activity;
	}

	public void pushActivity(Activity activity) {
		if (activities == null) {
			activities = new ArrayList<Activity>();
		}
		activities.add(activity);
	}

	// 删除指定类名的activity
	public void popOneActivity(Class cls) {
		for (int i = 0; i < activities.size(); i++) {
			Activity activity = activities.get(i);
			if (activity == null)
				break;
			if (activity.getClass().equals(cls)) {
				// Log.e("activity.getClass equals",
				// activity.getClass().getName());
				popActivity(activity);
				break;
			}
		}
	}
}
