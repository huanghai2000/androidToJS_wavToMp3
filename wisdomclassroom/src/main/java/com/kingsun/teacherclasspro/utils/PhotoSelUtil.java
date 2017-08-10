package com.kingsun.teacherclasspro.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kingsun.teacherclasspro.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Used to select a local photo
 * 
 * @author huanghai
 * 
 */
public class PhotoSelUtil implements OnClickListener {
	public final int IMAGE_REQUEST_CODE = 0;
	public final int CAMERA_REQUEST_CODE = 1;
	public final int RESIZE_REQUEST_CODE = 2;
	private final String IMAGE_FILE_NAME = "header.jpg";
	private String imgString;// 图片的base64String
	public void setImgString(String imgString) {
		this.imgString = imgString;
	}

	private Bitmap photo;
	private Activity mContext;
	private int getType = 0;// 0 需裁剪 1不需裁剪
	private Dialog dialog;
	private int resizeHeight;
	public String DCIM = "";
	public String getDCIM() {
		return DCIM;
	}

	public void setDCIM(String dCIM) {
		DCIM = dCIM;
	}

	public String getImgString() {
		return imgString;
	}

	public Bitmap getBitmap() {
		return photo;
	}

	public void setGetType(int getType) {
		this.getType = getType;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		// 选择本地照片
		case R.id.textView_pic:
			// Intent galleryIntent = new Intent(
			// Intent.ACTION_GET_CONTENT);
			// galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
			// galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
			// galleryIntent.setType("image/*");
			// galleryIntent.putExtra("scaleUpIfNeeded", true);
			// mContext.startActivityForResult(galleryIntent,IMAGE_REQUEST_CODE);
			Intent intentPick = new Intent(Intent.ACTION_PICK, null);
			intentPick.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			mContext.startActivityForResult(intentPick, IMAGE_REQUEST_CODE);
			dialog.dismiss();
			break;

			// 拍照
		case R.id.textView_pz:
			if (isSdcardExisting()) {
				Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
				cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
				mContext.startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
			} else {
				Toast.makeText(mContext, "请插入sd卡", Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();
			break;

			// 取消
		case R.id.textView_cancel:
			dialog.dismiss();
			break;
		}
	}

	public void showPhoto(final Activity mActivity, String title) {
		mContext = mActivity;
		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.set_photo_layout);
		TextView titleView = (TextView) dialog.findViewById(R.id.textView_title);
		TextView picView = (TextView) dialog.findViewById(R.id.textView_pic);
		TextView pzView = (TextView) dialog.findViewById(R.id.textView_pz);
		TextView caecelView = (TextView) dialog.findViewById(R.id.textView_cancel);
		titleView.setText(title);
		picView.setOnClickListener(this);
		pzView.setOnClickListener(this);
		caecelView.setOnClickListener(this);
		dialog.show();
	}

	public void onActivityResult(final Activity mActivity,int requestCode, int resultCode, Intent data) {
		if (mContext == null) {
			mContext = mActivity;
		}
		if (resultCode != Activity.RESULT_OK) {
			return;
		} else {
			Log.e("MainActivity", "PhotoSelUtil = requestCode = "+requestCode);
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				if (getType == 0) {
					resizeImage(data.getData());
				} else {
					if (getType == 2) {
						resizeHeight = 800;
					} else {
						resizeHeight = 400;
					}
					getBitMap(data.getData());
				}
				break;
			case CAMERA_REQUEST_CODE:
				if (isSdcardExisting()) {
					if (getType == 0) {
						resizeImage(getImageUri());
					} else {
						if (getType == 2) {
							resizeHeight = 800;
						} else {
							resizeHeight = 400;
						}
						//压缩保存图片
						getPzBitMap(getImagePath());
						//						getSmallBitmap(getImagePath());
					}
				} else {
					Toast.makeText(mContext, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}
				break;

			case RESIZE_REQUEST_CODE:
				if (data != null) {
					showResizeImage(data);
				}
				break;
			}
		}
	}

	private boolean isSdcardExisting() {
		final String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	private void resizeImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		if (mContext!=null) {
			mContext.startActivityForResult(intent, RESIZE_REQUEST_CODE);
		}
	}

	public void showResizeImage(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			Log.e("MainActivity", "saveBitmap 111");
			saveBitmap(photo);
			//			bitmapToBase64(photo);
		}
	}

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public void bitmapToBase64(Bitmap bitmap) {
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				baos.flush();
				baos.close();
				byte[] bitmapBytes = baos.toByteArray();
				imgString = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Date date;
	String today;
	public Uri getImageUri() {
		try {
			createSDDir("");
			if (getType == 0) {
				return Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Image/",IMAGE_FILE_NAME));
			}else {
				date = new Date();
				SimpleDateFormat f = new SimpleDateFormat("yymmddhhmmss");
				today = f.format(date);
				return Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Image/",(today+".jpg")));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	//获取系统当前的具体时间
	//	private String getDate() {
	//		Calendar c = Calendar.getInstance();
	//		String year = String.valueOf(c.get(Calendar.YEAR));
	//		String month = String.valueOf(c.get(Calendar.MONTH));
	//		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
	//		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
	//		String mins = String.valueOf(c.get(Calendar.MINUTE));
	//		StringBuffer sbBuffer = new StringBuffer();
	//		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"+ mins);
	//		return sbBuffer.toString();
	//	}

	public String getImagePath() {
		if (getType == 0 ) {
			return Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Image/"+ IMAGE_FILE_NAME;
		}else {
			return Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Image/"+ (today+".jpg");
		}
	}

	private void getBitMap(Uri uri) {
		String fPath = uri2filePath(uri); // 转化为路径
		System.out.println("图片路径 url = "+fPath);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		//		Bitmap bitmap = BitmapFactory.decodeFile(fPath, options);
		int be = (int) (options.outHeight / (float) resizeHeight);
		if (be <= 0)
			be = 1;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig =  Bitmap.Config.RGB_565;
		options.inSampleSize = be;
		photo = BitmapFactory.decodeFile(fPath, options);
		Log.e("MainActivity", "saveBitmap 2222");
		saveBitmap(photo);
		//		setDCIM(fPath);
		//		bitmapToBase64(photo);
	}

	private void getPzBitMap(String path) {
		// TODO Auto-generated method stub
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		//		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		int be = (int) (options.outHeight / (float) resizeHeight);
		if (be <= 0)
			be = 1;
		options.inJustDecodeBounds = false;
		options.inSampleSize = be;
		photo = BitmapFactory.decodeFile(path, options);
		Log.e("MainActivity", "saveBitmap 333");
		saveBitmap(photo);
		//		setDCIM(path);
		//		bitmapToBase64(photo);
	}

	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public  void getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		photo = BitmapFactory.decodeFile(filePath, options);
		Log.e("MainActivity", "saveBitmap 444");
		saveBitmap(photo);
	}


	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public  int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	private String uri2filePath(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = mContext.getContentResolver().query(uri, proj, null,
				null, null);
		if (cursor == null)
			return "";
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(index);
		cursor.close();
		return path;
	}

	public void delFile(){
		File f = new File(Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Image/", IMAGE_FILE_NAME );
		if (f.exists()) {
			f.delete();
		}
	}
	public void saveBitmap(Bitmap bm) {
		try {
			if (!isFileExist("")) {
				createSDDir("");
			}
			File f = null;
			if (getType == 0) {
				f = new File(Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Image/", IMAGE_FILE_NAME );
			}else {
				f = new File(Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Image/", (today+".jpg"));

			}
			if (f.exists()) {
				System.out.println("存在--");
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm = comp(bm);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.e("MainActivity", "已经保存");
			imgString =  f.getPath();
			setDCIM(imgString);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File createSDDir(String dirName) throws IOException {
		File dir = new File(Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Image/" + dirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Image/" + fileName);
		file.isFile();
		return file.exists();
	}

	/**
	 *  图片按比例大小压缩方法（根据路径图片压缩）
	 * @author Sxf  
	 * @date 2015-3-23 下午3:46:56
	 */
	public Bitmap getimage(String srcPath) {  
		BitmapFactory.Options newOpts = new BitmapFactory.Options();  
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了  
		newOpts.inJustDecodeBounds = true;  
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
		newOpts.inJustDecodeBounds = false;  
		int w = newOpts.outWidth;  
		int h = newOpts.outHeight;  
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
		float hh = 800f;//这里设置高度为800f  
		float ww = 480f;//这里设置宽度为480f  
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
		int be = 1;//be=1表示不缩放  
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
			be = (int) (newOpts.outWidth / ww);  
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
			be = (int) (newOpts.outHeight / hh);  
		}  
		if (be <= 0)  
			be = 1;  
		newOpts.inSampleSize = be;//设置缩放比例  
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
	}  

	/**
	 * 图片按比例大小压缩方法（根据Bitmap图片压缩）
	 * @date 2015-3-23 下午2:55:21
	 */
	public Bitmap comp(Bitmap image) {  
		ByteArrayOutputStream baos = new ByteArrayOutputStream();         
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
			baos.reset();//重置baos即清空baos  
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中  
		}  
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
		BitmapFactory.Options newOpts = new BitmapFactory.Options();  
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了  
		newOpts.inJustDecodeBounds = true;  
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
		newOpts.inJustDecodeBounds = false;  
		int w = newOpts.outWidth;  
		int h = newOpts.outHeight;  
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
		float hh = 800f;//这里设置高度为800f  
		float ww = 480f;//这里设置宽度为480f  
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
		int be = 1;//be=1表示不缩放  
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
			be = (int) (newOpts.outWidth / ww);  
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
			be = (int) (newOpts.outHeight / hh);  
		}  
		if (be <= 0)  
			be = 1;  
		newOpts.inSampleSize = be;//设置缩放比例  
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
		isBm = new ByteArrayInputStream(baos.toByteArray());  
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
	}  
	/**
	 * 图片质量压缩
	 * @date 2015-3-23 下午2:54:32
	 */
	public Bitmap compressImage(Bitmap image) {  
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
		int options = 100;  
		while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
			baos.reset();//重置baos即清空baos  
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
			options -= 10;//每次都减少10  
		}  
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
		return bitmap;  
	}  
}