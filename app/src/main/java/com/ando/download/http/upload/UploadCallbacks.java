package com.ando.download.http.upload;

public interface UploadCallbacks {
    void onProgressUpdate(int percentage);

    void onError();

    void onFinish();
}