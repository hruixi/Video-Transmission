package com.rui.mvvm_jetpack.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

/**
 * @ClassName BaseFragment
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/4/9 17:49
 */
public abstract class BaseFragment extends Fragment implements IProgressBar, IViewFragment {
    protected View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViewBinding(inflater, container, savedInstanceState);
        initData();
        return mRootView;
    }

    private void registerEventBus() {
        if (isNeedEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }
    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    /**
     * 是否需要eventbus
     */
    protected  boolean isNeedEventBus() {
        return false;
    }

    //---------------------------- IProgressbar -------------------------//
    @Override
    public void showProgressBar() {
//        mProgressBarDialog.show();
    }
    @Override
    public void hideProgressBar() {
//        mProgressBarDialog.dismiss();
    }
}
