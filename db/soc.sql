-- Database for societies app
-- Unique IDs 6 digits
-- Names and emails 50 characters

DROP TABLE join_token;
DROP TABLE session;
DROP TABLE member_society;
DROP TABLE committee_society;
DROP TABLE admin_session;
DROP TABLE admin;
DROP TABLE society;
DROP TABLE member;

CREATE TABLE member(
	member_id INTEGER(6) PRIMARY KEY AUTO_INCREMENT,
	student_num VARCHAR(9),
	pass_hash CHAR(64),
	salt CHAR(32),
	name VARCHAR(50),
	email VARCHAR(50) UNIQUE,
	dob DATE,
	mobile VARCHAR(20),
	emergency_ph VARCHAR(20),
	date_joined TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	full_part_time CHAR,
	verified CHAR CHECK (verfied IN ('Y', 'N'))
);

CREATE TABLE society(
	society_id INTEGER(6) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) UNIQUE,
	email VARCHAR(50),
	description VARCHAR(400),
	chair_id INTEGER(6),

	FOREIGN KEY(chair_id) REFERENCES member(member_id)
);

CREATE TABLE admin(
	admin_id INTEGER(6) PRIMARY KEY AUTO_INCREMENT,
	email VARCHAR(50) UNIQUE,
	pass_hash CHAR(64),
	salt CHAR(32)
);

CREATE TABLE committee_society(
	member_id INTEGER(6),
	society_id INTEGER(6),

	PRIMARY KEY(member_id, society_id),
	FOREIGN KEY(member_id) REFERENCES member(member_id),
	FOREIGN KEY(society_id) REFERENCES society(society_id)
);	

CREATE TABLE member_society(
	member_id INTEGER(6),
	society_id INTEGER(6),

	PRIMARY KEY(member_id, society_id),
	FOREIGN KEY(member_id) REFERENCES member(member_id),
	FOREIGN KEY(society_id) REFERENCES society(society_id)
);

CREATE TABLE session(
	member_id INTEGER(6) PRIMARY KEY,
	session_id VARCHAR(32),
	
	FOREIGN KEY(member_id) REFERENCES member(member_id)
);

CREATE TABLE admin_session(
	admin_id INTEGER(6) PRIMARY KEY,
	session_id VARCHAR(32),
	
	FOREIGN KEY(admin_id) REFERENCES admin(admin_id)
);

CREATE TABLE join_token(
	society_id INTEGER(6),
	token VARCHAR(32),
	creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	PRIMARY KEY(society_id, token),
	FOREIGN KEY(society_id) REFERENCES society(society_id)
);

