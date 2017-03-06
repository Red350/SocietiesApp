#!/usr/bin/env python3

import uuid
import database
import http
import encrypt

data = http.get_request()
http.send_header()
conn = database.get_conn()
cur = conn.cursor()


student_num = data["student_num"].value
password = data["password"].value
name = data["name"].value
email = data["email"].value
dob = data["dob"].value
mobile = data["mobile"].value
emergency_ph = data["emergency_ph"].value
date_joined = data["date_joined"].value
full_part_time = data["full_part_time"].value

# Generate salt and password hash
salt = str(uuid.uuid4().hex)
pass_hash = encrypt.generate_hash(password, salt)

sql = "SELECT email FROM member WHERE(email = '" + email + "');"
cur.execute(sql)
if cur.rowcount != 0:
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
    cur.execute(sql)
    response = http.generate_returncode(0)

# Send response
http.send_response(response)

# Close db connection
conn.commit()
conn.close()

