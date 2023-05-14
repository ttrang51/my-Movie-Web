Use moviedbexample;

DELIMITER $$
CREATE FUNCTION add_star (star_name VARCHAR(100), birth_year INT)
RETURNS VARCHAR(10)
DETERMINISTIC
BEGIN
SET @prefix = 'nm';
SET @largest_varchar_id = (select max(id) from stars);
SET @numeric_part = CAST(SUBSTRING(@largest_varchar_id, 3) AS UNSIGNED) + 1;
SET @new_varchar_id = CONCAT(@prefix, @numeric_part);
insert into stars (id, name, birthYear) values (@new_varchar_id, star_name, birth_year);
RETURN @new_varchar_id;
END
$$
DELIMITER ;

-- Change DELIMITER to $$
DELIMITER $$
CREATE PROCEDURE add_movie (
	IN m_title VARCHAR(100), 
    IN m_year INT, 
    IN m_director VARCHAR(100),
    IN star_name VARCHAR(100),
    IN birth_year INT,
    IN genre_name VARCHAR(32)
    )
BEGIN
-- check movie duplication
DECLARE dup INT;
set @dup = (select exists(select 1 from movies as m 
						where m.title = m_title and m.year = m_year and m.director = m_director));
IF @dup = 1 THEN
SELECT "duplicated" as answer;
ELSE
BEGIN -- begin of else
	-- generate new movie id
	SET @prefix = 'tt';
	SET @largest_varchar_id = (select max(id) from movies);
	SET @numeric_part = CAST(SUBSTRING(@largest_varchar_id, 3) AS UNSIGNED) + 1;
	SET @new_movie_id = CONCAT(@prefix, @numeric_part);
    -- insert new movie
    insert into movies (id, title, year, director) values (@new_movie_id, m_title, m_year, m_director);
    
	-- check if star or genre exists
    set @star_flag = (select exists(select 1 from stars where name = star_name));
    set @genre_flag = (select exists(select 1 from genres where name = genre_name));
    
    -- add new star if not exist
    if @star_flag != 1 then
		set @star_id = (select add_star(star_name, birth_year));
	else 
		set @star_id = (select id from stars where name = star_name);
    end if;
    -- link star to newly added movie
    insert into stars_in_movies (starId, movieId) values (@star_id, @new_movie_id);
    
    -- add new genre if not exist 
    if @genre_flag != 1 then
		insert into genres (name) values (genre_name);
    end if;
    set @genre_id = (select id from genres where name = genre_name);
    -- link genre to newly added movie
    insert into genres_in_movies (genreId, movieId) values (@genre_id, @new_movie_id);
    
    -- link movie to ratings
    insert into ratings (movieId, rating, numVotes) values (@new_movie_id, 0.0, 0);
    
SELECT @new_movie_id as answer;
END; -- end of else
END IF;

END
$$
-- Change back DELIMITER to ;
DELIMITER ;

DELIMITER $$
CREATE FUNCTION add_genre (genre_name VARCHAR(32))
RETURNS INT
DETERMINISTIC
BEGIN
set @genre_flag = (select exists(select 1 from genres where name = genre_name));
-- add new genre if not exist 
    if @genre_flag != 1 then
		insert into genres (name) values (genre_name);
    end if;
SET @new_genre_id = (select id from genres where name = genre_name);
RETURN @new_genre_id;
END
$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE insert_movie (movie_id VARCHAR(10), 
	m_title VARCHAR(100), 
    m_year INT, 
    m_director VARCHAR(100))
BEGIN
DECLARE dup INT;
set @dup = (select exists(select 1 from movies as m 
						where m.title = m_title and m.year = m_year and m.director = m_director));
IF @dup != 1 THEN
    -- insert new movie
    insert into movies (id, title, year, director) values (movie_id, m_title, m_year, m_director);
    -- link movie to ratings
    insert into ratings (movieId, rating, numVotes) values (movie_id, 0.0, 0);
END IF;
END
$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE link_genre_movie (genre_id INT, movie_id VARCHAR(10))
BEGIN
DECLARE dup INT;
set @dup = (select exists(select 1 from genres_in_movies as gim 
						where gim.genreId = genre_id and gim.movieId = movie_id));
set @m_id_not_exist = (select count(*) from movies where id = movie_id);
IF (@dup != 1 and @m_id_not_exist != 0) THEN
insert into genres_in_movies (genreId, movieId) values (genre_id, movie_id);
END IF;
END
$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE link_star_movie (star_id VARCHAR(10), movie_id VARCHAR(10))
BEGIN
set @existed_star = (select count(*) from stars where id = star_id);
set @existed_movie = (select count(*) from movies where id = movie_id);
set @dup = (select exists(select 1 from stars_in_movies as sim 
						where sim.starId = star_id and sim.movieId = movie_id));
IF (@dup != 1 and @existed_star != 0 and @existed_movie != 0) THEN
insert into stars_in_movies (starId, movieId) values (star_id, movie_id);
END IF;
END
$$
DELIMITER ;