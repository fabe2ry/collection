package com.fabe2ry.Exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class RowMistaken extends RuntimeException {

    String sheetName;
    int rowIndex;
    List<CellMistaken> list = null;

    public RowMistaken(String sheetName, int rowIndex) {
        list = new ArrayList<>();
        this.sheetName = sheetName;
        this.rowIndex = rowIndex;
    }

    public void addCellMistaken(CellMistaken cellMistaken){
        list.add(cellMistaken);
    }

    public int getCellMistakenCount(){
        return list.size();
    }

    @Override
    public String getMessage() {
//        return super.getMessage();
        StringBuilder stringBuilder = new StringBuilder("sheet:" + sheetName + " row:" + (rowIndex + 1) + "   ");
        stringBuilder.append("{");
        for(int i = 0; i < list.size(); i ++){
            CellMistaken cellMistaken = list.get(i);
            if(i != list.size() - 1){
                stringBuilder.append(cellMistaken.getMessage() + ", ");
            }else{
                stringBuilder.append(cellMistaken.getMessage());
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
