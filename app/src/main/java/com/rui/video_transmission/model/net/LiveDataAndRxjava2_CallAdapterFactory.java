package com.rui.video_transmission.model.net;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.rui.video_transmission.model.net.rxjava2CallAdapter.Rxjava2CallAdapterCopy;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.CallAdapter;
import retrofit2.CallAdapter.Factory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;


/**
 * @ClassName LiveDataCallAdapterFactory
 * @Description 适配工厂——让retrofit可以与LiveData结合使用
 * @Author He ruixiang
 * @Date 2020/4/7 16:14
 */
public class LiveDataAndRxjava2_CallAdapterFactory extends Factory {
    private static final String TAG = "LiveDataCallAdapterFact";

    @Nullable
    @Override
    public
    CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        /** getRawType()
         * 获取type所属的类型，比如:
         * 如果type是{@code List<? extends Runnable>} 则返回 {@code List.class}
         *
         * getParameterUpperBound()
         * 获取type的上界类，比如：
         * {@code Map<String, ? extends Runnable>}索引为1的返回类型， 返回 {@code Runnable}
         * **/
        Type responseType;
        Class<?> rawType = getRawType(returnType);
        if (rawType == LiveData.class) {
            Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
            Class<?> rawObservableType = getRawType(observableType);
            if (rawObservableType == Response.class) {
                if (!(observableType instanceof ParameterizedType)) {
//                Log.e(TAG, "return type must be parameterized");
                    throw new  IllegalArgumentException("Response must be parameterized");
                }
                responseType = getParameterUpperBound(0,(ParameterizedType)  observableType);
            } else {
                responseType = observableType;
            }
            return new LiveDataCallAdapter<>(responseType);
        } else {
            if (rawType == Completable.class) {
                // Completable is not parameterized (which is what the rest of this method deals with) so it
                // can only be created with a single configuration.
                return new Rxjava2CallAdapterCopy(Void.class, null, false, false, true, false, false,
                        false, true);
            }

            boolean isFlowable = rawType == Flowable.class;
            boolean isSingle = rawType == Single.class;
            boolean isMaybe = rawType == Maybe.class;
            if (rawType != Observable.class && !isFlowable && !isSingle && !isMaybe) {
                return null;
            }

            boolean isResult = false;
            boolean isBody = false;
            if (!(returnType instanceof ParameterizedType)) {
                String name = isFlowable ? "Flowable"
                        : isSingle ? "Single"
                        : isMaybe ? "Maybe" : "Observable";
                throw new IllegalStateException(name + " return type must be parameterized"
                        + " as " + name + "<Foo> or " + name + "<? extends Foo>");
            }

            Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
            Class<?> rawObservableType = getRawType(observableType);
            if (rawObservableType == Response.class) {
                if (!(observableType instanceof ParameterizedType)) {
                    throw new IllegalStateException("Response must be parameterized"
                            + " as Response<Foo> or Response<? extends Foo>");
                }
                responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            } else if (rawObservableType == Result.class) {
                if (!(observableType instanceof ParameterizedType)) {
                    throw new IllegalStateException("Result must be parameterized"
                            + " as Result<Foo> or Result<? extends Foo>");
                }
                responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
                isResult = true;
            } else {
                responseType = observableType;
                isBody = true;
            }

            return new Rxjava2CallAdapterCopy(responseType, null, false, isResult, isBody, isFlowable,
                    isSingle, isMaybe, false);
        }
//        else {
//            throw new IllegalStateException("return type must be LiveData.class");
//            Log.e(TAG, "return type must be parameterized");
//        }

    }
}
