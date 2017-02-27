-- Database for societies app

CREATE TABLE society(
	id INTEGER(4) PRIMARY KEY,
	pass_hash VARCHAR(64),
	salt VARCHAR(64),
	name VARCHAR(50),
	email VARCHAR(50),
	description VARCHAR(400),
);

CREATE TABLE member(
	member_id INTEGER(6) PRIMARY KEY
	student_num VARCHAR(9) 
	pass_hash VARCHAR(64),
	salt VARCHAR(64),
	name VARCHAR(50),
	email VARCHAR(50),
	dob DATE,
	mobile VARCHAR(20),
	emergency_ph VARCHAR(20),
	date_joined DATE,
	fullPartTime CHAR ENUM('F', 'P'),
	verified CHAR ENUM('Y', 'N')
);

CREATE TABLE admin(
	member_id INTEGER(6),
	
	FOREIGN KEY(member_id) REFERENCES member(member_id)
);
