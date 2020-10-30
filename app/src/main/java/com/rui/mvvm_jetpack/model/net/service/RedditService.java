package com.rui.mvvm_jetpack.model.net.service;

import com.rui.mvvm_jetpack.model.bean.RedditBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @ClassName RedditService
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/5/15 15:18
 */
public interface RedditService {
    @GET("/r/{subreddit}/hot.json")
    Observable<RedditBean> getReddit(@Path("subreddit") String subreddit,
                                     @Query("limit") int limit);

    @GET("/r/{subreddit}/hot.json")
    Observable<RedditBean> getRedditAfter(@Path("subreddit") String subreddit,
                                          @Query("after") String after,
                                          @Query("limit") int limit);
}
