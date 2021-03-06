package com.jayzhao.customactionbar.Widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jayzhao.customactionbar.MyUtils;
import com.jayzhao.customactionbar.R;

/**
 * Created by Jay on 16-8-22.
 * 周活跃时长图表
 */
public class WeekActiveTimeView extends View {
    private static final String TAG = "WeekActiveTimeView";

    private int mHeight = 0;
    private int mWidth = 0;

    //后期需要改成从string.xml中加载
    private String[] mWeeks = {"一", "二", "三", "四", "五", "六", "日"};

    //每日活跃时长
    //后期需要改成从服务器获取
    private int[] mActiveTimes = {20, 10, 80, 100, 9, 30, 50};
    private int[] mColumnHeight = {0, 0, 0, 0, 0, 0, 0};

    private int mBaseLineHeight = 80;

    private Paint mTextPaint = null;
    private Paint mBaseLinePaint = null;
    private Paint mColumnPaint = null;
    private Paint mTimePaint = null;

    private Resources mResources = null;

    private Rect mBaseLineRect = null;
    private Rect mColumnRect = null;

    private Context mContext = null;

    private Paint.FontMetricsInt mBaseLineFontMetrics = null;

    private int mTextBaseLine = 0;

    private ValueAnimator mAnimator = null;

    private boolean isDrawText = false;

    public WeekActiveTimeView(Context context) {
        this(context, null);
    }

    public WeekActiveTimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekActiveTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void setData(int[] data) {
        mActiveTimes = data;
    }

    public void isDrawText(boolean isDrawText) {
        this.isDrawText = isDrawText;
    }

    private void init() {
        mResources = getResources();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(MyUtils.sp2px(mContext, 12f));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mResources.getColor(R.color.black_60_percent));

        mTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimePaint.setTextSize(MyUtils.sp2px(mContext, 12f));
        mTimePaint.setTextAlign(Paint.Align.CENTER);
        mTimePaint.setColor(mResources.getColor(R.color.black_60_percent));

        mBaseLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBaseLinePaint.setColor(mResources.getColor(R.color.black_10_percent));

        mColumnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColumnPaint.setColor(mResources.getColor(R.color.blue_light));


        mBaseLineFontMetrics = mTextPaint.getFontMetricsInt();

        //先暂时做成这样
        mAnimator = ValueAnimator.ofInt(0, 1000);
        mAnimator.setDuration(900);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (int i = 0; i < mWeeks.length; i++) {
                    mColumnHeight[i] = (mActiveTimes[i] * (int)animation.getAnimatedValue()) / 1000;
                    Log.i(TAG, "mColumnHeight： " + mColumnHeight[i]);
                    postInvalidateOnAnimation();
                }
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isDrawText = true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;

        mBaseLineRect = new Rect(0, mHeight - mBaseLineHeight, mWidth, mHeight);
        mTextBaseLine = (mHeight - mBaseLineFontMetrics.bottom - mBaseLineFontMetrics.top) / 2 + mHeight / 2 - mBaseLineHeight / 2;
        mColumnRect = new Rect(0, 0, 0, mHeight - mBaseLineHeight);
    }

    private boolean mIsStarted = false;

    public void startLoading() {
        if(mAnimator != null) {
            if(!mAnimator.isRunning()) {
                mAnimator.start();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(mBaseLineRect, mBaseLinePaint);

        /*if(!mIsStarted) {
            mAnimator.start();
            mIsStarted = true;
        }*/

        for(int i=0; i< mWeeks.length; i++) {
            canvas.drawText(mWeeks[i], mWidth / 7 * i + mWidth / 14, mTextBaseLine, mTextPaint);
            mColumnRect.left = mWidth / 7 * i + 10;
            mColumnRect.right = mWidth / 7 * (i + 1) - 10;
            mColumnRect.top = mHeight - mBaseLineHeight - mColumnHeight[i] * 4;
            canvas.drawRect(mColumnRect, mColumnPaint);

            //画柱子上的刻度
            if(isDrawText) {
                canvas.drawText(mActiveTimes[i] + "分钟", mColumnRect.centerX(), mColumnRect.top - 10, mTimePaint);
            }
        }
    }
}
