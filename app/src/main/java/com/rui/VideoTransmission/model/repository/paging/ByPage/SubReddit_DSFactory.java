package com.rui.VideoTransmission.model.repository.paging.ByPage;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.rui.VideoTransmission.model.bean.RedditBean;
import com.rui.VideoTransmission.model.repository.RedditRepository;

/**
 * @ClassName SubReddit_DSFactory
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/5/12 17:36
 */
public class SubReddit_DSFactory
        extends DataSource.Factory<String, RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean> {
    private RedditRepository mRepository;
    public MutableLiveData<SubReddit_DS> sourceLiveData = new MutableLiveData<>();

    public SubReddit_DSFactory(RedditRepository repository) {
        mRepository = repository;
    }

    @Override
    public SubReddit_DS create() {
        SubReddit_DS source = new SubReddit_DS(mRepository);
        Log.e("rui", "SubReddit_DSFactory的create()，并postValue");
        sourceLiveData.postValue(source);
        return source;
    }
}
