Use moviedb;
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