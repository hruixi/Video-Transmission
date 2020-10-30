package com.rui.mvvm_jetpack.model.net.rxjava2CallAdapter;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @ClassName CallExcuteObservableCopy
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/5/20 12:15
 */
final class CallExecuteObservableCopy<T> extends Observable<Response<T>> {
    private final Call<T> originalCall;

    CallExecuteObservableCopy(Call<T> originalCall) {
        this.originalCall = originalCall;
    }

    @Override protected void subscribeActual(Observer<? super Response<T>> observer) {
        // Since Call is a one-shot type, clone it for each new observer.
        Call<T> call = originalCall.clone();
        CallExecuteObservableCopy.CallDisposable disposable = new CallExecuteObservableCopy.CallDisposable(call);
        observer.onSubscribe(disposable);
        if (disposable.isDisposed()) {
            return;
        }

        boolean terminated = false;
        try {
            Response<T> response = call.execute();
            if (!disposable.isDisposed()) {
                observer.onNext(response);
            }
            if (!disposable.isDisposed()) {
                terminated = true;
                observer.onComplete();
            }
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            if (terminated) {
                RxJavaPlugins.onError(t);
            } else if (!disposable.isDisposed()) {
                try {
                    observer.onError(t);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaPlugins.onError(new CompositeException(t, inner));
                }
            }
        }
    }

    private static final class CallDisposable implements Disposable {
        private final Call<?> call;
        private volatile boolean disposed;

        CallDisposable(Call<?> call) {
            this.call = call;
        }

        @Override public void dispose() {
            disposed = true;
            call.cancel();
        }

        @Override public boolean isDisposed() {
            return disposed;
        }
    }
}
