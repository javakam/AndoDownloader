package com.ando.download.http.upload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    /**
     * 文件上传
     */
    @Multipart
    @POST("v1/upload")
    Call<Object> uploadFile(@Part MultipartBody.Part file);

}