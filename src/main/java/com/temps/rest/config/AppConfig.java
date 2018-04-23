package com.temps.rest.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebMvc
@ComponentScan("com.temps.rest")
public class AppConfig {

//	@Bean(name = "tempsSource")
//	@ConfigurationProperties(prefix = "temps.mysql")
//	public DataSource tempsSource() {
//		// BasicDataSource dataSource = new BasicDataSource();
//		DataSource dataSource = DataSourceBuilder.create().build();
//
//		return dataSource;
//	}
}
