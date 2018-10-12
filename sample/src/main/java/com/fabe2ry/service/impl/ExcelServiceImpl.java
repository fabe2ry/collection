package com.fabe2ry.service.impl;

import com.fabe2ry.ExcelUtil;
import com.fabe2ry.dao.GoodsDao;
import com.fabe2ry.model.example.Goods;
import com.fabe2ry.model.util.ListVo;
import com.fabe2ry.service.ExcelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxq on 2018/10/9.
 */
@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    GoodsDao goodsDao;

    @Override
    public boolean storeGoodsList(List<Goods> list) {
        if(list.size() != 0){
            return goodsDao.insert(list);
        }
        return false;
    }

    @Override
    public ListVo<List<Goods>> getAllGoodsByPageHelper(int pageIndex, int pageSize) {
        if(pageIndex < 0 || pageSize <= 0){
            ListVo failListVo = new ListVo();
            failListVo.setSuccess(false);
            failListVo.setTotal(0);
            failListVo.setMessage("参数错误");
            failListVo.setResult(null);
            return failListVo;
        }

        PageHelper.startPage(pageIndex, pageSize);
        PageInfo pageInfo = new PageInfo(goodsDao.getAllGoods());

        List list = pageInfo.getList();
        int pageTotalCount = (int) pageInfo.getTotal();
        ListVo<List<Goods>> returnVo = new ListVo<>();
        returnVo.setResult(list);
        returnVo.setMessage("查阅成功");
        returnVo.setTotal(pageTotalCount);
        returnVo.setSuccess(true);
        return returnVo;
    }

    @Override
    public void exportGoodsExcel(HttpServletRequest request, HttpServletResponse response) {
        List<Goods> list = goodsDao.getAllGoods();
        List<List> allList = new ArrayList<>();
        allList.add(list);

        Class[] classes  = {Goods.class};

        Workbook afterWookbook = ExcelUtil.createWorkBook(allList, classes);
        ExcelUtil.writeWorkBookToResponse(afterWookbook, response);
    }


}
