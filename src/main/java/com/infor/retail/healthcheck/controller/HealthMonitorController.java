package com.infor.retail.healthcheck.controller;

import com.infor.retail.healthcheck.model.Service;
import com.infor.retail.healthcheck.service.HealthMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class HealthMonitorController {

    @Autowired
    private HealthMonitorService hms;

    public HealthMonitorController() {}

    @RequestMapping("/homepage")
    public String homepage(Model model) {
        ArrayList<Service> healthChecks = hms.getHealth_services();
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
