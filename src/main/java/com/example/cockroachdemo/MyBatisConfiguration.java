package com.example.cockroachdemo;

import javax.sql.DataSource;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@MapperScan(basePackages = "com.example.cockroachdemo.mapper", annotationClass = Mapper.class)
@MapperScan(basePackages = "com.example.cockroachdemo.batchmapper", annotationClass = Mapper.class,
            sqlSessionTemplateRef = "batchSqlSessionTemplate")
/**
 * This class configures MyBatis and sets up mappers for injection.
 * 
 * Most of the elements in this class are optional. This class demonstrates how to configure MyBatis when
 * some mappers require a BATCH executor. If you don't have that requirements, then you can remove everything from
 * this class except possibly the Datasource configuration. By Default, the MyBatis Spring Boot Starter will find all
 * mappers annotated with @Mapper and will automatically wire your Datasource to the underlying MyBatis infrastructure.
 */
public class MyBatisConfiguration {

    @Bean
    public DataSource datasource() {
        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url("jdbc:postgresql://localhost:26257/bank?ssl=false")
            .username("maxroach")
            .build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(datasource());
        return factory.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    @Bean(name = "batchSqlSessionTemplate")
    public SqlSessionTemplate batchSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory(), ExecutorType.BATCH);
    }
}