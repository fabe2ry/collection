package com.fabe2ry;

import com.fabe2ry.Exception.ParseSettingException;
import com.fabe2ry.model.ClassSettingBean;
import com.fabe2ry.util.*;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class ExcelUtil {

    static Logger logger = Logger.getLogger(ExcelUtil.class.getName());

    /**
     * 从request获取workbook
     * @param request
     * @return
     */
    public static Workbook getWorkbookFromRequest(HttpServletRequest request) {
        return TransportUtil.getWorkbookFromRequest(request);
    }

    /**
     * 将workbook输出
     * @param workbook
     * @param response
     */
    public static void writeWorkBookToResponse(Workbook workbook, HttpServletResponse response) {
        TransportUtil.writeWorkBookToRespone(workbook, response);
    }

    /**
     * 解析workbook，转换成为对象
     * @param workbook
     * @param classes
     */
//    TODO:查查怎么公司和这边同步配置
    public static List<List> parseWorkbook(Workbook workbook, Class<?>[] classes){
        List<ClassSettingBean> classSettingBeans = ParseSettingUtil.parseSettingPhase(workbook, classes);
        if(classSettingBeans == null || classSettingBeans.size() <= 0){
            throw new ParseSettingException("解析注解错误，为空或者长度为0");
        }

        return AssembleUtil.assemble(workbook, classSettingBeans);
    }

    /**
     * 将对象到出workbook
     * @param allList
     * @param classes
     * @return
     */
    public static Workbook createWorkBook(List<List> allList, Class[] classes) {
        if(allList == null){
            throw new NullPointerException();
        }
//        TODO:添加map支持
        return DisAssembleUtil.disassemble(allList, classes);
    }


    /**
     * 便于测试大量数据
     * @return
     */
    public static List<List> createTestList(Class[] classes, int maxSize){
        return TestUtil.createTestList(classes, maxSize);
    }

    public static List createTestList(Class clazz, int maxSize){
        return TestUtil.createTestList(clazz, maxSize);
    }

}
