package com.xieziming.stap.file.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Suny on 5/10/16.
 */
public class StapFileDbUtil {
    private static JdbcTemplate jdbcTemplate;
    public static JdbcTemplate getJdbcTemplate(){
        if(jdbcTemplate == null){
            synchronized (StapFileDbUtil.class){
                if(jdbcTemplate == null){
                    ApplicationContext context = new ClassPathXmlApplicationContext("stap-file-db-context.xml");
                    jdbcTemplate = (JdbcTemplate)context.getBean("jdbcTemplate");
                }
            }
        }
        return jdbcTemplate;
    }

}
