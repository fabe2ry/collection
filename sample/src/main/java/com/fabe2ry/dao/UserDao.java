package com.fabe2ry.dao;

import com.fabe2ry.model.User;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * Created by xiaoxq on 2018/9/18.
 */
public interface UserDao {

    //    插入用户
    boolean insert(User user);

    //    根据account查询用户
    List<User> selectUserByAccount(User user);

    //    根据account和password
    List<User> selectUserByAccountAndPassword(User user);

    //    根据当前页码和页数量来查询
    List<User> selectUserByPage(int start, int pageSize);

//    获取总条数
    int selectTotal();

//    使用pagehelper
    Page<User> selectTotalForPageHelper();

//    删除用户
    boolean delete(User user);

//    修改用户
    boolean update(User user);

}
