package com.ygsoft;

import com.mysql.jdbc.StringUtils;
import com.ygsoft.common.InitConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(App.class, args);
        final String initFilePath = configurableApplicationContext.getEnvironment().getProperty("initFilePath");
        if (!StringUtils.isNullOrEmpty(initFilePath)) {
            InitConst.setInitFilePath(initFilePath);
        }
    }
}
