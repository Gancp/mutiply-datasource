package com.again.mutiplydatasource;

import com.again.mutiplydatasource.dao.ds1.DemoOneMapper;
import com.again.mutiplydatasource.dao.ds2.DemoTwoMapper;
import com.again.mutiplydatasource.domain.ds1.DemoOne;
import com.again.mutiplydatasource.domain.ds2.DemoTwo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MutiplyDatasourceApplication.class})
class MutiplyDatasourceApplicationTests {
    public static int id = new Random(100).nextInt();
    @Autowired
    private DemoOneMapper demoOneMapper;

    @Autowired
    private DemoTwoMapper demoTwoMapper;

    @Autowired
    private TestService testService;


    /**
     * 插入两条数据，然后抛出异常，然后查询这两条数据是插入
     */
    @Test
    public void tes2() {
        try {
            testService.rollbackDTwo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(demoOneMapper.selectByPrimaryKey(id));
        System.out.println(demoTwoMapper.selectByPrimaryKey(id));
        Assert.assertTrue(demoOneMapper.selectByPrimaryKey(id) == null && demoTwoMapper.selectByPrimaryKey(id) == null);
    }
}
