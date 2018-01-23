package cn.bmob.imdemo.YiYou.UI;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;

/**
 * Created by DELL on 2017/6/1.
 */

public class webview extends BaseActivity {
    WebView web;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        web= (WebView) findViewById(R.id.webview);
        if(null!=getIntent().getStringExtra("url")){
            web.loadUrl(getIntent().getStringExtra("url"));
            web.setWebViewClient(new WebViewClient());
            WebSettings webSettings = web.getSettings();
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setSupportZoom(false);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        }



    }


}
