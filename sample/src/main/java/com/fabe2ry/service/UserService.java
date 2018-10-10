package com.fabe2ry.service;

import com.fabe2ry.model.User;
import com.fabe2ry.model.util.ListVo;
import com.fabe2ry.model.util.ResultVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiaoxq on 2018/9/18.
 */
@Service
public interface UserService {

    String test() throws Throwable;


    //    用户注册
//    account:账号
//    password：密码
//    name：用户名
    ResultVo<User> register(String account, String password, String name);

    //    根据account查询用户
//    account：账号
    boolean findUserByAccount(String account);

    //    用户登陆
//    account：账号
//    password：密码
    ResultVo<User> login(String account, String password);

    //    分页查询
//    pageIndex：当前页码
//    pageSize：每页的个数
    ListVo<List<User>> getUsers(int pageIndex, int pageSize);

//    获取用户总条数
    int getUsersCount();

//    使用pagehelper分页查询
    ListVo<List<User>> getUsersByPageHelper(int pageIndex, int pageSize);

//    增加用户接口
//    account:账号
//    password：密码
//    name：用户名
//    privilege：权限
    ResultVo<User> addUser(String account, String password, String name, int privilege);

    //    增加用户接口
//    adminAccount：管理员账号
//    account:账号
//    password：密码
//    name：用户名
//    privilege：权限
    ResultVo<User> addUser(String adminAccount, String account, String password, String name, int privilege);

//    判断用户是否权限大于1
    boolean judgeUserAdmin(String account);

//    寻找用户
    User findUser(String account);

//    删除用户
//    adminAccount：管理员账号
//    deleteAccount：被删除的账号
    ResultVo deleteUser(String adminAccount, String deleteAccount);

    //    删除用户
//    deleteAccount：被删除的账号
    ResultVo deleteUser(String deleteAccount);

//    修改用户
    ResultVo updaetUser(String adminAccount, String updateAccount,
                        String password, String name);


}
