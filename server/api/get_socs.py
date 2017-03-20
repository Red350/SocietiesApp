#!/usr/bin/env python3

import database
import http

# Send header
http.send_json_header()

# Ensure the correct post keys were sent
if http.check_keys(("member_id", "session_id", "society_id")):
    member_id = http.post["member_id"].value
    session_id = http.post["session_id"].value
    
    if database.check_session(member_id, session_id):
        response = http.generate_returncode(0)
        # Split the society_id string and store in list
        soc_ids = [id.strip() for id in http.post['society_id'].value.split(',')] 
    
        # Get the data for each society
        sql = "SELECT society_id, name, email, description FROM society WHERE(society_id IN(" + ','.join(soc_ids) + "));"
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
else:
    response = http.generate_returncode(5)

# Send response
http.send_response(response)

# Close db connection
database.close()

