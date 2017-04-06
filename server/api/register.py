#!/usr/bin/env python3

import uuid
import database
import api
import encrypt

db = database.Database()
api = api.Api("json")

# Ensure the correct post keys were sent
if api.check_keys(("email", "password", "student_num", "name", "dob", "mobile", "emergency_ph", "full_part_time")):
    student_num = api.request["student_num"].value
    password = api.request["password"].value
    name = api.request["name"].value
    email = api.request["email"].value
    dob = api.request["dob"].value
    mobile = api.request["mobile"].value
    emergency_ph = api.request["emergency_ph"].value
    full_part_time = api.request["full_part_time"].value
    
    # Generate salt and password hash
    salt = str(uuid.uuid4().hex)
    pass_hash = encrypt.generate_hash(password, salt)
    
    sql = "SELECT email FROM member WHERE(email = '" + email + "');"
    db.cur.execute(sql)
    if db.cur.rowcount != 0:
        response = api.set_returncode(3)
    else:
        sql = ("INSERT INTO member(student_num, pass_hash, salt, name, email, dob, mobile, emergency_ph, full_part_time, verified) VALUES("
               + "'" + student_num + "'" + ", "
               + "'" + pass_hash + "'" + ", "
               + "'" + salt + "'" + ", "
               + "'" + name + "'" + ", "
               + "'" + email + "'" + ", "
               + "'" + dob+ "'" + ", "
               + "'" + mobile + "'" + ", "
               + "'" + emergency_ph + "'" + ", "
               + "'" + full_part_time + "'" + ", 'Y');"
               )
        print(sql)
        db.cur.execute(sql)
        # Check that the user was created successfully
        if db.cur.rowcount != 0:
            response = api.set_returncode(0)
        else:
            response = api.set_returncode(6)
else:
    response = api.set_returncode(5)

# Send response
api.send_response()

# Close db connection
db.close()
