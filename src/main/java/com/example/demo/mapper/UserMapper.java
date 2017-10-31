package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by jerry-jx on 2017/8/8.
 */
@Mapper
public interface UserMapper {
    @Select("select * from users where userId = #{userId}")
    User findUserByUserid(@Param("userId") String userId);

    @Insert("insert into users (userId,pwd) values (#{userId},#{pwd})")
    boolean insertUsers (@Param("userId") String userId,@Param("pwd") String pwd);

    @Select("select count(*) from user where id = #{userId}")
    int getUserCount(@Param("userId") String userId);
}