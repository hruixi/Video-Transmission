package com.rui.VideoTransmission.model.net.rxjava2CallAdapter;

import androidx.annotation.Nullable;

import java.lang.reflect.Type;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;

/**
 * @ClassName Rxjava2CallAdapterCopy
 * @Description 因为官方的的RxJava2CallAdapter是final和private的，所以将源码拷贝过来
 * @Author He ruixiang
 * @Date 2020/5/20 11:54
 */
public class Rxjava2CallAdapterCopy<R> implements CallAdapter<R, Object> {
    private final Type responseType;
    private final @Nullable Scheduler scheduler;
    private final boolean isAsync;
    private final boolean isResult;
    private final boolean isBody;
    private final boolean isFlowable;
    private final boolean isSingle;
    private final boolean isMaybe;
    private final boolean isCompletable;

    public Rxjava2CallAdapterCopy(Type responseType, @Nullable Scheduler scheduler, boolean isAsync,
                       boolean isResult, boolean isBody, boolean isFlowable, boolean isSingle, boolean isMaybe,
                       boolean isCompletable) {
        this.responseType = responseType;
        this.scheduler = scheduler;
        this.isAsync = isAsync;
        this.isResult = isResult;
        this.isBody = isBody;
        this.isFlowable = isFlowable;
        this.isSingle = isSingle;
        this.isMaybe = isMaybe;
        this.isCompletable = isCompletable;
    }

    @Override public Type responseType() {
        return responseType;
    }

    @Override public Object adapt(Call<R> call) {
        Observable<Response<R>> responseObservable = isAsync
                ? new CallEnqueueObservableCopy<>(call)
                : new CallExecuteObservableCopy<>(call);

        Observable<?> observable;
        if (isResult) {
            observable = new ResultObservableCopy<>(responseObservable);
        } else if (isBody) {
            observable = new BodyObservableCopy<>(responseObservable);
        } else {
            observable = responseObservable;
        }

        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler);
        }

        if (isFlowable) {
            return observable.toFlowable(BackpressureStrategy.LATEST);
        }
        if (isSingle) {
            return observable.singleOrError();
        }
        if (isMaybe) {
            return observable.singleElement();
        }
        if (isCompletable) {
            return observable.ignoreElements();
        }
        return RxJavaPlugins.onAssembly(observable);
    }
}
