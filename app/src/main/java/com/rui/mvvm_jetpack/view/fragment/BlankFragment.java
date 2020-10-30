package com.rui.mvvm_jetpack.view.fragment;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rui.mvvm_jetpack.R;
import com.rui.mvvm_jetpack.base.BaseFragment;

import com.rui.mvvm_jetpack.databinding.FragmentBlankBinding;
import com.rui.mvvm_jetpack.view.activity.CameraXActivity;
import com.rui.mvvm_jetpack.view.activity.PagedListActivity;
import com.rui.mvvm_jetpack.viewmodel.Common_vm;
import com.rui.mvvm_jetpack.viewmodel.viewmodel_factory.MainVmFactory;
import com.tbruyelle.rxpermissions2.RxPermissions;

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
//        mBinding.setVm(mMainViewModel);

        mMainViewModel.testApi().observe(this, displayBean -> {
            mBinding.fraTestApi.setText(displayBean.toStr());
        });

        mBinding.fraTv.setOnClickListener( v -> getPermission());
        mBinding.fraPagedList.setOnClickListener( v -> PagedListActivity.start(getActivity()));
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
