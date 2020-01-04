package com.again.mutiplydatasource.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DefinitionRegistryPostProcessor implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {
    private Environment environment;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Map map = new HashMap();
        map.put("name", "hi111");
        registerBean(beanDefinitionRegistry, "hello", HelloWord.class, map);
        map.put("name", "hi22");
        registerBean(beanDefinitionRegistry, "helloWord", HelloWord.class, map);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }


    private void registerBean(BeanDefinitionRegistry registry, String name, Class<?> beanClass) {
        registerBean(registry, name, beanClass, null);
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
        this.environment = environment;
        System.out.println(environment.getProperty("spring.datasource"));
    }
}
