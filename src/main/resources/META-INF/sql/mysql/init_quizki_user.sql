CREATE USER 'quizki'@'localhost' IDENTIFIED BY 'quizki';
CREATE DATABASE quizki_db;
GRANT ALL ON quizki_db.* TO 'quizki'@'localhost';
