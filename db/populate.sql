INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, full_part_time, verified)
	VALUES(1, 'C1', 'hash', 'salt', 'John', 'c12345678@example.com', '1990-01-01', '1234', '1234', 'F', 'Y');
INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, full_part_time, verified)
	VALUES(2, 'C2', 'hash', 'salt', 'Daniel', 'c12345679@example.com', '1990-01-01', '1234', '1234', 'F', 'Y');
INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, full_part_time, verified)
	VALUES(3, 'C3', 'hash', 'salt', 'Rob', 'c92345679@example.com', '1990-01-01', '1234', '1234', 'F', 'Y');
INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, full_part_time, verified)
	VALUES(4, 'C4', 'hash', 'salt', 'Eoghan', 'c4@example.com', '1990-01-01', '1234', '1234', 'F', 'Y');

INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, full_part_time, verified)
	VALUES(5, 'C5', '680c22c62f6e2988a9c00a3f342b0e7e13260094ff4f1fbcb7d71b61f71e8855', '3afcbf5db698460a9955de31933cdffb', 'test', 'test@example.com', '1990-01-01', '1234', '1234', 'F', 'Y');

INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, full_part_time, verified)
	VALUES(6, 'C6', '01619183f6d8027223349084438db860a3a3c219459f3c63d459a7a4ebbb419f', '8887050dd11c4ba3919e7317a7da9399', 'Simon', 'test@test.com', '1990-01-01', '1234', '1234', 'F', 'Y');

-- Test user 50
INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, full_part_time, verified)
	VALUES(50, 'C5', 'a4105b96aad650d6789a99d5cce5b97899afb591d5cf872976e0c9721435b41a', '33b38c18bec14640b0a9b168f637c3a2', 'test', 'test', '1990-01-01', '1234', '1234', 'F', 'Y');
-- Test user s
INSERT INTO member(member_id, student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, full_part_time, verified)
	VALUES(51, 'C6', '33d520e63beb407d102983e0e53827de119c02b2dcbf85e12f770614de51e699', '3fc900dd3fbe4bbb85d808d1fa04fe46', 'Simon', 's', '1990-01-01', '1234', '1234', 'F', 'Y');

-- Create an admin
INSERT INTO admin(admin_id, email, pass_hash, salt)
	VALUES(1, 'blah@blah.blah', '13f78571c402d2fded28dfa1fa10ca5f11ede839a43d7b44a79f9c0a62b0e46c', 'e740c300b567480e872a6eb287218596');

-- Create compsoc and chess soc
-- Rob and Mary are chairs of compsoc and chess respectively
INSERT INTO society(society_id, name, email, description, chair_id) VALUES (1, 'CompSoc', 'compsoc@example.com', 'Best society', 50);
INSERT INTO society(society_id, name, email, description, chair_id) VALUES (2, 'Chess Soc', 'chess@example.com', 'We play chess', 2);
INSERT INTO society(society_id, name, email, description, chair_id) VALUES (3, 'Circus', 'circ@example.com', 'We play circus', 2);
INSERT INTO society(society_id, name, email, description, chair_id) VALUES (4, 'Fashion', 'fash@example.com', 'We play fash', 2);
INSERT INTO society(society_id, name, email, description, chair_id) VALUES (5, 'Eoghan Soc', 'es@example.com', 'nvm', 2);

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


-- Stuff for test user
INSERT INTO session VALUES(5, '783ed58689a74fbd8b60139399a42862');
INSERT INTO committee_society VALUES(5,1);
INSERT INTO committee_society VALUES(5,2);
INSERT INTO member_society VALUES(5,1);
INSERT INTO member_society VALUES(5,2);


-- Test user 50
-- Is a member of chess, circus and eoghansoc
-- Is a committee member of compsoc and circus
-- Is chair of Compsoc
INSERT INTO member_society(member_id, society_id) VALUES (50, 1);
INSERT INTO member_society(member_id, society_id) VALUES (50, 3);
INSERT INTO member_society(member_id, society_id) VALUES (50, 5);

INSERT INTO committee_society(member_id, society_id) VALUES (50, 1);
INSERT INTO committee_society(member_id, society_id) VALUES (50, 3);
