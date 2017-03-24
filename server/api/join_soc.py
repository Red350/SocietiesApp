#!/usr/bin/env python3

import database
import http

# Send header
http.send_json_header()

# Ensure the correct post keys were sent
if http.check_keys(("member_id", "session_id", "society_id", "token")):
    member_id = http.post["member_id"].value
    session_id = http.post["session_id"].value
    society_id = http.post["society_id"].value
    token = http.post["token"].value
    
    # Check if the user is logged in
    if database.check_session(member_id, session_id):
        # Check if the token is valid
        try:
            sql = "SELECT * FROM join_token WHERE(society_id = " + society_id " AND token = '" + token + "');"
            database.cur.execute(sql)
            if database.cur.rowcount != 0:
                # Insert the user into the database
                sql = "INSERT INTO member_society(member_id, society_id) VALUES(" + member_id, +", " + society_id + ");"
                database.cur.execute(sql)
                response = http.generate_returncode(0)
            else:
                # Invalid join token
                response = http.generate_returncode(8)
        except:
            # Database error
            response = http.generate_returncode(6)
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

