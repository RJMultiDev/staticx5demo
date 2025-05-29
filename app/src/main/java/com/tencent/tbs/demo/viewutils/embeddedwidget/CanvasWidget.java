package com.tencent.tbs.demo.viewutils.embeddedwidget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;

import com.tencent.smtt.export.external.embeddedwidget.interfaces.IEmbeddedWidget;
import com.tencent.smtt.export.external.embeddedwidget.interfaces.IEmbeddedWidgetClient;

import java.util.Map;

public class CanvasWidget implements IEmbeddedWidgetClient {
    private static final String TAG = "CanvasWidget";
    private final String mTagName;
    private final Map<String, String> mAttributes;
    private final IEmbeddedWidget mWidget;
    private Surface mSurface;
    private final Paint mCirclePaint;
    private final Paint mTextPaint;
    private final Handler mHandler;
    private int mDrawCount = 0;
    private boolean mModeNum = true;
    // 像素密度，需要从Activity中获取
    private float mDensity = 1;
    private final Runnable rmRunnable = new Runnable() {
        @Override
        public void run() {
            if (mSurface == null)
                return;
            if (mModeNum) {
                Canvas canvas = mSurface.lockCanvas(null);
                canvas.drawColor(Color.parseColor("#00aaaa"));
                canvas.drawText("" + mDrawCount++, 350, 250, mTextPaint);
                mSurface.unlockCanvasAndPost(canvas);
                mHandler.postDelayed(rmRunnable, 100);
            }
        }
    };

    public CanvasWidget(String tagName, Map<String, String> attributes, IEmbeddedWidget widget, DisplayMetrics metrics) {
        mTagName = tagName;
        mAttributes = attributes;
        mWidget = widget;
        mDensity = metrics.density;
        Log.i(TAG, "new CanvasEmbeddedWidgetClient, tagName:" + tagName);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(1);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setTextSize(80);
        mHandler = new Handler();
    }

    @Override
    public void onSurfaceCreated(Surface surface) {
        mSurface = surface;
        Log.i(TAG, "onSetSurface, surface is valid:" + surface.isValid());
        mHandler.postDelayed(rmRunnable, 50);
    }

    @Override
    public void onSurfaceDestroyed(Surface surface) {
        Log.i(TAG, "onSurfaceDestroyed, surface is valid:" + surface.isValid());
        mHandler.removeCallbacks(rmRunnable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mModeNum = !mModeNum;
            if (!mModeNum) {
                float newX = event.getX();
                float newY = event.getY();
                Log.i(TAG, "onTouchEvent ACTION_DOWN: " + event.getX() + ", " + event.getY());
                Log.i(TAG, "onTouchEvent new: " + newX + ", " + newY);
                Canvas canvas = mSurface.lockCanvas(null);
                canvas.drawColor(Color.BLUE);
                canvas.drawCircle(newX, newY, 50, mCirclePaint);
                mSurface.unlockCanvasAndPost(canvas);
            } else {
                mHandler.post(rmRunnable);
            }
        }
        return true;
    }

    @Override
    public void onRectChanged(Rect rect) {
        Log.i(TAG, "CanvasEmbeddedWidgetClient.onRectChanged, rect:" + rect);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        mSurface = null;
        mHandler.removeCallbacks(rmRunnable);
    }

    @Override
    public void onActive() {
        Log.i(TAG, "onActive");
    }

    @Override
    public void onDeactive() {
        Log.i(TAG, "onActive");
    }

    @Override
    public void onRequestRedraw() {
    }

    @Override
    public boolean onSetAttribute(String s, String s1) {
        return false;
    }

    @Override
    public void onVisibilityChanged(boolean visibility) {
        Log.i(TAG, "onVisibilityChanged, visibility:" + visibility);
    }
}