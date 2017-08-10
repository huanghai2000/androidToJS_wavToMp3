package com.kingsun.teacherclasspro.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.TextView;

import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.widgets.CustomTypefaceSpan;
/***
 * 
 * String工具类
 * 
 * @author HH
 * 
 */
public class StringUtils {

	/*
	 * 
	 * MD5加密
	 */

	public static String getMD5Str(String str) {

		MessageDigest messageDigest = null;

		try {

			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));

		} catch (NoSuchAlgorithmException e) {

			System.exit(-1);

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {

			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)

				md5StrBuff.append("0").append(

						Integer.toHexString(0xFF & byteArray[i]));

			else

				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));

		}

		// 16位加密，从第9位到25位

		return md5StrBuff.substring(8, 24).toString().toUpperCase(Locale.getDefault());

	}



	/**
	 * MD5 32位加密方法一 小写
	 * 
	 * @param str
	 * @return
	 */

	public final static String get32MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			// 使用MD5创建MessageDigest对象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				// 将没个数(int)b进行双字节加密
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * MD5 32位加密方法二 小写
	 * 
	 * @param str
	 * @return
	 */

	public final static String get32MD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	/**
	 * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input)||"null".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 订单状态 字典
	 * 
	 * @param 学科
	 * @return
	 */
	public static String orderState(String state) {
		if (TextUtils.isEmpty(state))
			return "";
		return new String[] { "未确认", "确认", "已取消", "无效", "退货", "合并", }[Integer.parseInt(state)];
	}

	public static String orderState(int state) {
		return orderState(state + "");
	}

	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	public static boolean containsEmoji(String source) {
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isNotPhone(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通） 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	public static boolean isLetter(String str) {
		Pattern p = Pattern.compile("[a-zA-Z]");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			// 是字母
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 验证表情
	 */
	public static boolean isReg(String mobiles) {
		//匹配非表情符号的正则表达式
		final String reg ="^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){3,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(mobiles);
		if(!matcher.matches()){
			return false;
		}else {
			return true;
		}



	}

	public static void exitApplication() {
		MyApplication app = MyApplication.getInstance();// 获取应用程序全局的实例引用
		if (app != null) {
			for (Activity act : app.activities) {
				act.finish();// 显式结束
			}
		}
	}

	//处理汉字和拼音混排的字体、数学分数显示问题显示问题

	public static void setTextSHPYFace(TextView textView,String textString,String questionModel,Context context){
		if ("Y".equals(questionModel.substring(0, 1))) {  //设置语文学科内容

			if (!StringUtils.isEmpty(textString)) {
				textString = textString.replaceAll("\\\\n", "\\\n").replaceAll("<br/>", "\n").replaceAll("\\/n", "\\\n").
						replace("\n\n", "\n").replaceAll("（", "(").replaceAll("）", ")").replaceAll("</u></u>", "</u>").replaceAll("<u><u>", "<u>").replaceAll("</py></py>", "</py>").replaceAll("<py><py>", "<py>");
				StringBuilder textBuilder = new StringBuilder(textString);
				SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textString);
				String subStringStart = "<py>";
				String subStringEnd = "</py>";
				String underLineStart = "<u>";
				String underLineEnd = "</u>";
				//全部设置汉字字体楷体
				spannableStringBuilder.setSpan(new CustomTypefaceSpan("serif", MyApplication.getSHTypeface()), 0, textBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				int syartPostion = textBuilder.indexOf(subStringStart);
				int endPostion = textBuilder.indexOf(subStringEnd);
				//设置拼音字体
				if (syartPostion!=-1) {
					setPYSpanableString(textBuilder,spannableStringBuilder,syartPostion,endPostion); 
				}	
				//设置下划线
				if (textBuilder.indexOf(underLineStart)!=-1) {
					int lineStartPosition = textBuilder.indexOf(underLineStart);
					int lineEndPosition = textBuilder.indexOf(underLineEnd);
					setLineSpanableString(textBuilder,spannableStringBuilder,lineStartPosition,lineEndPosition);
				}
				textView.setText(spannableStringBuilder);
			}else {
				textView.setTypeface(MyApplication.getSHTypeface());
			}	

		}else if("S".equals(questionModel.substring(0, 1))){   //设置英语和数学学科内容
			setTextFenshuText(textView, textString,context);
		}else {
			if (textString.length()>=2&&"\n".equals(textString.substring(0, 1))) {
				textString = textString.substring(1);				
			}
			if (textString.length()>0&&"\n".equals(textString.substring(textString.length()-1, textString.length()))) {
				textString = textString.substring(0,textString.length()-1);
			}
			textString = textString.replaceAll("\\\\n", "<br/>").replaceAll("\\/n", "<br/>").replaceAll("\n\n", "<br/>").replaceAll("\\n\n", "<br/>").replaceAll(" +", " ").replaceAll("/n\n", "<br/>").replaceAll("\n", "<br/>").replaceAll("<br/><br/>", "<br/>").trim();
			textView.setText(Html.fromHtml(textString));
		}
	}

	private static void setPYSpanableString(StringBuilder textString,SpannableStringBuilder spannableStringBuilder, int syartPostion2, int endPostion2) {
		String subStringStart = "<py>";
		String subStringEnd = "</py>";
		int syartPostion = syartPostion2+subStringStart.length();
		int endPostion = endPostion2+subStringEnd.length();
		spannableStringBuilder.setSpan(new CustomTypefaceSpan("serif", MyApplication.getPinyinTypeface()),syartPostion,
				endPostion2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableStringBuilder.delete(endPostion2,endPostion); //删除</py>
		textString.delete(endPostion2,endPostion); //删除</py>
		spannableStringBuilder.delete(syartPostion2, syartPostion);//删除<py>
		textString.delete(syartPostion2, syartPostion);//删除<py>
		int checkPosion = endPostion-subStringStart.length()-subStringEnd.length();
		if (checkPosion<textString.length()) {
			int syartPostion3 = textString.indexOf(subStringStart,checkPosion);
			int endPostion3 = textString.indexOf(subStringEnd,checkPosion);
			if (syartPostion3!=-1) {
				setPYSpanableString(textString, spannableStringBuilder,syartPostion3,endPostion3);
			}	
		}

	}

	private static void setLineSpanableString(StringBuilder textString,SpannableStringBuilder spannableStringBuilder, int syartPostion2, int endPostion2) {
		String subStringStart = "<u>";
		String subStringEnd = "</u>";
		int syartPostion = syartPostion2+subStringStart.length();

		spannableStringBuilder.setSpan(new UnderlineSpan(),syartPostion,
				endPostion2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		int endPostion = endPostion2+subStringEnd.length();
		spannableStringBuilder.delete(endPostion2, endPostion); //删除<u>
		textString.delete(endPostion2, endPostion); //删除<u>
		spannableStringBuilder.delete(syartPostion2, syartPostion);//删除</u>
		textString.delete(syartPostion2, syartPostion);//删除</u>
		if (endPostion-endPostion-syartPostion<spannableStringBuilder.length()) {
			int syartPostion3 = textString.indexOf(subStringStart,endPostion);
			int endPostion3 = textString.indexOf(subStringEnd,endPostion);
			if (syartPostion3!=-1) {
				setLineSpanableString(textString, spannableStringBuilder,syartPostion3,endPostion3);
			}	
		}
	}

	//只有汉字的显示楷体
	public static void setTextSHFace(TextView textView,String textString,String questionModel){
		if ("Y".equals(questionModel.substring(0, 1))) {
			textView.setTypeface(MyApplication.getSHTypeface());
		}	
		if (!StringUtils.isEmpty(textString)) {
			textString = textString.replaceAll("\\\\n", "\\\n").replaceAll("\\/n", "\\\n").replaceAll("  +", " ").replaceAll("&nbsp;", "").trim().replace("\n\n", "\n");
			textView.setText(Html.fromHtml(textString));
		}
	}

	//只有拼音时的显示国际拼音
	public static void setTextPYFace(TextView textView,String textString,String questionModel){
		if ("Y".equals(questionModel.substring(0, 1))) {
			textView.setTypeface(MyApplication.getPinyinTypeface());
		}	
		if (!StringUtils.isEmpty(textString)) {
			textString = textString.replaceAll("\\\\n", "\\\n").replaceAll("\\/n", "\\\n").replaceAll("  +", " ").replaceAll("&nbsp;", "").trim().replace("\n\n", "\n");
			textView.setText(Html.fromHtml(textString));
		}
	}


	//处理数学分数显示
	public static void setTextFenshuText(TextView textView,String texString,Context context ){
//		final String fenshuStartString = "<fs>";
//		final String fenshuEndString = "</fs>";
//		final String fenshuLineString = "/";
//		String underLineStart = "<u>";
//		String underLineEnd = "</u>";
//		texString = texString.replaceAll("\\\\n", "\\\n").replaceAll("<br/>", "\n").replaceAll("\\/n", "\\\n").replaceAll("  +", " ").replaceAll("&nbsp;", "").trim().replace("\n\n", "\n").replaceAll("<br/>", "\n").replaceAll("</u></u>", "</u>");
//		StringBuilder textSb = new StringBuilder(texString);
//		int fenshuStartPosion = textSb.indexOf(fenshuStartString);
//		if (fenshuStartPosion!=-1) {
//			int fenshuLinePosion = textSb.indexOf(fenshuLineString,fenshuStartPosion);
//			int fenshuEndPosion = textSb.indexOf(fenshuEndString,fenshuStartPosion);
//			SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textSb);
//			//			spannableStringBuilder.setSpan(new SuperscriptSpan(), 0, textSb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			setFenshuFace(textView,spannableStringBuilder,textSb,fenshuStartPosion,fenshuLinePosion,fenshuEndPosion,context);			
//			//设置下划线
//			if (textSb.indexOf(underLineStart)!=-1) {
//				int lineStartPosition = textSb.indexOf(underLineStart);
//				int lineEndPosition = textSb.indexOf(underLineEnd);
//				setLineSpanableString(textSb,spannableStringBuilder,lineStartPosition,lineEndPosition);
//			}
//			textView.setText(spannableStringBuilder);		
//		}else {
//			textView.setText(Html.fromHtml(texString));
//		}
	}



	/**  设置分数形式
	 * @param textView 
	 * @param spannableStringBuilder
	 * @param texString
	 * @param fenshuStartPosion
	 * @param fenshuLinePosion
	 * @param fenshuEndPosion
	 * @param context 
	 */
//	private static void setFenshuFace(
//			TextView textView, SpannableStringBuilder spannableStringBuilder,StringBuilder textSb,
//			int fenshuStartPosion, int fenshuLinePosion, int fenshuEndPosion, Context context) {
//		final String fenshuStartString = "<fs>";
//		final String fenshuEndString = "</fs>";
//		final String fenshuLineString = "/";
//		int fenshuForSonFirstPosion = fenshuStartPosion+fenshuStartString.length();
//		int chectStartPosion = fenshuEndPosion+fenshuEndString.length();
//		Drawable dd = LayoutToDrawable.setFenshuLayoutToDrawable(textView,context,textSb.substring(fenshuForSonFirstPosion,
//				fenshuLinePosion),textSb.substring(fenshuLinePosion+fenshuLineString.length(), fenshuEndPosion));
//		if (dd!=null) {
//			spannableStringBuilder.setSpan(new MyImageSpan(dd), fenshuStartPosion, fenshuEndPosion+fenshuEndString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			dd.setBounds(0, 0, dd.getIntrinsicWidth(), dd.getIntrinsicHeight()); 
//		}else {
//			Log.e("setFenshuLayoutToDrawable", "drawable==null");
//		}
//		if (chectStartPosion<textSb.length()) {
//			int mFenshuStartPosion = textSb.indexOf(fenshuStartString,chectStartPosion);			
//			if (mFenshuStartPosion>-1) {	
//				int mFenshuLinePosion = textSb.indexOf(fenshuLineString,mFenshuStartPosion);
//				int mFenshuEndPosion = textSb.indexOf(fenshuEndString,mFenshuStartPosion);
//				setFenshuFace(textView,spannableStringBuilder,textSb,mFenshuStartPosion,mFenshuLinePosion,mFenshuEndPosion,context);									
//			}
//		}
//	}
}