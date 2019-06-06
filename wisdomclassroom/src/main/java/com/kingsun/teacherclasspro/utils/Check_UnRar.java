package com.kingsun.teacherclasspro.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import com.kingsun.teacherclasspro.config.Constant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

/****
 * Created by hai.huang on 2017/9/21.
 * Check_UnRar 解压RAR文件
 */
public class Check_UnRar extends Thread {
	private String TAG = "UnRar";
	private Handler handler;
	private String unzipTo;
	private String fileName;
	private String openName;
	public Check_UnRar(String sourceFile, Handler handler, String unzipTo) throws IOException {
		super();
		this.handler=handler;
		this.unzipTo=unzipTo;
		this.fileName = sourceFile;
	}
	@Override
	public void run() {
		Message msg=new Message();
		File file=new File(fileName);
		try {
			if(file.length()*3>getAvaliableBytes()){//剩余空间不足压缩文件3倍，视为空间不足
				//解压空间不足
				msg.what=Constant.UNZIP_NO_SPACE;
				msg.obj="SD卡空间不足";
				handler.sendMessage(msg);
			}else{
				Archive a = null;
				FileOutputStream fos = null;
				a = new Archive(file);
				for(FileHeader e = a.nextFileHeader(); e != null; e = a.nextFileHeader()) {
					String compressFileName;
					String destFileName;
					if(e.getFileNameString().trim().toString().indexOf(".") == -1) {
						if (e.isUnicode()) {//解決中文乱码
							compressFileName = e.getFileNameW().trim();
						} else {
							compressFileName = e.getFileNameString().trim();
						}
						destFileName = "";
						compressFileName = compressFileName.replaceAll("\\\\", "/");
						destFileName = unzipTo + "/" + compressFileName;
						File destDirName = new File(destFileName);
						Log.i(TAG, "un1-->" + compressFileName);
						if (destFileName.endsWith(".ppt")||destFileName.endsWith(".pptx")){
							openName = destFileName;
						}
						if(!destDirName.exists() || !destDirName.isDirectory()) {
							destDirName.mkdirs();
						}
					}

					if(!e.isDirectory()) {
						if (e.isUnicode()) {//解決中文乱码
							compressFileName = e.getFileNameW().trim();
						} else {
							compressFileName = e.getFileNameString().trim();
						}
						Log.i(TAG, "un2-->" + compressFileName);
						destFileName = "";
						String destDirName1 = "";
						compressFileName = compressFileName.replaceAll("\\\\", "/");
						destFileName = unzipTo + "/" + compressFileName;
						if (destFileName.endsWith(".ppt")||destFileName.endsWith(".pptx")){
							openName = destFileName;
						}
						destDirName1 = destFileName.substring(0, destFileName.lastIndexOf("/"));
						File dir = new File(destDirName1);
						if(!dir.exists() || !dir.isDirectory()) {
							dir.mkdirs();
						}
						fos = new FileOutputStream(new File(destFileName));
						a.extractFile(e, fos);
						fos.close();
						fos = null;
					}
				}
				a.close();
				a = null;
				//解压完成
				Log.e(TAG, "openName end : "+openName);
				msg.what=Constant.DOWNLOAD_UNZIP_RESULT;
				msg.arg1=1;
				msg.obj=openName;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			//解压出错
			e.printStackTrace();
			msg.what=Constant.UNZIP_ERROR;
			msg.arg1=-1;
			msg.obj="解压失败";
			handler.sendMessage(msg);
//			deletZipFile(file);
		}
	}

	private boolean deletZipFile(File file){
		final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
		file.renameTo(to);
		while(!to.delete())
			to.delete();
		return true;
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