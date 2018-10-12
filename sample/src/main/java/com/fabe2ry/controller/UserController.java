package com.fabe2ry.controller;

import com.fabe2ry.ExcelUtil;
import com.fabe2ry.aop.annotation.WebLogAnnotation;
import com.fabe2ry.controller.util.LoginHelper;
import com.fabe2ry.model.example.Goods;
import com.fabe2ry.model.result.SheetResult;
import com.fabe2ry.model.util.ListVo;
import com.fabe2ry.model.util.ResultVo;
import com.fabe2ry.service.ExcelService;
import com.fabe2ry.service.ImgService;
import com.fabe2ry.service.LogService;
import com.fabe2ry.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by xiaoxq on 2018/9/18.
 */
@RestController
@RequestMapping("/api/user/")
@CrossOrigin(origins = "http://localhost:63342/", maxAge = 100, exposedHeaders = {"Content-Disposition"})
public class UserController {

    Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    LogService logService;

    @Autowired
    ExcelService excelService;

    @Autowired
    ImgService imgService;

    @Value("${excel.local.path}")
    String excelLocalPath;

    @WebLogAnnotation(annotationName = "测试")
    @GetMapping("/test")
    public String test() throws Throwable {
        return userService.test();
    }

    @ApiOperation(value="用户注册", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "account", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "password", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "name", required = true, dataType = "String")
    })
    @WebLogAnnotation(annotationName = "注册")
    @PostMapping("/register")
    @ResponseBody
    public ResultVo register(@RequestParam String account, @RequestParam String password, @RequestParam String name){
        ResultVo returnResultVo = userService.register(account, password, name);
        return returnResultVo;
    }

    @ApiOperation(value="用户登陆", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "account", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "password", required = true, dataType = "String"),
    })
    @WebLogAnnotation(annotationName = "登陆")
    @PostMapping("/login")
    @ResponseBody
    public ResultVo login(HttpServletRequest request, HttpServletResponse response, @RequestParam String account, @RequestParam String password){
        ResultVo resultVo = userService.login(account, password);
//        添加cookie
        if(resultVo.isSuccess()){
            LoginHelper.addCookieAndSeesion(request, response, account, password);
        }
        return resultVo;
    }

    @ApiOperation(value="用户注销", notes="")
    @WebLogAnnotation(annotationName = "注销")
    @GetMapping("/logout")
    @ResponseBody
    public ResultVo logout(HttpServletRequest request, HttpServletResponse response){
//        注销cookie
        if(LoginHelper.checkHasLogined(request, response)){
            LoginHelper.removeCookieAndSession(request, response);
            ResultVo resultVo = new ResultVo();
            resultVo.setSuccess(true);
            resultVo.setResult(null);
            resultVo.setMessage("注销成功");
            return resultVo;
        }
        ResultVo resultVo = new ResultVo();
        resultVo.setSuccess(false);
        resultVo.setResult(null);
        resultVo.setMessage("请先登陆");
        return resultVo;
    }

    @ApiOperation(value="查询用户", notes="根据参数查询用户")
    @WebLogAnnotation(annotationName = "获取列表")
    @GetMapping("/getUsers/{pageIndex}/{pageSize}")
    @ResponseBody
    public ListVo getUsers(HttpServletRequest request, HttpServletResponse response,
                           @PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailListVo("请先登陆");
        }
//        ListVo resultVo = userService.getUsers(pageIndex, pageSize);
        ListVo resultVo = userService.getUsersByPageHelper(pageIndex, pageSize);
        return resultVo;
    }

    @ApiOperation(value="添加用户", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "account", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "password", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "name", required = true, dataType = "String"),
            @ApiImplicitParam(name = "privilege", value = "privilege", required = true, dataType = "int"),
    })
    @WebLogAnnotation(annotationName = "添加用户")
    @PostMapping("/add")
    @ResponseBody
    public ResultVo add(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam String account, @RequestParam String password,
                        @RequestParam String name, @RequestParam int privilege){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailResultVo("请先登陆");
        }

        String adminAccount = LoginHelper.getCookie(request, response);
        if(adminAccount == null){
            return LoginHelper.getFailResultVo("cookie出现错误");
        }

        return userService.addUser(adminAccount, account, password, name, privilege);
    }

    @ApiOperation(value="删除用户", notes="")
    @WebLogAnnotation(annotationName = "删除用户")
    @PostMapping("/delete")
    @ResponseBody
    public ResultVo delete(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam String account){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailResultVo("请先登陆");
        }

        String adminAccount = LoginHelper.getCookie(request, response);
        if(adminAccount == null){
            return LoginHelper.getFailResultVo("cookie出现错误");
        }

        return userService.deleteUser(adminAccount, account);
    }

    @ApiOperation(value="修改用户", notes="")
    @WebLogAnnotation(annotationName = "修改用户")
    @PostMapping("/update")
    @ResponseBody
    public ResultVo update(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam String account, @RequestParam String password, @RequestParam String name){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailResultVo("请先登陆");
        }

        String adminAccount = LoginHelper.getCookie(request, response);
        if(adminAccount == null){
            return LoginHelper.getFailResultVo("cookie出现错误");
        }

        return userService.updaetUser(adminAccount, account, password, name);
    }


    @ApiOperation(value="获取日志列表", notes="")
    @WebLogAnnotation(annotationName = "获取日志列表")
    @GetMapping("/getLogs/{pageIndex}/{pageSize}")
    @ResponseBody
    public ListVo getAllLog(HttpServletRequest request, HttpServletResponse response,
                            @PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailListVo("请先登陆");
        }
        ListVo resultVo = logService.getAllLogByPageHelper(pageIndex, pageSize);
        return resultVo;
    }

    @ApiOperation(value="获取goods测试excel", notes="")
    @WebLogAnnotation(annotationName = "获取goods测试excel")
    @GetMapping("/getTestExcel")
//    @ResponseBody
    public void getTestExcel(HttpServletRequest request, HttpServletResponse response){
        if(!LoginHelper.checkHasLogined(request, response)){
//            return LoginHelper.getFailListVo("请先登陆");
            return ;
        }

        Class[] classes = new Class[]{Goods.class};
        List<List> allList = ExcelUtil.createTestList(classes, 300);
        Workbook afterWookbook = ExcelUtil.createWorkBook(allList, classes);
        ExcelUtil.writeWorkBookToResponse(afterWookbook, response);

//        ListVo listVo = new ListVo();
//        listVo.setSuccess(true);
//        listVo.setTotal(allList.get(0).size());
//        listVo.setMessage("获取成功");
//        listVo.setResult(allList.get(0));
//        return listVo;
    }

    @ApiOperation(value="导入goods的excel", notes="会直接返回excel文件")
    @WebLogAnnotation(annotationName = "导入goods的excel")
    @PostMapping("/importGoodsExcelWithDirectReturn")
    @ResponseBody
    public ListVo importGoodsExcelWithDirectReturn(HttpServletRequest request, HttpServletResponse response){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailListVo("请先登陆");
        }

        try{
            Workbook workbook = ExcelUtil.getWorkbookFromRequest(request);

            SheetResult sheetResult = ExcelUtil.parseWorkbook(workbook, Goods.class);
            if(sheetResult != null){
                if(! sheetResult.contailError()){
                    excelService.storeGoodsList(sheetResult.getNormalObjectList());
                    ListVo listVo = new ListVo();
                    listVo.setSuccess(! sheetResult.contailError());
                    listVo.setTotal(sheetResult.getTotalRow());
                    listVo.setMessage("上传成功，总共上传条数：" + sheetResult.getTotalRow());
                    listVo.setResult(sheetResult.getNormalObjectList());
                    return listVo;
                }else{
                    ExcelUtil.writeWorkBookToResponse(workbook, response);
                    return null;
                }
            }
            return LoginHelper.getFailListVo("无法创建sheet result，设置存在错误");
        }catch (RuntimeException e){
            e.printStackTrace();
            return LoginHelper.getFailListVo(e.getMessage());
        }
    }

    @ApiOperation(value="导入goods的excel", notes="不会直接返回excel文件，而是返回excel文件链接")
    @WebLogAnnotation(annotationName = "导入goods的excel")
    @PostMapping("/importGoodsExcelWithReturn")
    @ResponseBody
    public ListVo importGoodsExcelWithReturn(HttpServletRequest request, HttpServletResponse response){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailListVo("请先登陆");
        }

        try{
            Workbook workbook = ExcelUtil.getWorkbookFromRequest(request);

            SheetResult sheetResult = ExcelUtil.parseWorkbook(workbook, Goods.class);
            if(sheetResult != null){
                if(! sheetResult.contailError()){
                    excelService.storeGoodsList(sheetResult.getNormalObjectList());
                    ListVo listVo = new ListVo();
                    listVo.setSuccess(! sheetResult.contailError());
                    listVo.setTotal(sheetResult.getTotalRow());
                    listVo.setMessage("上传成功，总共上传条数：" + sheetResult.getTotalRow());
                    listVo.setResult(sheetResult.getNormalObjectList());
                    return listVo;
                }else{
                    String localPath = excelLocalPath;
                    String fileName = UUID.randomUUID().toString() + ".xlsx";
                    String prefix = "http://localhost:8080/";

                    ExcelUtil.writeWorkBookToFile(workbook, localPath, fileName);
                    String url = prefix + fileName;

                    ListVo listVo = new ListVo();
                    listVo.setSuccess(! sheetResult.contailError());
                    listVo.setTotal(sheetResult.getTotalRow());
                    listVo.setMessage("上传失败, 包含错误条数：" + sheetResult.getErrorRow() + "   " + sheetResult.getErrorMessage());
                    listVo.setResult(url);
                    return listVo;
                }
            }
            return LoginHelper.getFailListVo("无法创建sheet result，设置存在错误");
        }catch (RuntimeException e){
            e.printStackTrace();
            return LoginHelper.getFailListVo(e.getMessage());
        }
    }

    @ApiOperation(value="获得Goods的列表", notes="")
    @WebLogAnnotation(annotationName = "获得Goods的列表")
    @GetMapping("/getGoods/{pageIndex}/{pageSize}")
    @ResponseBody
    public ListVo getAllGoods(HttpServletRequest request, HttpServletResponse response,
                              @PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailListVo("请先登陆");
        }
        ListVo resultVo = excelService.getAllGoodsByPageHelper(pageIndex, pageSize);
        return resultVo;
    }

    @ApiOperation(value="获得Goods的excel", notes="")
    @WebLogAnnotation(annotationName = "获得Goods的excel")
    @GetMapping("/exportGoodsExcel")
    public void exportGoodsExcel(HttpServletRequest request, HttpServletResponse response){
        if(!LoginHelper.checkHasLogined(request, response)){
            return;
        }
        excelService.exportGoodsExcel(request, response);
    }


    @ApiOperation(value = "上传图片")
    @WebLogAnnotation(annotationName = "上传图片")
    @PostMapping("/imgUplaod")
    public ResultVo imgUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile multipartFile){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailResultVo("请先登陆");
        }
        return imgService.imgUpload(multipartFile);
    }

//    @ApiOperation(value = "上传多个图片")
//    @WebLogAnnotation(annotationName = "上传多个图片")
//    @PostMapping("/multiImgUpload")
//    public void multiImgUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile[] multipartFiles){
////        if(!LoginHelper.checkHasLogined(request, response)){
////            return;
////        }
//        imgService.imgUpload(multipartFiles);
//    }


    @ApiOperation(value = "下载图片")
    @WebLogAnnotation(annotationName = "下载图片")
    @GetMapping("/imgDownload")
    public ResultVo imgDownload(HttpServletRequest request, HttpServletResponse response){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailResultVo("请先登陆");
        }

        return imgService.imgDownload();
    }

    @ApiOperation(value = "展示图片")
    @WebLogAnnotation(annotationName = "展示图片")
    @GetMapping("/imgShow")
    public ResultVo imgShow(HttpServletRequest request, HttpServletResponse response){
        if(!LoginHelper.checkHasLogined(request, response)){
            return LoginHelper.getFailResultVo("请先登陆");
        }

        return imgService.imgShow();
    }






}
