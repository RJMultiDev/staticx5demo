package com.tencent.tbs.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.export.external.interfaces.IAuthRequestCallback;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.QbSdk.PreInitCallback;
import com.tencent.smtt.sdk.QbSdk.PrivateCDNMode;
import com.tencent.smtt.sdk.TbsCommonCode;
import com.tencent.smtt.sdk.TbsFramework;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.smtt.sdk.TbsVersionController;
import com.tencent.smtt.sdk.TbsVersionController.CallBack;
import com.tencent.smtt.sdk.X5Downloader;
import com.tencent.tbs.demo.feature.X5WebViewActivity;
import com.tencent.tbs.demo.utils.DemoConfig;
import com.tencent.tbs.demo.utils.FileUtil;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InstallationActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ProgressBar mProgressBar;
    AlertDialog muskDialog;

    private PreInitCallback localPreInitCallback;
    private Intent navIntent;
    private static boolean hasCreated = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.dl_progress);
        mProgressBar.setVisibility(View.GONE);

        muskDialog = new AlertDialog.Builder(this).setMessage("环境初始化中...\n\n").create();
        muskDialog.setCanceledOnTouchOutside(false);
        muskDialog.show();

        final Context fContext = this;
        navIntent = new Intent(fContext, NavigationActivity.class);
        navIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


        localPreInitCallback = new PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean isX5Core) {
                hasCreated = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        muskDialog.dismiss();
                        mProgressBar.setProgress(100);
                        AlertDialog alertDialog = new AlertDialog.Builder(fContext)
                                .setMessage("本地内核授权初始化完成：" + isX5Core)
                                .setPositiveButton("完成", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InstallationActivity.this.startActivity(navIntent);
                                    }
                                }).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                });
            }
        };

        boolean NEED_AUTO_SAVE = true;
        final SharedPreferences prefs = InstallationActivity.this.getSharedPreferences("app_prefs", MODE_PRIVATE);
        // 第一步
        // TbsFramework.setUp(this, YOUR_APP_LICENSE_KEY);
        // Demo在DemoApplication里已经设置了
        // 第二步
        boolean installResult = TbsFramework.installStaticX5(); // 同步安装静态X5，存在文件IO耗时，App可自行放子线程
        if (!prefs.getBoolean("tbsAuth", false)) { // #1
            TbsFramework.authenticateX5(NEED_AUTO_SAVE, new IAuthRequestCallback() {
                @Override
                public void onResponse(String license) {
                    // 授权成功，license为当前设备的授权码

                    prefs.edit().putBoolean("tbsAuth", true).apply();
                    // #1 saveYourAuthenticateFlag() App可以保留鉴权成功的Flag，下次启动就不用query了。
                    // 第三步：强制预初始化内核加载内核
                    QbSdk.preInit(InstallationActivity.this, localPreInitCallback);
                }

                @Override
                public void onFailed(int errCode, String msg) {
                    // 授权失败
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            muskDialog.dismiss();
                            mProgressBar.setProgress(100);
                            AlertDialog alertDialog = new AlertDialog.Builder(fContext)
                                    .setMessage("内核在线授权失败：" + errCode + ": " + msg)
                                    .setPositiveButton("完成", new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            InstallationActivity.this.startActivity(navIntent);
                                        }
                                    }).create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                        }
                    });
                }
            });
        } else {
            // 第三步：预初始化内核加载内核
            QbSdk.preInit(this, localPreInitCallback);
        }
    }

}
