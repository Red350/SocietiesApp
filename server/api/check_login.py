#!/usr/bin/env python3

import database
import http

# Send header
http.send_json_header()

# Ensure the correct post keys were sent
if http.check_keys(("member_id", "session_id")):
    member_id = http.post["member_id"].value
    session_id = http.post["session_id"].value
    
    if database.check_session(member_id, session_id):
        response = http.generate_returncode(0)
    else:
        response = http.generate_returncode(1)
else:
    response = http.generate_returncode(5)

# Send response
http.send_response(response)

# Close db connection
database.close()

