package com.ando.download.demo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.dispatcher.CallbackDispatcher;
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher;
import com.liulishuo.okdownload.core.download.DownloadStrategy;
import com.liulishuo.okdownload.core.file.DownloadUriOutputStream;
import com.liulishuo.okdownload.core.file.ProcessFileStrategy;

import java.io.File;
import java.io.FilenameFilter;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

public class Utils {
    public static final String PARENT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aatest";
    public static final String PARENT_PATH2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/manytest";

    public static void launchOrInstallApp(Context context, String pkgName) {
        if (!TextUtils.isEmpty(pkgName)) {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            if (intent == null) {//如果未安装，则先安装
                installApk(context, new File(PARENT_PATH, pkgName));
            } else {//如果已安装，跳转到应用
                context.startActivity(intent);
            }
        } else {
            Toast.makeText(context, "包名为空！", Toast.LENGTH_SHORT).show();
            installApk(context, new File(PARENT_PATH, pkgName));
        }
    }

    //1、申请两个权限：WRITE_EXTERNAL_STORAGE 和 REQUEST_INSTALL_PACKAGES ；2、配置FileProvider
    public static void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            //【content://{$authority}/external/temp.apk】或【content://{$authority}/files/123/temp2.apk】
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//【file:///storage/emulated/0/temp.apk】
            uri = Uri.fromFile(file);
        }
        Log.i("123", "【Uri】" + uri);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static OkDownload buildOkDownload(Context context) {
        return new OkDownload.Builder(context.getApplicationContext())
                .downloadStore(Util.createDefaultDatabase(context)) //断点信息存储的位置，默认是SQLite数据库
                .callbackDispatcher(new CallbackDispatcher()) //监听回调分发器，默认在主线程回调
                .downloadDispatcher(new DownloadDispatcher()) //下载管理机制，最大下载任务数、同步异步执行下载任务的处理
                .connectionFactory(Util.createDefaultConnectionFactory()) //选择网络请求框架，默认是OkHttp
                .outputStreamFactory(new DownloadUriOutputStream.Factory()) //构建文件输出流DownloadOutputStream，是否支持随机位置写入
                .processFileStrategy(new ProcessFileStrategy()) //多文件写文件的方式，默认是根据每个线程写文件的不同位置，支持同时写入
                //.monitor(monitor); //下载状态监听
                .downloadStrategy(new DownloadStrategy())//下载策略，文件分为几个线程下载
                .build();
    }

    /**
     * 删除一个文件，或删除一个目录下的所有文件
     *
     * @param dirFile      要删除的目录，可以是一个文件
     * @param filter       对要删除的文件的匹配规则(不作用于目录)，如果要删除所有文件请设为 null
     * @param isDeleateDir 是否删除目录，false时只删除目录下的文件而不删除目录
     */
    public static void deleteFiles(File dirFile, FilenameFilter filter, boolean isDeleateDir) {
        if (dirFile.isDirectory()) {//是目录
            for (File file : dirFile.listFiles()) {
                deleteFiles(file, filter, isDeleateDir);//递归
            }
            if (isDeleateDir) {
                System.out.println("目录【" + dirFile.getAbsolutePath() + "】删除" + (dirFile.delete() ? "成功" : "失败"));//必须在删除文件后才能删除目录
            }
        } else if (dirFile.isFile()) {//是文件。注意 isDirectory 为 false 并非就等价于 isFile 为 true
            String symbol = isDeleateDir ? "\t" : "";
            if (filter == null || filter.accept(dirFile.getParentFile(), dirFile.getName())) {//是否满足匹配规则
                System.out.println(symbol + "- 文件【" + dirFile.getAbsolutePath() + "】删除" + (dirFile.delete() ? "成功" : "失败"));
            } else {
                System.out.println(symbol + "+ 文件【" + dirFile.getAbsolutePath() + "】不满足匹配规则，不删除");
            }
        } else {
            System.out.println("文件不存在");
        }
    }

    public static void dealEnd(Context context, ItemInfo itemInfo, @NonNull EndCause cause) {
        if (cause == EndCause.COMPLETED) {
            Toast.makeText(context, "任务完成", Toast.LENGTH_SHORT).show();
            itemInfo.status = 3; //修改状态
            Utils.launchOrInstallApp(context, itemInfo.pkgName);
        } else {
            itemInfo.status = 2; //修改状态
            if (cause == EndCause.CANCELED) {
                Toast.makeText(context, "任务取消", Toast.LENGTH_SHORT).show();
            } else if (cause == EndCause.ERROR) {
                Log.i("123", "【任务出错】");
            } else if (cause == EndCause.FILE_BUSY || cause == EndCause.SAME_TASK_BUSY || cause == EndCause.PRE_ALLOCATE_FAILED) {
                Log.i("123", "【taskEnd】" + cause.name());
            }
        }
    }
}