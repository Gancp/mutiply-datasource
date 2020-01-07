package com.again.mutiplydatasource;

import com.again.multiplydatasource.MutiplyDatasourceApplication;
import com.again.multiplydatasource.service.HelloWord;
import com.again.multiplydatasource.dao.ds1.DemoOneMapper;
import com.again.multiplydatasource.dao.ds2.DemoTwoMapper;
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
    public static int id = Math.abs(new Random(100).nextInt());
    @Autowired
    private DemoOneMapper demoOneMapper;

    @Autowired
    private DemoTwoMapper demoTwoMapper;

    @Autowired
    private TestService testService;

    @Autowired
    private HelloWord helloWord;

    /**
     * 插入两条数据，然后抛出异常，然后查询这两条数据是插入
     */
    @Test
    public void testJTAIsUseful() {
        try {
            testService.rollbackDTwo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(String.format("数据库查看下id:%s的数据是否存在", id));
        System.out.println("demoOne is " + demoOneMapper.selectByPrimaryKey(id));
        System.out.println("demoTwo is " + demoTwoMapper.selectByPrimaryKey(id));
        Assert.assertTrue(demoOneMapper.selectByPrimaryKey(id) == null && demoTwoMapper.selectByPrimaryKey(id) == null);
    }


    /**
     * 测试是否可以手动注册bean
     */
    @Test
    public void manualAddBeanTest() {
        // add a Hellworld Bean by  DataSourceRegistry.postProcessBeanDefinitionRegistry  method , then test it can be retrieval
        System.out.println(helloWord.say());
        Assert.assertTrue("hello heieigan".equals(helloWord.say()));

    }
}
