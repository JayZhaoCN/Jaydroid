package com.jayzhao.customactionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jayzhao.customactionbar.Widget.MyDialogFragment;

import java.lang.ref.WeakReference;


public class MainActivity extends MyBaseTitleActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private TextView mRecyclerViewText;
    private TextView mTableText;
    private TextView mWebText;
    private TextView mChangeAnimation;
    private TextView mPromptDialogText;
    private TextView mNextPage;

    private MyLoadingDialog mDialog;

    private MyHandler mMyHandler = null;
    private WeakHandler mWeakHandler = null;

    class MyHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            /**
             * 额，虽然我复写了dispatchMessgae()方法，但是，额，也调用了super.dispatchMessage(msg)啊
             * 我好蠢...
             */
            super.dispatchMessage(msg);
            Log.e(TAG, "disaptch message" + ", data is: "+ msg.getData().getString(TAG));
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "handle message");
        }
    }

    /**
     * 如何避免在使用Handler时导致的内存泄露？
     * 很简单，把需要使用的Handler定义为静态内部类即可。
     * 静态内部类不会持有外部类的引用，故不会导致内存泄露。
     * 详见：http://www.cnblogs.com/xujian2014/p/5025650.html
     */
    static class WeakHandler extends Handler {
        WeakReference<MainActivity> mWeakReference = null;
        public WeakHandler(MainActivity activity) {
            mWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final MainActivity activity = mWeakReference.get();
            if(msg.what == 1) {
                activity.mTableText.setText("Weak Reference");
                sendEmptyMessageDelayed(2, 1000);
            } else if(msg.what == 2) {
                activity.mTableText.setText("TableLayout");
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                mDialog.setSuccess();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        setStyle(STYLE.BACK_AND_MORE);

        /**
         * 测试Handler
         * 设置Message的mCallback
         * 在Handler发送Message后，Looper对象会调用Handler的dispatchMessage方法，从而调用Message的mCallback
         */
        mMyHandler = new MyHandler();
        Message msg = Message.obtain(mMyHandler, new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "Message Callback");
            }
        });
        Bundle data = new Bundle();
        data.putString(TAG, "Good");
        msg.setData(data);
        mMyHandler.sendMessage(msg);

        mWeakHandler = new WeakHandler(this);
        mWeakHandler.sendEmptyMessageDelayed(1, 1000);


        mRecyclerViewText = (TextView) findViewById(R.id.recyclerViewText);
        mRecyclerViewText.setOnClickListener(this);

        mTableText = (TextView) findViewById(R.id.tableLayoutText);
        mTableText.setOnClickListener(this);

        mWebText = (TextView) findViewById(R.id.webText);
        mWebText.setOnClickListener(this);

        mChangeAnimation = (TextView) findViewById(R.id.changeAnimation);
        mChangeAnimation.setOnClickListener(this);

        mPromptDialogText = (TextView) findViewById(R.id.promptDiglog);
        mPromptDialogText.setOnClickListener(this);

        mNextPage = (TextView) findViewById(R.id.nextPage);
        mNextPage.setOnClickListener(this);

        getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MyAlertDialog dialog = new MyAlertDialog(MainActivity.this);
                final Dialog myDialog = dialog.getDialog(3, "Toast", "Custom Toast", "Custom Toast 2");
                dialog.setmOnItemClickListener(new MyAlertDialog.MyOnItemClickListener() {
                    @Override
                    public void firstItemClick(View v) {
                        MyUtils.showToast(MainActivity.this, "Custom Toast");
                        myDialog.dismiss();
                    }

                    @Override
                    public void secondItemClick(View v) {
                        MyUtils.showIconToast(MainActivity.this, "Icon Toast!", R.mipmap.ic_launcher);
                        myDialog.dismiss();
                    }

                    @Override
                    public void thirdItemClick(View v) {
                        MyUtils.showCustomToast(MainActivity.this, "Jay Zhao", "Custom Toast", R.mipmap.ic_launcher);
                        myDialog.dismiss();
                    }

                    @Override
                    public void fourthItemClick(View v) {

                    }
                });
                myDialog.show();
            }
        });
    }

    public void click(View view) {
        //Intent intent = new Intent(MainActivity.this, MyWebActivity.class);
        //intent.putExtra("URL", "https://www.baidu.com/");
        //startActivity(intent);
        startActivity(new Intent(MainActivity.this, JayActivity.class));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.recyclerViewText:
                startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
                break;
            case R.id.tableLayoutText:
                startActivity(new Intent(MainActivity.this, MyTableLayout.class));
                break;
            case R.id.webText:
                intent = new Intent(MainActivity.this, MyWebActivity.class);
                intent.putExtra("URL", "http://www.sina.com.cn/");
                startActivity(intent);
                break;
            case R.id.changeAnimation:
                intent = new Intent(MainActivity.this, ChangeAnimation.class);
                startActivity(intent);
                break;
            case R.id.promptDiglog:
                mDialog = new MyLoadingDialog(this);
                mDialog.showDialog();
                mDialog.setOnLoadingDoneListener(new MyLoadingDialog.OnLoadingDoneListener() {
                    @Override
                    public void onLoadingDone() {
                        MyUtils.showToast(MainActivity.this, "done");
                    }
                });
                mHandler.sendEmptyMessageDelayed(1, 2000);
                break;
            case R.id.nextPage:
                startActivity(new Intent(MainActivity.this, NextActivity.class));
                break;
        }
    }
}
