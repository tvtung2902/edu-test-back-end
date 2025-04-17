package com.javaweb.edutest;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EdutestApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdutestApplication.class, args);
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dfb1utqck",
                "api_key", "896436333846459",
                "api_secret", "h8HGSZmDVmQiL6jS7KjsZXz7-NA",
                "secure",true
        ));
    }
}
