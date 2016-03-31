package com.infor.retail.healthcheck.config;

import com.infor.retail.healthcheck.model.Service;
import com.infor.retail.healthcheck.service.AmazonS3Service.AccessS3;
import com.infor.retail.healthcheck.service.HealthMonitorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;

@Configuration
public class AppConfig {

    // Bean allows function to run automatically
    @Bean(name="serviceList")
    public ArrayList<Service> start() throws IOException {

        ArrayList<Service> health_services = new ArrayList<>();
        HealthMonitorService hms = new HealthMonitorService();

//      read endpoints from config file stored in Amazon S3
        AccessS3 accessS3 = new AccessS3();
        String endpoints = accessS3.s3reader();
        for (String entry : endpoints.split(" ;")) { // each service separated by ";"
            String[] item = entry.split("="); // each service separated by "="
            String serviceName = item[0];
            String serviceURL = item[1];
            int responseCode = hms.getStatusCode(serviceURL);
            String subservice = hms.getSubService();
            health_services.add(new Service(serviceName, responseCode, subservice));
        }
        return health_services;
    }
}
