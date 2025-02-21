package com.example.wms.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "com.example.wms.infrastructure.mapper",
        sqlSessionTemplateRef = "sqlSessionTemplate"
)
@Slf4j
public class MybatisConfig {

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // 매퍼 리소스 경로 설정
        Resource[] mapperResources = applicationContext.getResources("classpath:mybatis/**/*.xml");
        factoryBean.setMapperLocations(mapperResources);

        // 타입 별칭 패키지 설정
        factoryBean.setTypeAliasesPackage("com.example.wms.**.domain");

        // MyBatis 설정
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setDefaultStatementTimeout(30);
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.VARCHAR);
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}