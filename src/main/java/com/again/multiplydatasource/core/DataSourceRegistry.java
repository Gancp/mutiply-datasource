package com.again.multiplydatasource.core;

import com.again.multiplydatasource.service.HelloWord;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * bean的手工注册:用于注册数据源和整合mybatisØ
 *
 * @author again
 */
public class DataSourceRegistry implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {
    private Binder binder;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // 获取到所有的数据源配置
        Map<String, Map<String, Object>> config = binder.bind("spring.datasource", Map.class).get();
        config.forEach((dsName, dsproperties) -> {
            // 数据源
            Supplier<DataSource> dataSourceSupplier = () -> {
                DruidXADataSource druidXADataSource = new DruidXADataSource();
                druidXADataSource.setUrl(dsproperties.get("jdbc-url").toString());
                druidXADataSource.setUsername(dsproperties.get("username").toString());
                druidXADataSource.setPassword(dsproperties.get("password").toString());
                druidXADataSource.setDriverClassName(dsproperties.get("driver-class-name").toString());
                AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
                atomikosDataSourceBean.setXaDataSource(druidXADataSource);
                atomikosDataSourceBean.setUniqueResourceName(dsName + "Datasource");
                atomikosDataSourceBean.setPoolSize(5);
                // 这里还没有配置连接池大小等数据，可以自行扩展
                return atomikosDataSourceBean;
            };
            this.registerBean(beanDefinitionRegistry, dsName + "Datasource", DataSource.class, dataSourceSupplier);
            // SqlSessionFactory
            Supplier<SqlSessionFactory> sqlSessionFactorySupplier = () -> {
                try {
                    SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
                    // 注入数据源
                    sessionFactoryBean.setDataSource(dataSourceSupplier.get());
                    // 设置mapper.xml文件的地址 和 实体包的位置
                    String xmlpath = (dsproperties.get("sqlmappath").toString());
                    String aliasespath = (dsproperties.get("typealiasespackage").toString());
                    sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(xmlpath));
                    // 设置对应实体类的位置
                    sessionFactoryBean.setTypeAliasesPackage(aliasespath);
                    return sessionFactoryBean.getObject();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            };
            this.registerBean(beanDefinitionRegistry, dsName + "SqlSessionFactory", SqlSessionFactoryBean.class, sqlSessionFactorySupplier);

            // sqlSessionTemplate
            Supplier<SqlSessionTemplate> sqlSessionTemplateSupplier = () -> {
                return new SqlSessionTemplate(sqlSessionFactorySupplier.get());
            };
            this.registerBean(beanDefinitionRegistry, dsName + "SqlSessionTemplate", SqlSessionTemplate.class, sqlSessionTemplateSupplier);

            // ClassPathMapperScanner 用于扫描dao包的位置
            ClassPathMapperScanner mapperScannerConfigurer = new ClassPathMapperScanner(beanDefinitionRegistry);
            String daoPackage = dsproperties.get("mapper").toString();
            mapperScannerConfigurer.setSqlSessionTemplateBeanName(dsName + "SqlSessionTemplate");
            mapperScannerConfigurer.registerFilters();
            mapperScannerConfigurer.doScan(daoPackage);
        });

        //  testunit
        Supplier<HelloWord> helloWordSupplier = () -> {
            HelloWord helloWord = new HelloWord();
            helloWord.setName("heieigan");
            return helloWord;
        };
        registerBean(beanDefinitionRegistry, "helloworld", HelloWord.class, helloWordSupplier);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    /**
     * 注册bean
     *
     * @param registry
     * @param name
     * @param beanClass
     */

    private void registerBean(BeanDefinitionRegistry registry, String name, Class<?> beanClass) {
        registerBean(registry, name, beanClass, new HashMap<>());
    }

    /**
     * @param registry
     * @param name
     * @param beanClass
     * @param propertyValues 需要修改的成员变量
     */
    private void registerBean(BeanDefinitionRegistry registry, String name, Class<?> beanClass, Map<String, Object> propertyValues) {
        RootBeanDefinition bean = new RootBeanDefinition(beanClass);
        if (null != propertyValues && !propertyValues.isEmpty()) {
            propertyValues.forEach((key, value) -> {
                bean.getPropertyValues().add(key, value);
            });
        }
        registry.registerBeanDefinition(name, bean);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.binder = Binder.get(environment);
    }


    /**
     * 注册bean
     *
     * @param registry
     * @param name
     * @param beanClass
     * @param obj       将需要传入的对象用Supplier封装，交由注册器注册即可
     */
    private void registerBean(BeanDefinitionRegistry registry, String name, Class<?> beanClass, Supplier obj) {
        // 先转换成beandefinition
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass.getClass(), obj).getBeanDefinition();
        // 交由注册器注册bean
        registry.registerBeanDefinition(name, beanDefinition);
    }

}
