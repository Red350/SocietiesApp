#!/usr/bin/env python3

import database
import http

# Send header
http.send_header()

member_id = http.post["member_id"].value
session_id = http.post["session_id"].value

if database.check_session(member_id, session_id):
    sql = "SELECT society_id FROM committee_society WHERE(member_id = " + member_id + ");"
    database.cur.execute(sql)
    result = database.cur.fetchall()
    list_of_socs = []
    for row in result:
        list_of_socs.append(str(row[0]))
    response = http.generate_returncode(0)
    response["society_id"] = list_of_socs
else:
    response = http.generate_returncode(1)

# Send response
http.send_response(response)

# Close db connection
database.close()

