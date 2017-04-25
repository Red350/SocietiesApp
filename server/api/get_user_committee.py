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
        sql = "SELECT society_id FROM committee_society WHERE(member_id = " + member_id + ");"
        db.cur.execute(sql)
        result = db.cur.fetchall()
        list_of_socs = []
        for row in result:
            list_of_socs.append(str(row[0]))
        response = api.set_returncode(0)
        api.update_response("society_id", list_of_socs)
    else:
        response = api.set_returncode(1)
else:
    response = api.set_returncode(5)

# Send response
api.send_response()

# Close db connection
db.close()

