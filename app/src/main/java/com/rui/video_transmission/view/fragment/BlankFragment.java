package com.rui.video_transmission.view.fragment;

import android.Manifest;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rui.video_transmission.R;
import com.rui.video_transmission.base.BaseFragment;

import com.rui.video_transmission.databinding.FragmentBlankBinding;
import com.rui.video_transmission.view.activity.CameraXActivity;
import com.rui.video_transmission.view.activity.PagedListActivity;
import com.rui.video_transmission.viewmodel.Common_vm;
import com.rui.video_transmission.viewmodel.viewmodel_factory.MainVmFactory;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wuyr.pathview.PathView;

import io.reactivex.functions.Consumer;

public class BlankFragment extends BaseFragment {
    Common_vm mMainViewModel;
    private FragmentBlankBinding mBinding;

    private static final int  ANIMATE_DURATION = 2000;
    private static final int  LINE_WIDTH = 5;

    public static BlankFragment newInstance() {
        return new BlankFragment();
    }

    @Override
    public void initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_blank, container, false);
        mBinding = FragmentBlankBinding.bind(mRootView);
    }

    @Override
    public void initData() {
        mMainViewModel = new ViewModelProvider(this, new MainVmFactory()).get(Common_vm.class);

        bindView();
    }

    private void bindView() {
        mBinding.getRoot().post(new Runnable() {
            @Override
            public void run() {
                initPathViewUp(mBinding.pathViewUp.getWidth(), mBinding.pathViewUp.getHeight());
                initPathViewDown(mBinding.pathViewDown.getWidth(), mBinding.pathViewDown.getHeight());
            }
        });
    }

    private void initPathViewUp(int width, int height) {
        // 设置基础属性
        mBinding.pathViewUp.setLineWidth(LINE_WIDTH);
        mBinding.pathViewUp.setDuration(ANIMATE_DURATION);
        mBinding.pathViewUp.setRepeat(true);
        mBinding.pathViewUp.setMode(PathView.MODE_AIRPLANE);
        mBinding.pathViewUp.setDarkLineColor(ResourcesCompat.getColor(getResources(), R.color.gray_bg, null));
        mBinding.pathViewUp.setLightLineColor(getResources().getColor(R.color.second_color));
        // 设置路径
        mBinding.pathViewUp.setPath(getPath(width, height));
        mBinding.pathViewUp.start();
    }

    private void initPathViewDown(int width, int height) {
        // 设置基础属性
        mBinding.pathViewDown.setLineWidth(LINE_WIDTH);
        mBinding.pathViewDown.setDuration(ANIMATE_DURATION);
        mBinding.pathViewDown.setRepeat(true);
        mBinding.pathViewDown.setMode(PathView.MODE_AIRPLANE);
        mBinding.pathViewUp.setDarkLineColor(ResourcesCompat.getColor(getResources(), R.color.gray_bg, null));
        mBinding.pathViewDown.setLightLineColor(getResources().getColor(R.color.second_color));
        // 设置路径
        mBinding.pathViewDown.setPath(getPath_inverse(width, height));
        mBinding.pathViewDown.start();
    }

    private Path getPath(int width, int height) {
        Path path = new Path();
        path.moveTo(0, height * (1F / 2F));
        path.lineTo(width * (1F / 3F), height * (1F / 2F));            // line1
        path.lineTo(width * (5F / 12F), 0);                            // line2
        path.lineTo(width * (7F / 12F), height);                          // line3
        path.lineTo(width * (2F / 3F), height * (1F / 2F));            // line4
        path.lineTo(width, height * (1F / 2F));                           // line5
        return path;
    }

    private Path getPath_inverse(int width, int height) {
        Path path = new Path();
        path.moveTo(width, height * (1F / 2F));
        path.lineTo(width * (2F / 3F), height * (1F / 2F));            // line1
        path.lineTo(width * (7F / 12F), height);                          // line2
        path.lineTo(width * (5F / 12F), 0);                            // line3
        path.lineTo(width * (1F / 3F), height * (1F / 2F));            // line4
        path.lineTo(0, height * (1F / 2F));                            // line5
        return path;
    }

    private void getPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isGrant) throws Exception {
                        if (isGrant) {
                            CameraXActivity.start(getActivity());
                        }
                    }
                })
                .isDisposed();
    }
}
