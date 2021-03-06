package com.jayzhao.customactionbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayzhao.customactionbar.Widget.MyDialogFragment;
import com.jayzhao.customactionbar.Widget.MyProgressBar;
import com.jayzhao.customactionbar.Widget.SimpleDialogFragment;
import com.jayzhao.customactionbar.Widget.SubItem;

/**
 * Created by hm on 16-4-20.
 */
public class NextActivity extends MyBaseTitleActivity implements View.OnClickListener {

    private SubItem mSubItem = null;
    private Button mStartButton = null;
    private Animation mAnimation = null;
    private View mStartBg = null;
    private TextView mValueAnimation = null;

    private MyProgressBar myProgressBar = null;

    ValueAnimator mAnimator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE.BACK_AND_MORE);
        setContentView(R.layout.next_layout);
        setTitle("Hey Young Blood!");

        mSubItem = (SubItem) findViewById(R.id.subItem);
        mSubItem.setOnClickListener(this);

        mStartButton = (Button) findViewById(R.id.start_button);
        mStartBg = findViewById(R.id.start_bg);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.button_anim);
        mStartBg.startAnimation(mAnimation);
        mValueAnimation = (TextView) findViewById(R.id.valueAnimation);
        mValueAnimation.setOnClickListener(this);

        mStartButton.setOnClickListener(this);

        myProgressBar = (MyProgressBar) findViewById(R.id.progressbar);

        initAnimation();

        getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialogFragment fragment = new SimpleDialogFragment();

                fragment.setOpClickListener(new MyDialogFragment.OnOpClickListener() {
                    @Override
                    public void onEmptyAreaClick(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                    @Override
                    public void onRightClick(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                    @Override
                    public void onLeftClick(DialogFragment dialog) {
                        MyUtils.showToast(NextActivity.this, "Clicked");
                    }
                });
                fragment.show(getFragmentManager(), null);
            }
        });

    }

    public void initAnimation() {
        mAnimator = ValueAnimator.ofInt(0, 200);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e("animation: ", "fuck update");
                int value = (int) animation.getAnimatedValue();
                myProgressBar.setmProgress(value);
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                Log.e("onAnimationEnd: ", "fuck cancel");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e("onAnimationEnd: ", "fuck end");
            }
        });
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.subItem:
                MyUtils.showToast(NextActivity.this, "you have clicked item!");
                startActivity(new Intent(NextActivity.this, RefreshActivity.class));
                break;
            case R.id.valueAnimation:
                startActivity(new Intent(NextActivity.this, AnimationTest.class));
                break;
            case R.id.start_button:
                startActivity(new Intent(NextActivity.this, SildeActivity.class));
                break;
        }
    }
}
