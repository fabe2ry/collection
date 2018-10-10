package com.fabe2ry.model;

import java.util.Date;

/**
 * Created by xiaoxq on 2018/9/20.
 */
public class LogModel {
    int log_id;
    String type;
    String title;
    String remote_addr;
    String request_uri;
    String method;
    String params;
    String exception;
    Date operate_date;
    String user_id;
    String timeout;

    @Override
    public String toString() {
        return "LogModel{" +
                "log_id=" + log_id +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", remote_addr='" + remote_addr + '\'' +
                ", request_uri='" + request_uri + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", exception='" + exception + '\'' +
                ", operate_date=" + operate_date +
                ", user_id='" + user_id + '\'' +
                ", timeout='" + timeout + '\'' +
                '}';
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public Date getOperate_date() {
        return operate_date;
    }

    public void setOperate_date(Date operate_date) {
        this.operate_date = operate_date;
    }

    public String getRequest_uri() {
        return request_uri;
    }

    public void setRequest_uri(String request_uri) {
        this.request_uri = request_uri;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemote_addr() {
        return remote_addr;
    }

    public void setRemote_addr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
