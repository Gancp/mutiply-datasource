package com.again.mutiplydatasource;

import com.again.multiplydatasource.dao.ds1.DemoOneMapper;
import com.again.multiplydatasource.dao.ds2.DemoTwoMapper;
import com.again.multiplydatasource.domain.ds1.DemoOne;
import com.again.multiplydatasource.domain.ds2.DemoTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestService {

    @Autowired
    private DemoOneMapper demoOneMapper;

    @Autowired
    private DemoTwoMapper demoTwoMapper;

    /**
     * 测试数据回滚
     */
    public void rollbackDTwo() {
        DemoOne demoOne = new DemoOne();
        demoOne.setId(MutiplyDatasourceApplicationTests.id);
        demoOne.setMark("demoOne");
        DemoTwo demoTwo = new DemoTwo();
        demoTwo.setMark("demoTwo");
        demoTwo.setId(MutiplyDatasourceApplicationTests.id);
        demoOneMapper.insert(demoOne);
        demoTwoMapper.insert(demoTwo);
        if (true) {
            throw new RuntimeException("就是要抛出异常2");
        }
    }
}
