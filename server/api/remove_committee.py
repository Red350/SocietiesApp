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
            # Remove committee member
            sql = "DELETE FROM committee_society WHERE(member_id = " + member_id + " AND society_id = " + society_id + ");"
            try:
                db.cur.execute(sql)
                response = api.set_returncode(0)
            except:
                # Database error
                response = api.set_returncode(6)
        else:
            # Invalid permissions
            response = api.set_returncode(7)
    else:
        # Invalid session id
        response = api.set_returncode(1)
else:
    # Invalid post request
    response = api.set_returncode(5)

# Send response
api.send_response()

# Close db connection
db.close()

