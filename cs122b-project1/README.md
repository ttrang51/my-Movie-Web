# CS 122B Project 5 Report

- # General
    - #### Team#: Nightowls
    
    - #### Names: Anthony Martinez and Thi Thuy Trang Tran
    
    - #### Project 5 Video Demo Link:

    - #### Instruction of deployment:
        #### To deploy falbix on any instance (master, slave, instance 1):
        ### 1. Connect to tomcat manager (keep tab open to manually deploy fablix)
        ###     - http://3.143.24.138:8080/manager/html/ (instance 1)
        ###     - http://3.145.134.44:8080/manager/html/ (instance 2 (master))
        ###     - http://18.219.157.91:8080/manager/html/(instance 3 (slave))
        ### 2. (In terminal) change working directory to /s23-122b-cs122b-team-nightowls/cs122b-project1 ("cd s23-122b-cs122b-team-nightowls/cs122b-project1")
        ### 3. run "mvn clean"
        ### 4. run "mvn package"
        ### 5. click "undeploy" button on tomcat manager webpage for "cs122b-spring21-project1-api-example" to remove the current falbix
        ### 6. click "Browse..." button under "Deploy" section of tomcat manager webpage and choose the WAR file (located in s23-122b-cs122b-team-nightowls-1/cs122b-project1/target) that was created from running "mvn package" in previous step
        ### 7. Click on hyperlink titled "cs122b-spring21-project1-api-example" under the "Path" section of the page to navigate to newly deployed Falbix


    - #### Collaborations and Work Distribution:

    ***Anthony Martinez***
    
    Completed:
    Task 1: Enabled Fabflix with Connection Pooling. 
    Task 2: Setup a MySQL cluster on AWS that includes a master and a slave.
    Task 3: Setup a load balancer to balance the traffic to multiple Fabflix instances.


    ***Thi Thuy Trang Tran***
        
    Completed: 
    Task 4: Measured the performance of the keyword search feature using Apache JMeter.


    ***Cooperated Work***
        
        Peer-review 
        Testing and Inspection


- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
        #### In s23-122b-cs122b-team-nightowls-1/cs122b-project1/src/SQLQueryTemplate.java
        #### on lines 85 through 88 we have SQL Queries predefined using the Stored procedures we have in our mysql database.
        #### These Queries are used in...
            #### s23-122b-cs122b-team-nightowls-1/cs122b-project1/src/addMovieServlet.java (line 58)
            #### s23-122b-cs122b-team-nightowls-1/cs122b-project1/src/addStarServlet.java (line 53)
            #### s23-122b-cs122b-team-nightowls-1/cs122b-project1/src/MBrowseServlet.java (line 62)
            #### s23-122b-cs122b-team-nightowls-1/cs122b-project1/src/FTSearchServlet.java (line 71)
    
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
        #### We used connection pooling in our project to reduce the cost of creating new connections to the database by keeping a "pool" of open connections that
        #### can be passed around from database operation to database operation. Specifically, we have prepared statements setup in our Mysql database that utilize the
        #### pooling, so we can send queries using the prepared statments to get a faster data return.
    
    - #### Explain how Connection Pooling works with two backend SQL.
        #### Connection Pooling works with the two backend SQL databases by allowing for the speedy data retreival while also 
        #### dividing up the workload between the two database servers.
        
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
        #### s23-122b-cs122b-team-nightowls-1/cs122b-project1/WebContent/META-INF/context.xml
        #### /etc/apache2/sites-enabled/000-default.conf (on apache server of instance 1)

    - #### How read/write requests were routed to Master/Slave SQL?
        #### Read requests are routed to either the Master or Slave servers based on the balancer that's set up on instance 1 (and GCP instance).
        #### Write are routed only to the Master SQL by the load balancer.
    

- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.


- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis**                                                                                                                                                         |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 179                        | 110.61812297734627                  | 110.38133764832794        | Expected to have the smallest average times among all cases because testing against 1 single thread. Server will respond quickly as it's not under heavy stress/load |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | 590                        | 523.6816009557945                   | 523.468538430904          | Expected to high increasing in the average times compared to 1 thread due to server being under heavy stress/load of the 10 threads                                  |
| Case 3: HTTPS/10 threads                       | ![](path to image in img/)   | 595                        | 510.795154185022                    | 510.26571886263514        | Expected to have similar TJ and TS average times compared to HTTP with 10 threads, higher average query time due to overhead of secure connections through HTTPS     |
| Case 4: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 593                        | 519.7291583566015                   | 448.44834463502195        | Without connection pooling, the average times are still similar to case with connection pooling enabled                                                              |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis**                                                                                                                                                                                                                                         |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 280                        | 208.038789025544                    | 207.2961210974456         | Expected to have similar average times as case 1 from single instance because we were sending requests to only one server with sticky session                                                                                                        |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | 607                        | 535.9202630497329                   | 535.1765310316482         | Expected to see a significant decrease in the average times compared to single-instance version as requested are distributed into both the master and slave instances. Loads are balance among two instances hence reduce the heavy stress onto one  |
| Case 3: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 548                        | 478.2522723056947                   | 476.92060842144315        | Expected to see higher average times compared to other scaled-instance cases due to the overhead of establish new connection for each request. However, comparing to single-instance version, the average times are all smaller due to load balancer |


    



    

    
