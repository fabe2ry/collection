package com.fabe2ry.model.example;

import com.fabe2ry.annotation.ExcelColumnSetting;
import com.fabe2ry.annotation.ExcelSheetSetting;
import com.fabe2ry.type.ExcelColumnType;

/**
 * Created by xiaoxq on 2018/10/9.
 */
@ExcelSheetSetting(sheetName = "货物表")
public class Goods {

//    @ExcelColumnSetting(columnName = "货物ID", columnType = ExcelColumnType.NUMBER)
    int id;

    @ExcelColumnSetting(columnName = "货物名字", columnType = ExcelColumnType.STRING)
    String name;

    @ExcelColumnSetting(columnName = "货物型号", columnType = ExcelColumnType.STRING_PURE_NUMBER)
    String type;

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
