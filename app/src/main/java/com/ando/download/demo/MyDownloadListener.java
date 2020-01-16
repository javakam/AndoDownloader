package com.ando.download.demo;

import android.widget.ProgressBar;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener3;

import androidx.annotation.NonNull;

/**
 * Title: $
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/15  15:02
 */
public class MyDownloadListener extends DownloadListener3 {
    public MyDownloadListener(ItemInfo itemInfo, ProgressBar progressBar) {

    }

    @Override
    protected void started(@NonNull DownloadTask task) {

    }

    @Override
    protected void completed(@NonNull DownloadTask task) {

    }

    @Override
    protected void canceled(@NonNull DownloadTask task) {

    }

    @Override
    protected void error(@NonNull DownloadTask task, @NonNull Exception e) {

    }

    @Override
    protected void warn(@NonNull DownloadTask task) {

    }

    @Override
    public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

    }

    @Override
    public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {

    }

    @Override
    public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {

    }
}