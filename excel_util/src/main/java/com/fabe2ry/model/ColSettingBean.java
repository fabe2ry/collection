package com.fabe2ry.model;


import com.fabe2ry.type.ExcelColumnType;

import java.lang.reflect.Method;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class ColSettingBean {

//    对应注解的属性
    String columnName;
    int exportOrder;
    ExcelColumnType columnType;
    boolean allowEmopty;
    int columnIndex;

    //    反射使用
    Method injectMethod;
    Method ectractMethod;

    @Override
    public String toString() {
        return "ColSettingBean{" +
                "columnName='" + columnName + '\'' +
                ", exportOrder=" + exportOrder +
                ", columnType=" + columnType +
                ", allowEmopty=" + allowEmopty +
                ", columnIndex=" + columnIndex +
                ", injectMethod=" + injectMethod +
                ", ectractMethod=" + ectractMethod +
                '}';
    }

    public Method getEctractMethod() {
        return ectractMethod;
    }

    public void setEctractMethod(Method ectractMethod) {
        this.ectractMethod = ectractMethod;
    }

    public Method getInjectMethod() {
        return injectMethod;
    }

    public void setInjectMethod(Method injectMethod) {
        this.injectMethod = injectMethod;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public boolean isAllowEmopty() {
        return allowEmopty;
    }

    public void setAllowEmopty(boolean allowEmopty) {
        this.allowEmopty = allowEmopty;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getExportOrder() {
        return exportOrder;
    }

    public void setExportOrder(int exportOrder) {
        this.exportOrder = exportOrder;
    }

    public ExcelColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ExcelColumnType columnType) {
        this.columnType = columnType;
    }
}
