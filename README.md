Infor Service Health Dashboard is a simple internal web application that checks URL endpoints of various services and notifies the user on the current status of each service. This page serves as a guide on how to add endpoints to the health monitor dashboard. 

Notes about the application: 
* Infor Service Health Dashboard - https://health.retailcs.awsdev.infor.com/infor-health-service-dashboard
* This is a spring application that runs on an Amazon EC2 micro instance in a background tmux session
* Reads various endpoints to test from a config.properties file stored in Infor's Amazon S3
* While running the application, the program reads these endpoints with their corresponding URL(s) and makes HTTP requests
* Retrieves the response code for each individual service while also acquiring the JSON output 
* Determines whether each service is operating or not depending on if each service returns a response code of 200 

Step-by-step guide

Follow the steps listed below:
1. Locate the config.properties file stored in the Amazon S3 bucket infor-devops-dev-retailcs under the health-monitor-dashboard folder 
2. Download the file and add the desired endpoint(s) by entering the name of the service followed by "=" sign, followed by the respective 3. URL then (space) and lastly a ";" sign. Follow the examples below for clarification: 
    Acceptable -> Dev Example=https://example.com ;
    Unacceptable -> Dev Example =https://example.com ; 
    Unacceptable -> Dev Example= https://example.com ;
    Unacceptable -> Dev Example=https://example.com      ;
4. After adding all endpoint(s) save the file as config.properties and store it back in the health-monitor-dashboard folder, replacing the older version of the file.
5. Exit the current application by going inside the retailcs-health-164 instance and running it again using the mvn spring-boot:run command 
 
Contact Information:
For questions and/or concerns pertaining to the Infor Service Health Dashboard please contact Ongda Dorjee, email: dorje005@umn.edu . 
