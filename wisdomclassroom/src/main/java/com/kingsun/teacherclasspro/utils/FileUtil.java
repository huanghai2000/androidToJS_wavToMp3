package com.kingsun.teacherclasspro.utils;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/** 文件处理工具类 **/
public class FileUtil {
	private static String TAG ="MainActivity";
	/** 获取SD路径 **/
	public static String getSDPath() {
		// 判断sd卡是否存在
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.getPath();
		}
		return "/sdcard";
	}


	/** 转换文件大小 **/
	public static String formetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = fileS + " B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + " K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + " M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + " G";
		}
		return fileSizeString;
	}

	/** 合并路径 **/
	public static String combinPath(String path, String fileName) {
		return path + (path.endsWith(File.separator) ? "" : File.separator) + fileName;
	}

	/** 复制文件 **/
	public static boolean copyFile(File src, File tar) throws Exception {
		if (src.isFile()) {
			InputStream is = new FileInputStream(src);
			OutputStream op = new FileOutputStream(tar);
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(op);
			byte[] bt = new byte[1024 * 8];
			int len = bis.read(bt);
			while (len != -1) {
				bos.write(bt, 0, len);
				len = bis.read(bt);
			}
			bis.close();
			bos.close();
		}
		if (src.isDirectory()) {
			File[] f = src.listFiles();
			tar.mkdir();
			for (int i = 0; i < f.length; i++) {
				copyFile(f[i].getAbsoluteFile(), new File(tar.getAbsoluteFile() + File.separator
						+ f[i].getName()));
			}
		}
		return true;
	}

	/** 移动文件 **/
	public static boolean moveFile(File src, File tar) throws Exception {
		if (copyFile(src, tar)) {
			deleteFile(src);
			return true;
		}
		return false;
	}

	/** 删除文件 **/
	public static void deleteFile(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; ++i) {
					deleteFile(files[i]);
				}
			}
		}
		f.delete();
	}

	/** 获取MIME类型 **/
	public static String getMIMEType(String name) {
		String type = "";
		String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
		if (end.equals("apk")) {
			return "application/vnd.android.package-archive";
		} else if (end.equals("mp4") || end.equals("avi") || end.equals("3gp")
				|| end.equals("rmvb")) {
			type = "video";
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf")
				|| end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("txt") || end.equals("log")) {
			type = "text";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}

	/**
	 * 根据原始rar路径，解压到指定文件夹下.
	 * @param srcRarPath 原始rar路径
	 * @param dstDirectoryPath 解压到的文件夹
	 */
//	public static void unRarFile(String srcRarPath, String dstDirectoryPath) {
//		BaseActivity.Elog(TAG, "srcRarPath = "+srcRarPath);
//		BaseActivity.Elog(TAG, "dstDirectoryPath = "+dstDirectoryPath);
//		if (!srcRarPath.toLowerCase().endsWith(".rar")) {
//			BaseActivity.Elog(TAG, "非rar文件！");
//			return;
//		}
//		File dstDiretory = new File(dstDirectoryPath);
//		if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
//			dstDiretory.mkdirs();
//		}
//
//		try {
//			File sourceFlie = new  File(srcRarPath);
//			Archive a = null;
//			if (sourceFlie.isFile() && sourceFlie.exists()){
//				BaseActivity.Elog(TAG, "info = ok");
//			}
//			a = new Archive(sourceFlie);
//			BaseActivity.Elog(TAG, "info = xxx");
//			if (a != null) {
//				a.getMainHeader().print();
//				BaseActivity.Elog(TAG, "info = 1111");
//				//a.getMainHeader().print(); // 打印文件信息.
//				FileHeader fh = a.nextFileHeader();
//				while (fh != null) {
//					//防止文件名中文乱码问题的处理
//					String fileName = fh.getFileNameW().isEmpty()?fh.getFileNameString():fh.getFileNameW();
//					BaseActivity.Elog(TAG, "info = fileName "+fileName);
//					if (fh.isDirectory()) { // 文件夹
//						File fol = new File(dstDirectoryPath + File.separator + fileName);
//						fol.mkdirs();
//					} else { // 文件
//						File out = new File(dstDirectoryPath + File.separator + fileName.trim());
//						try {
//							if (!out.exists()) {
//								if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
//									out.getParentFile().mkdirs();
//								}
//								out.createNewFile();
//							}
//							FileOutputStream os = new FileOutputStream(out);
//							a.extractFile(fh, os);
//							os.close();
//						} catch (Exception ex) {
//							ex.printStackTrace();
//						}
//					}
//					fh = a.nextFileHeader();
//				}
//				a.close();
//				BaseActivity.Elog(TAG, "解压成功");
//			}
//		} catch (Exception e) {
//			BaseActivity.Elog(TAG, "解压失败");
//			e.printStackTrace();
//		}
//	}
}
