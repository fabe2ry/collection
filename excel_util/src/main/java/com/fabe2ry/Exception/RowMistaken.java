package com.fabe2ry.Exception;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class RowMistaken extends RuntimeException {

    int cellMistakenCount = 0;



    public RowMistaken() {
    }

    public void addCellMistakenCount() {
        cellMistakenCount ++;
    }

    public int getCellMistakenCount(){
        return cellMistakenCount;
    }
}
