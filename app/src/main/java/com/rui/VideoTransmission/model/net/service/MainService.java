package com.rui.VideoTransmission.model.net.service;

import androidx.lifecycle.LiveData;

import com.rui.VideoTransmission.model.bean.DisplayBean;

import retrofit2.http.GET;

public interface MainService {
    @GET("loanapp/display")
    LiveData<DisplayBean> display();

}
