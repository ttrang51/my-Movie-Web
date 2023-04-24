
with r(movieId, rating) as (select movieId, rating
from ratings order by rating desc limit 20),
g(movieId, genres) as (select movieId, GROUP_CONCAT(g.name) as genres
from genres_in_movies as gim, genres as g
where gim.genreId = g.id
group by movieId),
s(movieId, stars, starId) as(select movieId, substring_index(group_concat(s.name separator ','), ',', 3) as stars,
substring_index(group_concat(sim.starId separator ','), ',', 3) as starId
from stars_in_movies as sim, stars as s
where sim.starId = s.id
group by movieId)  

select Id, m.title, m.year, m.director, genres, stars, s.starId, rating
from movies as m, r, g, s
where r.movieId = m.Id and r.movieId = g.movieId and r.movieId = s.movieId
order by rating desc;
