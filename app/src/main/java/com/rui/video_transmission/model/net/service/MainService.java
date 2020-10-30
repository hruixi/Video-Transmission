package com.rui.video_transmission.model.net.service;

import androidx.lifecycle.LiveData;

import com.rui.video_transmission.model.bean.DisplayBean;

import retrofit2.http.GET;

public interface MainService {
    @GET("loanapp/display")
    LiveData<DisplayBean> display();

}
