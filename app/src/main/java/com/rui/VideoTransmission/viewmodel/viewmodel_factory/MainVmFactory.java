package com.rui.VideoTransmission.viewmodel.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.rui.VideoTransmission.model.repository.CommonRepository;
import com.rui.VideoTransmission.viewmodel.Common_vm;

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
