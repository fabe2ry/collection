package com.fabe2ry.dao;

import com.fabe2ry.model.LogModel;
import com.github.pagehelper.Page;

/**
 * Created by xiaoxq on 2018/9/20.
 */
public interface LogDao {

//    插入日志
    boolean insertLog(LogModel logModel);

//    读取日志
    Page<LogModel> getAllLog();

}
