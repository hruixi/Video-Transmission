package com.rui.VideoTransmission.model.repository;

import com.rui.VideoTransmission.R;
import com.rui.VideoTransmission.base.BaseApplication;
import com.rui.VideoTransmission.model.bean.RedditBean;
import com.rui.VideoTransmission.model.net.RetrofitProvider;
import com.rui.VideoTransmission.model.net.service.RedditService;

import io.reactivex.Observable;

/**
 * @ClassName RedditRepository
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/5/15 15:42
 */
public class RedditRepository {
    private RedditService mService;

    public RedditRepository() {
        mService = RetrofitProvider.get(BaseApplication.getInstance().getResources().getString(R.string.base_url_reddit))
                .create(RedditService.class);
    }

    public Observable<RedditBean> getReddit(String subreddit, int limit) {
        return mService.getReddit(subreddit, limit);
    }

    public Observable<RedditBean> getRedditAfter(String subreddit, String after, int limit) {
        return mService.getRedditAfter(subreddit, after, limit);
    }
}
