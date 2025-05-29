package com.tencent.tbs.demo.viewutils.embeddedwidget;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.tencent.smtt.export.external.embeddedwidget.interfaces.IEmbeddedWidget;
import com.tencent.smtt.export.external.embeddedwidget.interfaces.IEmbeddedWidgetClient;
import com.tencent.smtt.export.external.embeddedwidget.interfaces.IEmbeddedWidgetClientFactory;

import java.util.Map;

public class WidgetClientFactory implements IEmbeddedWidgetClientFactory {

    private Activity mContext;
    private DisplayMetrics mDisplayMetrics;

    public WidgetClientFactory(Activity context) {
        mContext = context;
        mDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    /**
     * 通知宿主创建Native组件
     * tagName: 对应的web标签名]
     * attributes: 标签属性列表
     * widget: 内核对应的组件对象
     */
    @Override
    public IEmbeddedWidgetClient createWidgetClient(String tag, Map<String, String> attributes, IEmbeddedWidget iEmbeddedWidget) {
        if (tag.equalsIgnoreCase("video")) {
            return new VideoWidget(tag, attributes, iEmbeddedWidget);
        } else if (tag.equalsIgnoreCase("my-canvas")) {
            return new CanvasWidget(tag, attributes, iEmbeddedWidget, mDisplayMetrics);
        }
        return null;
    }
}
