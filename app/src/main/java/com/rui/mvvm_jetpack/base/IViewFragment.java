package com.rui.mvvm_jetpack.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * @ClassName IVewFragment
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/4/10 11:31
 */
public interface IViewFragment extends IView {
    void initViewBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
