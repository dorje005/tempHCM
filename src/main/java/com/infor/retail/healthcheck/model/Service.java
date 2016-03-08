package com.infor.retail.healthcheck.model;

/**
 * Created by odorjee on 2/29/2016.
 */
public class Service {

    private int responseCode;
    private String serviceName;
    private String subService;

    public Service(String name, int code) {
        this.serviceName = name;
        this.responseCode = code;
        this.subService = null;
    }

    public Service(String name, int code, String name2) {
        this.serviceName = name;
        this.responseCode = code;
        this.subService = name2;
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

    public void setResponseCode(int code) {
        this.responseCode = code;
    }

    public void setServiceName(String name) {
        this.serviceName = name;
    }

    public void setSubService(String name2) {
        this.serviceName = name2;
    }

}
