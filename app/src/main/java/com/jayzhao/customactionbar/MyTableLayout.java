package com.jayzhao.customactionbar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by hm on 16-4-12.
 */
public class MyTableLayout extends MyBaseTitleActivity implements View.OnClickListener {

    private Dialog mDialog = null;
    private Context mContext = null;

    private MyAlertDialog mMyAlertDialog = null;

    private TextView mRightButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.table_layout);
        this.setStyle(STYLE.BACK_AND_MORE);

        mContext = this;

        mRightButton = getRightButton();
        mRightButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right:
                mMyAlertDialog = new MyAlertDialog(mContext);
                mMyAlertDialog.getDialog(2).show();

                mMyAlertDialog.setmOnItemlickListener(new MyAlertDialog.MyOnItemlickListener() {
                    @Override
                    public void firstItemClick(View v) {
                        //MyTableLayout.this.setContentView(R.layout.table_layout);

                    }

                    @Override
                    public void secondItemClick(View v) {
                        //MyTableLayout.this.setContentView(R.layout.table_layout2);

                    }

                    @Override
                    public void thirdItemClick(View v) {

                    }

                    @Override
                    public void fourthItemClick(View v) {

                    }
                });
                break;
        }
    }
}