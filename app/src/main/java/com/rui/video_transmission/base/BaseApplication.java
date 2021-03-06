package com.rui.video_transmission.base;

import android.app.Application;

import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;

public class BaseApplication extends Application implements CameraXConfig.Provider{
    private static BaseApplication mBaseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;
    }

    public static BaseApplication getInstance() {
        return mBaseApplication;
    }

    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}