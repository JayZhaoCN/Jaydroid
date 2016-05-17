package com.jayzhao.customactionbar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

/**
 * Created by hm on 16-4-8.
 */
public class MyWebActivity extends MyBaseTitleActivity {

    private WebView mWebView;
    private TextView mTips;
    private WebSettings mWebSettings;
    private static final String TAG = "MyWebActivity";
    private ProgressBar mProgressBar;
    private String mUrl;
    private PullToRefreshWebView mRefreshWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        setStyle(STYLE.SINGLE_BACK);
        mUrl = getIntent().getStringExtra("URL");
        setTitle("Surf Internet");
        mRefreshWebView = (PullToRefreshWebView) findViewById(R.id.refresh_holder);
        mWebView = mRefreshWebView.getRefreshableView();
        mRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                mWebView.reload();
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.web_view_progress);
        mProgressBar.setVisibility(View.VISIBLE);
        mTips = (TextView) findViewById(R.id.tips);
        mTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                mTips.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                mRefreshWebView.onRefreshComplete();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if(errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
                    mTips.setVisibility(View.VISIBLE);
                }

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress + 5);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }
}
