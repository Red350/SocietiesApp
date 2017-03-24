#!/usr/bin/env python3

import database
import api

db = database.Database()
api = api.Api("json")

# Ensure the correct post keys were sent
if api.check_keys(("member_id", "session_id")):
    member_id = api.request["member_id"].value
    session_id = api.request["session_id"].value
    
    if db.check_session(member_id, session_id):
        api.set_returncode(0)
    else:
        api.set_returncode(1)
else:
    api.set_returncode(5)

# Send response
api.send_response()

# Close db connection
db.close()

