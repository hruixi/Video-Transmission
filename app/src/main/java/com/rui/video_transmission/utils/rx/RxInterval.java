package com.rui.video_transmission.utils.rx;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/** ProgressBarDialog里的RxTimer会先消费掉里面的mDispose
 *  解决方案：拆分成RxTimer和RxInterval
 * **/
public class RxInterval {
    private static Disposable mDisposable;

    /**
     * 每隔 seconds（秒）后执行指定动作
     *
     * @param seconds
     * @param rxAction
     */
    public static void interval(long seconds, final RxInterval.RxAction rxAction) {
        mDisposable = Observable.interval(seconds, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (rxAction != null) {
                            rxAction.action(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        RxInterval.cancel();
                    }

                    @Override
                    public void onComplete() {
                        RxInterval.cancel();
                    }
                });
    }

    /**
     * 取消订阅
     */
    public static void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public interface RxAction {
        /**
         * 让调用者指定指定动作
         *
         * @param number
         */
        void action(long number);
    }
}
