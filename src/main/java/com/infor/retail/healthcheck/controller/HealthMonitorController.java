package com.infor.retail.healthcheck.controller;

import com.infor.retail.healthcheck.model.Service;
import com.infor.retail.healthcheck.service.AmazonS3Service.AccessS3;
import com.infor.retail.healthcheck.service.HealthMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by odorjee on 2/18/2016.
 */

@Controller
public class HealthMonitorController {

    @Autowired
    private HealthMonitorService hms;

    public HealthMonitorController() {}

    @Scheduled(cron="0 0 9 * * * ") // run at 9am every day
    public void doSomeTask() throws IOException {

        // append latest results to history
        AccessS3 accessS3 = new AccessS3();
        accessS3.append(hms.getServiceQueue().peek());

    }

    @RequestMapping("/infor-health-service-dashboard")
    public String homepage(Model model) throws IOException {
        Queue<ArrayList<Service>> serviceQueue = hms.getServiceQueue();
        ArrayList<Service> healthChecks = serviceQueue.peek(); // returns most recent item added in the queue
        String date = hms.getTodaysDate();

	    for (ArrayList<Service> obj : serviceQueue) {
	        for (Service svc : obj) {
                System.out.println("Service: " + svc.getServiceName() + " Date: " + svc.getCheckDate()); 
	        }
	    }
		
        model.addAttribute("serviceLog", serviceQueue);
        model.addAttribute("services", healthChecks);
        model.addAttribute("dateTime", date);
        return "infor-health-service-dashboard";
    }
}
