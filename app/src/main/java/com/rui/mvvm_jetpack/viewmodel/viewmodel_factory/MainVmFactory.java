package com.rui.mvvm_jetpack.viewmodel.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.rui.mvvm_jetpack.model.repository.CommonRepository;
import com.rui.mvvm_jetpack.viewmodel.Common_vm;

/**
 * @ClassName MainVmFactory
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/4/7 18:04
 */
public class MainVmFactory implements ViewModelProvider.Factory {
    @Override
    public  <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        CommonRepository commonRepository = new CommonRepository();
        Common_vm common_vm = new Common_vm(commonRepository);
        return (T) common_vm;
    }
}
