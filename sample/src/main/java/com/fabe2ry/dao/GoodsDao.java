package com.fabe2ry.dao;

import com.fabe2ry.model.example.Goods;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * Created by xiaoxq on 2018/10/9.
 */
public interface GoodsDao {

    boolean insert(List<Goods> list);

    Page<Goods> getAllGoods();
}
