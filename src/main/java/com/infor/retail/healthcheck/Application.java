package com.infor.retail.healthcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Created by odorjee on 2/18/2016.
 */

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
//        AccessS3 accessS3 = new AccessS3();
//        accessS3.s3reader();
        SpringApplication.run(Application.class);
    }
}