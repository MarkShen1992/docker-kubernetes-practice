package com.mark.user.service;

import com.mark.thrift.user.UserInfo;
import com.mark.thrift.user.UserService;
import com.mark.user.dao.UserDao;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService.Iface {

    @Autowired
    private UserDao userDao;

    @Override
    public UserInfo getUserById(int id) throws TException {
        return userDao.getUserById(id);
    }

    @Override
    public UserInfo getTeacherById(int id) throws TException {
        return userDao.getTeacherById(id);
    }

    @Override
    public UserInfo getUserByName(String username) throws TException {
        return userDao.getUserByName(username);
    }

    @Override
    public void registerUser(UserInfo userInfo) throws TException {
        System.out.println(userInfo.getPassword());
        userDao.register(userInfo);
    }
}
