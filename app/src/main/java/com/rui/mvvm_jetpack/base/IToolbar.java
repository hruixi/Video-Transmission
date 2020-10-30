package com.rui.mvvm_jetpack.base;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

public interface IToolbar {
    //设置toolbar的icon
    Drawable setToolbarIcon(@DrawableRes int iconId);

    //设置toolbar的title
    String setToolbarTitle();

    //设置是否显示icon
    boolean isShowToolbarIcon();
}
