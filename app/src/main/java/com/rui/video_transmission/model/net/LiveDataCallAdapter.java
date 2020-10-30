package com.rui.video_transmission.model.net;

import androidx.lifecycle.LiveData;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ClassName LiveDataCallAdapter
 * @Description 要让retrofit适配LiveData，最终会调用到adapt(call)
 * @Author He ruixiang
 * @Date 2020/4/7 16:14
 */
public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {
    private Type mResponseType;

    public LiveDataCallAdapter (Type responseType) {
        mResponseType = responseType;
    }

    @Override
    public LiveData<T> adapt(Call<T> call) {
        return new LiveData<T>() {

            private AtomicBoolean started = new AtomicBoolean(false);
            @Override
            public void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {//确保执行一次
                    /** 在此控制是同步响应还是异步响应 **/
//                    try {
//                        call.execute(); //同步调用
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    // 异步
                    call.enqueue(new Callback<T>() {
                        @Override
                        public void onResponse(Call<T> call, Response<T> response) {
                            postValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<T> call, Throwable t) {
                            ErrorResponse value = new ErrorResponse();
                            value.setCode(-1);
                            value.setException("");
                            value.setMessage(t.getMessage());
                            postValue((T) value);
                        }
                    });
                }
            }
        };
    }

    @Override
    public Type responseType() {
        return mResponseType;
    }

}
