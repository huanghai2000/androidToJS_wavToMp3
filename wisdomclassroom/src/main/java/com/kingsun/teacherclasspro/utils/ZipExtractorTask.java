package com.kingsun.teacherclasspro.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;

import com.kingsun.teacherclasspro.config.Constant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipExtractorTask extends AsyncTask<Void, Integer, Long> {
	private final String TAG = "ZipExtractorTask";
	private final File mInput;
	private final File mOutput;
	private final ProgressDialog mDialog;
	private int mProgress = 0;
	private final Context mContext;
	private boolean mReplaceAll;
	private Handler handler;
	private String openName;
	public ZipExtractorTask(String in, String out, Context context, boolean replaceAll,Handler handler){
		super();
		mInput = new File(in);
		mOutput = new File(out);
		this.handler=handler;
		if(!mOutput.exists()){
			if(!mOutput.mkdirs()){
				Log.e(TAG, "Failed to make directories:"+mOutput.getAbsolutePath());
			}
		}
		if(context!=null){
			mDialog = new ProgressDialog(context);
		}
		else{
			mDialog = null;
		}
		mContext = context;
		mReplaceAll = replaceAll;
	}
	@Override
	protected Long doInBackground(Void... params) {
		return unzip();
	}
	
	@Override
	protected void onPostExecute(Long result) {
		//super.onPostExecute(result);
		if(mDialog!=null&&mDialog.isShowing()){
			mDialog.dismiss();
		}
		Message msg=new Message();
		msg.what=Constant.DOWNLOAD_UNZIP_RESULT;
		msg.arg1=1;
		msg.obj=openName;
		handler.sendMessage(msg);
		if(isCancelled())
			return;
	}
	@Override
	protected void onPreExecute() {
		if(mDialog!=null){
			mDialog.setTitle("解压文件");
			mDialog.setMessage(mInput.getName());
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}
			});
			mDialog.show();
		}
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		if(mDialog==null)
			return;
		if(values.length>1){
			int max=values[1];
			mDialog.setMax(max);
//			double xp = max;
//			xp = xp/1024/1024;
//			NumberFormat nf = NumberFormat.getNumberInstance();
//			nf.setMaximumFractionDigits(2);
//			Log.e(TAG, "max = "+(max/1024)+"; "+nf.format(xp));
		}
		else{
//			mDialog.setProgress(values[0].intValue());
		}
	}
	private long unzip(){
		long extractedSize = 0L;
		if(mInput.length()*3>getAvaliableBytes()){//剩余空间不足压缩文件3倍，视为空间不足
			//解压空间不足
			Message msg=new Message();
			msg.what= Constant.UNZIP_NO_SPACE;
			msg.obj="SD卡空间不足";
			handler.sendMessage(msg);
		}else{
			Enumeration<ZipEntry> entries;
			ZipFile zip = null;
			try {
				zip = new ZipFile(mInput);
				long uncompressedSize = getOriginalSize(zip);
				publishProgress(0, (int) uncompressedSize);
				entries = (Enumeration<ZipEntry>) zip.entries();
				while(entries.hasMoreElements()){
					ZipEntry entry = entries.nextElement();
					if(entry.isDirectory()){
						continue;
					}
					File destination = new File(mOutput, entry.getName());
//					Log.e(TAG, "name="+entry.getName());
					if (entry.getName().endsWith("cover1.gif")){
						openName = mOutput.getAbsolutePath()+"/"+entry.getName();
//						Log.e(TAG, "name= "+openName);
					}
					if(!destination.getParentFile().exists()){
//						Log.e(TAG, "make="+destination.getParentFile().getAbsolutePath());
						destination.getParentFile().mkdirs();
					}
					if(destination.exists()&&mContext!=null&&!mReplaceAll){

					}
//					if (destination.getParentFile().getAbsolutePath().endsWith("/fm001")){
//						openName = destination.getParentFile().getAbsolutePath()+"/bg.jpg";
//					}
					ProgressReportingOutputStream outStream = new ProgressReportingOutputStream(destination);
					extractedSize+=copy(zip.getInputStream(entry),outStream);
					outStream.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
				e.printStackTrace();
				Message msg=new Message();
				msg.what=Constant.UNZIP_ERROR;
				msg.arg1=-1;
				msg.obj="解压失败";
				handler.sendMessage(msg);
			}finally{
				try {
					zip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return extractedSize;
	}

	private long getOriginalSize(ZipFile file){
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) file.entries();
		long originalSize = 0l;
		while(entries.hasMoreElements()){
			ZipEntry entry = entries.nextElement();
			if(entry.getSize()>=0){
				originalSize+=entry.getSize();
			}
		}
		return originalSize;
	}
	
	private int copy(InputStream input, OutputStream output){
		byte[] buffer = new byte[1024*8];
		BufferedInputStream in = new BufferedInputStream(input, 1024*8);
		BufferedOutputStream out  = new BufferedOutputStream(output, 1024*8);
		int count =0,n=0;
		try {
			while((n=in.read(buffer, 0, 1024*8))!=-1){
				out.write(buffer, 0, n);
				count+=n;
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	
	private final class ProgressReportingOutputStream extends FileOutputStream{

		public ProgressReportingOutputStream(File file)
				throws FileNotFoundException {
			super(file);
		}

		@Override
		public void write(byte[] buffer, int byteOffset, int byteCount)
				throws IOException {
			super.write(buffer, byteOffset, byteCount);
		    mProgress += byteCount;
		    publishProgress(mProgress);
		}
	}

	@SuppressLint("NewApi")
	private long getAvaliableBytes(){
		StatFs sFs=new StatFs(Environment.getExternalStorageDirectory() + File.separator);
		long availableBolocks;
		long blockSize;
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){
			//可用的blocks的数量
			availableBolocks=sFs.getBlockCountLong();
			//单个block的大小
			blockSize=sFs.getBlockSizeLong();
		}else{
			//可用的blocks的数量
			availableBolocks=sFs.getBlockCount();
			//单个block的大小
			blockSize=sFs.getBlockSize();
		}
		//sd卡的剩余空间
		return availableBolocks*blockSize;
	}
}