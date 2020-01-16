package com.ando.download.config;

/**
 * Title: TaskBean
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/16  16:10
 */
public class TaskBean {

    private String tname;
    private String url;
    private String parentFile;

    public TaskBean(String tname, String url) {
        this.tname = tname;
        this.url = url;
    }

    public TaskBean(String tname, String url, String parentFile) {
        this.tname = tname;
        this.url = url;
        this.parentFile = parentFile;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParentFile() {
        return parentFile;
    }

    public void setParentFile(String parentFile) {
        this.parentFile = parentFile;
    }
}