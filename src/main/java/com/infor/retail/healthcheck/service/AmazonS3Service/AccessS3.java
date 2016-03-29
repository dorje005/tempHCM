package com.infor.retail.healthcheck.service.AmazonS3Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by odorjee on 3/29/2016.
 */
public class AccessS3 {
    private static final String bucketName = "infor-devops-dev-retailcs";
//    private static final String folderName = "health-monitor-dashboard";
    private static final String folder = "/lambda/";
    private static final String key = "config.properties";

    public String s3reader() throws IOException {
        String endpoints = "";
        AmazonS3 s3Client = new AmazonS3Client();
        // EC2 initializes this by itself by checking IAM Role
        try {
//            S3Object s3object = s3Client.getObject(new GetObjectRequest(
//                    bucketName, key2));
//            ObjectListing listing = s3Client.listObjects(bucketName);
            ObjectListing listing = s3Client.listObjects(new ListObjectsRequest().withBucketName(bucketName)
                    .withPrefix(folder));
            System.out.println("Here");
            for (S3ObjectSummary object : listing.getObjectSummaries()){
                System.out.println("Object: " + object.getKey());
                System.out.println();     
            }

//            System.out.println("Content-Type: "  +
//                    s3object.getObjectMetadata().getContentType());
//            endpoints = displayTextInputStream(s3object.getObjectContent());

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
    private static String displayTextInputStream(InputStream input)
            throws IOException {
        // Read one text line at a time and display.
        System.out.println("Printing contents of file . . .");
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(input));
        String endpoints = "";
        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;
            System.out.println("    " + line);
            endpoints = endpoints + line;
        }
        System.out.println();
        return endpoints;
    }
}