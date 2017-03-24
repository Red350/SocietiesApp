#!/usr/bin/env python3

import database
import http

# Send header
http.send_json_header()

# Ensure the correct post keys were sent
if http.check_keys(("chair_id", "session_id", "society_id", "member_id")):
    chair_id = http.post["chair_id"].value
    session_id = http.post["session_id"].value
    society_id = http.post["society_id"].value
    member_id = http.post["member_id"].value
    
    if database.check_session(chair_id, session_id):
        if database.check_chair(chair_id, society_id):
            # Add member as committee member
            sql = "INSERT INTO committee_society(member_id, society_id) VALUES(" + member_id + ", " + society_id + ");"
            try:
                database.cur.execute(sql)
                response = http.generate_returncode(0)
            except:
                # Database error
                response = http.generate_returncode(6)
        else:
            # Invalid permissions
            response = http.generate_returncode(7)
    else:
        # Invalid session id
        response = http.generate_returncode(1)
else:
    # Invalid post request
    response = http.generate_returncode(5)

# Send response
http.send_response(response)

# Close db connection
database.close()

