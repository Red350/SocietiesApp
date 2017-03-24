#!/usr/bin/env python3

import database
import api

db = database.Database()
api = api.Api("json")

# Ensure the correct request keys were sent
if api.check_keys(("member_id", "session_id")):
    member_id = api.request["member_id"].value
    session_id = api.request["session_id"].value
    
    # Ensure user has a valid session
    if db.check_session(member_id, session_id):
        try:
            sql = "SELECT society_id FROM society WHERE(chair_id = " + member_id + ");"
            db.cur.execute(sql)
            result = db.cur.fetchall()
            list_of_socs = []
            for row in result:
                list_of_socs.append(str(row[0]))
            api.set_returncode(0)
            api.update_response("society_id", list_of_socs)
        except:
            api.set_returncode(6)
    else:
        api.set_returncode(1)
else:
    api.set_returncode(5)

# Send response
api.send_response()

# Close db connection
db.close()

