package com.mark.user.dao;

import com.mark.thrift.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("SELECT id, username, password, realname, mobile, email FROM pe_user WHERE id=#{id}")
    UserInfo getUserById(@Param("id") int id);

    @Select("SELECT id, username, password, realname, mobile, email FROM pe_user WHERE username=#{username}")
    UserInfo getUserByName(@Param("username") String username);

    @Insert("INSERT INTO pe_user (username, password, realname, mobile, email) " +
            "VALUES (#{u.username}, #{u.password}, #{u.realname}, #{u.mobile}, #{u.email})")
    void register(@Param("u") UserInfo userInfo);
}
