package com.fabe2ry.service.util;

import com.fabe2ry.model.util.ResultVo;

import java.util.List;

/**
 * Created by xiaoxq on 2018/9/18.
 */
public class UserHelper {

    //    检查数据是错有错误
    public static boolean checkParasCorrect(List<String> params){
        for(int i = 0; i < params.size(); i ++){
            String param = params.get(i);
            if(param == null || param.length() <= 0){
                return false;
            }
        }
        return true;
    }

    //    返回一个错误的ResultVo
    public static ResultVo getFailResultVo(String message){
        ResultVo resultVo = new ResultVo();
        resultVo.setSuccess(false);
        resultVo.setMessage(message);
        resultVo.setResult(null);
        return resultVo;
    }

}
