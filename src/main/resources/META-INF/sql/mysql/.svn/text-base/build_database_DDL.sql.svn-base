
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
	id BIGINT NOT NULL AUTO_INCREMENT,
	text	VARCHAR(100)	NOT NULL,
	PRIMARY KEY (id)
	);

CREATE TABLE question_type (
	id	BIGINT	NOT NULL,
	text	VARCHAR(100)	NOT NULL,
	PRIMARY KEY (id)
	);

CREATE TABLE difficulty (
	id	BIGINT	NOT NULL,
	text	VARCHAR(100)	NOT NULL,
	PRIMARY KEY (id)
	);

CREATE TABLE users (
	id BIGINT NOT NULL AUTO_INCREMENT,
	username	VARCHAR(64)	NOT NULL,
	password	VARCHAR(64)	NOT NULL,
	PRIMARY KEY (id)
	);

CREATE TABLE user_roles (
	id	BIGINT	NOT NULL,
	text	VARCHAR(100)	NOT NULL,
	PRIMARY KEY (id)
	);

CREATE TABLE users_roles_map (
	user_id	BIGINT	NOT NULL,
	role_id	BIGINT	NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (role_id) REFERENCES user_roles(id)
	);

CREATE TABLE question (
	id BIGINT NOT NULL AUTO_INCREMENT,
	description	VARCHAR(1000)	NOT NULL,
	text	VARCHAR(1000)	NOT NULL,	
	difficulty_id BIGINT NOT NULL,
	type_id	BIGINT	NOT NULL,
	user_id BIGINT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (difficulty_id) REFERENCES difficulty(id),
	FOREIGN KEY (type_id) REFERENCES question_type(id),
	FOREIGN KEY (user_id) REFERENCES users(id)
	);

CREATE TABLE choice (
	id BIGINT NOT NULL AUTO_INCREMENT,
	text	VARCHAR(1000)	NOT NULL,
	isCorrect	INTEGER	NOT NULL,
	sequence	INTEGER,
	PRIMARY KEY (id)
	);

CREATE TABLE question_choice (
	question_id	BIGINT	NOT NULL,
	choice_id	BIGINT	NOT NULL,
	FOREIGN KEY (question_id) REFERENCES question(id),
	FOREIGN KEY (choice_id) REFERENCES choice(id)
	);

CREATE TABLE exam (
	id BIGINT NOT NULL AUTO_INCREMENT,
	title	VARCHAR(128)	NOT NULL,
	user_id	BIGINT	NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users(id)
	);

CREATE TABLE exam_question (
	exam_id	BIGINT	NOT NULL,
	question_id	BIGINT	NOT NULL,
	FOREIGN KEY (exam_id) REFERENCES exam(id),	
	FOREIGN KEY (question_id) REFERENCES question(id)
	);

CREATE TABLE question_topic (
	question_id	BIGINT	NOT NULL,
	topic_id	BIGINT	NOT NULL,
	FOREIGN KEY (question_id) REFERENCES question(id),	
	FOREIGN KEY (topic_id) REFERENCES topic(id)
	);

--CREATE TABLE user_question (
--	question_id	BIGINT	NOT NULL,
--	user_id	BIGINT	NOT NULL,
--	FOREIGN KEY (question_id) REFERENCES question(id),	
--	FOREIGN KEY (user_id) REFERENCES users(id)
--	);

--CREATE TABLE question_version (
--	question_id	BIGINT	FOREIGN KEY REFERENCES question(id),
--	archived_q_id	BIGINT	NOT NULL,
--	q_version	BIGINT	NOT NULL
--	);

--CREATE TABLE archived_questions (
--	id	BIGINT	PRIMARY KEY,
--	text	VARCHAR(1000)	NOT NULL,
--	type	BIGINT	NOT NULL
--	);

--CREATE TABLE archived_choice (
--	id	BIGINT	PRIMARY KEY,
--	text	VARCHAR(1000)	NOT NULL,
--	isCorrect	BIGINT	NOT NULL
--	);


--CREATE TABLE user_exam (
--	exam_id	BIGINT	NOT NULL,
--	user_id	BIGINT	NOT NULL,
--	FOREIGN KEY (exam_id) REFERENCES exam(id),	
--	FOREIGN KEY (user_id) REFERENCES users(id)
--	);

--CREATE TABLE vote (
--	user_id	BIGINT	FOREIGN KEY REFERENCES users(id),
--	question_id	BIGINT	FOREIGN KEY REFERENCES question(id)
--	question_version	BIGINT 	NOT NULL,
--	vote	BIGINT	NOT NULL
--	);
