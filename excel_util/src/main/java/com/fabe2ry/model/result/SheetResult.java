package com.fabe2ry.model.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxq on 2018/10/12.
 */
public class SheetResult {

    String sheetName;
    int totalRow;
    int errorRow;
    List<RowResult> normalRowList;
    List<RowResult> errorRowList;

    public SheetResult(){
        normalRowList = new ArrayList<>();
        errorRowList = new ArrayList<>();
    }

    public SheetResult(String sheetName){
        this();
        this.setSheetName(sheetName);
    }

    public SheetResult(List<RowResult> list){
        this();
        this.loadList(list);
    }

    public SheetResult(String sheetName, List<RowResult> list){
        this(sheetName);
        this.loadList(list);
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public int getErrorRow() {
        return errorRow;
    }

    public void loadList(List<RowResult> list){
        if(list == null){
            throw new NullPointerException();
        }

        for(RowResult rowResult : list){
            addRowResult(rowResult);
        }
    }

    public boolean contailError(){
        return errorRow > 0;
    }

    public void addRowResult(RowResult rowResult){
        totalRow ++;

        if(rowResult.isNormal()){
            normalRowList.add(rowResult);
        }else{
            errorRow ++;
            errorRowList.add(rowResult);
        }
    }

    public List getNormalObjectList(){
        List list = new ArrayList();
        for(RowResult rowResult : normalRowList){
            list.add(rowResult.getObject());
        }
        return list;
    }

    public List<RowResult> getNormalRowListWithMessage(){
        return normalRowList;
    }

    public List<RowResult> getErrorRowListWithMessage(){
        return errorRowList;
    }


}
