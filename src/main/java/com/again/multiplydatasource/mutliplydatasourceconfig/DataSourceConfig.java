package com.again.multiplydatasource.mutliplydatasourceconfig;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.util.Map;

/**
 * 多数据源配置类： 配置第一个数据源
 */

/**
 *  配置dao类的位置以及
 */
//@Configuration  // 这里关掉配置功能，改用core包下的自动注册bean的功能，这两种方法都是可以的
public class DataSourceConfig implements EnvironmentAware {

    String ds_key = "ds1";
    private Environment environment;
    private Binder binder;


    /**
     * 第一步，读取数据源参数信息
     *
     * @return
     */
    @Primary
    @Bean(name = "ds1datasource")
    public DataSource dataSource() throws ClassNotFoundException {
        Map<String, Map<String, Object>> config = binder.bind("spring.datasource", Map.class).get();
        System.out.println(config);
        Map<String, Object> dsproperties = config.get(ds_key);
        DruidXADataSource druidXADataSource = new DruidXADataSource();
        druidXADataSource.setUrl(dsproperties.get("jdbc-url").toString());
        druidXADataSource.setUsername(dsproperties.get("username").toString());
        druidXADataSource.setPassword(dsproperties.get("password").toString());
        druidXADataSource.setDriverClassName(dsproperties.get("driver-class-name").toString());

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(druidXADataSource);
        atomikosDataSourceBean.setUniqueResourceName(ds_key + "datasource");
        atomikosDataSourceBean.setPoolSize(5);

        return atomikosDataSourceBean;
    }

    /**
     * 创建数据库的连接工厂
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "ds1sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier(value = "ds1datasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        // 注入数据源
        sessionFactoryBean.setDataSource(dataSource);
        // 设置mapper.xml文件的地址
        String xmlpath = environment.getProperty("spring.datasource." + ds_key + ".sqlmappath");
        String aliasespath = environment.getProperty("spring.datasource." + ds_key + ".typealiasespackage");

        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(xmlpath));
        // 设置对应实体类的位置
        sessionFactoryBean.setTypeAliasesPackage(aliasespath);
        return sessionFactoryBean.getObject();
    }

    /**
     * 创建sql会话模版
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "ds1SqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier(value = "ds1datasource") DataSource dataSource) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory(dataSource));
    }


    /**
     * 使用这个来做总事务 后面的数据源就不用设置事务了
     */
    @Bean(name = "transactionManager")
    @Primary
    public JtaTransactionManager regTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        UserTransaction userTransaction = new UserTransactionImp();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        binder = Binder.get(this.environment);
    }

    /**
     * 手动指定dao包的位置
     * @return
     */
    @Bean(name = "ds1mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        String daoPackage = environment.getProperty("spring.datasource." + ds_key + ".mapper");
        mapperScannerConfigurer.setBasePackage(daoPackage);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName(ds_key + "sqlSessionFactory");
        mapperScannerConfigurer.setSqlSessionTemplateBeanName(ds_key + "SqlSessionTemplate");
        return mapperScannerConfigurer;
    }
}
