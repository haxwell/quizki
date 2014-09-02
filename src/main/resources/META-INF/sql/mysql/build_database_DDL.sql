-- Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
--
-- This file is part of Quizki.
--
-- Quizki is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 2 of the License, or
-- (at your option) any later version.
--
-- Quizki is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with Quizki. If not, see http://www.gnu.org/licenses.



DROP TABLE IF EXISTS users_roles_map;
DROP TABLE IF EXISTS question_topic;
DROP TABLE IF EXISTS question_choice;
DROP TABLE IF EXISTS question_reference;
DROP TABLE IF EXISTS exam_question;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS topic;
DROP TABLE IF EXISTS question_type;
DROP TABLE IF EXISTS difficulty;
DROP TABLE IF EXISTS choice;
DROP TABLE IF EXISTS exam;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS reference;

CREATE TABLE reference (
	id BIGINT NOT NULL AUTO_INCREMENT,
	text	VARCHAR(256)	NOT NULL,
	PRIMARY KEY (id)
	);

CREATE TABLE topic (
	id BIGINT NOT NULL AUTO_INCREMENT,
	text	VARCHAR(128)	NOT NULL,
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
	text	VARCHAR(10000)	NOT NULL,	
	difficulty_id BIGINT NOT NULL,
	type_id	BIGINT	NOT NULL,
	user_id BIGINT NOT NULL,
	description	VARCHAR(1000),
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

CREATE TABLE question_reference (
	question_id	BIGINT	NOT NULL,
	reference_id	BIGINT	NOT NULL,
	FOREIGN KEY (question_id) REFERENCES question(id),	
	FOREIGN KEY (reference_id) REFERENCES reference(id)
	);

CREATE TABLE question_topic (
	question_id	BIGINT	NOT NULL,
	topic_id	BIGINT	NOT NULL,
	FOREIGN KEY (question_id) REFERENCES question(id),	
	FOREIGN KEY (topic_id) REFERENCES topic(id)
	);
	
