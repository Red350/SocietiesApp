#!/usr/bin/env python3

import uuid
import database
import api
import encrypt

db = database.Database()
api = api.Api("json")

# Ensure the correct post keys were sent
if api.check_keys(("member_id", "session_id", "name", "mobile", "emergency_ph")):
    member_id = api.request["member_id"].value
    session_id = api.request["session_id"].value
    name = api.request["name"].value
    mobile = api.request["mobile"].value
    emergency_ph = api.request["emergency_ph"].value

    # Ensure user is logged in
    if db.check_session(member_id, session_id):
        sql = ("UPDATE member SET "
               + "name = '" + name + "', "
               + "mobile = '" + mobile + "', "
               + "emergency_ph = '" + emergency_ph + "' "
               + "WHERE member_id = " + member_id + ";"
               )
        db.cur.execute(sql)

        response = api.set_returncode(0)
    else:
        response = api.set_return_code(1)
else:
    response = api.set_returncode(5)

# Send response
api.send_response()

# Close db connection
db.close()
