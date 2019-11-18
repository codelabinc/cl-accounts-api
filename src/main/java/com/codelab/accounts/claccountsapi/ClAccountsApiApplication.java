package com.codelab.accounts.claccountsapi;

import com.codelab.accounts.conf.configuration.ServiceLayerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
@EnableScheduling
@Import({ServiceLayerConfiguration.class})
public class ClAccountsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClAccountsApiApplication.class, args);
    }

}
