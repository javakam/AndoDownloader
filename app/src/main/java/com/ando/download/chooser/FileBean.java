package com.ando.download.chooser;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: FileBean
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/21  14:36
 */
public class FileBean implements Parcelable {

    private String id;
    private String name;
    private String path;
    private String mimeType;
    private String size;
    private String date;
    private String[] filterTypes;
    private boolean isSelected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getFilterTypes() {
        return filterTypes;
    }

    public void setFilterTypes(String[] filterTypes) {
        this.filterTypes = filterTypes;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.mimeType);
        dest.writeString(this.size);
        dest.writeString(this.date);
        dest.writeStringArray(this.filterTypes);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public FileBean() {
    }

    protected FileBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.path = in.readString();
        this.mimeType = in.readString();
        this.size = in.readString();
        this.date = in.readString();
        this.filterTypes = in.createStringArray();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<FileBean> CREATOR = new Parcelable.Creator<FileBean>() {
        @Override
        public FileBean createFromParcel(Parcel source) {
            return new FileBean(source);
        }

        @Override
        public FileBean[] newArray(int size) {
            return new FileBean[size];
        }
    };
}