package com.ando.download.config;

/**
 * Title: TaskStatus
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/15  16:32
 */
public enum TaskStatus {

    /**
     *
     */
    IDLE(0, "未下载"),
    DOING(1, "下载中"),
    SUSPEND(2, "暂停"),
    COMPLETE(3, "完成");


    public int status;
    public String text;

    TaskStatus(int status, String text) {
        this.status = status;
        this.text = text;
    }

}