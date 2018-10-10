package com.fabe2ry.util;

import com.fabe2ry.Exception.AssembleException;
import com.fabe2ry.Exception.CellMistaken;
import com.fabe2ry.Exception.CellTransformException;
import com.fabe2ry.Exception.RowMistaken;
import com.fabe2ry.model.ClassSettingBean;
import com.fabe2ry.model.ColSettingBean;
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

//    /**
//     *
//     * @param workbook
//     * @param classes
//     * @return
//     */
//    public static List<List> assemble(Workbook workbook, Class[] classes){
//        if(workbook == null || classes == null){
//            throw new NullPointerException();
//        }
//
//        return assembleWithClasses(workbook, classes);
//    }
//
//    /**
//     *
//     * @param workbook
//     * @param classes
//     * @return
//     */
//    private static List<List> assembleWithClasses(Workbook workbook, Class[] classes) {
//        if(workbook == null || classes == null){
//            throw new NullPointerException();
//        }
//        List<List> allList = null;
//        if(canAssembleWithClasses(workbook, classes)){
//            return startAssembleWithClasses(workbook, classes);
//        }
//        return allList;
//    }
//
//    private static List<List> startAssembleWithClasses(Workbook workbook, Class[] classes) {
//        List<List> allList = null;
//        for(Class clazz : classes){
//            List list = assembleWithClass(workbook, clazz);
//
//            if(list != null){
//                if(allList == null){
//                    allList = new ArrayList<>();
//                }
//                allList.add(list);
//            }
//        }
//        return allList;
//    }
//
//    private static List assembleWithClass(Workbook workbook, Class clazz) {
//        ClassSettingBean classSettingBean = parseSettingFromClass(clazz);
//        return assembleWithBean(workbook, classSettingBean);
//    }
//
//    private static List assembleWithBean(Workbook workbook, ClassSettingBean classSettingBean) {
//        if(canAssembleWithBean(workbook, classSettingBean)){
//            return startAssembleBean(workbook, classSettingBean);
//        }
//        return null;
//    }
//
//    private static List startAssembleBean(Workbook workbook, ClassSettingBean classSettingBean) {
//        return loadSheet(workbook, classSettingBean);
//    }
//
//    private static List loadSheet(Workbook workbook, ClassSettingBean classSettingBean) {
//        String requireSheetName = classSettingBean.getSheetSettingBean().getSheetName().trim();
//        if(requireSheetName == null || requireSheetName.length() == 0){
//            throw new AssembleException("表名为空，或者不存在");
//        }
//        Sheet sheet = workbook.getSheet(requireSheetName);
//        if(sheet == null){
//            throw new AssembleException("无法找到表名：" + requireSheetName);
//        }
//        return loadSheet(sheet, classSettingBean);
//    }
//
//    private static List loadSheet(Sheet sheet, ClassSettingBean classSettingBean){
////        TODO:从这里开始
//        return null;
//    }
//
//    private static boolean canAssembleWithBean(Workbook workbook, ClassSettingBean classSettingBean) {
//        if(workbook == null || workbook.getNumberOfSheets() == 0 || classSettingBean == null){
//            return false;
//        }
//
//        SheetSettingBean sheetSettingBean = classSettingBean.getSheetSettingBean();
//        List<ColSettingBean> colSettingBeans = classSettingBean.getColSettingBeans();
//
//        if(sheetSettingBean == null || colSettingBeans == null){
//            throw new NullPointerException();
//        }
//
//        if(colSettingBeans.size() == 0){
//            return false;
//        }
//
//        for(ColSettingBean colSettingBean : colSettingBeans){
//            if(colSettingBean == null){
//                throw new NullPointerException();
//            }
//        }
//        return true;
//    }
//
//    private static ClassSettingBean parseSettingFromClass(Class clazz) {
//        return ParseSettingUtil.parseSettingFromClass(clazz);
//    }
//
//    /**
//     * 判断是否可以进行
//     * @param workbook
//     * @param classes
//     * @return
//     */
//    private static boolean canAssembleWithClasses(Workbook workbook, Class[] classes) {
//        if(workbook == null || classes == null){
//            return false;
//        }
//
//        return ! emptyClasses(classes) && ! emptyWorkbook(workbook);
//    }
//
//    private static boolean emptyWorkbook(Workbook workbook) {
//        if(workbook == null || workbook.getNumberOfSheets() == 0){
//            return true;
//        }
//
//        for(Sheet sheet : workbook){
//            if(sheet == null){
//                throw new NullPointerException();
//            }
//        }
//        return true;
//    }
//
//    private static boolean emptyClasses(Class[] classes) {
//        if(classes == null || classes.length == 0){
//            return true;
//        }
//        for(Class clazz : classes){
//            if(clazz == null){
////                TODO:这里的异常处理似乎才有意义
//                throw new NullPointerException();
//            }
//        }
//        return false;
//    }

    /**
     *  @param workbook
     * @param classSettingBeans
     */
    public static List<List> assemble(Workbook workbook, List<ClassSettingBean> classSettingBeans) {
        if(workbook == null || classSettingBeans == null || classSettingBeans.size() <= 0)
            throw new NullPointerException("参数为空");

        List<List> allList = null;

        for(ClassSettingBean classSettingBean : classSettingBeans){
            String requireSheetName = classSettingBean.getSheetSettingBean().getSheetName();
            Sheet sheet = workbook.getSheet(requireSheetName);
            if(sheet == null){
                throw new AssembleException("找不到sheet:" + requireSheetName);
            }

            List list = assemble(sheet, classSettingBean);
            if(list != null){
                if(allList == null){
                    allList = new ArrayList<>();
                }
                allList.add(list);
            }
        }
        return allList;
    }

    /**
     * 装载一个sheet对应一个class注解对象
     * @param sheet
     * @param classSettingBean
     * @return
     */
    private static List assemble(Sheet sheet, ClassSettingBean classSettingBean) {
        if(sheet == null || classSettingBean == null){
            throw new NullPointerException("参数为空");
        }

        List list = null;

        if(canAssemble(sheet, classSettingBean)){
            prepareAssemble(sheet, classSettingBean);

            list = startAssemble(sheet, classSettingBean);

            list = afterAssemble(list);
        }

        return list;
    }

    /**
     * 对异常的情况进行挽救
     * @param list
     */
    private static List afterAssemble(List list) {
//        TODO:解决指针问题
        if(list == null){
            return new ArrayList();
        }
        return list;
    }

    /**
     * 解析对象
     * @param sheet
     * @param classSettingBean
     * @return
     */
    private static List startAssemble(Sheet sheet, ClassSettingBean classSettingBean) {
        if(sheet == null || classSettingBean == null){
            throw new NullPointerException("参数不能为空");
        }

        Class clazz = classSettingBean.getClazz();
        if(clazz == null){
            throw new AssembleException("clazz不能为空");
        }

        List list = null;
        int headerRowIndex = classSettingBean.getSheetSettingBean().getHeaderRowIndex();
        int totalColumnSize = classSettingBean.getColSettingBeans().size();
        for(Row row : sheet){
//            跳过header row
            if(row.getRowNum() == headerRowIndex){
                continue;
            }

//            跳过空行
            if(isBlankRow(row, totalColumnSize)){
                continue;
            }

            int tailIndex = totalColumnSize;
            prepareLoadObject(row, tailIndex);

            try {
                Object object = clazz.newInstance();
                try{
                    loadObject(object, row, classSettingBean);
                }catch (RowMistaken rowMistaken){
//                    这行数据出现问题，跳过这行数据
                    object = null;
                }

                if(object != null){
                    if(list == null){
                        list = new ArrayList();
                    }

                    list.add(object);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 装载数据前，预处理
     * @param row
     */
    private static void prepareLoadObject(Row row, int tailIndex) {
        eraseMistakenPrompt(row, tailIndex);
    }

    /**
     * 擦出错误信息
     * @param row
     */
    private static void eraseMistakenPrompt(Row row, int tailIndex) {
        MistakenHandleUtil.eraseMistakenPrompt(row, tailIndex);
    }

    /**
     * 判断是否是空行
     * @param row
     * @param totalColumnSize
     * @return
     */
    private static boolean isBlankRow(Row row, int totalColumnSize) {
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


    /**
     * 调用反射生成对象
     * @param object
     * @param row
     * @param classSettingBean
     */
    private static void loadObject(Object object, Row row, ClassSettingBean classSettingBean) {
        if(object == null || row == null || classSettingBean == null){
            throw new NullPointerException("参数为空");
        }

        List<ColSettingBean> list = classSettingBean.getColSettingBeans();
        if(list == null || list.size() == 0){
            throw new AssembleException("classSettingBean不能为空");
        }

        int realTotalColumnSize = row.getLastCellNum();
        int requireTotalColumnSize = list.size();

//        行异常
        RowMistaken rowMistaken = null;

        for(ColSettingBean colSettingBean : list){
            int requireColumnIndex = colSettingBean.getColumnIndex();
            if(requireColumnIndex > realTotalColumnSize){
                throw new AssembleException("需要长度：" + requireColumnIndex + ", 实际长度：" + realTotalColumnSize);
            }

            try {
                Cell cell = row.getCell(requireColumnIndex);
                if(cell == null){
                    int rowIndex = row.getRowNum();
                    int colIndex = requireColumnIndex;
                    String mistaken = "不能为空";
                    throw new CellMistaken(rowIndex, colIndex, mistaken);
                }
                loadObject(object, row, cell, colSettingBean);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (CellMistaken cellMistaken){
//                填表格出现的错误，需要提示
                handlerMistaken(row, requireTotalColumnSize, cellMistaken);

//                提交给上层
                if(rowMistaken == null){
                    rowMistaken = new RowMistaken();
                }
                rowMistaken.addCellMistakenCount();
            }
        }

        if(rowMistaken != null && rowMistaken.getCellMistakenCount() != 0){
            throw rowMistaken;
        }
    }

    /**
     * 添加错误信息
     * @param row
     * @param cellMistaken
     */
    private static void handlerMistaken(Row row, int tailIndex, CellMistaken cellMistaken) {
//        默认可以
        boolean allowAddMistakenMessage = true;
        if(allowAddMistakenMessage){
            MistakenHandleUtil.createMistakenPrompt(row, tailIndex, cellMistaken.getRowIndex(), cellMistaken.getColIndex(), cellMistaken.getMessage());
        }


    }


    /**
     * 装载数据
     * @param object
     * @param row
     * @param cell
     * @param colSettingBean
     */
    private static void loadObject(Object object, Row row, Cell cell, ColSettingBean colSettingBean) throws InvocationTargetException, IllegalAccessException {
        int rowIndex = row.getRowNum();
        int colIndex = cell.getColumnIndex();

        boolean requireAllowEmpty = colSettingBean.isAllowEmopty();
        Method requireMethod = colSettingBean.getInjectMethod();
        ExcelColumnType columnType = colSettingBean.getColumnType();

        if(cell.getCellType().equals(CellType.BLANK)){
            if(!requireAllowEmpty){
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
        }
    }

    /**
     * 装载对象前预处理
     * @param sheet
     * @param classSettingBean
     */
    private static void prepareAssemble(Sheet sheet, ClassSettingBean classSettingBean) {
        if(sheet == null || classSettingBean == null){
            throw new AssembleException("参数为空");
        }

//        header下标预处理
        int requireHeaderIndex = classSettingBean.getSheetSettingBean().getHeaderRowIndex();
        Row headerRow = getHeaderRowFromSheet(sheet, requireHeaderIndex);
        prepareHeaderIndex(headerRow, classSettingBean);
    }

    /**
     * 处理header下标
     * @param headerRow
     * @param classSettingBean
     */
    private static void prepareHeaderIndex(Row headerRow, ClassSettingBean classSettingBean) {
        if(headerRow == null || classSettingBean == null){
            throw new AssembleException("参数为空");
        }

        List<ColSettingBean> colSettingBeanList = classSettingBean.getColSettingBeans();
        if(colSettingBeanList == null){
            throw new AssembleException("list为空");
        }

        int requireSize = colSettingBeanList.size();
        int realSize = headerRow.getLastCellNum();

        if(requireSize == 0){
            return;
        }
        if(requireSize > realSize){
            throw new AssembleException("需要header size:" + requireSize + ", 实际header size:" + realSize);
        }

        injectHeaderIndex(headerRow, colSettingBeanList);
    }

    /**
     * 实际赋值
     * @param headerRow
     * @param colSettingBeanList
     */
    private static void injectHeaderIndex(Row headerRow, List<ColSettingBean> colSettingBeanList) {
        if(headerRow == null || colSettingBeanList == null){
            throw new AssembleException("参数不能为空");
        }

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
                if(!headerIndexMap.containsKey(requireColumnName)){
                    throw new AssembleException("无法为header：" + requireColumnName + "找到下标");
                }

                int autoFindHeaderIndex = headerIndexMap.get(requireColumnName);
//                注入下标
                colSettingBean.setColumnIndex(autoFindHeaderIndex);
            }else{
//                用户手动设置了index
//                int autoFindHeaderIndex = headerIndexMap.get(requireColumnName);
//                TODO：暂时不支持，有问题待解决，
            }
        }
    }


    /**
     * 判断是否可以装载
     * @param sheet
     * @param classSettingBean
     * @return
     */
    private static boolean canAssemble(Sheet sheet, ClassSettingBean classSettingBean) {
        if(sheet == null || classSettingBean == null)
            throw new AssembleException("参数不能为空");

        String requireSheetName = classSettingBean.getSheetSettingBean().getSheetName();
        if(! compareSheetName(sheet, requireSheetName)){
            return false;
        }

        if(! compareHeader(sheet, classSettingBean)){
            return false;
        }

        return true;
    }

    /**
     * 判断header是否对应
     * @param sheet
     * @param classSettingBean
     * @return
     */
    private static boolean compareHeader(Sheet sheet, ClassSettingBean classSettingBean) {
        if(sheet == null || classSettingBean == null){
            throw new AssembleException("参数为空");
        }

        int requireHeaderRowIndex = classSettingBean.getSheetSettingBean().getHeaderRowIndex();
        if(requireHeaderRowIndex < 0){
            throw new AssembleException("headerRowIndex小于0");
        }

        Row headerRow = getHeaderRowFromSheet(sheet, requireHeaderRowIndex);
        if(headerRow == null){
            throw new AssembleException("无法在" + requireHeaderRowIndex + "获取headerRow");
        }

        return compareHeader(headerRow, classSettingBean);
    }

    /**
     * 获取表头
     * @param sheet
     * @param requireHeaderRowIndex
     * @return
     */
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


    /**
     * 判断header行是否满足
     * @param row
     * @param classSettingBean
     * @return
     */
    private static boolean compareHeader(Row row, ClassSettingBean classSettingBean){
        List<ColSettingBean> colSettingList = classSettingBean.getColSettingBeans();
        int requireHeaderLength = colSettingList.size();
        String sheetName = classSettingBean.getSheetSettingBean().getSheetName();

        if(requireHeaderLength == 0){
//            没有column注解，允许这种情况
            return false;
        }

//        收集设置的header
        String[] requireHeaders = getHeaders(colSettingList);

//        收集row的header
        String[] realHeaders = getHeaders(row);

        boolean result;
        try {
            result = compareHeader(realHeaders, requireHeaders);
        }catch (AssembleException assembleException){
            throw new AssembleException("表：" + sheetName + " " + assembleException.getMessage());
        }
        return result;
    }

    /**
     * 比较2个headder
     * @param realHeaders
     * @param requireHeaders
     * @return
     */
    private static boolean compareHeader(String[] realHeaders, String[] requireHeaders) {
        int realHeaderSize = realHeaders.length;
        int requireHeaderSize = requireHeaders.length;
        if(realHeaderSize == 0){
            return false;
        }
        if(requireHeaderSize > realHeaderSize){
            return false;
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
                throw new AssembleException("没有找到表头：" + requireHeader);
            }
        }

        return true;
    }

    /**
     * 获取表头字符
     * @param row
     * @return
     */
    private static String[] getHeaders(Row row) {
        if(row == null){
            throw new AssembleException("参数为空");
        }

        int rowSize = row.getLastCellNum();
        if(rowSize < 0){
            throw new AssembleException("header row长度小于0");
        }

        String[] headers = new String[rowSize];
        for(int i = 0; i < rowSize; i ++){
            Cell cell = row.getCell(i);
            String header = CellTransformUtil.getCellStringValue(cell).trim();
            if(header.length() <= 0){
                throw new AssembleException("row header全是空格");
            }

            headers[i] = header;
        }
        return headers;
    }

    /**
     * 获取表头字符
     * @param colSettingList
     * @return
     */
    private static String[] getHeaders(List<ColSettingBean> colSettingList) {
        if(colSettingList == null){
            throw new AssembleException("参数为空");
        }

        if(colSettingList.size() == 0){
            return null;
        }

        int size = colSettingList.size();
        String[] headers = new String[size];

        for(int i  = 0; i < size; i ++){
            ColSettingBean colSettingBean = colSettingList.get(i);
            if(colSettingBean == null){
                throw new AssembleException();
            }

            headers[i] = colSettingBean.getColumnName().trim();
        }

        return headers;
    }


    /**
     * 判断sheet名字是否对应
     * @param sheet
     * @param requireSheetName
     * @return
     */
    private static boolean compareSheetName(Sheet sheet, String requireSheetName) {
        if(sheet == null || requireSheetName == null){
            throw new AssembleException("参数不能为空");
        }

        String realSheetName = sheet.getSheetName();
        if(realSheetName.equals(realSheetName)){
            return true;
        }
        return false;
    }
}
