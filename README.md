# java-filmorate
На рисунке можно найти схему базы данных для приложения.
![diagramm of database](https://github.com/KonstantinPanchik/java-filmorate/blob/main/diagram_filmorate.png)

пример запроса по нахождению общих друзей у двух пользователей
'''SQL
SELECT f.friend_id
FROM friendship AS f
WHERE f.status='yes' AND 
f.user_id=1 AND
f.friend_id IN(SELECT fs.friend_id
               FROM friendship AS fs
               WHERE fs.user_id=2 AND fs.status='yes') 
'''
