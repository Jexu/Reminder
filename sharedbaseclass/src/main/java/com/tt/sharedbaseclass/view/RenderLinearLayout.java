package com.tt.sharedbaseclass.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zhengguo on 5/31/16.
 */


/**
 * MaterialLayout是模拟Android 5.0中View被点击的波纹效果的布局，与其他的模拟Material
 * Desigin效果的View不同，所有在MaterialLayout布局下的子视图被点击时都会产生波纹效果,而不是某个特定的View才会有这样的效果.
 *
 */
public class RenderLinearLayout extends LinearLayout implements Runnable {

  private static final int DEFAULT_RADIUS = 10;
  private static final int DEFAULT_FRAME_RATE = 10;
  private static final int DEFAULT_DURATION = 200;
  private static final int DEFAULT_ALPHA = 255;
  private static final float DEFAULT_SCALE = 0.8f;
  private static final int DEFAULT_ALPHA_STEP = 5;

  private View mTargetView;

  private MotionEvent mEvent;

  int mRevealRadius;

  /**
   * 动画帧率
   */
  private int mFrameRate = DEFAULT_FRAME_RATE;
  /**
   * 渐变动画持续时间
   */
  private int mDuration = DEFAULT_DURATION;
  /**
   *
   */
  private Paint mPaint = new Paint();
  /**
   * 被点击的视图的中心点
   */
  private Point mCenterPoint = null;
  /**
   * 视图的Rect
   */
  private RectF mTargetRectf;
  /**
   * 起始的圆形背景半径
   */
  private int mRadius = DEFAULT_RADIUS;
  /**
   * 最大的半径
   */
  private int mMaxRadius = DEFAULT_RADIUS;

  /**
   * 渐变的背景色
   */
  private int mCirclelColor = Color.LTGRAY;
  /**
   * 每次重绘时半径的增幅
   */
  private int mRadiusStep = 1;
  /**
   * 保存用户设置的alpha值
   */
  private int mBackupAlpha;

  /**
   * 圆形半径针对于被点击视图的缩放比例,默认为0.8
   */
  private float mCircleScale = DEFAULT_SCALE;
  /**
   * 颜色的alpha值, (0, 255)
   */
  private int mColorAlpha = DEFAULT_ALPHA;
  /**
   * 每次动画Alpha的渐变递减值
   */
  private int mAlphaStep = DEFAULT_ALPHA_STEP;

  /**
   * @param context
   */
  public RenderLinearLayout(Context context) {
    this(context, null);
    init(context, null);
  }

  public RenderLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public RenderLinearLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    initPaint();

    this.setWillNotDraw(false);
    this.setDrawingCacheEnabled(true);
  }


  private void initPaint() {
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setColor(mCirclelColor);
    mPaint.setAlpha(mColorAlpha);

    // 备份alpha属性用于动画完成时重置
    mBackupAlpha = mColorAlpha;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    int x = (int) ev.getX();
    int y = (int) ev.getY();
    if (ev.getAction() == MotionEvent.ACTION_UP) {
      if (isClickable() && isEnabled()) {
        mTargetView = getTargetView(this, x, y);
        postInvalidateDelayed(200);
      }
    } else if (ev.getAction() == MotionEvent.ACTION_UP) {
      postInvalidateDelayed(200);
      mEvent = ev;
      postDelayed(this, 400);
      return true;
    } else if (ev.getAction() == MotionEvent.ACTION_CANCEL) {
      postInvalidateDelayed(200);
    }

    return super.dispatchTouchEvent(ev);
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    if (mTargetView == null) {
      return;
    }

    if (mRevealRadius > mTargetView.getMeasuredHeight() / 2) {
      mRevealRadius += 1 * 4;
    } else {
      mRevealRadius += 1;
    }
    int[] location = new int[2];
    mTargetView.getLocationOnScreen(location);
    int left = location[0];
    int top = location[1];
    int right = left + mTargetView.getMeasuredWidth();
    int bottom = top + mTargetView.getMeasuredHeight();

    canvas.save();
    canvas.clipRect(left, top, right, bottom);
    canvas.drawCircle(mEvent.getX(), mEvent.getY(), mRevealRadius, mPaint);
    canvas.restore();

    if (mRevealRadius <= 10) {
      postInvalidateDelayed(400, left, top, right, bottom);
    }
  }

  protected View getTargetView(View view, int x, int y) {
    int locations[] = new int[2];
    view.getLocationOnScreen(locations);
    int left = locations[0];
    int top = locations[1];
    int right = locations[0] + view.getMeasuredWidth();
    int bottom = locations[1] + view.getMeasuredHeight();
    if (x >= left && x <= right && y >= top && y <= bottom) {
      return this;
    } else {
      return  null;
    }
  }

  @Override
  public void run() {

    if (getTargetView(this, (int)mEvent.getX(), (int)mEvent.getY()) != null) {
      mTargetView.dispatchTouchEvent(mEvent);
    }
  }
}
