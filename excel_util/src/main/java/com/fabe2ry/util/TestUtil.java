package com.fabe2ry.util;

import com.fabe2ry.Exception.TestException;
import com.fabe2ry.model.ClassSettingBean;
import com.fabe2ry.model.ColSettingBean;
import com.fabe2ry.model.SheetSettingBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xiaoxq on 2018/9/30.
 */
//TODO：修饰符
public class TestUtil {

    static Random random = new Random();

    /**
     * 生成大量数据
     * @param classes
     * @return
     */
    public static List<List> createTestList(Class[] classes, int maxSize) {
        return createTestListWithClasses(classes, maxSize);
    }

    private static List<List> createTestListWithClasses(Class[] classes, int maxSize) {
        if(canCreateListWithClasses(classes, maxSize)){
            return startCreateListWithClasses(classes,  maxSize);
        }
        return null;
    }

    private static List<List> startCreateListWithClasses(Class[] classes, int maxSize) {
        List<List> allList = null;
        for(Class clazz : classes){
            List list = createTestListWithClass(clazz, maxSize);

            if(list != null){
                if(allList == null){
                    allList = new ArrayList<>();
                }
                allList.add(list);
            }
        }
        return allList;
    }

    private static boolean canCreateListWithClasses(Class[] classes, int maxSize) {
        if(classes == null){
            throw new NullPointerException();
        }

        for(Class clazz : classes){
            if(! canCreateListWithClass(clazz, maxSize)){
                return false;
            }
        }
        return true;
    }

    private static boolean allowMaxSize(int maxSize) {
        if(maxSize <= 0 || maxSize > 5000){
            throw new TestException("设置maxSize在要（0，5000）");
        }
        return true;
    }

    public static List createTestList(Class clazz, int maxSize) {
        return createTestListWithClass(clazz, maxSize);
    }

    private static List createTestListWithClass(Class clazz, int maxSize) {
        if(canCreateListWithClass(clazz, maxSize)){
            return startCreateListWithClass(clazz, maxSize);
        }
        return null;
    }

    private static List startCreateListWithClass(Class clazz, int maxSize) {
        ClassSettingBean classSettingBean = ParseSettingUtil.parseSettingFromClass(clazz);
        return createTestListWithBean(classSettingBean, maxSize);
    }

    private static List createTestListWithBean(ClassSettingBean classSettingBean, int maxSize) {
        if(canCreateListWithBean(classSettingBean, maxSize)){
            return startCreateListWithBean(classSettingBean, maxSize);
        }
        return null;
    }

    private static List startCreateListWithBean(ClassSettingBean classSettingBean, int maxSize) {
        List list = null;

        int i = 0;
        while(i ++ < maxSize) {
            Object object = loadObject(classSettingBean);
            if(object != null){
                if(list == null){
                    list = new ArrayList();
                }

                list.add(object);
            }
        }
        return list;
    }

    private static Object loadObject(ClassSettingBean classSettingBean) {
        List<ColSettingBean> colSettingBeanList = classSettingBean.getColSettingBeans();

        Object object = createEmptyObject(classSettingBean);
        if(object == null){
            throw new NullPointerException();
        }

        for(ColSettingBean colSettingBean : colSettingBeanList){
            injectValue(object, colSettingBean);
        }
        return object;
    }

    private static void injectValue(Object object, ColSettingBean colSettingBean) {
        if(canInjectValue(object, colSettingBean)){
            startInjectValue(object, colSettingBean);
        }
    }

    private static void startInjectValue(Object object, ColSettingBean colSettingBean) {
        Method injectMethod = colSettingBean.getInjectMethod();

        switch (colSettingBean.getColumnType()){
            case STRING_PURE_NUMBER:
                String pureNumberStringValue = createPureNumberStringValue();
                try {
                    injectMethod.invoke(object, pureNumberStringValue);
                } catch (Exception e) {
                    throw new TestException("STRING_PURE_NUMBER设置必须是String");
                }
                break;
            case NUMBER:
                int numberValue = createNumberValue();
                try {
                    injectMethod.invoke(object, numberValue);
                } catch (Exception e) {
                    throw new TestException("NUMBER设置必须是int");
                }
                break;
            case STRING:
                String stringValue = createStringValue();
                try {
                    injectMethod.invoke(object, stringValue);
                } catch (Exception e) {
                    throw new TestException("STRING设置必须是String");
                }
                break;
        }
    }

    private static String createStringValue() {
        int maxStringLength = 10;

        int chooseMaxStringLength = random.nextInt(maxStringLength) + 3;

        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < chooseMaxStringLength; i ++){
            int r = random.nextInt(26) + 'a';
            stringBuilder.append((char)r);
        }
        return stringBuilder.toString();
    }

    private static int createNumberValue() {
        return random.nextInt(Integer.MAX_VALUE - 10);
    }

    private static String createPureNumberStringValue() {
        int pureNumber = random.nextInt(Integer.MAX_VALUE - 10);
        return String.valueOf(pureNumber);
    }

    private static boolean canInjectValue(Object object, ColSettingBean colSettingBean) {
        if(object == null || colSettingBean == null){
            throw new NullPointerException();
        }

        Method method = colSettingBean.getInjectMethod();
        return method != null;
    }

    private static Object createEmptyObject(ClassSettingBean classSettingBean) {
        Class clazz = classSettingBean.getClazz();

        Object emptyObject = null;
        try {
            emptyObject = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return emptyObject;
    }

    private static boolean canCreateListWithBean(ClassSettingBean classSettingBean, int maxSize) {
        if(classSettingBean == null){
            throw new NullPointerException();
        }

        SheetSettingBean sheetSettingBean = classSettingBean.getSheetSettingBean();
        List<ColSettingBean> colSettingBeanList = classSettingBean.getColSettingBeans();

        if(sheetSettingBean == null || colSettingBeanList == null){
            throw new NullPointerException();
        }

        int rquireHeaderIndex = sheetSettingBean.getHeaderRowIndex();
        String requireSheetName = sheetSettingBean.getSheetName().trim();
        if(rquireHeaderIndex < 0){
            throw new TestException("rquireHeaderIndex小于0");
        }
        if(requireSheetName == null || requireSheetName.length() == 0){
            throw new TestException("requireSheetName不能为空");
        }

        for(ColSettingBean colSettingBean : colSettingBeanList){
            if(colSettingBean == null){
                throw new NullPointerException();
            }
        }

        return colSettingBeanList.size() != 0;
    }

    private static boolean canCreateListWithClass(Class clazz, int maxSize) {
        if(clazz == null){
            throw new NullPointerException();
        }

        return allowMaxSize(maxSize);
    }
}
