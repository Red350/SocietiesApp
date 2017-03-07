#!/usr/bin/env python3

import database
import http

# Send header
http.send_header()

member_id = http.post["member_id"].value
session_id = http.post["session_id"].value

if database.check_session(member_id, session_id):
    response = http.generate_returncode(0)

    # Parse the society IDs that are being requested
    soc_ids = []
    for i in range(len(http.post["society_id"])):
        soc_ids.append(http.post["society_id"][i].value)

    # Get the data for each society
    #sql = "SELECT society_id, name, email, description FROM member_society WHERE(society_id IN(" + str(soc_ids) + "));"
    sql = "SELECT society_id, name, email, description FROM society WHERE("
    for id in soc_ids:
        sql += "society_id = "
        sql += id
        sql += " OR "
    sql = sql[:-4]  # Remove the trailing " OR "
    sql += ")"
    database.cur.execute(sql)
    result = database.cur.fetchall()

    soc_arr = []
    for row in result:
        soc_details ={}
        soc_details["society_id"] = row[0]
        soc_details["name"] = row[1]
        soc_details["email"] = row[2]
        soc_details["description"] = row[3]

        soc_arr.append(soc_details)
    response["society_details"] = soc_arr

else:
    response = http.generate_returncode(1)

# Send response
http.send_response(response)

# Close db connection
database.close()

