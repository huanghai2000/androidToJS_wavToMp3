package com.kingsun.teacherclasspro.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
/**
 * Created by kings on 2016-3-8.
 */
public class SharedPreferencesUtil {
	public final static String AppID = "APPID";// APPID
	public final static String USER_ID = "USER_ID";// user ID
	public final static String USER_NAME = "USER_NAME";// 用户名
	public final static String PASS_WORD = "PASS_WORD";// 用户密码
	public static SharedPreferences preferences;
	public static void initPreferences(Context context) {
		if (preferences == null) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
	}

	/**    

	 * saveUserDataToPreferences(  真实姓名登陆学校)    
	 * @param   zhanglu    
	 * @param  @return    设定文件    
	 * @return String    DOM对象     
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1    
	 */
	public static void saveTrueNameSchoolPreferences(String schoolName, String schoolID){
		Editor editor = preferences.edit();	
		editor.commit();
	}
}
