package com.kingsun.teacherclasspro.callback;

import android.app.Activity;
import android.content.Intent;
public class InterFace {
	Activity mActivity ;
	public InterFace(Activity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	/** �����׿�App
	 * 
	 */
	public void startAPP(){
		Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage("com.clovsoft.ikv3");
		mActivity.startActivity(intent);
	}
}
