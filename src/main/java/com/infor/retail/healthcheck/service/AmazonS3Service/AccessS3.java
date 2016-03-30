package com.infor.retail.healthcheck.service.AmazonS3Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by odorjee on 3/29/2016.
 */
public class AccessS3 {
    private static final String bucketName = "infor-devops-dev-retailcs";
    private static final String key = "health-monitor-dashboard/config.properties";

    public String s3reader() throws IOException {
        String endpoints = null;
        AmazonS3 s3Client = new AmazonS3Client();
        // EC2 initializes this by itself by checking IAM Role
        try {

            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key)
                    .withMatchingETagConstraint("d358ebaecd0f00c3909decc765cdc04e"));

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
}