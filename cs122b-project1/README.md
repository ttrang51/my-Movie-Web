# CS 122B Project 5 Report

- # General
    - #### Team#: Nightowls
    
    - #### Names: Anthony Martinez and Thi Thuy Trang Tran
    
    - #### Project 5 Video Demo Link:

    - #### Instruction of deployment:

    - #### Collaborations and Work Distribution:

    ***Anthony***
    
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
        ####
        ####
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
        #### s23-122b-cs122b-team-nightowls-1/cs122b-project1/WebContent/META-INF/context.xml
        #### /etc/apache2/sites-enabled/000-default.conf (on apache server of instance 1)

    - #### How read/write requests were routed to Master/Slave SQL?
        #### 
        #### 
    

- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.


- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 3: HTTPS/10 threads                       | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 4: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 3: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |


    



    

    
