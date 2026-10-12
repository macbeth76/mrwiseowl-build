package net.wiseowltech.mrwiseowl.mrwiseowl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MrWiseOwlApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrWiseOwlApplication.class, args);
    }

}
