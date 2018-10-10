package com.fabe2ry.service.impl;

import com.fabe2ry.dao.LogDao;
import com.fabe2ry.model.LogModel;
import com.fabe2ry.model.util.ListVo;
import com.fabe2ry.service.LogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiaoxq on 2018/9/20.
 */
@Service
//TODO:那个有用
public class LogServiceImpl implements LogService {

    @Autowired
    LogDao logDao;

    @Override
    public boolean insertLog(LogModel logModel) {
        return logDao.insertLog(logModel);
    }

    @Override
    public ListVo<List<LogModel>> getAllLogByPageHelper(int pageIndex, int pageSize) {
        if(pageIndex < 0 || pageSize <= 0){
            ListVo failListVo = new ListVo();
            failListVo.setSuccess(false);
            failListVo.setTotal(0);
            failListVo.setMessage("参数错误");
            failListVo.setResult(null);
            return failListVo;
        }
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo pageInfo = new PageInfo(logDao.getAllLog());

        List list = pageInfo.getList();
        int pageTotalCount = (int) pageInfo.getTotal();
        ListVo<List<LogModel>> returnVo = new ListVo<>();
        returnVo.setResult(list);
        returnVo.setMessage("查阅成功");
        returnVo.setTotal(pageTotalCount);
        returnVo.setSuccess(true);
        return returnVo;
    }
}
