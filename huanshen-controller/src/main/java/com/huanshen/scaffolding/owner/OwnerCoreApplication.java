package com.huanshen.scaffolding.owner;

import com.huanshen.scaffolding.security.auth.AuthTypeEnum;
import com.huanshen.scaffolding.security.auth.DefaultAuth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.huanshen.scaffolding"})
//@MapperScan("com.huanshen.scaffolding.**.mapper")
@EnableScheduling
@DefaultAuth(AuthTypeEnum.NO_AUTH)
public class OwnerCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OwnerCoreApplication.class, args);
    }

}
