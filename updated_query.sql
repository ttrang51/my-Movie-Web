with r(movieId, rating) as (select movieId, rating from ratings order by rating desc limit 20) 
select r.movieId as Idstarsstarsstars, m.title, m.year, m.director, t.genres, z.stars, z.starId, r.rating 
from (select movieId, GROUP_CONCAT(g.name) as genres from genres_in_movies as gim, genres as g 
where gim.genreId = g.id group by movieId) as t, 
(select movieId, substring_index(group_concat(s.name separator ','), ',', 3) as stars, 
substring_index(group_concat(sim.starId separator ','), ',', 3) as starId 
from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z, r, 
movies as m 
where r.movieId = t.movieId and r.movieId = z.movieId and r.movieId = m.Id 
order by rating desc;  

select m.title, m.id, m.year, m.director, t.genres, z.stars, z.starId, rat.rating
from (select movieId, GROUP_CONCAT(g.name order by g.name asc) as genres from genres_in_movies as gim, genres as g
where gim.genreId = g.id group by movieId) as t,
(select movieId, group_concat(s.name order by s.name separator ',') as stars,
group_concat(sim.starId order by s.name separator ',') as starId
from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,
movies as m, ratings as rat
where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId 
and m.title like '%term%' and z.stars like '%tom%';

select m.title, m.year, m.director, t.genres, z.stars, z.starId, rat.rating
from (select movieId, GROUP_CONCAT(g.name order by g.name asc) as genres from genres_in_movies as gim, genres as g
where gim.genreId = g.id group by movieId) as t,
(select movieId, substring_index(group_concat(s.name separator ','), ',', 3) as stars,
substring_index(group_concat(sim.starId separator ','), ',', 3) as starId
from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,
movies as m, ratings as rat
where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId;

select s.name, s.birthYear, a.titles, a.movieId
from  (select starId, group_concat(m.title order by m.year desc, m.title asc separator ',') as titles,
group_concat(sim.movieId order by m.year desc, m.title asc separator ',') as movieId
from stars_in_movies as sim, movies as m where sim.movieId = m.id group by starId) as a, stars as s
where a.starId = ? and s.Id = ?;

select m.title, m.year, m.director, t.genres, z.stars, z.starId, rat.rating
from (select movieId, GROUP_CONCAT(g.name order by g.name asc) as genres from genres_in_movies as gim, genres as g
where gim.genreId = g.id group by movieId) as t,
(select movieId, group_concat(s.name order by s.name separator ',') as stars,
group_concat(sim.starId order by s.name separator ',') as starId
from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,
movies as m, ratings as rat
where t.movieId = ? and z.movieId = ? and m.Id = ? and rat.movieId = ?;



