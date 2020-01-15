package com.ando.download.config;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: TaskParam
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/15  16:48
 */
public class TaskParam implements Parcelable {

    private String url;
    private String pkgName;
    private TaskStatus status;
    private long progress;

    public TaskParam(String url, String pkgName) {
        this.url = url;
        this.pkgName = pkgName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.pkgName);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeLong(this.progress);
    }

    protected TaskParam(Parcel in) {
        this.url = in.readString();
        this.pkgName = in.readString();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : TaskStatus.values()[tmpStatus];
        this.progress = in.readLong();
    }

    public static final Parcelable.Creator<TaskParam> CREATOR = new Parcelable.Creator<TaskParam>() {
        @Override
        public TaskParam createFromParcel(Parcel source) {
            return new TaskParam(source);
        }

        @Override
        public TaskParam[] newArray(int size) {
            return new TaskParam[size];
        }
    };
}