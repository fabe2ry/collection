package com.fabe2ry.util;

import com.fabe2ry.Exception.ParseSettingException;
import com.fabe2ry.annotation.ExcelColumnSetting;
import com.fabe2ry.annotation.ExcelSheetSetting;
import com.fabe2ry.model.ClassSettingBean;
import com.fabe2ry.model.ColSettingBean;
import com.fabe2ry.model.SheetSettingBean;
import com.fabe2ry.type.ExcelColumnType;
import org.apache.poi.ss.usermodel.Workbook;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class ParseSettingUtil {

    /**
     * 解析参数阶段
     * @param workbook
     * @param classes
     */
    public static List<ClassSettingBean> parseSettingPhase(Workbook workbook, Class<?>[] classes) {
        if(workbook == null || classes == null || classes.length <= 0){
            throw new ParseSettingException("参数错误");
        }

        List<ClassSettingBean> list = null;
        for(Class clazz : classes){
            ClassSettingBean classSettingBean = tempFunctionName(clazz);

            if(list == null){
                list = new ArrayList<>();
            }
            list.add(classSettingBean);
        }
        return list;
    }

    /**
     *  TODO；这里太耦合了，代码也不能复用
     *  TODO:要会改代码
     *  TODO:改代码，要遵守一定原则，不要改出bug
     *  TODO:这样获得list，返回list有点蠢
     *  TODO:临时解耦开，名字记得换
     * @param clazz
     * @return
     */
    private static ClassSettingBean tempFunctionName(Class clazz) {
        if(clazz == null){
            throw new NullPointerException();
        }

//        TODO:更多时候不用这样一行简单的注释，代码名字就很好表面意思了
        if(canParse(clazz)){
//                sheet
            SheetSettingBean sheetSettingBean = parseClassSheetSetting(clazz);

//                column
            List<ColSettingBean> colSettingBeans = parseClassColSettings(clazz);

//                verify
            if(canAssemble(sheetSettingBean, colSettingBeans)){
//                    assemble
                ClassSettingBean classSettingBean = new ClassSettingBean();
                classSettingBean.setSheetSettingBean(sheetSettingBean);
                classSettingBean.setColSettingBeans(colSettingBeans);
                classSettingBean.setClazz(clazz);

                return classSettingBean;
            }
        }
        return null;
    }

    /**
     * 判断是否解析结果正确
     * @param sheetSettingBean
     * @param colSettingBeans
     * @return
     */
    private static boolean canAssemble(SheetSettingBean sheetSettingBean, List<ColSettingBean> colSettingBeans) {
        if(sheetSettingBean == null || colSettingBeans == null){
            return false;
        }

//        允许一个sheet，而没有col
        if(colSettingBeans.size() <= 0){
            return true;
        }

        return true;
    }

    /**
     * 解析clazz的ColSettingBean
     * @param clazz
     * @return
     */
    private static List<ColSettingBean> parseClassColSettings(Class clazz) {
        List<ColSettingBean> list = null;
//        TODO:是否使用getField
        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){
            ColSettingBean colSettingBean = parseClassColSetting(field);
            if(colSettingBean == null){
                continue;
            }
            InjectMethod(colSettingBean, clazz, field);

            if(colSettingBean != null){
                if(list == null){
                    list = new ArrayList<>();
                }
                list.add(colSettingBean);
            }
        }

//        默认长度为零
        if(list == null){
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 设置反射方法
     * @param colSettingBean
     * @param field
     */
    private static void InjectMethod(ColSettingBean colSettingBean, Class beanClazz, Field field) {
        if(colSettingBean == null || field == null || beanClazz == null){
            throw new ParseSettingException("方法参数为空");
        }

        String fieldName = field.getName();
        PropertyDescriptor propertyDescriptor = null;
        try {
            propertyDescriptor = new PropertyDescriptor(fieldName, beanClazz);
        } catch (IntrospectionException e) {
            throw new ParseSettingException("无法获取get或set方法在" + beanClazz.toString() + " " + fieldName);
        }

        if(propertyDescriptor == null){
            throw new ParseSettingException("无法获取get或set方法在" + beanClazz.toString() + " " + fieldName);
        }

        Method getMethod = propertyDescriptor.getWriteMethod();
        if(getMethod == null){
            throw new ParseSettingException("获取到的get方法为空");
        }
        Method setMethod = propertyDescriptor.getReadMethod();
        if(setMethod == null){
            throw new ParseSettingException("获取到的set方法为空");
        }

        colSettingBean.setInjectMethod(getMethod);
        colSettingBean.setEctractMethod(setMethod);
    }

    /**
     * 解析field的ColSettingBean
     * @param field
     * @return
     */
    private static ColSettingBean parseClassColSetting(Field field) {
        boolean canAnnotation = field.isAnnotationPresent(ExcelColumnSetting.class);
        if(!canAnnotation){
            return null;
        }

//        get and verify
        ExcelColumnSetting columnSetting = field.getAnnotation(ExcelColumnSetting.class);
        String columnName = columnSetting.columnName().trim();
        if(columnName == null || columnName.length() <= 0){
            throw new ParseSettingException("columnName不能为空");
        }

        int columnExportOrder = columnSetting.exportOrder();
        if(columnExportOrder <= 0){
            throw new ParseSettingException("columnOrder要大于0");
        }

//        校验数据类型
        ExcelColumnType columnType = columnSetting.columnType();
        compareColumnType(columnType, field);

        boolean allowEmpty = columnSetting.allowEmpty();

        int columnIndex = columnSetting.columnIndex();
        if(columnIndex < -1){
            throw new ParseSettingException("columnIndex不能小于-1");
        }

//        TODO:暂时不支持该字段
        tempLimit(columnIndex);

//        assemble
        ColSettingBean colSettingBean = new ColSettingBean();
        colSettingBean.setColumnName(columnName);
        colSettingBean.setExportOrder(columnExportOrder);
        colSettingBean.setColumnType(columnType);
        colSettingBean.setAllowEmopty(allowEmpty);
        colSettingBean.setColumnIndex(columnIndex);
        return colSettingBean;
    }

    /**
     * 校验用户设置数据类型是否正确
     * @param columnType
     * @param field
     */
    private static void compareColumnType(ExcelColumnType columnType, Field field) {
        if(columnType == null || field == null){
            throw new ParseSettingException("参数不能为空");
        }

        Class fieldType = field.getType();
        if(! isBasicType(field)){
            throw new ParseSettingException("只能注解在基础类型上");
        }


        switch (columnType){
            case STRING_PURE_NUMBER:
            case STRING:
                if(! fieldType.equals(String.class)){
                    throw new ParseSettingException("希望：" + columnType + ", 实际：" + fieldType);
                }
                break;
            case NUMBER:
                if(! (fieldType.equals(int.class) || field.equals(Integer.class))){
                    throw new ParseSettingException("希望：" + columnType + ", 实际：" + fieldType);
                }
                break;
        }
    }

    /**
     * 判断数据类型是否是基础数据类型
     * @param field
     * @return
     */
    private static boolean isBasicType(Field field) {
        Class fieldType = field.getType();
        return isBasicType(fieldType);
    }

    /**
     * 返回基础类型判断
     * @param fieldType
     * @return
     */
    private static boolean isBasicType(Class fieldType) {
//        TODO:暂时只支持这些
        Class[] basicTypes = {
                int.class,
                Integer.class,
                String.class
        };

        for(Class clazz : basicTypes){
            if(clazz.equals(fieldType)){
                return true;
            }
        }
        return false;
    }

    /**
     * 临时方法，用于限制columnIndex为-1
     * @param columnIndex
     */
    private static void tempLimit(int columnIndex) {
        if(columnIndex != -1){
            throw new ParseSettingException("columnIndex只能为-1");
        }
    }

    /**
     * 解析clazz的ColSettingBean
     * @param clazz
     * @return
     */
    private static SheetSettingBean parseClassSheetSetting(Class clazz) {
        boolean isAnnotation = clazz.isAnnotationPresent(ExcelSheetSetting.class);
        if(!isAnnotation){
            return null;
        }

        ExcelSheetSetting sheetSetting = (ExcelSheetSetting) clazz.getAnnotation(ExcelSheetSetting.class);

//        get and verify
        String sheetName = sheetSetting.sheetName().trim();
        if(sheetName == null || sheetName.length() <= 0) {
            throw new ParseSettingException("sheetName全是空格");
        }

        int headerRowIndex = sheetSetting.headerRowIndex();
        if(headerRowIndex < 0){
            throw new ParseSettingException("headerRowIndex小于0");
        }

//        assemble
        SheetSettingBean sheetSettingBean = new SheetSettingBean();
        sheetSettingBean.setSheetName(sheetName);
        sheetSettingBean.setHeaderRowIndex(headerRowIndex);

        return sheetSettingBean;
    }

    /**
     * 判断是否可以解析
     * @param clazz
     * @return
     */
    private static boolean canParse(Class clazz) {
        if(clazz.isAnnotationPresent(ExcelSheetSetting.class)){
            return true;
        }
//        TODO:是否支持getFields
//        Field[]  fields = clazz.getFields();
        Field[]  fields = clazz.getDeclaredFields();
        for(Field field : fields){
            boolean canFiledAnnotation = field.isAnnotationPresent(ExcelColumnSetting.class);
            if(canFiledAnnotation){
                return true;
            }
        }

        return false;
    }

    /**
     * TODO:这里对private方法进行一层包装，这样就算我开放出来的代码有修改，也不会影响外面
     * @param clazz
     * @return
     */
    public static ClassSettingBean parseSettingFromClass(Class clazz) {
        if(clazz == null){
            throw new NullPointerException();
        }
        return tempFunctionName(clazz);
    }
}
