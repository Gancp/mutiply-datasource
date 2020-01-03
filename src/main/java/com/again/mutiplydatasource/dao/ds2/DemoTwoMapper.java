package com.again.mutiplydatasource.dao.ds2;

import com.again.mutiplydatasource.domain.ds2.DemoTwo;
import com.again.mutiplydatasource.domain.ds2.DemoTwoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DemoTwoMapper {
    int countByExample(DemoTwoExample example);

    int deleteByExample(DemoTwoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DemoTwo record);

    int insertSelective(DemoTwo record);

    List<DemoTwo> selectByExample(DemoTwoExample example);

    DemoTwo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DemoTwo record, @Param("example") DemoTwoExample example);

    int updateByExample(@Param("record") DemoTwo record, @Param("example") DemoTwoExample example);

    int updateByPrimaryKeySelective(DemoTwo record);

    int updateByPrimaryKey(DemoTwo record);
}