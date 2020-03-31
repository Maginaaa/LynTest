package com.lyntest.mapper;


import com.lyntest.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    User selectUserByCode(String code);

    Integer userRegistered(User user);
}
