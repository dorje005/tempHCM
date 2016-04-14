package com.infor.retail.healthcheck.model;

/**
 * Created by odorjee on 2/29/2016.
 */
public class Service {

    private int responseCode;
    private String serviceName;
    private String subService;
    private String checkDate;

    public Service(String serviceName, int responseCode) {
        this.serviceName = serviceName;
        this.responseCode = responseCode;
        this.subService = null;
    }

    public Service(String serviceName, int responseCode, String subService, String checkDate) {
        this.serviceName = serviceName;
        this.responseCode = responseCode;
        this.subService = subService;
        this.subService = checkDate;
    }

    public Service(String serviceName, String checkDate, int responseCode) {
        this.serviceName = serviceName;
        this.responseCode = responseCode;
        this.checkDate = checkDate;
    }


    public int getResponseCode() {
        return this.responseCode;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public String getSubService() {
        return this.subService;
    }

    public String getCheckDate() { return this.checkDate;}

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setSubService(String subService) {
        this.serviceName = subService;
    }

    public void setCheckDate(String checkDate) { this.checkDate = checkDate; }
}
