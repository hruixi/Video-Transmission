package com.rui.mvvm_jetpack.utils.rx;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/** ProgressBarDialog里的RxTimer会先消费掉里面的mDispose
 *  解决方案：拆分成RxTimer和RxInterval
 * **/
public class RxTimer {
    private static Disposable mDisposable;

    /**
     * milliseconds（毫秒）后执行指定动作
     *
     * @param milliSeconds 毫秒
     * @param rxAction 指定动作
     */
    public static void timer(long milliSeconds, final RxTimer.RxAction rxAction) {
        Observable.timer(milliSeconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        cancel();
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (rxAction != null) {
                            rxAction.action(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //取消订阅
                        RxTimer.cancel();
                    }

                    @Override
                    public void onComplete() {
                        //取消订阅
                        RxTimer.cancel();
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
