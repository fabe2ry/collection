package com.fabe2ry.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.logging.Logger;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class MistakenHandleUtil {

    static Logger logger = Logger.getLogger(MistakenHandleUtil.class.getName());

    public static void createMistakenPrompt(Row row, int tailIndex, String message) {
//      使用策略封装这个决定
//        int tailCellIndex = row.getLastCellNum();
        int tailCellIndex = tailIndex;

        Cell cell = row.getCell(tailCellIndex);
        if(cell == null || cell.getCellType().equals(CellType.BLANK)){
            Cell tailCell = row.createCell(tailCellIndex);
            tailCell.setCellValue(message);
        }else{
//            采用添加，而不是覆盖
            String beforeStr = CellTransformUtil.getCellStringValue(cell);
            cell.setCellValue(beforeStr + "   " + message);
        }
    }

    /**
     * 擦出错误信息
     * @param row
     */
//    TODO:调整提示信息，设置index
//    TODO：看看在哪里添加记录
    public static void eraseMistakenPrompt(Row row, int tailIndex) {
//        int rowSize = row.getLastCellNum();
        int rowSize = tailIndex;
        Cell cell = row.createCell(rowSize);

        if(cell != null){
            row.removeCell(cell);
        }
    }
}
