package com.example.ftkj.httppointdownload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by FTKJ on 2017/8/17.
 */

public class DownLoadManager {
    private static DownLoadManager sDownLoadManager;
    private static Context mContext;
    private DownInfo mDownInfo;
    private HttpURLConnection mConnection;
    private long totleCount = 0;
    private boolean isfirst = true;
    private boolean isPause = true;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private DownLoadManager() {

    }

    static DownLoadManager getNewInstance(Context context) {
        mContext = context;
        if (sDownLoadManager == null) {
            synchronized (DownLoadManager.class) {
                if (sDownLoadManager == null) {
                    sDownLoadManager = new DownLoadManager();
                }
            }
        }
        return sDownLoadManager;
    }


    public void setDownloadInfo(DownInfo downloadInfo) {
        mDownInfo = downloadInfo;
    }


    public void start() {
        Toast.makeText(mContext, "" + "开始下载", Toast.LENGTH_SHORT).show();
        isPause = false;

        Thread downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                RandomAccessFile ra = null;
                InputStream inputStream = null;
                try {
                    mConnection = (HttpURLConnection) new URL(mDownInfo.getUrl()).openConnection();
                    mConnection.setRequestMethod("GET");
                    mConnection.setRequestProperty("RANGE", "bytes=" + mDownInfo.getCurrentLenth() + "-");
                    mConnection.setDoInput(true);
                    mConnection.setDoOutput(true);
                    mConnection.connect();
                    Log.d("AAA", "goDownLoad: " + mConnection.getResponseCode() + "====" + mDownInfo.getCurrentLenth());
                    if (mConnection.getResponseCode() == 206) {
                        if (isfirst) {
                            isfirst = false;
                            mDownInfo.setTotleLenth(mConnection.getContentLength());
                        }
                        ra = new RandomAccessFile(new File(mDownInfo.getFilePath()), "rw");
                        ra.seek(mDownInfo.getCurrentLenth());
                        inputStream = mConnection.getInputStream();
                        int len;
                        byte[] bytes = new byte[1024 * 8];
                        while ((len = inputStream.read(bytes)) != -1 && !isPause) {
                            ra.write(bytes, 0, len);
                            totleCount = totleCount + len;
                            Log.d("BBB", "run: " + totleCount);
                            update();
                        }
                    } else {
                        Log.d("AAA", "run: " + mConnection.getResponseCode());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ra != null) {
                        try {
                            ra.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        downloadThread.start();


    }


    private void update() {
        int procent = (int) ((totleCount * 1.0) / mDownInfo.getTotleLenth() * 100);

        Log.d("AAA", "update: " + totleCount + "========" + mDownInfo.getTotleLenth() + "===" + procent);

        mDownInfo.getDownloadListener().update(procent);
        if (totleCount == mDownInfo.getTotleLenth()) {
            stop();
            mDownInfo.getDownloadListener().stop();
        }

    }


    void pause() {
        isPause = true;

        mDownInfo.setCurrentLenth(totleCount);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mConnection.disconnect();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownInfo.getDownloadListener().pause();
                    }
                });

            }
        }).start();

    }

    private void stop() {
        totleCount = 0;
        isPause = true;
        isfirst = true;
    }


}
