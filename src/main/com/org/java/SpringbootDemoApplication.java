package org.java;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Administrator
 */
@SpringBootApplication
@MapperScan("org.java.mapper")
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
public class SpringbootDemoApplication {

    public static void main(String[] args) {
          new SpringApplication(SpringbootDemoApplication.class).run(args);
       // SpringApplication.run(SpringbootDemoApplication.class, args);
    }

}
