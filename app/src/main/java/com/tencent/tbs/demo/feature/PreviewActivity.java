package com.tencent.tbs.demo.feature;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.tencent.tbs.demo.R;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.tbs.demo.utils.FileUtil;

import java.io.File;

public class PreviewActivity extends AppCompatActivity {

    private RelativeLayout mFlRoot;
    private File mFile;
    private TbsReaderView mTbsReaderView;

    public static void start(Context context, String fileUrl) {
        context = context.getApplicationContext();
        Intent starter = new Intent(context, PreviewActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        starter.putExtra("fileUrl", fileUrl);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        String fileUrl = getIntent().getStringExtra("fileUrl");
        mFile = new File(fileUrl);

        initView();
        addTbsReaderView();
    }

    private void initView() {
        mFlRoot = findViewById(R.id.fl_container);
    }

    private void addTbsReaderView() {
        //1、设置回调
        TbsReaderView.ReaderCallback readerCallback = new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {
                //回调结果参考 TbsReaderView.ReaderCallback
            }
        };
        //2、创建TbsReaderView
        mTbsReaderView = new TbsReaderView(this, readerCallback);

        //3、将TbsReaderView 添加到RootLayout中（可添加到自定义标题栏的下方）
        mFlRoot.addView(
                mTbsReaderView,
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        //4、传入指定参数
        Bundle bundle = new Bundle();
        bundle.putString(TbsReaderView.KEY_FILE_PATH, mFile.getPath());
        bundle.putString(TbsReaderView.KEY_TEMP_PATH, getExternalFilesDir("temp").getAbsolutePath());
        String extensionName = FileUtil.getFileType(mFile.getPath());

        //5、调用preOpen判断是否支持当前文件类型 （若tbs支持的文档类型返回false，则说明内核未加载成功）
        boolean result = mTbsReaderView.preOpen(extensionName, false);
        if (result) {
        //6、调用openFile打开文件
            mTbsReaderView.openFile(bundle);
        }
    }

    @Override
    public void onDestroy() {
        //7、结束时一定调用onStop方法
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
        super.onDestroy();
    }
}