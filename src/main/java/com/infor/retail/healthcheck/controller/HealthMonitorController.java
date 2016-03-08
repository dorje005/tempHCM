package com.infor.retail.healthcheck.controller;

import com.infor.retail.healthcheck.model.Service;
import com.infor.retail.healthcheck.service.HealthMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

/**
 * Created by odorjee on 2/18/2016.
 */

@Controller
public class HealthMonitorController {

    @Autowired
    private HealthMonitorService hms;

    public HealthMonitorController() {}

    @RequestMapping("/infor-health-service-dashboard")
    public String homepage(Model model) {
        ArrayList<Service> healthChecks = hms.getHealth_services();
        String date = hms.getTodaysDate();
        model.addAttribute("services", healthChecks);
        model.addAttribute("dateTime", date);
        return "infor-health-service-dashboard";
    }
}
