# java-filmorate
Template repository for Filmorate project.

## Схема базы данных filmorate
![filmorate](src/main/resources/db-scheme/filmorate-db-scheme.png)

## Часть SQL запросов

>#### получение всех фильмов

```sql
SELECT name
FROM film;
```
---
>#### топ N наиболее популярных фильмов
```sql
SELECT f.name film_name,
count(f.id) likes
FROM film f
JOIN film_likes fl ON f.id = fl.film_id
GROUP BY film_name
ORDER BY likes DESC
LIMIT 5;
```
---
>#### всех пользователей
```sql
SELECT *
FROM public.user;
```

---
>#### список друзей пользователя по id
```sql
SELECT name
FROM public.user
WHERE id IN
    (SELECT uf.friend_id
     FROM public.user u
     LEFT JOIN public.user_friends uf ON u.id = uf.user_id
     WHERE u.id = 1);
```

---
>#### список общих друзей с другим пользователем
```sql
--через union
SELECT name
FROM
  (SELECT uf.friend_id
   FROM public.user u
   LEFT JOIN public.user_friends uf ON u.id = uf.user_id
   WHERE u.id = 1
   UNION ALL 
SELECT uf.friend_id
   FROM public.user u
   LEFT JOIN public.user_friends uf ON u.id = uf.user_id
   WHERE u.id = 3) t
INNER JOIN public.user u ON u.id = t.friend_id
GROUP BY t.friend_id,
         u.id
HAVING count(friend_id) >1;

--через cte
WITH cte AS
         (SELECT uf.friend_id
          FROM public.user u
                   LEFT JOIN public.user_friends uf ON u.id = uf.user_id
          WHERE u.id = 1)
SELECT uf.friend_id, u1.name
FROM public.user u
         LEFT JOIN public.user_friends uf ON u.id = uf.user_id
         INNER JOIN cte ON uf.friend_id = cte.friend_id
         INNER JOIN public.user u1 ON u1.id = uf.friend_id
WHERE u.id = 3;
```
---


