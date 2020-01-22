package com.ando.download;

import android.app.Application;

/**
 * Title: App
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/22  16:09
 */
public class App extends Application {

    private static App ins = null;

    @Override
    public void onCreate() {
        super.onCreate();
        ins = this;
    }

    public static App getApp() {
        return ins;
    }

}