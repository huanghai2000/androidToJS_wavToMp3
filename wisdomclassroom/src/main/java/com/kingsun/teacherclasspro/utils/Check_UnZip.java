package com.kingsun.teacherclasspro.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;

import com.kingsun.teacherclasspro.config.Constant;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class Check_UnZip extends Thread {
	private String TAG = "Unzip";
	private Handler handler;
	private String unzipTo;
	private String fileName;
	private String openName;
	public Check_UnZip(String file,Handler handler,String unzipTo) throws IOException {
		super();
		this.handler=handler;
		this.unzipTo=unzipTo;
		this.fileName = file;
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

				/**
				 * 使用 org.apache.tools.zip.ZipFile 解压文件，它与 java 类库中的 java.util.zip.ZipFile
				 * 使用方式是一新的，只不过多了设置编码方式的 接口。
				 * 
				 * 注，apache 没有提供 ZipInputStream 类，所以只能使用它提供的ZipFile 来读取压缩文件。
				 */
				BufferedInputStream bi;
				ZipFile zf = new ZipFile(file, "GBK");// 支持中文
//				ZipFile zf = new ZipFile(file);// 支持中文
				String unZipFileName=null;
				Enumeration entries = zf.getEntries();
				int i=0;
				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) entries.nextElement();
					String entryName = entry.getName();
					String enTs = new String(entryName.getBytes(),"GBK");
//					Log.i(TAG, "entryName = "+entryName+";size = "+entryName.length());
//					Log.i(TAG, "enTs = "+enTs+";size = "+enTs.length());
					if(++i==1){
						unZipFileName=entryName.split("/")[0]+File.separator;
					}
					String filePath = unzipTo+"/"+entryName;
//					Log.i(TAG, "filePath = "+filePath);
//					Log.i(TAG, "filePath = "+new String(filePath.getBytes(),"GBK"));
					if (filePath.endsWith(".ppt")||filePath.endsWith(".pptx")) {
						openName = filePath;
					}
					if (entry.isDirectory()) {
						File decompressDirFile = new File(filePath);
						if (!decompressDirFile.exists()) {
							decompressDirFile.mkdirs();
						}
					} else {
						String fileDir = filePath.substring(0, filePath.lastIndexOf("/"));  
						File fileDirFile = new File(fileDir);
						if (!fileDirFile.exists()) {
							fileDirFile.mkdirs();
						}
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
						bi = new BufferedInputStream(zf.getInputStream(entry));
						byte[] readContent = new byte[1024];
						int readCount = bi.read(readContent);
						while (readCount != -1) {
							bos.write(readContent, 0, readCount);
							readCount = bi.read(readContent);
						}
						bos.close();
					}
				}
				zf.close();
				//解压完成
//				Log.e(TAG, "unZipFileName: "+unZipFileName);
				msg.what=Constant.DOWNLOAD_UNZIP_RESULT;
				msg.arg1=1;
				msg.obj=openName;
				handler.sendMessage(msg);
//				deletZipFile(file);
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