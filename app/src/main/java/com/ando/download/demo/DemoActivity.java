package com.ando.download.demo;

import android.Manifest;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.core.app.ActivityCompat;

/**
 * Title:MainActivity
 * <pre>
 *      https://github.com/lingochamp/okdownload/wiki/Simple-Use-Guideline
 *
 *      https://www.cnblogs.com/baiqiantao/p/10679677.html
 * </pre>
 *
 * @author Changbao
 * @date 2020/1/15 14:48
 */
public class DemoActivity extends ListActivity {


    static final String URL1 = "https://dldir1.qq.com/foxmail/work_weixin/wxwork_android_2.4.5.5571_100001.apk";
    static final String URL2 = "https://cdn.llscdn.com/yy/files/xs8qmxn8-lls-LLS-5.8-800-20171207-111607.apk";
    static final String URL3 = "https://downapp.baidu.com/appsearch/AndroidPhone/1.0.78.155/1/1012271b/20190404124002/appsearch_AndroidPhone_1-0-78-155_1012271b.apk";

    ProgressBar progressBar;
    List<ItemInfo> list;
    HashMap<String, DownloadTask> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);

        String[] array = {"使用DownloadListener4WithSpeed",
                "使用DownloadListener3",
                "使用DownloadListener2",
                "使用DownloadListener3",
                "使用DownloadListener",
                "=====删除下载的文件，并重新启动Activity=====",
                "查看任务1的状态",
                "查看任务2的状态",
                "查看任务3的状态",
                "查看任务4的状态",
                "查看任务5的状态",};
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array));
        list = Arrays.asList(new ItemInfo(URL1, "com.ando.download"),
                new ItemInfo(URL1, "哎"),
                new ItemInfo(URL2, "英语流利说"),
                new ItemInfo(URL2, "百度手机助手"),
                new ItemInfo(URL3, "哎哎哎"));

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(false);
        getListView().addFooterView(progressBar);

        new File(Utils.PARENT_PATH).mkdirs();
        //OkDownload.setSingletonInstance(Utils.buildOkDownload(getApplicationContext()));//注意只能执行一次，否则报错
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //OkDownload.with().downloadDispatcher().cancelAll();
        for (String key : map.keySet()) {
            DownloadTask task = map.get(key);
            if (task != null) {
                task.cancel();
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                download(position);
                break;
            case 5:
                Utils.deleteFiles(new File(Utils.PARENT_PATH), null, false);
                recreate();
                break;
            default:
                ItemInfo itemInfo = list.get(position - 6);
                DownloadTask task = map.get(itemInfo.pkgName);
                if (task != null) {
                    Toast.makeText(this, "状态为：" + StatusUtil.getStatus(task).name(), Toast.LENGTH_SHORT).show();
                }

                BreakpointInfo info = StatusUtil.getCurrentInfo(itemInfo.url, Utils.PARENT_PATH, itemInfo.pkgName);
                //BreakpointInfo info = StatusUtil.getCurrentInfo(task);
                if (info != null) {
                    float percent = (float) info.getTotalOffset() / info.getTotalLength() * 100;
                    Log.i("123", "【当前进度】" + percent + "%");
                    progressBar.setMax((int) info.getTotalLength());
                    progressBar.setProgress((int) info.getTotalOffset());
                } else {
                    Log.i("123", "【任务不存在】");
                }
                break;
        }
    }

    private void download(int position) {
        ItemInfo itemInfo = list.get(position);
        DownloadTask task = map.get(itemInfo.pkgName);
        // 0：没有下载  1：下载中  2：暂停  3：完成
        if (itemInfo.status == 0) {
            if (task == null) {
                task = createDownloadTask(itemInfo);
                map.put(itemInfo.pkgName, task);
            }
            task.enqueue(createDownloadListener(position));
            itemInfo.status = 1; //更改状态
            Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
        } else if (itemInfo.status == 1) {//下载中
            if (task != null) {
                task.cancel();
            }
            itemInfo.status = 2;
            Toast.makeText(this, "暂停下载", Toast.LENGTH_SHORT).show();
        } else if (itemInfo.status == 2) {
            if (task != null) {
                task.enqueue(createDownloadListener(position));
            }
            itemInfo.status = 1;
            Toast.makeText(this, "继续下载", Toast.LENGTH_SHORT).show();
        } else if (itemInfo.status == 3) {//下载完成的，直接跳转安装APP
            Utils.launchOrInstallApp(this, itemInfo.pkgName);
            Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
        }
    }

    private DownloadTask createDownloadTask(ItemInfo itemInfo) {
        return new DownloadTask.Builder(itemInfo.url, new File(Utils.PARENT_PATH)) //设置下载地址和下载目录，这两个是必须的参数
                .setFilename(itemInfo.pkgName)//设置下载文件名，没提供的话先看 response header ，再看 url path(即启用下面那项配置)
                .setFilenameFromResponse(false)//是否使用 response header or url path 作为文件名，此时会忽略指定的文件名，默认false
                .setPassIfAlreadyCompleted(true)//如果文件已经下载完成，再次下载时，是否忽略下载，默认为true(忽略)，设为false会从头下载
                .setConnectionCount(1)  //需要用几个线程来下载文件，默认根据文件大小确定；如果文件已经 split block，则设置后无效
                .setPreAllocateLength(false) //在获取资源长度后，设置是否需要为文件预分配长度，默认false
                .setMinIntervalMillisCallbackProcess(100) //通知调用者的频率，避免anr，默认3000
                .setWifiRequired(false)//是否只允许wifi下载，默认为false
                .setAutoCallbackToUIThread(true) //是否在主线程通知调用者，默认为true
                //.setHeaderMapFields(new HashMap<String, List<String>>())//设置请求头
                //.addHeader(String key, String value)//追加请求头
                .setPriority(0)//设置优先级，默认值是0，值越大下载优先级越高
                .setReadBufferSize(4096)//设置读取缓存区大小，默认4096
                .setFlushBufferSize(16384)//设置写入缓存区大小，默认16384
                .setSyncBufferSize(65536)//写入到文件的缓冲区大小，默认65536
                .setSyncBufferIntervalMillis(2000) //写入文件的最小时间间隔，默认2000
                .build();
    }

    private DownloadListener createDownloadListener(int position) {
        switch (position) {
            case 0:
                return new MyDownloadListener4WithSpeed(list.get(position), progressBar);
            case 1:
                return new MyDownloadListener3(list.get(position), progressBar);
            case 2:
                return new MyDownloadListener2(list.get(position), progressBar);
            case 3:
                return new MyDownloadListener1(list.get(position), progressBar);
            default:
                return new MyDownloadListener(list.get(position), progressBar);
        }
    }

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
//        // This method is optimize specially for bunch of tasks
//        DownloadTask.enqueue(tasks, listener);
//        // cancel, this method is also optmize specially for bunch of tasks
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