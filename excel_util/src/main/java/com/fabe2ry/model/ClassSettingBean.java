package com.fabe2ry.model;

import java.util.List;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class ClassSettingBean {

    Class clazz;

    SheetSettingBean sheetSettingBean;

    List<ColSettingBean> colSettingBeans;

    @Override
    public String toString() {
        return "ClassSettingBean{" +
                "clazz=" + clazz +
                ", sheetSettingBean=" + sheetSettingBean +
                ", colSettingBeans=" + colSettingBeans +
                '}';
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public SheetSettingBean getSheetSettingBean() {
        return sheetSettingBean;
    }

    public void setSheetSettingBean(SheetSettingBean sheetSettingBean) {
        this.sheetSettingBean = sheetSettingBean;
    }

    public List<ColSettingBean> getColSettingBeans() {
        return colSettingBeans;
    }

    public void setColSettingBeans(List<ColSettingBean> colSettingBeans) {
        this.colSettingBeans = colSettingBeans;
    }
}
