package com.ando.download.many;

import android.content.Context;
import android.widget.ProgressBar;

import java.io.File;

import androidx.annotation.NonNull;

/**
 * Title: DownloadFileHelper
 * <p>
 * Description:  处理下载相关的文件
 * </p>
 *
 * @author Changbao
 * @date 2020/1/17  16:48
 */
public class DownloadFileHelper {

    public static void calcProgressToView(ProgressBar progressBar, long offset, long total) {
        final float percent = (float) offset / total;
        progressBar.setProgress((int) (percent * progressBar.getMax()));
    }

    public static File getParentFile(@NonNull Context context) {
        final File externalSaveDir = context.getExternalCacheDir();
        if (externalSaveDir == null) {
            return context.getCacheDir();
        } else {
            return externalSaveDir;
        }
    }

}