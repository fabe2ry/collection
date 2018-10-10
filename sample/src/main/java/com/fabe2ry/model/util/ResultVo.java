package com.fabe2ry.model.util;

/**
 * Created by xiaoxq on 2018/9/18.
 */
public class ResultVo<T> {

    boolean success = false;
    String message;
    T result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
