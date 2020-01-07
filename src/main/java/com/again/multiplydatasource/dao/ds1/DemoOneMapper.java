package com.again.multiplydatasource.dao.ds1;

import com.again.multiplydatasource.domain.ds1.DemoOne;
import com.again.multiplydatasource.domain.ds1.DemoOneExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DemoOneMapper {
    int countByExample(DemoOneExample example);

    int deleteByExample(DemoOneExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DemoOne record);

    int insertSelective(DemoOne record);

    List<DemoOne> selectByExample(DemoOneExample example);

    DemoOne selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DemoOne record, @Param("example") DemoOneExample example);

    int updateByExample(@Param("record") DemoOne record, @Param("example") DemoOneExample example);

    int updateByPrimaryKeySelective(DemoOne record);

    int updateByPrimaryKey(DemoOne record);
}