#!/usr/bin/env python3

import uuid
import database
import api
import encrypt

db = database.Database()
api = api.Api("json")

# Ensure the correct post keys were sent
if api.check_keys(("email", "password")):
    email = api.request["email"].value
    password = api.request["password"].value
    
    # Check user's password, and that they are verified
    sql = "SELECT verified, pass_hash, salt, member_id FROM member WHERE( email = '" + email + "');"
    db.cur.execute(sql)
    if db.cur.rowcount == 1:
        row = db.cur.fetchone()
        if row[0] == 'N':
            response = api.set_returncode(4)
        else:
            # Check the users password
            pass_hash = row[1]
            salt = row[2]
            check_hash = encrypt.generate_hash(password, salt)
            if pass_hash == check_hash:
                
                # Clear any old session IDs before generating a new one
                member_id = row[3]
                sql = "DELETE FROM session WHERE(member_id = " + str(member_id) + ");"
                db.cur.execute(sql)
    
                # Generate random session ID for user
                session_id = str(uuid.uuid4().hex)
    
                # Store session ID for that user in the database
                sql = "INSERT INTO session VALUES(" + str(member_id) + ",'" + str(session_id) + "');"
                db.cur.execute(sql)
    
                response = api.set_returncode(0)
                api.update_response("session_id", session_id)
                api.update_response("member_id", member_id)
            else:
                response = api.set_returncode(2)
    else:
        response = api.set_returncode(2)
else:
    response = api.set_returncode(5)

# Send response
api.send_response()

# Close db connection
db.close()
