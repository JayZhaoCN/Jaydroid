package com.jayzhao.customactionbar.Widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.jayzhao.customactionbar.MyUtils;
import com.jayzhao.customactionbar.R;

/**
 * Created by hm on 16-4-26.
 */
public class MyProgressBar extends View {

    private int mDefaultColor = 0;
    private int mRadius = 345;
    private int mTextColor = 0;
    private int mColor = 0;
    private String mText = null;
    private int mStrokeWidth = 0;

    private boolean mIsCanceled = false;

    private Context mContext = null;

    private Paint mProgressPaint = null;
    private Paint mBackgroundPaint = null;
    private Paint mTextPaint = null;

    private int mProgress = 0;

    private RectF mBound = null;
    private RectF mBoundArc = null;

    ValueAnimator mAnimator = null;
    private static String TAG = "MYProgressBar";

    public void setmProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public MyProgressBar(Context context) {
        this(context, null);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        obtainAttrs(attrs, defStyleAttr);

        initAnimation();

        setUp();
    }

    private void initAnimation() {
        mAnimator = ValueAnimator.ofInt(0, 200);
        mAnimator.setDuration(5000);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                setmProgress(value);
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mIsCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(mIsCanceled) {
                    mIsCanceled = false;
                    return;
                }

                MyUtils.showToast(mContext, "Animation Completed!");

                //TODO do something

            }
        });
    }

    private void setUp() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(mDefaultColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(mColor);
        mProgressPaint.setStrokeWidth(mStrokeWidth);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(20);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void obtainAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.MyProgressBar, 0, defStyleAttr);

        mDefaultColor = ta.getColor(R.styleable.MyProgressBar_strokeDefaultColor, Color.GRAY);
        mTextColor = ta.getColor(R.styleable.MyProgressBar_textColor, Color.WHITE);
        mColor = ta.getColor(R.styleable.MyProgressBar_strokeColor, Color.CYAN);

        mText = ta.getString(R.styleable.MyProgressBar_centertext);
        //Log.e("obtainAttrs: ", mText);
        mStrokeWidth = ta.getDimensionPixelOffset(R.styleable.MyProgressBar_strokeWidth, 45);
        mRadius = ta.getDimensionPixelOffset(R.styleable.MyProgressBar_radius, 45);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthDesire = mRadius*2 + getPaddingLeft() + getPaddingRight();
        int heightDesire = mRadius*2 + getPaddingTop() + getPaddingBottom();

        int width = resolveSize(widthDesire, widthMeasureSpec);
        int height = resolveSize(heightDesire, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(mBound, mBackgroundPaint);
        canvas.drawArc(mBoundArc, 0, (float) mProgress / 200 * 360, false, mProgressPaint);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();

        //Log.e(TAG, fm.top + "");

        canvas.drawText(mText, mRadius, mRadius - fm.bottom/2 - fm.top/2, mTextPaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBackgroundPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
                invalidate();
                mAnimator.start();
                break;
            case MotionEvent.ACTION_UP:
                mBackgroundPaint.setColor(getResources().getColor(R.color.user_agreement_text));
                invalidate();
                mAnimator.cancel();
                setmProgress(0);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = w/2;
        mBound = new RectF(0, 0, mRadius*2, mRadius*2);
        mBoundArc = new RectF(mStrokeWidth/2, mStrokeWidth/2, mRadius*2 - mStrokeWidth/2, mRadius*2 - mStrokeWidth/2);
    }
}
