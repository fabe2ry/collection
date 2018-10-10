package com.fabe2ry.service.impl;

import com.fabe2ry.dao.UserDao;
import com.fabe2ry.model.User;
import com.fabe2ry.model.util.ListVo;
import com.fabe2ry.model.util.ResultVo;
import com.fabe2ry.service.UserService;
import com.fabe2ry.service.util.UserHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxq on 2018/9/18.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public String test() throws Throwable {
        int i = 1;
        if(i == 1){
            throw new Throwable("test");
        }
        return "test";
    }

    public ResultVo register(String account, String password, String name){
        //        检查输入数据
        List<String> list = new ArrayList<>();
        list.add(account);
        list.add(password);
        list.add(name);
        if(!UserHelper.checkParasCorrect(list)){
            return UserHelper.getFailResultVo("输入信息不正确");
        }

//        生成user类
        int defaultPrivilege = 1;
        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setName(name);
        user.setPrivilege(defaultPrivilege);

//        检查是否重复注册
        if(findUserByAccount(account)){
            ResultVo failResultVo = UserHelper.getFailResultVo("注册失败，账号已存在");
            return failResultVo;
        }

//        根据dao插入结果，返回数据
        ResultVo resultVo = new ResultVo();
        if(userDao.insert(user)){
            resultVo.setSuccess(true);
            resultVo.setMessage("注册成功");
            resultVo.setResult(user);
        }else{
            resultVo.setSuccess(false);
            resultVo.setMessage("注册失败");
            resultVo.setResult(null);
        }

        return resultVo;
    }

    @Override
    public boolean findUserByAccount(String account) {
//        拼装查询数据
        User user = new User();
        user.setAccount(account);

        List list = userDao.selectUserByAccount(user);
        if(list.size() > 0)
            return true;

        return false;
    }

    @Override
    public ResultVo<User> login(String account, String password) {
        //        检查输入数据
        List<String> list = new ArrayList<>();
        list.add(account);
        list.add(password);
        if(!UserHelper.checkParasCorrect(list)){
            return UserHelper.getFailResultVo("输入信息不正确");
        }

//        拼装查询数据
        User user = new User();
        user.setAccount(account);
        user.setPassword(password);

        List users = userDao.selectUserByAccountAndPassword(user);
        if(users.size() <= 0){
            ResultVo failResultVo = UserHelper.getFailResultVo("用户不存在或者密码错误");
            return failResultVo;
        }

        ResultVo resultVo = new ResultVo();
        resultVo.setSuccess(true);
        resultVo.setMessage("登陆成功");
        resultVo.setResult(users.get(0));
        return resultVo;
    }

    @Override
    public ListVo<List<User>> getUsers(int pageIndex, int pageSize) {
        if(pageIndex < 0 || pageSize <= 0){
            ListVo failListVo = new ListVo();
            failListVo.setSuccess(false);
            failListVo.setMessage("输入信息不正确");
            failListVo.setTotal(0);
            failListVo.setResult(null);
            return failListVo;
        }

        int start = pageIndex * pageSize;
        List list = userDao.selectUserByPage(start, pageSize);
        ListVo failListVo = new ListVo();
        failListVo.setSuccess(true);
        failListVo.setResult("查询成功");
        failListVo.setResult(list);

        int totalUser = getUsersCount();
        failListVo.setTotal(totalUser);

        return failListVo;
    }

    @Override
    public int getUsersCount() {
        int totalUser = userDao.selectTotal();
        return totalUser;
    }

    @Override
    public ListVo<List<User>> getUsersByPageHelper(int pageIndex, int pageSize) {
        if(pageIndex < 0 || pageSize <= 0){
            ListVo failListVo = new ListVo();
            failListVo.setSuccess(false);
            failListVo.setMessage("输入信息不正确");
            failListVo.setTotal(0);
            failListVo.setResult(null);
            return failListVo;
        }

        PageHelper.startPage(pageIndex, pageSize);
        PageInfo pageInfo = new PageInfo(userDao.selectTotalForPageHelper());

        List list = pageInfo.getList();
        int total = (int) pageInfo.getTotal();

        ListVo listVo = new ListVo();
        listVo.setSuccess(true);
        listVo.setMessage("查询成功");
        listVo.setTotal(total);
        listVo.setResult(list);
        return listVo;
    }

    @Override
    public ResultVo<User> addUser(String account, String password, String name, int privilege) {
        List checkList = new ArrayList();
        checkList.add(account);
        checkList.add(password);
        checkList.add(name);
        if(!UserHelper.checkParasCorrect(checkList) || privilege <= 0){
            return UserHelper.getFailResultVo("输入信息有错误");
        }

        if(findUser(account) != null){
            return UserHelper.getFailResultVo("用户已经存在");
        }

        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setName(name);
        user.setPrivilege(privilege);

        boolean success = userDao.insert(user);

        ResultVo resultVo = new ResultVo();
        if(success){
            resultVo.setSuccess(true);
            resultVo.setMessage("添加用户成功");
            resultVo.setResult(user);
        }else{
            resultVo.setSuccess(false);
            resultVo.setMessage("添加用户失败");
            resultVo.setResult(null);
        }

        return resultVo;
    }

    @Override
    public ResultVo<User> addUser(String adminAccount, String account, String password, String name, int privilege) {
        List checkList = new ArrayList();
        checkList.add(adminAccount);
        checkList.add(account);
        checkList.add(password);
        checkList.add(name);
        if(!UserHelper.checkParasCorrect(checkList) || privilege <= 0){
            return UserHelper.getFailResultVo("输入信息有错误");
        }

        if(!judgeUserAdmin((adminAccount))){
            ResultVo failResultVo = new ResultVo();
            failResultVo.setSuccess(false);
            failResultVo.setMessage("用户权限不够");
            failResultVo.setResult(null);
            return failResultVo;
        }

        ResultVo resultVo = addUser(account, password, name, privilege);
        return resultVo;
    }

    @Override
    public boolean judgeUserAdmin(String account) {
        User user = findUser(account);
        if(user != null){
            if(user.getPrivilege() > 1){
                return true;
            }
        }
        return false;
    }

    @Override
    public User findUser(String account) {
        User user = new User();
        user.setAccount(account);
        List list = userDao.selectUserByAccount(user);
        if(list.size() >= 1){
            return (User) list.get(0);
        }
        return null;
    }

    @Override
    public ResultVo deleteUser(String adminAccount, String deleteAccount) {
        List checkList = new ArrayList();
        checkList.add(deleteAccount);
        checkList.add(adminAccount);
        if(!UserHelper.checkParasCorrect(checkList)){
            return UserHelper.getFailResultVo("输入数据错误");
        }

        User userAdmin = findUser(adminAccount);
        User deleteUser = findUser(deleteAccount);

        if(userAdmin == null){
            return UserHelper.getFailResultVo("管理员账号不存在");
        }
        if(deleteUser == null){
            return UserHelper.getFailResultVo("被删除用户不存在");
        }

        if(!judgeUserAdmin(adminAccount)){
            return UserHelper.getFailResultVo("管理员账号权限不够");
        }

        return deleteUser(deleteAccount);
    }

    @Override
    public ResultVo deleteUser(String deleteAccount) {
        List checkList = new ArrayList();
        checkList.add(deleteAccount);
        if(!UserHelper.checkParasCorrect(checkList)){
            return UserHelper.getFailResultVo("输入数据错误");
        }

        if(findUser(deleteAccount) == null){
            return UserHelper.getFailResultVo("用户不存在");
        }

        User user = new User();
        user.setAccount(deleteAccount);

        boolean success = userDao.delete(user);
        if(!success){
            return UserHelper.getFailResultVo("删除失败");
        }

        ResultVo resultVo = new ResultVo();
        resultVo.setSuccess(true);
        resultVo.setMessage("删除成功");
        resultVo.setResult(null);
        return resultVo;
    }

    @Override
    public ResultVo updaetUser(String adminAccount, String updateAccount, String password, String name) {
        List checkList = new ArrayList();
        checkList.add(adminAccount);
        checkList.add(updateAccount);
        checkList.add(password);
        checkList.add(name);
        if(!UserHelper.checkParasCorrect(checkList)){
            return UserHelper.getFailResultVo("参数错误");
        }

        if(!judgeUserAdmin(adminAccount)){
            return UserHelper.getFailResultVo("管理员账号不存在错误权限不够");
        }
        User updateUser = findUser(updateAccount);
        if(updateUser == null){
            return UserHelper.getFailResultVo("被修改用户不存在");
        }

        updateUser.setPassword(password);
        updateUser.setName(name);
        if(!userDao.update(updateUser)){
            return UserHelper.getFailResultVo("修改失败");
        }

        ResultVo resultVo = new ResultVo();
        resultVo.setSuccess(true);
        resultVo.setResult(updateUser);
        resultVo.setMessage("修改成功");
        return resultVo;
    }


}
