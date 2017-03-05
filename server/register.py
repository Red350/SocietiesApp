#!/usr/bin/env python3

import database
import http

data = http.get_request()
http.send_header()
conn = database.get_conn()
cur = conn.cursor()


member_id = data["member_id"].value
session_id = data["session_id"].value


if database.check_session(member_id, session_id):
    response = "User registered"
else:
    response = http.generate_error(1)

# Send response
http.send_response(response)

# Close db connection
conn.close()

