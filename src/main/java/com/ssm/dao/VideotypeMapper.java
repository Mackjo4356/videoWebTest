package com.ssm.dao;


import java.util.List;

import com.ssm.entity.Videotype;
import com.ssm.entity.VideotypeExample;
import org.apache.ibatis.annotations.Param;

public interface VideotypeMapper {
    int countByExample(VideotypeExample example);

    int deleteByExample(VideotypeExample example);

    int deleteByPrimaryKey(Integer typeid);

    int insert(Videotype record);

    int insertSelective(Videotype record);

    List<Videotype> selectByExample(VideotypeExample example);

    Videotype selectByPrimaryKey(Integer typeid);

    int updateByExampleSelective(@Param("record") Videotype record, @Param("example") VideotypeExample example);

    int updateByExample(@Param("record") Videotype record, @Param("example") VideotypeExample example);

    int updateByPrimaryKeySelective(Videotype record);

    int updateByPrimaryKey(Videotype record);
}