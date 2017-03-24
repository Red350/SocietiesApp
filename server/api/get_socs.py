#!/usr/bin/env python3

import database
import api

db = database.Database()
api = api.Api("json")

# Ensure the correct post keys were sent
if api.check_keys(("member_id", "session_id", "society_id")):
    member_id = api.request["member_id"].value
    session_id = api.request["session_id"].value
    
    if db.check_session(member_id, session_id):
        # Split the society_id string and store in list
        soc_ids = [id.strip() for id in api.request['society_id'].value.split(',')] 
    
        # Get the data for each society
        sql = "SELECT society_id, name, email, description FROM society WHERE(society_id IN(" + ','.join(soc_ids) + "));"
        try:
            db.cur.execute(sql)
            result = db.cur.fetchall()
    
            soc_arr = []
            for row in result:
                soc_details ={}
                soc_details["society_id"] = row[0]
                soc_details["name"] = row[1]
                soc_details["email"] = row[2]
                soc_details["description"] = row[3]
    
                soc_arr.append(soc_details)
            
            api.set_returncode(0)
            api.update_response("society_details", soc_arr)
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

