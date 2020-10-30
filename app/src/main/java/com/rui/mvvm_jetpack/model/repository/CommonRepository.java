package com.rui.mvvm_jetpack.model.repository;

import androidx.lifecycle.LiveData;

import com.rui.mvvm_jetpack.R;
import com.rui.mvvm_jetpack.base.BaseApplication;
import com.rui.mvvm_jetpack.model.net.service.MainService;
import com.rui.mvvm_jetpack.model.net.RetrofitProvider;
import com.rui.mvvm_jetpack.model.bean.DisplayBean;

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
