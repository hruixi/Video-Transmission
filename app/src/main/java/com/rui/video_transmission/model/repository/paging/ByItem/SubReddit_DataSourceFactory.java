package com.rui.video_transmission.model.repository.paging.ByItem;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.rui.video_transmission.model.bean.RedditBean;
import com.rui.video_transmission.model.repository.RedditRepository;

/**
 * @ClassName SubReddit_DataSourceFactory
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/5/11 11:56
 */
public class SubReddit_DataSourceFactory
        extends DataSource.Factory<String, RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean> {
    private RedditRepository mRepository;
    public MutableLiveData<SubReddit_DataSource> sourceLiveData = new MutableLiveData<>();

    public SubReddit_DataSourceFactory(RedditRepository repository) {
        mRepository = repository;
    }

    @Override
    public SubReddit_DataSource create() {
        SubReddit_DataSource source = new SubReddit_DataSource(mRepository);
        sourceLiveData.postValue(source);
        return source;
    }
}
