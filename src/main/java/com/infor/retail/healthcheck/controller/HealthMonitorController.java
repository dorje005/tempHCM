package com.infor.retail.healthcheck.controller;

import com.infor.retail.healthcheck.model.Service;
import com.infor.retail.healthcheck.service.AmazonS3Service.AccessS3;
import com.infor.retail.healthcheck.service.HealthMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by odorjee on 2/18/2016.
 */

@Controller
public class HealthMonitorController {

    @Autowired
    private HealthMonitorService hms;

    public HealthMonitorController() {}

//    @Scheduled(cron="0 0 9 * * * ") // run at 9am every day
    @Scheduled(fixedRate = 60000) // 60 seconds
    public void doSomeTask() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date dateTime = new Date();
        String date = dateFormat.format(dateTime);

        System.out.println("Date: " + date);
        ArrayList<Service> healthChecks = hms.getHealth_services();
        String dailyLog = "";

        // format = serviceName-ResponseCode-Date
        for (int i=0; i<healthChecks.size(); i++) {
            dailyLog = dailyLog + "\n" + healthChecks.get(i).getServiceName()
                    + "-" + healthChecks.get(i).getResponseCode()
                    + "-" + date;
        }
//        System.out.println(dailyLog);
        AccessS3 accessS3 = new AccessS3();
        String newLog = accessS3.logReader();
        newLog = newLog + dailyLog;

//        String newLog = "Most Recent-200-4/12\n" + "Next-200-4/12\n" + "Least Recent-200-4/12";
//        newLog = newLog + dailyLog;
//        System.out.println(newLog);

//         Create file with updated Log
        try {
            File file = new File("service_log.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getName(),true);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.write(newLog); // put new data into log File
            bufferWriter.close();
            if (accessS3.updateLog(file)) {
                file.delete();
            }
//            file.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/infor-health-service-dashboard")
    public String homepage(Model model) {
        ArrayList<Service> healthChecks = hms.getHealth_services();
        String date = hms.getTodaysDate();
        model.addAttribute("services", healthChecks);
        model.addAttribute("dateTime", date);
        return "infor-health-service-dashboard";
    }
}
