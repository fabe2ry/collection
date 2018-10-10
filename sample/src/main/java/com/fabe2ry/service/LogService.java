package com.fabe2ry.service;

import com.fabe2ry.model.LogModel;
import com.fabe2ry.model.util.ListVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiaoxq on 2018/9/20.
 */
@Service
public interface LogService {

//    插入日志
    boolean insertLog(LogModel logModel);

    ListVo<List<LogModel>> getAllLogByPageHelper(int pageIndex, int pageSize);

}
