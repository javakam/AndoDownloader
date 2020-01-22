package com.ando.download.chooser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.ando.download.App;

import java.io.File;

import androidx.core.content.FileProvider;

/**
 * 打开文件
 * <p>
 * https://www.cnblogs.com/kangweifeng/p/11264217.html
 */
public class OpenFileUtils {

    private static final String fileProvider = ".fileProvider";

    /**
     * 声明各种类型文件的dataType
     **/
    private static final String DATA_TYPE_APK = "application/vnd.android.package-archive";
    private static final String DATA_TYPE_VIDEO = "video/*";
    private static final String DATA_TYPE_AUDIO = "audio/*";
    private static final String DATA_TYPE_HTML = "text/html";
    private static final String DATA_TYPE_IMAGE = "image/*";
    private static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    private static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
    private static final String DATA_TYPE_WORD = "application/msword";
    private static final String DATA_TYPE_CHM = "application/x-chm";
    private static final String DATA_TYPE_TXT = "text/plain";
    private static final String DATA_TYPE_PDF = "application/pdf";
    /**
     * 未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
     */
    private static final String DATA_TYPE_ALL = "*/*";


    public static Intent openFile(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        /* 取得扩展名 */
        final String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        switch (end) {
            case "m4a":
            case "mp3":
            case "mid":
            case "xmf":
            case "ogg":
            case "wav":
                return getAudioFileIntent(filePath);
            case "3gp":
            case "mp4":
            case "m3u8":
            case "avi":
                return getVideoFileIntent(filePath);
            case "jpg":
            case "gif":
            case "png":
            case "jpeg":
            case "bmp":
                return getImageFileIntent(filePath);
            case "apk":
                return getApkFileIntent(filePath);
            case "ppt":
            case "pptx":
                return getPptFileIntent(filePath);
            case "xls":
                return getExcelFileIntent(filePath);
            case "doc":
            case "docx":
                return getWordFileIntent(filePath);
            case "pdf":
                return getPdfFileIntent(filePath);
            case "chm":
                return getChmFileIntent(filePath);
            case "txt":
                return getTextFileIntent(filePath, false);
            case "html":
            case "htm":
            case "md":
                return getHtmlFileIntent(filePath);
            default:
                return getAllIntent(filePath);
        }
    }

    private static Uri getUri(String path) {
        return getUri(App.getApp(), path);
    }

    private static Uri getUri(Context context, String path) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = context.getPackageName() + fileProvider;
            uri = FileProvider.getUriForFile(context, authority, new File(path));
        } else {
            uri = Uri.fromFile(new File(path));
        }
        return uri;
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, DATA_TYPE_ALL);
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, DATA_TYPE_APK);
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, DATA_TYPE_VIDEO);
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, DATA_TYPE_AUDIO);
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, DATA_TYPE_HTML);
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, DATA_TYPE_IMAGE);
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, DATA_TYPE_PPT);
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = getUri(param);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, DATA_TYPE_EXCEL);
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = getUri(param);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, DATA_TYPE_WORD);
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, DATA_TYPE_CHM);
        return intent;
    }

    //Android获取一个用于打开文本文件TXT的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = getUri(param);
            intent.setDataAndType(uri1, DATA_TYPE_TXT);
        } else {
            Uri uri2 = getUri(param);
            intent.setDataAndType(uri2, DATA_TYPE_TXT);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, DATA_TYPE_PDF);
        return intent;
    }

}