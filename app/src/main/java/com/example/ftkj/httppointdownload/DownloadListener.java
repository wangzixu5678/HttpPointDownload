package com.example.ftkj.httppointdownload;

/**
 * Created by FTKJ on 2017/8/17.
 */

public interface DownloadListener {

    void update(int procent);

    void stop();

    void pause();
}
