package com.ssm.dao;


import java.util.List;

import com.ssm.entity.Usercollection;
import com.ssm.entity.UsercollectionExample;
import org.apache.ibatis.annotations.Param;

public interface UsercollectionMapper {
    int countByExample(UsercollectionExample example);

    int deleteByExample(UsercollectionExample record);

    int deleteByidcoll(@Param("userid") String userid, @Param("collection") String collection);
    
    int insert(Usercollection record);

    int insertSelective(Usercollection record);

    List<Usercollection> selectByExample(UsercollectionExample example);
    
    List<Usercollection> selectById(@Param("userid") String userid);

    int updateByExampleSelective(@Param("record") Usercollection record, @Param("example") UsercollectionExample example);

    int updateByExample(@Param("record") Usercollection record, @Param("example") UsercollectionExample example);
}