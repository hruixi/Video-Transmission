package com.rui.mvvm_jetpack.model.net.service;

import androidx.lifecycle.LiveData;

import com.rui.mvvm_jetpack.model.bean.DisplayBean;

import retrofit2.http.GET;

public interface MainService {
    @GET("loanapp/display")
    LiveData<DisplayBean> display();

}
