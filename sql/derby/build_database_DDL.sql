
DROP TABLE users_roles_map;
DROP TABLE question_topic;
DROP TABLE question_choice;
DROP TABLE exam_question;
DROP TABLE question;
DROP TABLE user_roles;
DROP TABLE topic;
DROP TABLE question_type;
DROP TABLE difficulty;
DROP TABLE choice;
DROP TABLE exam;
DROP TABLE users;

CREATE TABLE topic (
	id INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	text	VARCHAR(100)	NOT NULL
	);

CREATE TABLE question_type (
	id	INTEGER	PRIMARY KEY,
	text	VARCHAR(100)	NOT NULL
	);

CREATE TABLE difficulty (
	id	INTEGER	PRIMARY KEY,
	text	VARCHAR(100)	NOT NULL
	);

CREATE TABLE users (
	id INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	username	VARCHAR(64)	NOT NULL,
	password	VARCHAR(64)	NOT NULL
	);

CREATE TABLE user_roles (
	id	INTEGER	PRIMARY KEY,
	text	VARCHAR(100)	NOT NULL
	);

CREATE TABLE users_roles_map (
	user_id	INTEGER	NOT NULL,
	role_id	INTEGER	NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (role_id) REFERENCES user_roles(id)
	);

CREATE TABLE question (
	id INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	description	VARCHAR(1000)	NOT NULL,
	text	VARCHAR(1000)	NOT NULL,	
	difficulty_id INTEGER NOT NULL,
	type_id	INTEGER	NOT NULL,
	user_id INTEGER NOT NULL,
	FOREIGN KEY (difficulty_id) REFERENCES difficulty(id),
	FOREIGN KEY (type_id) REFERENCES question_type(id),
	FOREIGN KEY (user_id) REFERENCES users(id)
	);

CREATE TABLE choice (
	id INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	text	VARCHAR(1000)	NOT NULL,
	isCorrect	INTEGER	NOT NULL,
	sequence	INTEGER
	);

CREATE TABLE question_choice (
	question_id	INTEGER	NOT NULL,
	choice_id	INTEGER	NOT NULL,
	FOREIGN KEY (question_id) REFERENCES question(id),
	FOREIGN KEY (choice_id) REFERENCES choice(id)
	);

--CREATE TABLE user_question (
--	question_id	INTEGER	NOT NULL,
--	user_id	INTEGER	NOT NULL,
--	FOREIGN KEY (question_id) REFERENCES question(id),	
--	FOREIGN KEY (user_id) REFERENCES users(id)
--	);

--CREATE TABLE question_version (
--	question_id	INTEGER	FOREIGN KEY REFERENCES question(id),
--	archived_q_id	INTEGER	NOT NULL,
--	q_version	INTEGER	NOT NULL
--	);

--CREATE TABLE archived_questions (
--	id	INTEGER	PRIMARY KEY,
--	text	VARCHAR(1000)	NOT NULL,
--	type	INTEGER	NOT NULL
--	);

--CREATE TABLE archived_choice (
--	id	INTEGER	PRIMARY KEY,
--	text	VARCHAR(1000)	NOT NULL,
--	isCorrect	INTEGER	NOT NULL
--	);

CREATE TABLE exam (
	id INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	title	VARCHAR(128)	NOT NULL,
	user_id	INTEGER	NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id)
	);

CREATE TABLE exam_question (
	exam_id	INTEGER	NOT NULL,
	question_id	INTEGER	NOT NULL,
	FOREIGN KEY (exam_id) REFERENCES exam(id),	
	FOREIGN KEY (question_id) REFERENCES question(id)
	);

--CREATE TABLE user_exam (
--	exam_id	INTEGER	NOT NULL,
--	user_id	INTEGER	NOT NULL,
--	FOREIGN KEY (exam_id) REFERENCES exam(id),	
--	FOREIGN KEY (user_id) REFERENCES users(id)
--	);

CREATE TABLE question_topic (
	question_id	INTEGER	NOT NULL,
	topic_id	INTEGER	NOT NULL,
	FOREIGN KEY (question_id) REFERENCES question(id),	
	FOREIGN KEY (topic_id) REFERENCES topic(id)
	);

--CREATE TABLE vote (
--	user_id	INTEGER	FOREIGN KEY REFERENCES users(id),
--	question_id	INTEGER	FOREIGN KEY REFERENCES question(id)
--	question_version	INTEGER 	NOT NULL,
--	vote	INTEGER	NOT NULL
--	);

