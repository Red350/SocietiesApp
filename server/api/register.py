#!/usr/bin/env python3

import uuid
import database
import http
import encrypt

# Send header
http.send_header()

student_num = http.post["student_num"].value
password = http.post["password"].value
name = http.post["name"].value
email = http.post["email"].value
dob = http.post["dob"].value
mobile = http.post["mobile"].value
emergency_ph = http.post["emergency_ph"].value
date_joined = http.post["date_joined"].value
full_part_time = http.post["full_part_time"].value

# Generate salt and password hash
salt = str(uuid.uuid4().hex)
pass_hash = encrypt.generate_hash(password, salt)

sql = "SELECT email FROM member WHERE(email = '" + email + "');"
database.cur.execute(sql)
if database.cur.rowcount != 0:
    response = http.generate_returncode(3)
else:
    sql = ("INSERT INTO member(student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, date_joined, full_part_time, verified) VALUES("
           + "'" + student_num + "'" + ", "
           + "'" + pass_hash + "'" + ", "
           + "'" + salt + "'" + ", "
           + "'" + name + "'" + ", "
           + "'" + email + "'" + ", "
           + "'" + dob+ "'" + ", "
           + "'" + mobile + "'" + ", "
           + "'" + emergency_ph + "'" + ", "
           + "'" + date_joined + "'" + ", "
           + "'" + full_part_time + "'" + ", 'Y');"
           )
    database.cur.execute(sql)
    response = http.generate_returncode(0)

# Send response
http.send_response(response)

# Close db connection
database.close()