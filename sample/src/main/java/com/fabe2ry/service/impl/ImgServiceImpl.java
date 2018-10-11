package com.fabe2ry.service.impl;

import com.fabe2ry.controller.util.LoginHelper;
import com.fabe2ry.model.util.ResultVo;
import com.fabe2ry.service.ImgService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.UUID;

/**
 * Created by xiaoxq on 2018/10/11.
 */
@Service
public class ImgServiceImpl implements ImgService {

    public static String SESSION_KEY_IMG_PATH = "imgPath";

    @Value("${img.upload.path}")
    String imgLocalStorePath;

    @Override
    public ResultVo imgUpload(MultipartFile multipartFile) {
        if(multipartFile == null || multipartFile.isEmpty()){
            return LoginHelper.getFailResultVo("参数错误");
        }

        String imgName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String imgUploadStorePath = imgLocalStorePath;
        String imgFileName = uuid + "-" + imgName + ".png";

        saveImg(multipartFile, imgUploadStorePath, imgFileName);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("http://localhost:8080/" + imgFileName);
        resultVo.setMessage("上传成功");
        resultVo.setSuccess(true);
        return resultVo;
    }

    @Override
    public void imgUpload(MultipartFile[] multipartFiles) {
        for(MultipartFile multipartFile : multipartFiles){
            System.out.println(multipartFile.getName());
            System.out.println(multipartFile.getOriginalFilename());
            imgUpload(multipartFile);
        }
    }

    @Override
    public ResultVo imgDownload() {
        try{
            String imgFilename = smashSeession();
            return loadImg(imgFilename);
        }catch (RuntimeException e){
            return LoginHelper.getFailResultVo(e.getMessage());
        }
    }

    @Override
    public ResultVo imgShow() {
        try{
            String imgFilename = smashSeession();
            String url = "http://localhost:8080/" + imgFilename;

            ResultVo resultVo = new ResultVo();
            resultVo.setSuccess(true);
            resultVo.setMessage("获取成功");
            resultVo.setResult(url);
            return resultVo;
        }catch (RuntimeException e){
            ResultVo resultVo = new ResultVo();
            resultVo.setSuccess(false);
            resultVo.setMessage(e.getMessage());
            resultVo.setResult(null);
            return resultVo;
        }
    }

    private ResultVo loadImg(String imgFilename) {
        String imgFullPath = imgLocalStorePath + File.separator + imgFilename;
        File img = new File(imgFullPath);
        if(img.exists()){
            ResultVo resultVo = new ResultVo();
            resultVo.setSuccess(true);
            resultVo.setMessage("获取成功");
            resultVo.setResult("http://localhost:8080/" + imgFilename);
            return resultVo;
        }
        return LoginHelper.getFailResultVo("文件不存在");
    }

//    private void download(File img) {
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletResponse response = servletRequestAttributes.getResponse();
//
//        setHeader(response, img.getName());
//        transport(response, img);

//    }

    private void transport(HttpServletResponse response, File img) {
        try {
            FileInputStream fileInputStream = new FileInputStream(img);
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());

            byte[] bs = new byte[1024];
            int len;
            while((len = fileInputStream.read(bs)) != -1){
                bos.write(bs, 0, len);
            }
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setHeader(HttpServletResponse response, String filename) {
        response.setContentType("application/octet-stream");
        String encodeFileName = null;
        try {
            encodeFileName = new String(filename.getBytes("GB2312"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.addHeader("Content-Disposition", "attachment; filename=" + encodeFileName);
    }

    private String smashSeession() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        HttpSession httpSession = httpServletRequest.getSession();

        if(httpSession == null || httpSession.getAttribute(SESSION_KEY_IMG_PATH) == null){
            throw new RuntimeException("没有保存图片或者session过期");
        }

        return (String) httpSession.getAttribute(SESSION_KEY_IMG_PATH);
    }

    private void saveImg(MultipartFile multipartFile, String imgUploadStorePath, String imgFileName) {
        File directory = new File(imgUploadStorePath);
        if(!directory.exists()){
            directory.mkdirs();
        }

        try {
            FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
            String imgFullPath = imgUploadStorePath + File.separator + imgFileName;
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imgFullPath));

            byte[] bs = new byte[1024];
            int len;
            while((len = fileInputStream.read(bs)) != -1){
                bos.write(bs, 0, len);
            }
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupSeesion(imgFileName);
    }

    private void setupSeesion(String imgFileName) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        HttpSession httpSession = httpServletRequest.getSession();

        if(httpSession != null){
            httpSession.setAttribute(SESSION_KEY_IMG_PATH, imgFileName);
        }
    }



}
