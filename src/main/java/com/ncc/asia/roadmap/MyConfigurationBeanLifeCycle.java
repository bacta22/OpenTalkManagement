package com.ncc.asia.roadmap;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MyConfigurationBeanLifeCycle {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MyBeanForLifeCycle myBeanForLifeCycle () {
        return new MyBeanForLifeCycle();
    }
}

// default bean will be singleton type