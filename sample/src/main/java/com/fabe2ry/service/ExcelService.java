package com.fabe2ry.service;

import com.fabe2ry.model.example.Goods;
import com.fabe2ry.model.util.ListVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xiaoxq on 2018/10/9.
 */
//@Service
public interface ExcelService {

    boolean storeGoodsList(List<Goods> list);

    ListVo<List<Goods>> getAllGoodsByPageHelper(int pageIndex, int pageSize);

    void exportGoodsExcel(HttpServletRequest request, HttpServletResponse response);
}
