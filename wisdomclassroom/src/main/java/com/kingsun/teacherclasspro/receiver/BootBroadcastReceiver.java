package com.kingsun.teacherclasspro.receiver;

/**
 * Created by hai.huang on 2017/11/9.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kingsun.teacherclasspro.activity.StartActivityAty;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static String TAG ="MainActivity";
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG,"onReceive "+intent.getAction());
        //接收广播：系统启动完成后运行程序
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent ootStartIntent = new Intent(context, StartActivityAty.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }
        //接收广播：安装更新后，自动启动自己。
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) || intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED))
        {
            Intent ootStartIntent = new Intent(context, StartActivityAty.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }
//        if (intent.getAction().equals(ACTION)) {
//            Intent mainActivityIntent = new Intent(context, StartActivityAty.class);  // 要启动的Activity
//            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mainActivityIntent);
//        }
    }

}
