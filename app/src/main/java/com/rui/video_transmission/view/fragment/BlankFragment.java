package com.rui.video_transmission.view.fragment;

import android.Manifest;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        initPathViewUp(mBinding.pathViewUp.getWidth(), mBinding.pathViewUp.getHeight());
        initPathViewDown(mBinding.pathViewDown.getWidth(), mBinding.pathViewDown.getHeight());
    }

    private void initPathViewUp(int width, int height) {
        // 设置基础属性
        mBinding.pathViewUp.setLineWidth(10);
        mBinding.pathViewUp.setDuration(1500);
        mBinding.pathViewUp.setRepeat(true);
        mBinding.pathViewUp.setMode(PathView.MODE_AIRPLANE);
        mBinding.pathViewUp.setDarkLineColor(Color.WHITE);
        mBinding.pathViewUp.setLightLineColor(getResources().getColor(R.color.second_color));
        // 设置路径
        mBinding.pathViewUp.setPath(getPath(width, height));
        mBinding.pathViewUp.start();
    }

    private void initPathViewDown(int width, int height) {
        // 设置基础属性
        mBinding.pathViewDown.setLineWidth(10);
        mBinding.pathViewDown.setDuration(1500);
        mBinding.pathViewDown.setRepeat(true);
        mBinding.pathViewDown.setMode(PathView.MODE_AIRPLANE);
        mBinding.pathViewDown.setDarkLineColor(Color.WHITE);
        mBinding.pathViewDown.setLightLineColor(getResources().getColor(R.color.second_color));
        // 设置路径
        mBinding.pathViewDown.setPath(getPath(width, height));
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
