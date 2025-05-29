package com.tencent.tbs.demo.viewutils.embeddedwidget;

import android.graphics.Rect;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;

import com.tencent.smtt.export.external.embeddedwidget.interfaces.IEmbeddedWidget;
import com.tencent.smtt.export.external.embeddedwidget.interfaces.IEmbeddedWidgetClient;

import java.io.IOException;
import java.util.Map;


public class VideoWidget implements IEmbeddedWidgetClient {
    private static final String TAG = "VideoWidget";
    private String mTagName;
    private Map<String, String> mAttributes;
    private IEmbeddedWidget mWidget;
    private MediaPlayer mMediaPlayer;
    private String mVideoSrc;
    public VideoWidget(String tagName, Map<String, String> attributes, IEmbeddedWidget widget) {
        mTagName = tagName;
        mAttributes = attributes;
        mWidget = widget;
        if (!TextUtils.isEmpty(attributes.get("src"))) {
            mVideoSrc = attributes.get("src");
        }
    }

    @Override
    public void onSurfaceCreated(Surface surface) {
        Log.i(TAG, "onSurfaceCreated, surface:" + surface);
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            if (mVideoSrc != null) {
                play();
            }
        }
        mMediaPlayer.setSurface(surface);
    }

    @Override
    public void onSurfaceDestroyed(Surface surface) {
        Log.i(TAG, "onSurfaceDestroyed, surface:" + surface);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVideoSrc == null) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
        }
        return true;
    }

    @Override
    public void onRectChanged(Rect rect) {
        Log.i(TAG, "VideoEmbeddedWidgetClient.onRectChanged, rect:" + rect.toString());
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onActive() {
        Log.i(TAG, "onActive");
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void onDeactive() {
        Log.i(TAG, "onDeactive");
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
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
        Log.i(TAG, "onVisibilityChanged, visibility:" + visibility + ", hashCode:" + hashCode());
        if (mMediaPlayer != null) {
            if (visibility) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }
    }

    private void play() {
        try {
            mMediaPlayer.setDataSource(mVideoSrc);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(mediaPlayer -> { 
                Log.i(TAG, "onPrepared"); 
                mediaPlayer.start(); 
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}