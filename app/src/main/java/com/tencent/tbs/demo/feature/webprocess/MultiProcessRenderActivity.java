package com.tencent.tbs.demo.feature.webprocess;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.tencent.smtt.sdk.MultiProcessHelper;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.tbs.demo.feature.BaseWebViewActivity;

public class MultiProcessRenderActivity extends BaseWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWebView() {
        super.initWebView();
        String type = "";
        if (QbSdk.getTbsVersion(this) >= 46300) {
             type = MultiProcessHelper.getMultiProcessType() == 1 ? ":多进程渲染" : ":单进程渲染";

        }
        Toast.makeText(this, mWebView.getIsX5Core() ?
                "X5内核: " + QbSdk.getTbsVersion(this) + type : "系统内核", Toast.LENGTH_SHORT).show();
    }
}
