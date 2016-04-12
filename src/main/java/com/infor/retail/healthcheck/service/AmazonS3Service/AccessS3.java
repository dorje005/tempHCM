package com.infor.retail.healthcheck.service.AmazonS3Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by odorjee on 3/29/2016.
 */

@Component
public class AccessS3 {
    private static final String bucketName = "infor-devops-dev-retailcs";
    private static final String key = "health-monitor-dashboard/config.properties";
    private static final String key2 = "health-monitor-dashboard/service_log.txt";

    public String s3reader() throws IOException {
        String endpoints = null;
        AmazonS3 s3Client = new AmazonS3Client();
        // EC2 initializes this by itself by checking IAM Role
        try {

            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));
            InputStream objectData = s3Object.getObjectContent();
            endpoints = convertToString(objectData);
            System.out.println();

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
                    " means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        return endpoints;

    }
    private static String convertToString(InputStream input) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String read;

        while((read = br.readLine()) != null) {
            sb.append(read);
        }

        br.close();
        return sb.toString();
    }

    public String logReader() throws IOException {
        String endpoints = null;
        AmazonS3 s3Client = new AmazonS3Client();
        // EC2 initializes this by itself by checking IAM Role
        try {

            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key2));
            InputStream objectData = s3Object.getObjectContent();
            endpoints = convertToString(objectData);
            System.out.println();

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
                    " means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        return endpoints;
    }

    public boolean updateLog(File updatedFile) {
        String endpoints = null;
        AmazonS3 s3Client = new AmazonS3Client();
        try {
            // Amazon S3 will override old file with new file
            s3Client.putObject(new PutObjectRequest(bucketName, key2, updatedFile));
            return true;
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
                    " means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            return false;
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            return false;
        }
    }
}
