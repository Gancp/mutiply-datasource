package com.again.multiplydatasource;

import com.again.multiplydatasource.core.JTAConfig;
import com.again.multiplydatasource.core.DataSourceRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 多数据源测试
 * 需要关掉 两个数据源的自动装配功能
 *
 * @author again
 */
@Import({DataSourceRegistry.class, JTAConfig.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class MutiplyDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MutiplyDatasourceApplication.class, args);
    }


}
