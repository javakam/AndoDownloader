package com.ando.download;

/**
 * Title: ItemInfo
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/15  14:50
 */
public class ItemInfo {
    String url;
    String pkgName; //包名
    int status;  // 0：没有下载 1：下载中 2：暂停 3：完成

    public ItemInfo(String url, String pkgName) {
        this.url = url;
        this.pkgName = pkgName;
    }
}