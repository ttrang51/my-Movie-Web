# CS 122B Project 4 Report


## URL of the Demo video

#### https://uci.zoom.us/rec/share/1Fop81yIrlWXGYLLfhwxy3RSk-wWYNojyLWysQqg-7W_QaKmf2ng35wqn01wBBLr.97KDByw3wmNQ_bmG?startTime=1685252252000

## Contribution
***Anthony***
    
    Completed:
    Task 2: Developed an Android App for Fabflix


***Thi Thuy Trang Tran***
    
    Completed: 
    Task 1: Improved the Fabflix by adding Full-text Search and Autocomplete


***Cooperated Work***
    
    Peer-review 
    Testing and Inspection






# Project 3 (PAST) Info Below

## Prepared Statement
    All the pages that accept user input will have its servlet to use PreparedStatement
    Other pages also use PreparedStatement
    User input pages: MSearchServlet, addMovieServlet, addStarServlet, LoginServlet, CheckoutServlet, etc.

## Parsing Time Optimization Strategies
### Naive Approach
    Individually perform insert/execute statements which is expensive and time-consuming
### Batch Insert
    Reduce overhead of individual insert statements
    Reduce round trips between the application and the database server by gouping multiple rows in a single batch
    Minimize parsing overhead as it only needs to parse once for the entire batch of data
    Provide transactional control as batch insert can be performed within a transaction
### LOAD DATA
    Faster parsing for large amount of data as it is designed and optimized for bulk data loading from external sources
    using an efficient parsing mechanism
    Reduce network traffic as data is read directly from the file system rather than being sent over the network
    Minimize logging and indexing during data loading process
    Provide transactional control
    Integration with MySQL features

## Report

### Numeric report 

    Parsed: 8751 movies
    Parsed:  6839 stars
    Parsed: 123 genres
    Inserted 123 genres
    Inserted 6839 stars
    Inserted 8719 movies
    Inserted 9798 into genres_in_movie
    Inserted 25350 into stars_in_movie
    Inconsistent movies: 48
    Dup movie: 12
    Null movie: 3352
    Inconsistent stars: 17053
    Dup star: 12
    Stars not found: 1325


### Inconsistent reasoning

    -   Inconsistent movies: duplicated movies which have same title and movieId; null movies whose required fields like title, movieId, director, year are empty. These
    will not be added to movies table. Either be ignored by DomParser or stored procedure insert_movie.
    -   Inconsistent casts: actors/movies not found. They are in main/actor xml files but are not in cast xml file or the other way around.
    -   Inconsistent stars: duplicated stars with same name and same birth year will be handled in DomParser. Stars with same name and birth year existed in
    database are still added as following instruction from task 5. 
    -   Inconsistent genres: movies existing with no cat are also considered inconsistent hence would not be added to the genres_in_movie table. Genres with not found
    movieId will also be ignored. These will be handeled by the stored procedure: link_genre_movie.


    

    



    

    
