package com.cit.web;

import com.cit.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableSwagger2Doc
@EnableTransactionManagement
@MapperScan("com.cit.web.*.dao")
@SpringBootApplication
public class WebbootApplication
{

    private static final Logger logger = LoggerFactory.getLogger(WebbootApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(WebbootApplication.class, args);
        logger.info("\n==================================================启动成功==================================================\n");
    }

}

