package com.rui.video_transmission.model.structure;


import com.rui.video_transmission.BuildConfig;

import java.util.Locale;

public class ST_httpError {
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
