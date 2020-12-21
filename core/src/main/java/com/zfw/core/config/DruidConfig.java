package com.zfw.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;


@Configuration
public class DruidConfig {

	@Value("${spring.datasource.driver-class-name}")
	private String driver;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Bean
	public FilterRegistrationBean getFilterRegistrationBean(){
		FilterRegistrationBean filter = new FilterRegistrationBean();
		filter.setFilter(new WebStatFilter());
		filter.setName("druidWebStatFilter");
		filter.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
		filter.addUrlPatterns("/*");
		return filter;
	}
	@Bean
	public ServletRegistrationBean getServletRegistrationBean(){
		ServletRegistrationBean servlet = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
		servlet.setName("druidStatViewServlet");
		servlet.addInitParameter("resetEnable", "false");
		return servlet;
	}
	@Bean
	public DruidDataSource setDruidDataSource(){
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(driver);
		druidDataSource.setUrl(url);
		druidDataSource.setUsername(username);
		druidDataSource.setPassword(password);
		try {
			druidDataSource.setFilters("stat,config,wall,slf4j");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return druidDataSource;
	}
}
