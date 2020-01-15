package com.ando.download;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDownloadListener4WithSpeed extends DownloadListener4WithSpeed {
    private ItemInfo itemInfo;
    private long totalLength;
    private String readableTotalLength;
    private ProgressBar progressBar;//谨防内存泄漏
    private Context context;//谨防内存泄漏

    public MyDownloadListener4WithSpeed(ItemInfo itemInfo, ProgressBar progressBar) {
        this.itemInfo = itemInfo;
        this.progressBar = progressBar;
        context = progressBar.getContext();
    }

    @Override
    public void taskStart(@NonNull DownloadTask task) {
        Log.i("123", "【1、taskStart】");
    }

    @Override
    public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info, boolean fromBreakpoint, @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {
        totalLength = info.getTotalLength();
        readableTotalLength = Util.humanReadableBytes(totalLength, true);
        Log.i("123", "【2、infoReady】当前进度" + (float) info.getTotalOffset() / totalLength * 100 + "%" + "，" + info.toString());
        progressBar.setMax((int) totalLength);
    }

    @Override
    public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaders) {
        Log.i("123", "【3、connectStart】" + blockIndex);
    }

    @Override
    public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaders) {
        Log.i("123", "【4、connectEnd】" + blockIndex + "，" + responseCode);
    }

    @Override
    public void progressBlock(@NonNull DownloadTask task, int blockIndex, long currentBlockOffset, @NonNull SpeedCalculator blockSpeed) {
        //Log.i("123", "【5、progressBlock】" + blockIndex + "，" + currentBlockOffset);
    }

    @Override
    public void progress(@NonNull DownloadTask task, long currentOffset, @NonNull SpeedCalculator taskSpeed) {
        String readableOffset = Util.humanReadableBytes(currentOffset, true);
        String progressStatus = readableOffset + "/" + readableTotalLength;
        String speed = taskSpeed.speed();
        float percent = (float) currentOffset / totalLength * 100;
        Log.i("123", "【6、progress】" + currentOffset + "[" + progressStatus + "]，速度：" + speed + "，进度：" + percent + "%");
        progressBar.setProgress((int) currentOffset);
    }

    @Override
    public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info, @NonNull SpeedCalculator blockSpeed) {
        Log.i("123", "【7、blockEnd】" + blockIndex);
    }

    @Override
    public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {
        Log.i("123", "【8、taskEnd】" + cause.name() + "：" + (realCause != null ? realCause.getMessage() : "无异常"));
        Utils.dealEnd(context, itemInfo, cause);
    }
}