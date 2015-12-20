DROP USER 'quizki'@'localhost';
CREATE USER 'quizki'@'localhost' IDENTIFIED BY 'quizki';
DROP DATABASE quizki_db;
CREATE DATABASE quizki_db;
GRANT ALL ON quizki_db.* TO 'quizki'@'localhost';
