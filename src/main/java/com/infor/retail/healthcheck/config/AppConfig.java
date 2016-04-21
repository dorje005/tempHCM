package com.infor.retail.healthcheck.config;

import com.infor.retail.healthcheck.model.Service;
import com.infor.retail.healthcheck.service.AmazonS3Service.AccessS3;
import com.infor.retail.healthcheck.service.HealthMonitorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

@Configuration
public class AppConfig {

    // Bean allows function to run automatically
    @Bean(name="serviceList")
    public Queue<ArrayList<Service>> start() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date dateTime = new Date();
        String date = dateFormat.format(dateTime);

        HealthMonitorService hms = new HealthMonitorService();
        // create Queue which holds history of all services
        Queue<ArrayList<Service>> serviceQueue = new LinkedList<>();
        // read from service_log.txt in S3
        AccessS3 accessS3 = new AccessS3();
	        
	String endpoints = accessS3.s3reader(); 
        // read today's health endpoints from config file stored in S3
        if (endpoints != null) {
	   ArrayList<Service> health_services = new ArrayList<>(); 
	   for (String entry : endpoints.split(" ;")) { // each service separated by ";"
                String[] item = entry.split("="); // each service separated by "="
                String serviceName = item[0];
                String serviceURL = item[1];
                int responseCode = hms.getStatusCode(serviceURL);
                String subservice = hms.getSubService();
	        health_services.add(new Service(serviceName, responseCode, subservice, date));
           }
           serviceQueue.add(health_services); // adds to the front of the queue (Linked List structure)
	   
        }
	
	endpoints = accessS3.logReader();
        // read file and create services and store them in the queue
        // format of entries: serviceName-ResponseCode-Date \n
	
	if (endpoints != null) {
            for (String entry : endpoints.split("=")) { // for each list of services in a week
                ArrayList<Service> health_services = new ArrayList<>(); 
                for (String each : entry.split(";")) { // for each service on given day
                    String[] item = each.split("-");
                    String serviceName = item[0];
                    String responseCode = item[1];
                    int response = Integer.parseInt(responseCode);
                    String checkDate = item[2];
		    health_services.add(new Service(serviceName, checkDate, response));
                }
		serviceQueue.add(health_services);
	    }
        }
	return serviceQueue;
    }
}
