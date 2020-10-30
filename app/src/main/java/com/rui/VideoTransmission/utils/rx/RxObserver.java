package com.rui.VideoTransmission.utils.rx;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.rui.VideoTransmission.model.structure.ST_httpError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class RxObserver<T> implements Observer<T> {
    public abstract void onSuccess(T t);
//    public abstract void onFail(Throwable e);
    public abstract void onFail(ST_httpError e);

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        handError(e);
    }

    @Override
    public void onComplete() {

    }

    /**
     * 对http的错误做统一处理
     * @param e
     */
    private void handError(Throwable e) {
        try {
            e.printStackTrace();
            ST_httpError httpError = new ST_httpError();
            if (e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                if (isCanHandle(httpException)) {
//                    httpError.setCode(httpException.code());
                    String body = httpException.response().errorBody().string();
                    ST_httpError responseErrorBody = new Gson().fromJson(body, ST_httpError.class);
                    httpError.setMessage(responseErrorBody.getMessage());
                    httpError.setException("KnownCode_Exception");
                    Log.e("ST_httpError", httpError.error2String());
                    onFail(httpError);
                    return;
                } else {
                    httpError.setCode(httpException.code());
                    httpError.setException("UnknownCode_Exception");
                }
            } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
                httpError.setCode(9001);
                httpError.setException( "JsonParse_Exception");
            } else if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException) {
                httpError.setCode(9002);
                httpError.setException( "Connect_Exception");
            } else if (e instanceof UnknownHostException) {
                httpError.setCode(9003);
                httpError.setException( "Unknown_Host_Exception");
            } else {
                httpError.setCode(9111);
                httpError.setException("Unknown_Exception");
            }
            httpError.setMessage(e.getMessage());
            Log.e("ST_httpError", httpError.error2String());
            onFail(httpError);
        } catch (Exception e1) {
            e1.printStackTrace();
            Log.e("ST_httpError", e1.toString());
        }
//        onComplete();
    }

    /**
     * 可预处理的错误
     */
    private boolean isCanHandle(HttpException exception) {
        switch (exception.code()) {
//            case 304: // 资源没有更新，客户端可以使用缓存
//                return true;
            case 400: // 短信发送失败
                return true;
            case 401: // 当前请求需要用户验证（Token失效）
//                CommonUtil.userLogout();
                return true;
            case 403: // 服务器已经理解请求，但是拒绝执行它。与401响应不同的是，身份验证并不能提供任何帮助
                return true;
            case 404: // 请求失败，请求所希望得到的资源未被在服务器上发现
                return true;
            case 409: // 版本更新
                return true;
            case 412: // 参数缺失
                return true;
            case 414: // 请求的URI 长度超过了服务器能够解释的长度，通常的情况包括：本应使用POST方法的表单提交变成了GET方法，导致查询字符串（Query String）过长
                return true;
            case 500: // 服务器遇到了不知道如何处理的情况
                return true;
            case 501: // 此请求方法不被服务器支持且无法被处理。只有GET和HEAD是要求服务器支持的
                return true;
        }
        return false;
    }
}
