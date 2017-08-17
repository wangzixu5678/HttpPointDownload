package com.example.ftkj.httppointdownload;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {

    private DownLoadManager mDownLoadManager;
    private ProgressBar mProgressbar;
    private TextView mText;
    private boolean isstop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressbar = (ProgressBar) findViewById(R.id.main_progressbar);
        mProgressbar.setMax(100);
        mText = (TextView) findViewById(R.id.main_text);
        mText.setText("快来下载我吧");
        mDownLoadManager = DownLoadManager.getNewInstance(this);
        DownInfo downInfo = new DownInfo();
        initDownloadInfo(downInfo);


    }

    public void btnDown(View view) {
        if (!isstop) {
            mDownLoadManager.start();
        }else {
            Toast.makeText(this, "已经下载完成", Toast.LENGTH_SHORT).show();
        }
    }

    private void initDownloadInfo(DownInfo downInfo) {
        downInfo.setCurrentLenth(0);
        downInfo.setFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Wechat.apk");
        downInfo.setUrl("http://dldir1.qq.com/weixin/android/weixin6330android920.apk");
        downInfo.setId(1);
        downInfo.setDownloadListener(new DownloadListener() {
            @Override
            public void update(final int procent) {
                mProgressbar.setProgress(procent);
                Log.d("AABB", "update: "+procent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mText.setText(String.valueOf(procent));
                    }
                });

            }

            @Override
            public void stop() {
                isstop = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mText.setText("结束");
                    }
                });

            }

            @Override
            public void pause() {

                mText.setText("暂停下载");
            }
        });

        mDownLoadManager.setDownloadInfo(downInfo);
    }

    public void PauseDown(View view) {
        mDownLoadManager.pause();
    }



}
