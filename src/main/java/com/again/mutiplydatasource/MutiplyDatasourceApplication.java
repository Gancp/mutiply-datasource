package com.again.mutiplydatasource;

import com.again.mutiplydatasource.core.DataSourceRegistry;
import com.again.mutiplydatasource.core.JTAConfig;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;

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
