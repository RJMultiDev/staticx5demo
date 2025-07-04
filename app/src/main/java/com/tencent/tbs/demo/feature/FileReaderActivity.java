package com.tencent.tbs.demo.feature;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.tbs.demo.R;
import com.tencent.tbs.demo.utils.FileUtil;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

public class FileReaderActivity extends AppCompatActivity {

    private final String TAG = "FileReaderActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        copyAssetsToSDCard();
        new AlertDialog.Builder(this).setTitle("FileReader")
                .setMessage("FileReader以dialog的方式打开文件，无需创建Layout，标题栏使用sdk默认实现。"
                        + "Demo在Assets中静态加入样板文件，运行时会拷贝到App的sdcard files/docs 目录，开发者可直接参考Activity中"
                        + "openFileReader方法实现，传入时必须是可读的Android绝对路径")
                .show();

        setContentView(R.layout.activity_file_reader);

        findViewById(R.id.exit).setOnClickListener(v->finish());
        findViewById(R.id.btn_open_pdf_reader).setOnClickListener(
                view -> openExternalFilesDirDocument("TBSFileTest.pdf"));
        findViewById(R.id.btn_open_doc_reader).setOnClickListener(
                view -> openExternalFilesDirDocument("TBSFileTest.doc"));
        findViewById(R.id.btn_open_docx_reader).setOnClickListener(
                view -> openExternalFilesDirDocument("TBSFileTest.docx"));
        findViewById(R.id.btn_open_ppt_reader).setOnClickListener(
                view -> openExternalFilesDirDocument("TBSFileTest.ppt"));
        findViewById(R.id.btn_open_pptx_reader).setOnClickListener(
                view -> openExternalFilesDirDocument("TBSFileTest.pptx"));
        findViewById(R.id.btn_open_xls_reader).setOnClickListener(
                view -> openExternalFilesDirDocument("TBSFileTest.xls"));
        findViewById(R.id.btn_open_xlsx_reader).setOnClickListener(
                view -> openExternalFilesDirDocument("TBSFileTest.xlsx"));
        findViewById(R.id.btn_open_txt_reader).setOnClickListener(
                view -> openExternalFilesDirDocument("TBSFileTest.txt"));
        findViewById(R.id.btn_open_epub_reader).setOnClickListener(
                view -> openExternalFilesDirDocument("TBSFileTest.epub"));
    }

    private void copyAssetsToSDCard() {
        AssetManager assetManager = this.getAssets();
        InputStream is = null;
        try {
            String[] fileNames = assetManager.list("docs");
            Log.i(TAG, "Documents file list: " + fileNames);
            for (String name: fileNames) {
                is = assetManager.open("docs/" + name);
                FileUtil.writeBytesToFile(is, new File(this.getExternalFilesDir("docs"), name));
                if (is != null) {
                    is.close();
                    is = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e(TAG, "copy complete!");
    }

    private void openExternalFilesDirDocument(String fileName) {
        openFileReader(FileReaderActivity.this.getExternalFilesDir("docs") + "/" + fileName);
    }

    private void openFileReader(String filePath) {
        Log.e(TAG, "openFileReader: " + filePath);
        HashMap<String, String> params =  new HashMap<String, String>();
        params.put("style", "1");
        params.put("local", "true");
        QbSdk.openFileReader(this, filePath, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.e(TAG, "OpenFile Callback: " + s);
                if ("openFileReader open in QB".equals(s)
                        || "filepath error".equals(s)
                        || "TbsReaderDialogClosed".equals(s)
                        || "fileReaderClosed".equals(s)) {
                    QbSdk.closeFileReader(FileReaderActivity.this);
                }
            }
        });

    }
}
