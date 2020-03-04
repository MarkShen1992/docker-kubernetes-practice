package com.mark.user.dao;

import com.mark.thrift.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("SELECT id, username, password, realname AS realName, mobile, email FROM pe_user WHERE id=#{id}")
    UserInfo getUserById(@Param("id") int id);

    @Select("SELECT id, username, password, realname AS realName, mobile, email FROM pe_user WHERE username=#{username}")
    UserInfo getUserByName(@Param("username") String username);

    @Insert("INSERT INTO pe_user (username, password, realname, mobile, email) " +
            "VALUES (#{u.username}, #{u.password}, #{u.realName}, #{u.mobile}, #{u.email})")
    void register(@Param("u") UserInfo userInfo);

    @Select("SELECT u.id, u.username, u.password, u.realname AS realName, u.mobile, u.email," +
            "t.intro, t.stars FROM pe_user u JOIN pe_teacher t ON u.id = t.user_id")
    UserInfo getTeacherById(@Param("id") int id);
}
