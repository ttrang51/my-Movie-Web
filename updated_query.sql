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





