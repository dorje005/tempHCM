package com.infor.retail.healthcheck.controller;

import com.infor.retail.healthcheck.model.Service;
import com.infor.retail.healthcheck.service.HealthMonitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by odorjee on 2/18/2016.
 */
@Controller
//@Configuration
//@PropertySource("C:/HCM/health-monitor-dashboard/src/main/resources/endpoint.properties")

public class HealthMonitorController {

//    @Value("${test.auth.name}")
//    public static String ends;
    public Map<String, String> endpoints = new LinkedHashMap<>();
    public static String subService;
    public HealthMonitorService hms = new HealthMonitorService(subService);


    public HealthMonitorController() {
//        System.out.println(ends);
        // create Linked Hash Map containing URL endpoints for various service health checks
        // along with the corresponding names of each service tested

//        System.out.println(props.toString());
        // endpoints=test.auth,test.hierarchy

        // test.auth.url
        // test.auth.name
        endpoints.put("https://test-api-authorization.retailcs.awsdev.infor.com/health", "Test API Authorization");
        endpoints.put("https://test-api-account.retailcs.awsdev.infor.com/health","Test API Account");
        endpoints.put("https://test-api-attribute.retailcs.awsdev.infor.com/health","Test API Attribute");
        endpoints.put("https://test-api-hierarchy.retailcs.awsdev.infor.com/health","Test API Hierarchy");
        endpoints.put("https://test-api-item.retailcs.awsdev.infor.com/health","Test API Item");
        endpoints.put("https://test-api-location.retailcs.awsdev.infor.com/health", "Test API Location");
        endpoints.put("https://test-api-reason.retailcs.awsdev.infor.com/health", "Test API Reason");
        endpoints.put("https://test-api-inventory.retailcs.awsdev.infor.com/health", "Test API inventory");
        endpoints.put("https://dev-api-authorization.retailcs.awsdev.infor.com/health","Dev API Authorization");
        endpoints.put("https://dev-api-account.retailcs.awsdev.infor.com/health","Dev API Account");
        endpoints.put("https://dev-api-attribute.retailcs.awsdev.infor.com/health","Dev API Attribute");
        endpoints.put("https://dev-api-hierarchy.retailcs.awsdev.infor.com/health","Dev API Hierarchy");
        endpoints.put("https://dev-api-item.retailcs.awsdev.infor.com/health","Dev API Item");
        endpoints.put("https://dev-api-location.retailcs.awsdev.infor.com/health","Dev API Location");
        endpoints.put("https://dev-api-reason.retailcs.awsdev.infor.com/health","Dev API Reason");
        endpoints.put("https://dev-api-inventory.retailcs.awsdev.infor.com/health","Dev API inventory");
        endpoints.put("https://dev-web.retailcs.awsdev.infor.com", "Dev Website");
        endpoints.put("http://www.nfl.com/blahhhh", "Faulty URL");

    }

    @RequestMapping("/homepage")
    public String homepage(Model model) {
        ArrayList<Service> healthChecks = new ArrayList<>();

        for (String url : endpoints.keySet()) {
            healthChecks.add(new Service(endpoints.get(url), hms.getStatusCode(url), subService));
        }
        String date = hms.getTodaysDate();
        model.addAttribute("services", healthChecks);
        model.addAttribute("dateTime", date);
        return "homepage";
    }

    // offline version
    @RequestMapping("/offline")
    public String offline(Model model) {
        HashMap<String, Integer> services = new LinkedHashMap<>();
        services.put("Sample Data 1", 200);
        services.put("Sample Data 2", 200);
        services.put("Sample Data 3", 200);
        services.put("Sample Data 4", 400);
        services.put("Sample Data 5", 200);

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ssa");
        Date date = new Date();

        model.addAttribute("services", services);
        model.addAttribute("dateTime", dateFormat.format(date));
        return "offline";
    }
}
