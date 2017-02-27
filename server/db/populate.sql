INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, date_joined, full_part_time, verified)
	VALUES(1, 'C1', 'hash', 'salt', 'John', 'c12345678@example.com', '1990-01-01', '1234', '1234', '2017-02-27', 'F', 'Y');
INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, date_joined, full_part_time, verified)
	VALUES(2, 'C2', 'hash', 'salt', 'Daniel', 'c12345679@example.com', '1990-01-01', '1234', '1234', '2017-02-27', 'F', 'Y');
INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, date_joined, full_part_time, verified)
	VALUES(3, 'C3', 'hash', 'salt', 'Rob', 'c92345679@example.com', '1990-01-01', '1234', '1234', '2017-02-27', 'F', 'Y');
INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, date_joined, full_part_time, verified)
	VALUES(4, 'C4', 'hash', 'salt', 'Eoghan', 'c4@example.com', '1990-01-01', '1234', '1234', '2017-02-27', 'F', 'Y');

-- Create compsoc and chess soc
-- Rob and Mary are chairs of compsoc and chess respectively
INSERT INTO society(society_id, name, email, description, chair_id) VALUES (1, 'CompSoc', 'compsoc@example.com', 'Best society', 3);
INSERT INTO society(society_id, name, email, description, chair_id) VALUES (2, 'Chess Soc', 'chess@example.com', 'We play chess', 2);

-- John is an admin
INSERT INTO admin(member_id) VALUES(1);

-- Rob and Eoghan are committee members of compsoc
INSERT INTO committee_society(member_id, society_id) VALUES (3, 1); 
INSERT INTO committee_society(member_id, society_id) VALUES (4, 1); 

-- Daniel is a committee member of chess
INSERT INTO committee_society(member_id, society_id) VALUES (4, 2); 

-- Daniel, Rob and Eoghan are members of compsoc
INSERT INTO member_society(member_id, society_id) VALUES (2, 1);
INSERT INTO member_society(member_id, society_id) VALUES (3, 1);
INSERT INTO member_society(member_id, society_id) VALUES (4, 1);

-- Daniel is a member of chess
INSERT INTO member_society(member_id, society_id) VALUES (2, 2);
