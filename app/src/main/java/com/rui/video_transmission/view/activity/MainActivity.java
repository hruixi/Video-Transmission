package com.rui.video_transmission.view.activity;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.rui.video_transmission.R;
import com.rui.video_transmission.base.BaseActivity;
import com.rui.video_transmission.viewmodel.Common_vm;
import com.rui.video_transmission.databinding.ActivityMainBinding;

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
        return getString(R.string.app_name_str);
    }
    @Override
    protected boolean isNeedToolbar() {
        return true;
    }
}
