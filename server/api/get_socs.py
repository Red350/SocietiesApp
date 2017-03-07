#!/usr/bin/env python3

import database
import http

member_id = http.post["member_id"].value
session_id = http.post["session_id"].value

if database.check_session(member_id, session_id):
    sql = "SELECT society_id, name, email, description FROM member_society WHERE( = " + member_id + ");"
    database.cur.execute(sql)
    result = database.cur.fetchall()
    list_of_socs = []
    for soc_id in result:
        list_of_socs.append(str(soc_id[0]))
    response = http.generate_returncode(0)
    response["society_id"] = list_of_socs
else:
    response = http.generate_returncode(1)

# Send response
http.send_response(response)

# Close db connection
database.close()

