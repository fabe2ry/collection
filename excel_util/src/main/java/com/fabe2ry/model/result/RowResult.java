package com.fabe2ry.model.result;

/**
 * Created by xiaoxq on 2018/10/12.
 */
public class RowResult {

    Object object;
    boolean isNormal;
    int rowIndex;
    int rowErrorCount;
    String errorMessage;

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getRowErrorCount() {
        return rowErrorCount;
    }

    public void setRowErrorCount(int rowErrorCount) {
        this.rowErrorCount = rowErrorCount;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
