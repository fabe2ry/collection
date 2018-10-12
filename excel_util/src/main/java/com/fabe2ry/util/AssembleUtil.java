package com.fabe2ry.util;

import com.fabe2ry.Exception.AssembleException;
import com.fabe2ry.Exception.CellMistaken;
import com.fabe2ry.Exception.CellTransformException;
import com.fabe2ry.Exception.RowMistaken;
import com.fabe2ry.model.ClassSettingBean;
import com.fabe2ry.model.ColSettingBean;
import com.fabe2ry.model.SheetSettingBean;
import com.fabe2ry.model.result.RowResult;
import com.fabe2ry.model.result.SheetResult;
import com.fabe2ry.type.ExcelColumnType;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class AssembleUtil {

    static Logger logger = Logger.getLogger(AssembleUtil.class.getName());

    /**
     *
     * @param workbook
     * @param classes
     * @return
     */
    public static List<SheetResult> assemble(Workbook workbook, Class[] classes){
        if(workbook == null || classes == null){
            throw new NullPointerException();
        }

        return assembleWithClasses(workbook, classes);
    }

    public static SheetResult assemble(Workbook workbook, Class clazz){
        if(workbook == null || clazz == null){
            throw new NullPointerException();
        }

        return assembleWithClass(workbook, clazz);
    }


    /**
     *
     * @param workbook
     * @param classes
     * @return
     */
    private static List<SheetResult> assembleWithClasses(Workbook workbook, Class[] classes) {
        if(workbook == null || classes == null){
            throw new NullPointerException();
        }
        List<SheetResult> allList = null;
        if(canAssembleWithClasses(workbook, classes)){
            return startAssembleWithClasses(workbook, classes);
        }
        return allList;
    }

    private static List<SheetResult> startAssembleWithClasses(Workbook workbook, Class[] classes) {
        List<SheetResult> allResult = null;
        for(Class clazz : classes){
            SheetResult sheetResult = assembleWithClass(workbook, clazz);

            if(sheetResult != null){
                if(allResult == null){
                    allResult = new ArrayList<>();
                }
                allResult.add(sheetResult);
            }
        }
        return allResult;
    }

    private static SheetResult assembleWithClass(Workbook workbook, Class clazz) {
        ClassSettingBean classSettingBean = parseSettingFromClass(clazz);
        return assembleWithBean(workbook, classSettingBean);
    }

    private static SheetResult assembleWithBean(Workbook workbook, ClassSettingBean classSettingBean) {
        if(canAssembleWithBean(workbook, classSettingBean)){
            return startAssembleBean(workbook, classSettingBean);
        }
        return null;
    }

    private static SheetResult startAssembleBean(Workbook workbook, ClassSettingBean classSettingBean) {
        return loadSheet(workbook, classSettingBean);
    }

    private static SheetResult loadSheet(Workbook workbook, ClassSettingBean classSettingBean) {
        String requireSheetName = classSettingBean.getSheetSettingBean().getSheetName().trim();
        if(requireSheetName == null || requireSheetName.length() == 0){
            throw new AssembleException("表名为空，或者不存在");
        }
        Sheet sheet = workbook.getSheet(requireSheetName);
        if(sheet == null){
            throw new AssembleException("无法找到表名：" + requireSheetName);
        }
        return loadSheet(sheet, classSettingBean);
    }

    private static SheetResult loadSheet(Sheet sheet, ClassSettingBean classSettingBean){
        if(canLoadSheet(sheet, classSettingBean)){
            preLoadSheet(sheet, classSettingBean);

            List<RowResult> list =  startLoadSheet(sheet, classSettingBean);
            return new SheetResult(sheet.getSheetName(), list);
        }
        return null;
    }

    private static void preLoadSheet(Sheet sheet, ClassSettingBean classSettingBean) {
//        header下标预处理
        int requireHeaderRowIndex = classSettingBean.getSheetSettingBean().getHeaderRowIndex();
        Row headerRow = getHeaderRowFromSheet(sheet, requireHeaderRowIndex);

        prepareHeaderIndex(sheet, headerRow, classSettingBean);
    }

    private static Row getHeaderRowFromSheet(Sheet sheet, int requireHeaderRowIndex) {
        if(requireHeaderRowIndex < 0){
            throw new AssembleException("headerRowIndex小于0");
        }

        Row headerRow = sheet.getRow(requireHeaderRowIndex);
        if(headerRow == null){
            throw new AssembleException("无法在" + requireHeaderRowIndex + "获取headerRow");
        }
        return headerRow;
    }

    private static void prepareHeaderIndex(Sheet sheet, Row headerRow, ClassSettingBean classSettingBean) {
        List<ColSettingBean> colSettingBeanList = classSettingBean.getColSettingBeans();

        int requireSize = colSettingBeanList.size();
        int realSize = headerRow.getLastCellNum();

        if(requireSize == 0){
            return;
        }
        if(requireSize > realSize){
            throw new AssembleException("sheet:" + sheet.getClass() + "  需要header size:" + requireSize + ", 实际header size:" + realSize);
        }

        injectHeaderIndex(sheet, headerRow, colSettingBeanList);
    }

    /**
     * 实际赋值
     * @param sheet
     * @param headerRow
     * @param colSettingBeanList
     */
    private static void injectHeaderIndex(Sheet sheet, Row headerRow, List<ColSettingBean> colSettingBeanList) {
        int requireSize = colSettingBeanList.size();
        if(requireSize == 0){
            return;
        }

        Map<String, Integer> headerIndexMap = new HashMap<>();
        for(Cell cell : headerRow){
            String cellStringValue = CellTransformUtil.getCellStringValue(cell).trim();
            int cellIndex = cell.getColumnIndex();

            headerIndexMap.put(cellStringValue, cellIndex);
        }

        for(ColSettingBean colSettingBean : colSettingBeanList){
            String requireColumnName = colSettingBean.getColumnName().trim();
            int requireColumnIndex = colSettingBean.getColumnIndex();

            if(requireColumnIndex == -1){
                if(! headerIndexMap.containsKey(requireColumnName)){
                    throw new AssembleException("sheet:" + sheet.getSheetName() + "  需要表头：" + requireColumnName + " 但是实际不存在");
                }

                int autoFindHeaderIndex = headerIndexMap.get(requireColumnName);
                colSettingBean.setColumnIndex(autoFindHeaderIndex);
            }else{
//                用户手动设置了index
//                int autoFindHeaderIndex = headerIndexMap.get(requireColumnName);
//                TODO：暂时不支持，有问题待解决，
            }
        }
    }

    private static List<RowResult> startLoadSheet(Sheet sheet, ClassSettingBean classSettingBean) {
        List<RowResult> list = null;

        for(Row row : sheet){
//            跳过header row和空行
            if(isHeaderRow(row, classSettingBean)){
                list = new ArrayList<>();
                continue;
            }

            if(isBlankRow(row, classSettingBean)){
                continue;
            }

            RowResult rowResult =  loadRow(sheet, row, classSettingBean);
            list.add(rowResult);
        }

        return list;
    }

    private static RowResult loadRow(Sheet sheet, Row row, ClassSettingBean classSettingBean) {
        preLoadObject(sheet, row, classSettingBean);

        RowResult rowResult = new RowResult();
        Object object = null;
        try{
            object = startLoadObject(sheet, row, classSettingBean);
        }catch (RowMistaken rowMistaken){
//            TODO:debug
            rowMistaken.printStackTrace();

            handleRowMistaken(sheet, row, classSettingBean, rowMistaken);

            rowResult.setNormal(false);
            rowResult.setRowIndex(row.getRowNum());
            rowResult.setRowErrorCount(rowMistaken.getCellMistakenCount());
            rowResult.setErrorMessage(rowMistaken.getMessage());
            rowResult.setObject(null);
            return rowResult;
        }

        if(object == null){
            rowResult.setNormal(false);
            rowResult.setRowIndex(row.getRowNum());
            rowResult.setRowErrorCount(0);
            rowResult.setErrorMessage("无法创建对象");
            rowResult.setObject(null);
        }else{
            rowResult.setNormal(true);
            rowResult.setRowIndex(row.getRowNum());
            rowResult.setRowErrorCount(0);
            rowResult.setErrorMessage(null);
            rowResult.setObject(object);
        }
        return rowResult;
    }

    private static void handleRowMistaken(Sheet sheet, Row row, ClassSettingBean classSettingBean, RowMistaken rowMistaken) {
//        默认可以
        boolean allowAddMistakenMessage = true;
        if(allowAddMistakenMessage){
            int tailIndex = classSettingBean.getColSettingBeans().size();
            String message = rowMistaken.getMessage();
            MistakenHandleUtil.createMistakenPrompt(row, tailIndex, message);
        }
    }


    private static Object startLoadObject(Sheet sheet, Row row, ClassSettingBean classSettingBean) {
        Class clazz = classSettingBean.getClazz();
        Object object = null;
        try {
            object = clazz.newInstance();
            loadObject(object, sheet, row, classSettingBean);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    private static void loadObject(Object object, Sheet sheet, Row row, ClassSettingBean classSettingBean) {
        int totalColumnSize = classSettingBean.getColSettingBeans().size();

        RowMistaken rowMistaken = null;

        for(ColSettingBean colSettingBean : classSettingBean.getColSettingBeans()){
            int requireColumnIndex = colSettingBean.getColumnIndex();
//            TODO:debug
            if(requireColumnIndex > totalColumnSize){
                throw new AssembleException("sheet" + sheet.getSheetName() + " row" + row.getRowNum() + " 需要长度：" + requireColumnIndex + ", 实际长度：" + totalColumnSize);
            }

            Cell cell = row.getCell(requireColumnIndex);
            if(cell == null){
                throw new AssembleException("sheet" + sheet.getSheetName() + " row" + row.getRowNum() + " 无法在" + requireColumnIndex + "获得cell");
            }

            try{
                loadObject(object, sheet, row, cell, colSettingBean);
            }catch (CellMistaken cellMistaken){
                cellMistaken.printStackTrace();

                if(rowMistaken == null){
                    String sheetName = sheet.getSheetName();
                    int rowIndex = row.getRowNum();
                    rowMistaken = new RowMistaken(sheetName, rowIndex);
                }

                rowMistaken.addCellMistaken(cellMistaken);
            }
        }

        if(rowMistaken != null && rowMistaken.getCellMistakenCount() != 0){
            throw rowMistaken;
        }
    }

    private static void loadObject(Object object, Sheet sheet, Row row, Cell cell, ColSettingBean colSettingBean) {
        int rowIndex = row.getRowNum();
        int colIndex = cell.getColumnIndex();

        boolean requireAllowEmpty = colSettingBean.isAllowEmopty();
        Method requireMethod = colSettingBean.getInjectMethod();
        ExcelColumnType columnType = colSettingBean.getColumnType();

        if(cell.getCellType().equals(CellType.BLANK)){
            if(! requireAllowEmpty){
                throw new CellMistaken(rowIndex, colIndex, "字段不允许为空");
            }
            return;
        }

//        根据类型，反射设置值
        try{
            switch (columnType){
                case STRING:
                    String strValue = CellTransformUtil.getCellStringValue(cell).trim();
                    requireMethod.invoke(object, strValue);
                    break;
                case NUMBER:
                    int intValue = CellTransformUtil.getCellNumberValue(cell);
                    requireMethod.invoke(object, intValue);
                    break;
                case STRING_PURE_NUMBER:
                    switch (cell.getCellType()){
                        case STRING:
                            try{
                                String sValue = CellTransformUtil.getCellStringValue(cell).trim();
                                if(sValue == null || sValue.length() == 0){
                                    throw new CellMistaken(rowIndex, colIndex, "不能是空的");
                                }
                                int iValue = Integer.valueOf((String) sValue);
                                requireMethod.invoke(object, sValue);
                            }catch (NumberFormatException e) {
                                throw new CellMistaken(rowIndex, colIndex, "该空不是纯数字字符");
                            }
                            break;
                        case NUMERIC:
                            String pureNumberStrValue = CellTransformUtil.getCellPureNumberStrValue(cell);
                            requireMethod.invoke(object, pureNumberStrValue);
                            break;
                    }
                    break;
            }
        }catch (CellTransformException cellTransformException){
            String message = cellTransformException.getMessage();
            throw new CellMistaken(rowIndex, colIndex, message);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void preLoadObject(Sheet sheet, Row row, ClassSettingBean classSettingBean) {
        eraseMistakenPrompt(row, classSettingBean);
    }

    private static void eraseMistakenPrompt(Row row, ClassSettingBean classSettingBean) {
        int tailIndex = classSettingBean.getColSettingBeans().size();
        MistakenHandleUtil.eraseMistakenPrompt(row, tailIndex);
    }

    private static boolean isHeaderRow(Row row, ClassSettingBean classSettingBean) {
        SheetSettingBean sheetSettingBean = classSettingBean.getSheetSettingBean();
        int requireHeaderRowIndex = sheetSettingBean.getHeaderRowIndex();
        return row.getRowNum() == requireHeaderRowIndex;
    }

    private static boolean isBlankRow(Row row, ClassSettingBean classSettingBean) {
        int totalColumnSize = classSettingBean.getColSettingBeans().size();
        int rowSize = row.getLastCellNum();
        int maxSize = rowSize < totalColumnSize ? rowSize : totalColumnSize;

        for (int i = 0; i < maxSize; i++){
            Cell cell = row.getCell(i);
            if(! cell.getCellType().equals(CellType.BLANK)){
                return false;
            }
        }
        return true;
    }

    private static boolean canLoadSheet(Sheet sheet, ClassSettingBean classSettingBean) {
        if(! checkSheetParamValid(sheet, classSettingBean)){
            return false;
        }
        return compareSheetName(sheet, classSettingBean) && compareSheetHeader(sheet, classSettingBean);
    }

    private static boolean compareSheetHeader(Sheet sheet, ClassSettingBean classSettingBean) {
        int requireHeaderRowIndex = classSettingBean.getSheetSettingBean().getHeaderRowIndex();
        Row headerRow = getHeaderRowFromSheet(sheet, requireHeaderRowIndex);
        return compareHeaderRow(sheet, headerRow, classSettingBean);
    }

    private static boolean compareHeaderRow(Sheet sheet, Row headerRow, ClassSettingBean classSettingBean) {
        String[] realHeaders = null;
        try{
            realHeaders = getHeaders(headerRow);
        }catch (AssembleException assembleException){
            throw new AssembleException("sheet:" + sheet.getSheetName() + " " + assembleException.getMessage());
        }
        String[] requireHeaders = getHeaders(classSettingBean);

        if(realHeaders.length < requireHeaders.length){
            throw new AssembleException("sheet" + sheet.getSheetName() + " 表头长度：" + realHeaders.length + "比设置的表头长度：" + requireHeaders.length + "少");
        }

//        TODO:怎么优化，on2
        for(String requireHeader : requireHeaders){
            boolean find = false;
            for(String realHeader : realHeaders){
                if(requireHeader.equals(realHeader)){
                    find = true;
                    break;
                }
            }
            if(! find){
                throw new AssembleException("sheet" + sheet.getSheetName() + "没有找到表头：" + requireHeader);
            }
        }

        return true;
    }

    private static String[] getHeaders(Row row) {
        if(row == null){
            throw new AssembleException("参数为空");
        }

        int rowSize = row.getLastCellNum();
        if(rowSize < 0){
            throw new AssembleException("表头长度小于0");
        }

        String[] headers = new String[rowSize];
        for(int i = 0; i < rowSize; i ++){
            Cell cell = row.getCell(i);
            String header = CellTransformUtil.getCellStringValue(cell).trim();
            if(header.length() <= 0){
                throw new AssembleException("表头第" + i + "个全是空格");
            }
            headers[i] = header;
        }
        return headers;
    }

    private static String[] getHeaders(ClassSettingBean classSettingBean) {
        List<ColSettingBean> colSettingBeanList = classSettingBean.getColSettingBeans();
        String[] headers = new String[colSettingBeanList.size()];
        for(int i  = 0; i < colSettingBeanList.size(); i ++){
            ColSettingBean colSettingBean = colSettingBeanList.get(i);
            headers[i] = colSettingBean.getColumnName().trim();
        }
        return headers;
    }

    private static boolean compareSheetName(Sheet sheet, ClassSettingBean classSettingBean) {
        SheetSettingBean sheetSettingBean = classSettingBean.getSheetSettingBean();
        String requireSheetName = sheetSettingBean.getSheetName();
        return sheet.getSheetName().equals(requireSheetName);
    }

    private static boolean checkSheetParamValid(Sheet sheet, ClassSettingBean classSettingBean) {
        if(sheet == null || classSettingBean == null){
            throw new NullPointerException();
        }

        SheetSettingBean sheetSettingBean = classSettingBean.getSheetSettingBean();
        List<ColSettingBean> colSettingBeanList = classSettingBean.getColSettingBeans();
        Class clazz = classSettingBean.getClazz();
        if(sheetSettingBean == null || colSettingBeanList == null || clazz == null){
            throw new NullPointerException();
        }

        if(colSettingBeanList.size() == 0){
            return false;
        }
        for(ColSettingBean colSettingBean : colSettingBeanList){
            if(colSettingBean == null){
                throw new NullPointerException();
            }
        }
        return true;
    }

    private static boolean canAssembleWithBean(Workbook workbook, ClassSettingBean classSettingBean) {
        if(workbook == null || workbook.getNumberOfSheets() == 0 || classSettingBean == null){
            return false;
        }

        SheetSettingBean sheetSettingBean = classSettingBean.getSheetSettingBean();
        List<ColSettingBean> colSettingBeans = classSettingBean.getColSettingBeans();

        if(sheetSettingBean == null || colSettingBeans == null){
            throw new NullPointerException();
        }

        if(colSettingBeans.size() == 0){
            return false;
        }

        for(ColSettingBean colSettingBean : colSettingBeans){
            if(colSettingBean == null){
                throw new NullPointerException();
            }
        }
        return true;
    }

    private static ClassSettingBean parseSettingFromClass(Class clazz) {
        return ParseSettingUtil.parseSettingFromClass(clazz);
    }

    /**
     * 判断是否可以进行
     * @param workbook
     * @param classes
     * @return
     */
    private static boolean canAssembleWithClasses(Workbook workbook, Class[] classes) {
        if(workbook == null || classes == null){
            return false;
        }

        return ! emptyClasses(classes) && ! emptyWorkbook(workbook);
    }

    private static boolean emptyWorkbook(Workbook workbook) {
        if(workbook == null || workbook.getNumberOfSheets() == 0){
            return true;
        }

        for(Sheet sheet : workbook){
            if(sheet == null){
                throw new NullPointerException();
            }
        }
        return true;
    }

    private static boolean emptyClasses(Class[] classes) {
        if(classes == null || classes.length == 0){
            return true;
        }
        for(Class clazz : classes){
            if(clazz == null){
//                TODO:这里的异常处理似乎才有意义
                throw new NullPointerException();
            }
        }
        return false;
    }

}
