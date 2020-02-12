package com.mark.user.service;

import com.mark.thrift.user.UserInfo;
import com.mark.thrift.user.UserService;
import org.apache.thrift.TException;

public class UserServiceImpl implements UserService.Iface {

    @Override
    public UserInfo getUserById(int id) throws TException {
        return null;
    }

    @Override
    public UserInfo getUserByName(String username) throws TException {
        return null;
    }

    @Override
    public void registerUser(UserInfo userInfo) throws TException {

    }
}
