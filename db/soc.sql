-- Database for societies app
-- Unique IDs 6 digits
-- Names and emails 50 characters

DROP TABLE session;
DROP TABLE member_society;
DROP TABLE committee_society;
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
	date_joined DATE,
	full_part_time CHAR CHECK (fullPartTime IN ('F', 'P', 'N')),
	verified CHAR CHECK (verfied IN ('Y', 'N'))
);

CREATE TABLE society(
	society_id INTEGER(6) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50),
	email VARCHAR(50),
	description VARCHAR(400),
	chair_id INTEGER(6),

	FOREIGN KEY(chair_id) REFERENCES member(member_id)
);

CREATE TABLE admin(
	member_id INTEGER(6) PRIMARY KEY,

	FOREIGN KEY(member_id) REFERENCES member(member_id)
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
