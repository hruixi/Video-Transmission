package com.rui.video_transmission.model.repository.paging.ByPage;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.rui.video_transmission.model.bean.RedditBean;
import com.rui.video_transmission.model.net.NetworkStatus;
import com.rui.video_transmission.model.repository.RedditRepository;
import com.rui.video_transmission.model.structure.ST_httpError;
import com.rui.video_transmission.utils.rx.RxObserver;
import com.rui.video_transmission.utils.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SubReddit_DS
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/5/12 17:35
 */
public class SubReddit_DS extends PageKeyedDataSource<String, RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean> {
    private static final String TAG = "SubReddit_DS";
    private RedditRepository mRepository;
    private MutableLiveData<NetworkStatus> mNetworkState = new MutableLiveData<>();

    public SubReddit_DS(RedditRepository repository) {
        mRepository = repository;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params,
                            @NonNull LoadInitialCallback<String , RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean> callback) {
        List<RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean> items = new ArrayList<>();

        mNetworkState.postValue(NetworkStatus.LOADING);
        mRepository.getReddit("subreddit", params.requestedLoadSize)
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new RxObserver<RedditBean>() {
                    @Override
                    public void onSuccess(RedditBean redditBean) {
                        for (RedditBean.RedditDataBean.ChildrenBean childrenBean : redditBean.getData().getChildren()) {
                            items.add(childrenBean.getData());
                        }
                        mNetworkState.postValue(NetworkStatus.SUCCESS);
                        callback.onResult(items, redditBean.getData().getBefore(), redditBean.getData().getAfter());
                    }

                    @Override
                    public void onFail(ST_httpError e) {
                        mNetworkState.postValue(NetworkStatus.FAILED);
                        Log.e(TAG, "onFail: " + e.error2String());
                    }
                });
    }

    @Override
    public void loadAfter(LoadParams<String> params,
                          LoadCallback<String , RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean> callback) {
        List<RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean> items = new ArrayList<>();

        mNetworkState.postValue(NetworkStatus.LOADING);
        mRepository.getRedditAfter("subreddit", params.key, params.requestedLoadSize)
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new RxObserver<RedditBean>() {
                    @Override
                    public void onSuccess(RedditBean redditBean) {
                        for (RedditBean.RedditDataBean.ChildrenBean childrenBean : redditBean.getData().getChildren()) {
                            items.add(childrenBean.getData());
                        }
                        mNetworkState.postValue(NetworkStatus.SUCCESS);
                        callback.onResult(items, redditBean.getData().getAfter());
                    }

                    @Override
                    public void onFail(ST_httpError e) {
                        mNetworkState.postValue(NetworkStatus.FAILED);
                        Log.e(TAG, "onFail: " + e.error2String());
                    }
                });
    }

    @Override
    public void loadBefore(LoadParams<String> params,
                           LoadCallback<String , RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean> callback) {
        // 忽略，因为只附加到初始负载
    }

    public MutableLiveData<NetworkStatus> getNetworkState() {
        return mNetworkState;
    }
}
