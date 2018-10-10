package com.fabe2ry.util;

import com.fabe2ry.ExcelUtil;
import com.fabe2ry.Exception.TransportExpection;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class TransportUtil {

    static Logger logger = Logger.getLogger(ExcelUtil.class.getName());

    /**
     * 根据excel文件后缀的名，生成对应的文档对象workbook
     * @param file
     * @return
     * @throws IOException
     */
    private static Workbook createWorkBook(MultipartFile file) throws IOException {
        Workbook workbook = null;
//        对xlsx和xls区分
        String importFileName = file.getOriginalFilename();
        if(importFileName.endsWith("xlsx")){
            workbook = new XSSFWorkbook(file.getInputStream());
        }else if(importFileName.endsWith("xls")){
            workbook = new HSSFWorkbook(file.getInputStream());
        }else{
            throw new TransportExpection("文件后缀名不对");
        }
        return workbook;
    }

    /**
     * 从request中获取workbook
     * @param request
     * @return
     */
    public static Workbook getWorkbookFromRequest(HttpServletRequest request){
        if(!(request instanceof  MultipartHttpServletRequest)){
            throw new TransportExpection("request不包含file文件");
        }
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile requestFile = multipartHttpServletRequest.getFile("file");
        if(requestFile == null){
            throw new TransportExpection("request file don't exist");
        }

        Workbook workbook = null;
        try {
            workbook = createWorkBook(requestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(workbook == null){
            logger.info("读取不正常，退出");
            throw new TransportExpection("读取不正常");
        }

        return workbook;
    }

    /**
     * 将workbook输出
     * @param workbook
     * @param response
     */
    public static void writeWorkBookToRespone(Workbook workbook, HttpServletResponse response) {
        if(workbook == null || response == null){
            throw new NullPointerException();
        }

        String downloadFileName = "下载.xlsx";
        setResponse(response, downloadFileName);

        try {
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加header
     * @param response
     * @param downloadFileName
     */
    private static void setResponse(HttpServletResponse response, String downloadFileName) {
//        设置header
        response.setContentType("application/octet-stream");

//        处理文件中文名
        String encodeFileName = null;
        try {
            encodeFileName = new String(downloadFileName.getBytes("GB2312"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        response.addHeader("Content-Disposition", "attachment; filename=" + encodeFileName);
    }
}
