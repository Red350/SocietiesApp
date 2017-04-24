#!/usr/bin/env python3

import database
import api

db = database.Database()
api = api.Api("json")

# Ensure the correct post keys were sent
if api.check_keys(("member_id", "session_id", "society_id", "committee_id")):
    chair_id = api.request["member_id"].value
    session_id = api.request["session_id"].value
    society_id = api.request["society_id"].value
    member_id = api.request["committee_id"].value
    
    if db.check_session(chair_id, session_id):
        if db.check_chair(chair_id, society_id):
            # Add member as committee member
            sql = "INSERT INTO committee_society(member_id, society_id) VALUES(" + member_id + ", " + society_id + ");"
            try:
                db.cur.execute(sql)
                api.set_returncode(0)
            except:
                # Database error
                api.set_returncode(6)
        else:
            # Invalid permissions
            api.set_returncode(7)
    else:
        # Invalid session id
        api.set_returncode(1)
else:
    # Invalid post request
    api.set_returncode(5)

# Send response
api.send_response()

# Close db connection
db.close()

