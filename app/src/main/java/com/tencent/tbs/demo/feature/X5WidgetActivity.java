package com.tencent.tbs.demo.feature;

import com.tencent.tbs.demo.viewutils.embeddedwidget.WidgetClientFactory;

public class X5WidgetActivity extends X5WebViewActivity {

    @Override
    protected void initWebView() {
        super.initWebView();
        String[] tags = {"video", "my-canvas"};
        mWebView.getX5WebViewExtension().registerEmbeddedWidget(tags, new WidgetClientFactory(this));
        mWebView.loadUrl("file:///android_asset/webpage/EmbeddedWidgetExamplePage.html");
    }
}
