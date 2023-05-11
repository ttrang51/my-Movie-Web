Use moviedb;
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