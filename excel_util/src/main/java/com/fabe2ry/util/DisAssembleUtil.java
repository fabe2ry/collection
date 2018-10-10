package com.fabe2ry.util;

import com.fabe2ry.Exception.DisassembleException;
import com.fabe2ry.model.ClassSettingBean;
import com.fabe2ry.model.ColSettingBean;
import com.fabe2ry.model.SheetSettingBean;
import com.fabe2ry.type.ExcelColumnType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * Created by xiaoxq on 2018/9/29.
 */
public class DisAssembleUtil {

//    TODO;一些问题，怎么解耦，获得泛型，空指针

    static Logger logger = Logger.getLogger(DisAssembleUtil.class.getName());

    public static Workbook disassemble(List<List> allList, Class[] classes){
        return disassembleWithClasses(allList, classes);
    }

    public static Workbook disassemble(Workbook workbook, List<List> allList, Class[] classes){
        return disassembleWithClasses(workbook, allList, classes);
    }

    public static Workbook disassemble(List list, Class clazz){
        return disassembleWithClass(null, list, clazz);
    }

    public static Workbook disassemble(Workbook workbook, List list, Class clazz){
        return disassembleWithClass(workbook, list, clazz);
    }


    /**
     * 用于拆分对象成workbook
     * @param allList
     * @param classes
     */
    private static Workbook disassembleWithClasses(List<List> allList, Class[] classes) {
        if(allList == null){
            throw new NullPointerException();
        }
        if(allList.size() <= 0 || classes.length <= 0){
            return null;
        }

        return disassembleWithClasses(null, allList, classes);
    }

    /**
     *
     * @param workbook
     * @param allList
     * @param classes
     * @return
     */
    private static Workbook disassembleWithClasses(Workbook workbook, List<List> allList, Class[] classes) {
        if(allList == null || classes == null){
            throw new NullPointerException();
        }

        if(canDisassembleWithClasses(allList, classes)){
            if(workbook == null) {
                workbook = createEmptyWorkbook(true);
            }

            workbook = startDisassembleWithClasses(workbook, allList, classes);
        }
        return workbook;
    }

    /**
     *  @param workbook
     * @param allList
     * @param classes
     */
    private static Workbook startDisassembleWithClasses(Workbook workbook, List<List> allList, Class[] classes) {
        if(workbook == null || allList == null || classes == null){
            throw new NullPointerException();
        }

//        TODO:这里不在重复检查长度，逻辑耦合了，想想怎么设计
        if(allList.size() != classes.length || allList.size() == 0){
            throw new DisassembleException("参数长度为0，或者不相等");
        }

        int sameSize = allList.size();
        for(int i = 0; i < sameSize; i ++){
            List list = allList.get(i);
            Class clazz = classes[i];

//            TODO;贼蠢，如果可能返回null，就gg了
            workbook = disassembleWithClass(workbook, list, clazz);
        }
        return workbook;
    }

    /**
     *
     * @param workbook
     * @param list
     * @param clazz
     */
    private static Workbook disassembleWithClass(Workbook workbook, List list, Class clazz) {
        if(list == null || clazz == null){
            throw new NullPointerException();
        }

        if(workbook == null){
            workbook = createEmptyWorkbook(true);
        }

        if(canDisassembleWithClass(list, clazz)){
            startDisassembleWithClass(workbook, list, clazz);
        }
        return workbook;
    }

    /**
     * 开始
     * @param workbook
     * @param list
     * @param clazz
     */
    private static void startDisassembleWithClass(Workbook workbook, List list, Class clazz) {
        if(workbook == null || list == null || clazz == null){
            throw new NullPointerException();
        }

        ClassSettingBean classSettingBean = parseSettingFromClass(clazz);
        disassembleWithBean(workbook, list, classSettingBean);
    }

    /**
     *
     * @param workbook
     * @param list
     * @param classSettingBean
     */
    private static void disassembleWithBean(Workbook workbook, List list, ClassSettingBean classSettingBean) {
        if(workbook == null || list == null || classSettingBean == null){
            throw new NullPointerException();
        }

        Sheet sheet = extractSheetFromWorkbook(workbook, classSettingBean);
        disassembleWithBean(sheet, list, classSettingBean);
    }

    /**
     *
     * @param sheet
     * @param list
     * @param classSettingBean
     */
    private static void disassembleWithBean(Sheet sheet, List list, ClassSettingBean classSettingBean){
        if(sheet == null || list == null || classSettingBean == null){
            throw new DisassembleException();
        }

        if(canLoadSheet(sheet, list, classSettingBean)){
            preLoadSheet(classSettingBean);
            startLoadSheet(sheet, list, classSettingBean);
        }
    }

    /**
     *
     * @param sheet
     * @param list
     * @param classSettingBean
     */
    private static void startLoadSheet(Sheet sheet, List list, ClassSettingBean classSettingBean) {
        if(! emptyClassSettingBean(classSettingBean)){
            loadSheetHeaderRow(sheet, classSettingBean);

            if(! emptyList(list)){
                loadSheetBody(sheet, list, classSettingBean);
            }
        }
    }

    /**
     *
     * @param sheet
     * @param list
     * @param classSettingBean
     */
    private static void loadSheetBody(Sheet sheet, List list, ClassSettingBean classSettingBean) {
        SheetSettingBean sheetSettingBean = classSettingBean.getSheetSettingBean();
        String requireSheetName = sheetSettingBean.getSheetName();
        int requireHeaderIndex = sheetSettingBean.getHeaderRowIndex();
//        从设置的header下标开始添加数据
        int nowRowIndex = requireHeaderIndex + 1;

        for(Object object : list){
            if(nowRowIndex < 0 && nowRowIndex == requireHeaderIndex){
                throw new DisassembleException(String.format("表： %s 无法创建row: %d, 行数错误",
                        requireSheetName, nowRowIndex));
            }
            Row row = sheet.createRow(nowRowIndex);
            nowRowIndex ++;
            if(row == null){
                throw new DisassembleException(String.format("无法在表:%s, row: %d创建行", requireSheetName, nowRowIndex));
            }

            loadRowBody(row, object, classSettingBean);
        }
    }

    /**
     *
     * @param row
     * @param object
     * @param classSettingBean
     */
    private static void loadRowBody(Row row, Object object, ClassSettingBean classSettingBean) {
        SheetSettingBean sheetSettingBean = classSettingBean.getSheetSettingBean();
        String requireSheetName = sheetSettingBean.getSheetName();

        List<ColSettingBean> colSettingBeanList = classSettingBean.getColSettingBeans();

        for(ColSettingBean colSettingBean : colSettingBeanList){
            int requireColumnIndex = colSettingBean.getColumnIndex();
            if(requireColumnIndex < 0){
                throw new DisassembleException(String.format("表： %s row: %d 无法创建cell，下标：%d 小于0",
                        requireSheetName, row.getRowNum(), requireColumnIndex));
            }

            Cell cell = row.createCell(requireColumnIndex);
            if(cell == null){
                throw new DisassembleException(String.format("表： %s row: %d col: %d 无法创建cell",
                        requireSheetName, row.getRowNum(), requireColumnIndex));
            }
            loadCellBody(row, cell, object, colSettingBean, requireSheetName);
        }
    }

    /**
     *
     * @param row
     * @param cell
     * @param object
     * @param colSettingBean
     * @param requireSheetName
     */
    private static void loadCellBody(Row row, Cell cell, Object object, ColSettingBean colSettingBean, String requireSheetName) {
        int rowIndex = row.getRowNum();
        int cellIndex = cell.getColumnIndex();
        if(cellIndex != colSettingBean.getColumnIndex()){
            throw new DisassembleException(String.format("表：%s 行：%d 装载cell时候出现错误，cell设置冲突 cellIndex：%d, column settiong: %d",
                    requireSheetName, rowIndex, cellIndex, colSettingBean.getColumnIndex()));
        }

        ExcelColumnType columnType = colSettingBean.getColumnType();
        boolean allowEmpty = colSettingBean.isAllowEmopty();
        Method extractMethod = colSettingBean.getEctractMethod();

        if(extractMethod == null){
            throw new DisassembleException(String.format("表：%s 行：%d 列：%d 装载cell时候出现错误，get方法为空",
                    requireSheetName, rowIndex, cellIndex));
        }

        try{
            extractCell(object, cell, extractMethod, columnType, allowEmpty);
        }catch (DisassembleException disassembleException){
            throw new DisassembleException(String.format("表：%s 行：%d 列：%d 错误信息：%s",
                    requireSheetName, rowIndex, cellIndex, disassembleException.getMessage()));
        }
    }
//TODO;解决int空是0可以跳过
    private static void extractCell(Object object, Cell cell, Method extractMethod, ExcelColumnType columnType, boolean allowEmpty) {
        try {
            Object value = extractMethod.invoke(object);
            if(value == null){
                if(! allowEmpty){
                    throw new DisassembleException("数据不可以为空");
                }
                extractEmptyData(cell, columnType);
            }else{
                extractNormalData(value, cell, columnType);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void extractNormalData(Object value, Cell cell, ExcelColumnType columnType) {
        Class objectClass = value.getClass();
        switch (columnType){
            case STRING:
                if(value.getClass() != String.class){
                    throw new DisassembleException("类型设置错误，STRING需要string");
                }
                cell.setCellValue(String.valueOf(value));
                cell.setCellType(CellType.STRING);
                break;
            case NUMBER:
                if(value.getClass() != int.class && value.getClass() != Integer.class){
//                    TODO：看看int和Integer有什么区别
                    throw new DisassembleException("类型设置错误，NUMBER需要int或者Integer");
                }
                cell.setCellValue((Integer)value);
                cell.setCellType(CellType.NUMERIC);
                break;
            case STRING_PURE_NUMBER:
                if(value.getClass() != String.class){
                    throw new DisassembleException("类型设置错误，STRING_PURE_NUMBER需要String");
                }
                try{
                    int intValue = Integer.valueOf((String) value);
//                TODO:格式化
                    cell.setCellValue(intValue);
                    cell.setCellType(CellType.NUMERIC);
                }catch (NumberFormatException e){
                    throw new DisassembleException("value:" + value + " 无法转换为int， STRING_PURE_NUMBER必须是纯数字字符");
                }
                break;
        }
    }

    private static void extractEmptyData(Cell cell, ExcelColumnType columnType) {
        switch (columnType){
            case STRING:
                cell.setCellValue("");
                cell.setCellType(CellType.STRING);
                break;
            case NUMBER:
                cell.setCellValue(0);
                cell.setCellType(CellType.NUMERIC);
                break;
            case STRING_PURE_NUMBER:
                cell.setCellValue(0);
                cell.setCellType(CellType.NUMERIC);
                break;
        }
    }

    private static boolean emptyClassSettingBean(ClassSettingBean classSettingBean) {
        return classSettingBean == null || classSettingBean.getSheetSettingBean() == null ||
                classSettingBean.getColSettingBeans() == null ||
                classSettingBean.getColSettingBeans().size() == 0;
    }

    /**
     * 判断是否为空
     * @param list
     * @return
     */
    private static boolean emptyList(List list) {
        return list == null || list.size() == 0;
    }


    /**
     * 判断是否可以装载
     * @param sheet
     * @param list
     * @param classSettingBean
     * @return
     */
    private static boolean canLoadSheet(Sheet sheet, List list, ClassSettingBean classSettingBean) {
        if(sheet == null || list == null || classSettingBean == null){
            throw new NullPointerException();
        }

        SheetSettingBean sheetSetting = classSettingBean.getSheetSettingBean();
        int requireHeaderRowIndex = sheetSetting.getHeaderRowIndex();
        List<ColSettingBean> colSettingBeanList = classSettingBean.getColSettingBeans();

//        TODO：提示信息更加人性化，更有意义
//        TODO；can方法来进行所有的逻辑判断，用来保护之后了逻辑异常，减少重复代码，用逻辑耦合减少代码，可能会降低鲁棒性
//        TODO:sheetSetting == null看看这个的提示
        if(sheetSetting == null || colSettingBeanList == null){
            throw new DisassembleException(String.format("表：%s，  excelSheetSetting设置出现问题", sheet.getSheetName()));
        }
        if(requireHeaderRowIndex < 0){
            throw new DisassembleException(String.format("表：%s， excelSheetSetting设置出现问题", sheet.getSheetName()));
        }

        return true;
    }

    /**
     * 预处理
     * @param classSettingBean
     */
    private static void preLoadSheet(ClassSettingBean classSettingBean) {
        InjectHeaderRowIndex(classSettingBean);
    }

    /**
     * 设置表头下标
     * @param classSettingBean
     */
    private static void InjectHeaderRowIndex(ClassSettingBean classSettingBean) {
        List<ColSettingBean> colSettingBeanList = classSettingBean.getColSettingBeans();
        if(colSettingBeanList.size() == 0){
            return;
        }

//        TODO:on的算法
        Stack<ColSettingBean> stack = new Stack<>();
        Stack<ColSettingBean> bucket = new Stack<>();
        for(ColSettingBean colSettingBean : colSettingBeanList){
            if(stack.empty()){
                stack.push(colSettingBean);
            }else{
                ColSettingBean topBean = stack.peek();
                while(topBean.getExportOrder() < colSettingBean.getExportOrder()){
                    bucket.push(stack.pop());
                    if(! stack.empty()){
                        topBean = stack.peek();
                    }
                }
                stack.push(colSettingBean);
                while(! bucket.empty()){
                    stack.push(bucket.pop());
                }
            }
        }

        int maxColumnIndex = stack.size() - 1;
        while(! stack.empty()){
            ColSettingBean nowBean = stack.pop();
            nowBean.setColumnIndex(maxColumnIndex);
            maxColumnIndex --;
        }
    }


    /**
     * 创建header
     * @param sheet
     * @param classSettingBean
     */
    private static void loadSheetHeaderRow(Sheet sheet, ClassSettingBean classSettingBean) {
        if(sheet == null || classSettingBean == null){
            throw new NullPointerException();
        }

        SheetSettingBean sheetSetting = classSettingBean.getSheetSettingBean();
        int requireHeaderRowIndex = sheetSetting.getHeaderRowIndex();
        List<ColSettingBean> colSettingBeanList = classSettingBean.getColSettingBeans();

        if(sheetSetting == null || colSettingBeanList == null){
            throw new DisassembleException(String.format("表 %s 装载表头出现问题， 检查excelSheetSetting设置是否问题", sheet.getSheetName()));
        }
        if(requireHeaderRowIndex < 0){
            throw new DisassembleException(String.format("表 %s 装载表头出现问题， headerRowIndex小于0", sheet.getSheetName()));
        }

        Row headerRow = sheet.createRow(requireHeaderRowIndex);
        if(headerRow == null){
            throw new DisassembleException("表 %s 装载表头出现问题， 无法生成表头行");
        }

        for(ColSettingBean colSettingBean : colSettingBeanList){
            if(classSettingBean != null){
                String requireColumnName = colSettingBean.getColumnName();
                int requireColumnIndex = colSettingBean.getColumnIndex();

                Cell cell = headerRow.createCell(requireColumnIndex);
                if(cell == null){
                    throw new DisassembleException(String.format("无法创建cell在row：%d col: %d 创建表头：%s",
                            headerRow.getRowNum(), requireColumnIndex, requireColumnName));
                }
                cell.setCellValue(requireColumnName);
                continue;
            }
            throw new NullPointerException();
        }
    }

    /**
     * 抽取sheet
     * @param workbook
     * @param classSettingBean
     * @return
     */
    private static Sheet extractSheetFromWorkbook(Workbook workbook, ClassSettingBean classSettingBean) {
        if(workbook == null || classSettingBean == null){
            throw new NullPointerException();
        }

        String requireSheetName = classSettingBean.getSheetSettingBean().getSheetName().trim();
        if(hasSheetName(workbook, requireSheetName)){
            throw new DisassembleException("已经存在表名：" + requireSheetName);
        }

        return workbook.createSheet(requireSheetName);
    }

    /**
     * 判断是否重复
     * @param workbook
     * @param requireSheetName
     * @return
     */
    private static boolean hasSheetName(Workbook workbook, String requireSheetName) {
        if(workbook == null || requireSheetName == null || requireSheetName.length() == 0){
            throw new NullPointerException();
        }

        for(Sheet sheet : workbook){
            if(sheet != null){
                String realSheetName = sheet.getSheetName();
                if(realSheetName.equals(requireSheetName)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 对工具类的包装
     * @param clazz
     * @return
     */
    private static ClassSettingBean parseSettingFromClass(Class clazz) {
        return ParseSettingUtil.parseSettingFromClass(clazz);
    }

    /**
     *  TODO:不管有几个入口会来到这里，都可以一样处理，这里只有逻辑，没有前后关联的考虑
     * @param list
     * @param clazz
     * @return
     */
    private static boolean canDisassembleWithClass(List list, Class clazz) {
        if(list == null || clazz == null){
            throw new NullPointerException();
        }
//        TODO:使用范性进行判断
//        TODO:这个函数不能去附加去判断==0的逻辑
        if(list.size() == 0){
//            TODO:这里用什么return，对比差别
//            不允许，不能继续
//            throw new DisassembleException("不能disassemble，长度为0");
//            允许，不用继续
            return true;
        }

        Object objectInList = list.get(0);
        String objectInListTypeName = objectInList.getClass().getTypeName();
        String clazzTypeName = clazz.getTypeName();

        return clazz.equals(objectInList.getClass());
    }

    /**
     * 判断是否可以进行
     * @param allList
     * @param classes
     * @return
     */
    private static boolean canDisassembleWithClasses(List<List> allList, Class[] classes) {
        if(emptyAssembleData(allList, classes)){
            return false;
        }
        return isSameSizeAssembleData(allList, classes);
    }

    /**
     * 判断是否相同长度
     * @param allList
     * @param classes
     * @return
     */
    private static boolean isSameSizeAssembleData(List<List> allList, Class[] classes) {
        if(allList == null || classes == null){
            throw new NullPointerException();
        }

        int allListSize = allList.size();
        int classesSize = classes.length;
        if(allListSize != classesSize){
            throw new DisassembleException(String.format("list size: %d  classes size: %d 不相等", allListSize, classesSize));
        }
        return true;
    }

    /**
     * 判断是为为空
     * @param allList
     * @param classes
     * @return
     */
    private static boolean emptyAssembleData(List<List> allList, Class[] classes) {
        return allList == null || allList.size() <= 0 ||
                classes == null || classes.length == 0;
    }

//    /**
//     *
//     * @param workbook
//     * @param list
//     * @param clazz
//     * @return
//     */
//    private static Workbook disassembleWithClass(Workbook workbook, List list, Class clazz) {
//        if(workbook == null || list == null || clazz == null){
//            throw new NullPointerException();
//        }
//
////        TODO:这样解耦，list=o的情况，放入startDisassembleWithClass
////        TODO：startDisassembleWithClass参数这么搞，返回怎么搞
////        TODO:命名和注释不能太随意，要养成好习惯
////          TODO:if(list == 0)判断太耦合了，emptyList()方法可以，但是还有有点耦合，应该起一个阶段的preDisassemble，放入里面
////        TODO：短的代码可以不用用方法封装
//
////        TODO:看看有没有类似todo的标签
////        TODO:合理划分逻辑，可以灵活的调整和服用
////        TODO：要想清楚，这样才不会出现方法间部分耦合preDisassembleWirhClass做的可能和canDisassembleWithClass有部分耦合，重复不说，可能还影响性能
////        TODO:而且后期不好测试，debug
////        TODO:删除函数和添加函数，要维护顺序，不能太随意，说明逻辑混乱
////        TODO:要反思为什么写那么多只能用一次的函数，说明不够复用，可能还是有问题
////        TODO:分太多阶段也不好，会混乱，不好找bug，而且说明逻辑不够简单
////        TODO:代码的东西想清楚，还是很有意思的
//        if(canDisassembleWithClass(list, clazz)){
//            ClassSettingBean classSettingBean = preDisassembleWithClass(clazz);
//
//            startDisassembleWithClass(workbook, list, classSettingBean);
//        }
//        return workbook;
//    }

    /**
     * 返回相应的workbook
     * @param needXlsx
     * @return
     */
    private static Workbook createEmptyWorkbook(boolean needXlsx) {
        if(needXlsx){
            return new XSSFWorkbook();
        }
        return new HSSFWorkbook();
    }

}
