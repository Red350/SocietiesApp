#!/usr/bin/env python3

import database
import api

db = database.Database()
api = api.Api("json")

# Ensure the correct post keys were sent
if api.check_keys(("member_id", "session_id")):
    member_id = api.request["member_id"].value
    session_id = api.request["session_id"].value
    
    # Ensure the user's session is valid
    if db.check_session(member_id, session_id):
        sql = "SELECT member_id, name, email, dob, mobile, emergency_ph, full_part_time FROM member WHERE(member_id = " + member_id + ");"
        db.cur.execute(sql)
        row = db.cur.fetchone();

        # Add user's details to response
        api.update_response("member_id", row[0])
        api.update_response("name", row[1])
        api.update_response("email", row[2])
        api.update_response("dob", str(row[3]))
        api.update_response("mobile", row[4])
        api.update_response("emergency_ph", row[5])
        api.update_response("full_part_time", row[6])

        response = api.set_returncode(0)
    else:
        response = api.set_returncode(1)
else:
    response = api.set_returncode(5)

# Send response
api.send_response()

# Close db connection
db.close()

