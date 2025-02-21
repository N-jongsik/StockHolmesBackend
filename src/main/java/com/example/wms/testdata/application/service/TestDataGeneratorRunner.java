package com.example.wms.testdata.application.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@MapperScan("com.example.wms.infrastructure.mapper")
public class TestDataGeneratorRunner {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(TestDataGeneratorRunner.class, args);
        TestDataGenerator generator = ctx.getBean(TestDataGenerator.class);
        generator.insertTestData();
        System.out.println("테스트 데이터 생성 완료");
        // 테스트 데이터 생성 후 애플리케이션 종료
        System.exit(0);
    }
}