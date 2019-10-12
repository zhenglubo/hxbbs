package com.lixin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = "com.lixin")
@EnableSwagger2
@EnableAutoConfiguration
public class HXBbsApplocation
{
    public static void main( String[] args )
    {
        SpringApplication.run(HXBbsApplocation.class,args);
    }
}
