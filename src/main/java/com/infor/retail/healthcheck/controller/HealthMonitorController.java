package com.infor.retail.healthcheck.controller;

import com.infor.retail.healthcheck.model.Service;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by odorjee on 2/18/2016.
 */

@Controller
public class HealthMonitorController {

    public Map<String, String> endpoints;
    public static String subService;

    public HealthMonitorController() {

        // create Linked Hash Map containing URL endpoints for various service health checks
        // along with the corresponding names of each service tested
        endpoints = new LinkedHashMap<>();

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
            healthChecks.add(new Service(endpoints.get(url), getStatusCode(url), subService));
        }
        String date = getTodaysDate();
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

    public static int getStatusCode(String testUrl) {

        int statusCode = 0;
        subService = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonData = null;

        try {
            URI uri = URI.create(testUrl);
            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return statusCode; //
            }

            // Retrieve entire JSON Data from redirection if URL is clean
            // JSON data may be of use in the future
            InputStream inputStream = urlConnection.getInputStream();

            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return statusCode;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Don't parse for empty streams
                return statusCode;
            }
            // store JSON data into string
            jsonData = buffer.toString();

        } catch (MalformedURLException e) {
            System.out.println("Created URL is incorrect!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    System.out.println("Error Closing Reader");
                }
            }
        }

        // check if Data retained is actually JSON
        if (jsonData.indexOf("{") == 0) {
            getSubService(jsonData); }
        return statusCode;
    }

    public static String getSubService(String jsonData) {
        final String HC = "healthChecks";
        final String SUB_SERVICE = "serviceName";
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray jsonArray = jsonObject.getJSONArray(HC);

        // sub service is always the first element of healthChecks
        JSONObject desiredObject = jsonArray.getJSONObject(0);
        subService = desiredObject.get(SUB_SERVICE).toString();

        // parse the String if needed
        if (subService.contains(".")) {
            String[] tokens = subService.split("\\.");
            subService = tokens[tokens.length - 1];
        }
        return subService;
    }

    public static String getTodaysDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ssa");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
