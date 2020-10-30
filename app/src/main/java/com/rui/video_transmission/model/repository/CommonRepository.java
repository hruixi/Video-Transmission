package com.rui.video_transmission.model.repository;

import androidx.lifecycle.LiveData;

import com.rui.video_transmission.R;
import com.rui.video_transmission.base.BaseApplication;
import com.rui.video_transmission.model.net.service.MainService;
import com.rui.video_transmission.model.net.RetrofitProvider;
import com.rui.video_transmission.model.bean.DisplayBean;

/**
 * @ClassName MainRepository
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/4/7 17:14
 */
public class CommonRepository {
    private MainService mService;

    public CommonRepository() {
        mService = RetrofitProvider.get(BaseApplication.getInstance().getResources().getString(R.string.base_url_xingsuying))
                .create(MainService.class);
    }

    public LiveData<DisplayBean> display() {
        return mService.display();
    }
}
