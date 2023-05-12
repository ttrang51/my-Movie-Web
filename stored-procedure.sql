Use moviedb;
-- Change DELIMITER to $$
DELIMITER $$
CREATE PROCEDURE test_add (
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
SELECT "movie is new" as answer;
END; -- end of else
END IF;

END
$$
-- Change back DELIMITER to ;
DELIMITER ;


