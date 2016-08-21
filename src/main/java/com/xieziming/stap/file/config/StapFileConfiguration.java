package com.xieziming.stap.file.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Suny on 5/16/16.
 */
@ComponentScan(value = {"com.xieziming.stap"})
@Configuration
@EnableWebMvc

public class StapFileConfiguration extends WebMvcConfigurerAdapter{
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
