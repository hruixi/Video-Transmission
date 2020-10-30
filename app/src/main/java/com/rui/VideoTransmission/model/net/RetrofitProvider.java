package com.rui.VideoTransmission.model.net;

import com.rui.VideoTransmission.BuildConfig;
import com.rui.VideoTransmission.base.BaseApplication;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @ClassName RetrofitProvider
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/4/7 17:15
 */
public class RetrofitProvider {
    private static Map<String, Retrofit> mRetrofitCache = new HashMap<>();
    private static Retrofit mRetrofit;

//    public static Retrofit get() {
//        if (mRetrofit != null) {
//            return mRetrofit;
//        } else {
//            return create();
//        }
//    }

    public static Retrofit get(String baseUrl) {
        if (mRetrofitCache.containsKey(baseUrl)) {
            return mRetrofitCache.get(baseUrl);
        } else {
            Retrofit newRetrofit = create(baseUrl);
            mRetrofitCache.put(baseUrl, newRetrofit);
            return newRetrofit;
        }
    }


//    private static Retrofit create() {
    private static Retrofit create(String baseUrl) {
        final int TIMEOUT = 15;
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();

        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(BaseApplication.getInstance().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.level(HttpLoggingInterceptor.Level.BODY);
            okhttpBuilder.addInterceptor(logging);
        }
        okhttpBuilder.cache(cache)
//                .retryOnConnectionFailure(true)
                    .connectTimeout(TIMEOUT,TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(5, 1,TimeUnit.SECONDS));

        mRetrofit = new Retrofit.Builder()
//                .baseUrl(BaseApplication.getInstance().getResources().getString(R.string.base_url_reddit))
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                /** 虽然添加了多个CallAdapterFactory，但在源码中其实只用了第一个；
                 *  具体可以看源码 {@link Retrofit#nextCallAdapter(CallAdapter.Factory, Type, Annotation[])} **/
                .addCallAdapterFactory(new LiveDataAndRxjava2_CallAdapterFactory())  // 自定义的LiveData/Rxjava2 + Retrofit2 网络请求框架
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // RxJava2 + Retrofit2 网络请求框架
                .client(okhttpBuilder.build())
                .build();
        return mRetrofit;
    }
}
