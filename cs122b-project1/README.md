# CS 122B Project 3 Report


## URL of the Demo video

#### https://uci.zoom.us/rec/share/CGzhl0H9pLgmIt_9WyTH-SBEHKmOzQ76YkkBYqXWgYLiKfu4tySZaU1iUX9swTEW.2N7MpbgnJFwxDYRn?startTime=1684132052000

## Contribution
***Anthonny***
    
    Completed:
    Task 1: Adding reCaptcha
    Task 2: Adding HTTPS
    Task 4: Use Encrypted Password
    Task 5: _Dashboard Login Page
    EC: Domain Registration

***Thi Thuy Trang Tran***
    
    Completed: 
    Task 3: Use PreparedStatement
    Task 5: Add Star/Movie Using Stored Procedure
    Task 6: XML Parsing

***Cooperated Work***
    
    Peer-review 
    Testing and Inspection


## Report

### Numeric report 

    Parsed: 8751 movies
    Parsed: 6839 stars
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
    
    
http://flablixnightowls.com/

    

    
