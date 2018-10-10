package com.fabe2ry.model;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class SheetSettingBean {

//    对应注解的属性
    String sheetName;
    int headerRowIndex;

    @Override
    public String toString() {
        return "SheetSettingBean{" +
                "sheetName='" + sheetName + '\'' +
                ", headerRowIndex=" + headerRowIndex +
                '}';
    }

    public int getHeaderRowIndex() {
        return headerRowIndex;
    }

    public void setHeaderRowIndex(int headerRowIndex) {
        this.headerRowIndex = headerRowIndex;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
