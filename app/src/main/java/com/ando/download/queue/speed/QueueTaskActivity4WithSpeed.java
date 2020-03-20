package com.ando.download.queue.speed;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.ando.download.R;
import com.ando.download.TempData;
import com.ando.download.demo.Utils;
import com.liulishuo.okdownload.DownloadContext;
import com.liulishuo.okdownload.DownloadContextListener;
import com.liulishuo.okdownload.DownloadMonitor;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Title: QueueTaskActivity4WithSpeed
 * <p>
 * Description: 带有下载速度
 * </p>
 *
 * @author Changbao
 * @date 2020/1/15  16:37
 */
public class QueueTaskActivity4WithSpeed extends AppCompatActivity {

    public static final String DOWNLOAD_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/manytest4speed";


    private Button mBtnDelete;
    private Button mTvAction;

    private DownloadView mDownLoadView;
    private QueueController4WithSpeed controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
        new File(DOWNLOAD_FILE_PATH).mkdirs();

        setContentView(R.layout.activity_task_many);

        mBtnDelete = findViewById(R.id.btn_delete_files);
        mTvAction = findViewById(R.id.tv_tasks_start);

        mDownLoadView = findViewById(R.id.download_view);
        controller = mDownLoadView.getController();

        initData();
        initAction();

    }

    private void initData() {
        //全局配置
        Util.enableConsoleLog();//okcat
        DownloadDispatcher.setMaxParallelRunningCount(2);
        OkDownload.with().setMonitor(new DownloadMonitor() {
            @Override
            public void taskStart(DownloadTask task) {
            }

            @Override
            public void taskDownloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {

            }

            @Override
            public void taskDownloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @Nullable ResumeFailedCause cause) {

            }

            @Override
            public void taskEnd(DownloadTask task, EndCause cause, @Nullable Exception realCause) {

            }
        });

        //RemitStoreOnSQLite.setRemitToDBDelayMillis(500);
        //OkDownload.with().setMonitor(monitor);
        //OkDownload.with().downloadDispatcher().cancelAll();
        //OkDownload.with().breakpointStore().remove(taskId);

        mDownLoadView.setData(TempData.getTaskBeans2(Utils.PARENT_PATH2), new DownloadContextListener() {
            @Override
            public void taskEnd(@NonNull DownloadContext context, @NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, int remainCount) {
            }

            @Override
            public void queueEnd(@NonNull DownloadContext context) {
                mTvAction.setText(R.string.start);
                // to cancel
                controller.stop();

                mBtnDelete.setEnabled(true);

                mDownLoadView.notifyDataSetChanged();
            }
        });

    }

    private void initAction() {
        //全部删除
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller != null) {
                    controller.deleteFiles();
                }
                mDownLoadView.notifyDataSetChanged();
            }
        });

        mTvAction.setText(R.string.start);

        //全部开始,全部暂停
        mTvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean started = (v.getTag() != null);
                if (started) {
                    if (controller != null) {
                        controller.stop();
                    }
                    v.setTag(null);
                } else {
                    v.setTag(true);

                    mTvAction.setText(R.string.cancel);

                    // to start
                    if (controller != null) {
                        controller.start(false);
                    }
                    mDownLoadView.notifyDataSetChanged();

                    mBtnDelete.setEnabled(false);


                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkDownload.with().downloadDispatcher().cancelAll();

        //如果指定任务集合
//        for (String key : map.keySet()) {
//            DownloadTask task = map.get(key);
//            if (task != null) {
//                task.cancel();
//            }
//        }

    }

    /**
     * 单文件下载
     */
//    private DownloadTask createDownloadTask(TaskParam taskParam) {
//        return new DownloadTask.Builder(taskParam.getUrl(), new File(Utils.PARENT_PATH)) //设置下载地址和下载目录，这两个是必须的参数
//                .setFilename(taskParam.getPkgName())//设置下载文件名，没提供的话先看 response header ，再看 url path(即启用下面那项配置)
//                .setFilenameFromResponse(false)//是否使用 response header or url path 作为文件名，此时会忽略指定的文件名，默认false
//                .setPassIfAlreadyCompleted(true)//如果文件已经下载完成，再次下载时，是否忽略下载，默认为true(忽略)，设为false会从头下载
//                .setConnectionCount(1)  //需要用几个线程来下载文件，默认根据文件大小确定；如果文件已经 split block，则设置后无效
//                .setPreAllocateLength(false) //在获取资源长度后，设置是否需要为文件预分配长度，默认false
//                .setMinIntervalMillisCallbackProcess(100) //通知调用者的频率，避免anr，默认3000
//                .setWifiRequired(false)//是否只允许wifi下载，默认为false
//                .setAutoCallbackToUIThread(true) //是否在主线程通知调用者，默认为true
//                //.setHeaderMapFields(new HashMap<String, List<String>>())//设置请求头
//                //.addHeader(String key, String value)//追加请求头
//                .setPriority(0)//设置优先级，默认值是0，值越大下载优先级越高
//                .setReadBufferSize(4096)//设置读取缓存区大小，默认4096
//                .setFlushBufferSize(16384)//设置写入缓存区大小，默认16384
//                .setSyncBufferSize(65536)//写入到文件的缓冲区大小，默认65536
//                .setSyncBufferIntervalMillis(2000) //写入文件的最小时间间隔，默认2000
//                .build();
//    }

//    private DownloadListener createDownloadListener(int position) {
//        switch (position) {
//            case 0:
//                return new ManyDownloadListener4WithSpeed(list.get(position), progressBar);
//                break;
//            default:
//        }
//    }
    private void downloadDemoOriginal() {


//        DownloadTask.Builder    task = new DownloadTask.Builder(url, parentFile)
//                .setFilename(filename)
//                // the minimal interval millisecond for callback progress
//                .setMinIntervalMillisCallbackProcess(30)
//                // do re-download even if the task has already been completed in the past.
//                .setPassIfAlreadyCompleted(false)
//                .build();
//
//        task.enqueue(listener);
//        task.cancel();
//        // execute task synchronized
//        task.execute(listener);
//
//        // 此方法专门针对一堆任务进行了优化
//        DownloadTask.enqueue(tasks, listener);
//        // 取消，此方法也专门针对一堆任务进行优化
//        DownloadTask.cancel(tasks);

//
//        DownloadContext.Builder builder = new DownloadContext.QueueSet()
//                .setParentPathFile(parentFile)
//                .setMinIntervalMillisCallbackProcess(150)
//                .commit();
//        builder.bind(url1);
//        builder.bind(url2).addTag(key, value);
//        builder.bind(url3).setTag(tag);
//        builder.setListener(contextListener);
//
//        DownloadTask task = new DownloadTask.Builder(url4, parentFile)
//                .setPriority(10).build();
//        builder.bindSetTask(task);
//
//        DownloadContext context = builder.build();
//        context.startOnParallel(listener);
//        // stop
//        context.stop();


//        StatusUtil.Status status = StatusUtil.getStatus(task);
//        status = StatusUtil.getStatus(url, parentPath, null);
//        status = StatusUtil.getStatus(url, parentPath, filename);
//
//        boolean isCompleted = StatusUtil.isCompleted(task);
//        isCompleted = StatusUtil.isCompleted(url, parentPath, null);
//        isCompleted = StatusUtil.isCompleted(url, parentPath, filename);
//
//        StatusUtil.Status completedOrUnknown = StatusUtil.isCompletedOrUnknown(task);
//
//        // If you set tag, so just get tag
//        task.getTag();
//        task.getTag(xxx);

    }

}