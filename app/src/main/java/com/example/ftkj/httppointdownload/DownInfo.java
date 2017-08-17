package com.example.ftkj.httppointdownload;

/**
 * Created by FTKJ on 2017/8/17.
 */

public class DownInfo {
    private long id;
    private long currentLenth;
    private long totleLenth;
    private String filePath;
    private String url;
    private DownloadListener mDownloadListener;

    public DownloadListener getDownloadListener() {
        return mDownloadListener;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCurrentLenth() {
        return currentLenth;
    }

    public void setCurrentLenth(long currentLenth) {
        this.currentLenth = currentLenth;
    }

    public long getTotleLenth() {
        return totleLenth;
    }

    public void setTotleLenth(long totleLenth) {
        this.totleLenth = totleLenth;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
