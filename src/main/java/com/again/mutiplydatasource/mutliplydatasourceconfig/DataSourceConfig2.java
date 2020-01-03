package com.again.mutiplydatasource.mutliplydatasourceconfig;

import com.alibaba.druid.pool.xa.DruidXADataSource;
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
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class DataSourceConfig2 implements EnvironmentAware {

    String ds_key = "ds2";
    private Environment environment;
    private Binder binder;

    /**
     * 第一步，读取数据源参数信息
     *
     * @return
     */

    @Bean(name = "ds2datasource")
    public DataSource dataSource() {
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
    @Bean(name = "ds2sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier(value = "ds2datasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        // 注入数据源
        sessionFactoryBean.setDataSource(dataSource);
        String xmlpath = environment.getProperty("spring.datasource." + ds_key + ".sqlmappath");
        String aliasespath = environment.getProperty("spring.datasource." + ds_key + ".typealiasespackage");

        // 设置mapper.xml文件的地址
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
    @Bean(name = "ds2SqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier(value = "ds2datasource") DataSource dataSource) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory(dataSource));
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        binder = Binder.get(this.environment);
    }


    @Bean(name = "ds2mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        String daoPackage = environment.getProperty("spring.datasource." + ds_key + ".mapper");
        mapperScannerConfigurer.setBasePackage(daoPackage);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName(ds_key + "sqlSessionFactory");
        mapperScannerConfigurer.setSqlSessionTemplateBeanName(ds_key + "SqlSessionTemplate");
        return mapperScannerConfigurer;
    }
}
