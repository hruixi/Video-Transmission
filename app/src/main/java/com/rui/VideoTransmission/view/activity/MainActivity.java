package com.rui.VideoTransmission.view.activity;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.rui.VideoTransmission.base.BaseActivity;
import com.rui.VideoTransmission.viewmodel.Common_vm;
import com.rui.VideoTransmission.databinding.ActivityMainBinding;

/**
 * @ClassName MainActivity
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/4/7 16:14
 */
public class MainActivity extends BaseActivity {
    Common_vm mMainViewModel;
    private ActivityMainBinding mBinding;

    @Override
    public void initViewBinding() {
        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this), mHostLayout, true);
    }

    @Override
    public void initData() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.e("rui", "displayMetrics: " + displayMetrics);

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        Log.e("rui", "dm: " + dm);
    }

    @Override
    public String setToolbarTitle() {
        return "<Main Activity>";
    }
    @Override
    protected boolean isNeedToolbar() {
        return true;
    }
}
