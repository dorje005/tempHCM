package com.infor.retail.healthcheck.service;

import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.Date;

public class HealthMonitorService {
    public static String subService;

    public HealthMonitorService(String subservice) {
        this.subService = subservice;
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
