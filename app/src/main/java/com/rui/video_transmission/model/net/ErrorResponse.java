package com.rui.video_transmission.model.net;


import com.rui.video_transmission.BuildConfig;

import java.util.Locale;

/**
 * @ClassName ErrorResponse
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/4/7 16:14
 */
public class ErrorResponse {
    private int code;
    private String exception = "";
    private String message = "";

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getException() {
        return exception;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String error2String() {
        return BuildConfig.DEBUG ? String.format(Locale.ENGLISH, "(%d)-(%s) -->\n %s", code, exception, message) : message;
    }
}
