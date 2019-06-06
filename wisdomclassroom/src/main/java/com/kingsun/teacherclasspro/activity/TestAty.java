package com.kingsun.teacherclasspro.activity;

import android.app.Activity;
import android.os.Bundle;

import com.kingsun.teacherclasspro.R;

/**
 * Created by hai.huang on 2017/9/13.
 */

public class TestAty extends Activity {
//    @InjectView(R.id.webView_main)
//    MyWebView webViewMain;
//    @InjectView(R.id.login)
//    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.test);
//        ButterKnife.inject(this);
//        webViewMain.getSettings().setJavaScriptEnabled(true);
//        webViewMain.getSettings().setDomStorageEnabled(true);
//        webViewMain.getSettings().setAllowFileAccess(true);
//        webViewMain.getSettings().setAppCacheEnabled(true);
//        webViewMain.getSettings().setDefaultTextEncodingName("utf-8");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 开启app内调试的开关，开启后将可以在谷歌浏览器中调试js
//            webViewMain.setWebContentsDebuggingEnabled(true);
//            webViewMain.getSettings().setLoadsImagesAutomatically(true);
//        }else {
//            webViewMain.getSettings().setLoadsImagesAutomatically(false);
//        }
//
//        webViewMain.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                if(!view.getSettings().getLoadsImagesAutomatically()) {
//                    view.getSettings().setLoadsImagesAutomatically(true);
//                }
//                super.onPageFinished(view, url);
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                view.clearHistory();
//                super.onReceivedError(view, errorCode, description, failingUrl);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.clearCache(true);// 清除网页访问留下的缓存，由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//        });
//        webViewMain.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int progress){
//            }
//        });
//        webViewMain.loadUrl(AppConfig.MathineUrl);
//    }
//
//    @OnClick({R.id.webView_main, R.id.login})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.webView_main:
//                break;
//            case R.id.login:
//                break;
//        }
    }
}
