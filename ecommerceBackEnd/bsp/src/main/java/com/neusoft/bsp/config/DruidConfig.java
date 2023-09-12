package com.neusoft.bsp.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;

@Configuration
public class  DruidConfig {
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }

    @Bean
    public ServletRegistrationBean druidServlet() {

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
                new StatViewServlet(), "/druid/*");

        servletRegistrationBean.addInitParameter("allow", "127.0.0.1,172.29.32.54");

        servletRegistrationBean.addInitParameter("deny", "192.168.1.110");

        servletRegistrationBean.addInitParameter("loginUsername", "admin");

        servletRegistrationBean.addInitParameter("loginPassword", "123456");

        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean ;
    }


}
