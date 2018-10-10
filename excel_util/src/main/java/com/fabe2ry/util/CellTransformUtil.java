package com.fabe2ry.util;

import com.fabe2ry.Exception.CellTransformException;
import org.apache.poi.ss.usermodel.Cell;

import java.text.DecimalFormat;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class CellTransformUtil {

//    TODO:没有校验用户设置的type和用户设置的数据类型
//    TODO:异常捕获
    //    TODO:列同名问题
//    TODO:主键问题
//  TODO:区别给程序员用户和使用导入excel功能的用户，来判断是使用exception和logger来进行提示
//    TODO:统一使用null，对于size=0，另外处理
//    TODO:列断层
    /**
     *
     * @param cell
     * @return
     */
    public static String getCellStringValue(Cell cell) {
        if(cell == null){
            throw new CellTransformException("cell为空");
        }

        switch (cell.getCellType()){
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BLANK:
                return "";
            case BOOLEAN:
                return cell.getBooleanCellValue()?"true":"false";
            case FORMULA:
                return cell.getCellFormula();
            case _NONE:
                throw new CellTransformException("还没见过");
            case ERROR:
                throw new CellTransformException("cell type异常");
            default:
                throw new CellTransformException("cell type异常");
        }
    }

    /**
     *
     * @param cell
     * @return
     */
    public static int getCellNumberValue(Cell cell) {
        if(cell == null){
            throw new CellTransformException("参数不能为空");
        }

        switch (cell.getCellType()){
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
            case BLANK:
            case BOOLEAN:
            case FORMULA:
            case _NONE:
            case ERROR:
            default:
//                throw new CellTransformException("cell type异常");
                throw new CellTransformException("必须是NUMERIC类型");
        }
    }

    /**
     *
     * @param cell
     * @return
     */
    public static String getCellPureNumberStrValue(Cell cell) {
        if(cell == null){
            throw new CellTransformException("参数不能为空");
        }

        switch (cell.getCellType()){
            case NUMERIC:
                DecimalFormat format = new DecimalFormat("#");
                double value = cell.getNumericCellValue();
                return format.format(value);
            case STRING:
            case BLANK:
            case BOOLEAN:
            case FORMULA:
            case _NONE:
            case ERROR:
            default:
                throw new CellTransformException("必须是NUMERIC类型");
        }
    }
}
