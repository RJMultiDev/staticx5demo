package com.tencent.tbs.demo;


import android.app.Application;
import android.util.Log;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.QbSdk.PreInitCallback;
import com.tencent.smtt.sdk.TbsFramework;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.tbs.demo.utils.DemoConfig;

import java.util.HashMap;
import java.util.Map;

public class DemoApplication extends Application {


    private static final String TAG = "DemoApplication";


    @Override
    public void onCreate() {
        super.onCreate();
        if (isRendererProcess()) {
            return;
        }

        TbsFramework.setUp(this, DemoConfig.APP_LICENSE_KEY);
        
        QbSdk.disableAutoCreateX5Webview();
        if (isWebProcess()) {
            Map<String, Object> settings = new HashMap<>();
            settings.put(TbsCoreSettings.MULTI_PROCESS_ENABLE, 1);
            QbSdk.initTbsSettings(settings);
            QbSdk.preInit(this, null);
        }
    }

    private boolean isRendererProcess() {
        String curProcessName = QbSdk.getCurrentProcessName(this);
        return curProcessName.contains(":sandboxed_process")
                || curProcessName.contains(":privileged_process");
    }

    private boolean isWebProcess() {
        String curProcessName = QbSdk.getCurrentProcessName(this);
        return curProcessName.contains(":web");
    }

}
