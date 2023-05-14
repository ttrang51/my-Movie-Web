Use moviedbexample;
DELIMITER $$
CREATE FUNCTION add_genre (genre_name VARCHAR(32))
RETURNS INT
DETERMINISTIC
BEGIN
insert into genres (name) values (genre_name);
SET @new_genre_id = (select id from genres where name = genre_name);
RETURN @new_genre_id;
END
$$
DELIMITER ;