package com.rui.VideoTransmission.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.rui.VideoTransmission.model.bean.DisplayBean;
import com.rui.VideoTransmission.model.bean.RedditBean;
import com.rui.VideoTransmission.model.net.NetworkStatus;
import com.rui.VideoTransmission.model.repository.CommonRepository;
import com.rui.VideoTransmission.model.repository.RedditRepository;
import com.rui.VideoTransmission.model.repository.paging.ByPage.SubReddit_DSFactory;

public class Common_vm extends ViewModel {
    private CommonRepository mCommonRepository;

    private LiveData<PagedList<RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean>> mReddits;
//    private SubReddit_DataSourceFactory mDataSourceFactory;
    private SubReddit_DSFactory mDataSourceFactory;

    public Common_vm(CommonRepository commonRepository) {
        mCommonRepository = commonRepository;
//        mDataSourceFactory = new SubReddit_DataSourceFactory(new RedditRepository());
        mDataSourceFactory = new SubReddit_DSFactory(new RedditRepository());

        PagedList.Config myPagingConfig = new PagedList.Config.Builder()
                .setPageSize(15)
                .setPrefetchDistance(45)
                .setEnablePlaceholders(true)
                .build();
        /** LivePagedListBuilder.build()时调用DataSource.Factory的onCreate();
         * 调完onCreate()以后才有DateSource生成 **/
        mReddits = new LivePagedListBuilder<String, RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean>(mDataSourceFactory, myPagingConfig)
//        mReddits = new LivePagedListBuilder<String, RedditBean.RedditDataBean.ChildrenBean.ChildreDataBean>(mDataSourceFactory, 30)
                .build();
    }

    public LiveData<DisplayBean> testApi() {
        return mCommonRepository.display();
    }

    public LiveData<PagedList<RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean>> getReddits() {
        return mReddits;
    }

    public void refresh() {
        if (mDataSourceFactory.sourceLiveData.getValue() != null)
            mDataSourceFactory.sourceLiveData.getValue().invalidate();
    }

    public MutableLiveData<NetworkStatus> getRefreshStatus() {
        if (mDataSourceFactory.sourceLiveData.getValue() != null)
            return mDataSourceFactory.sourceLiveData.getValue().getNetworkState();
        else
            return null;
    }
}
