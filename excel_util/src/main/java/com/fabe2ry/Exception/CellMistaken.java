package com.fabe2ry.Exception;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class CellMistaken extends RuntimeException{

    int rowIndex;
    int colIndex;

    public CellMistaken(int row, int col, String message) {
//        + 1 实际坐标从1开始
        super(String.format("row: %d col: %d message: %s", row + 1, col + 1, message));
        this.rowIndex = row;
        this.colIndex = col;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }
}
