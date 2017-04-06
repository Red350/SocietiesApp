#!/usr/bin/env python3

import database
import api
import subprocess

db = database.Database()
api = api.Api("json")

# Ensure the correct post keys were sent
if api.check_keys(("member_id", "session_id", "society_id", "token")):
    member_id = api.request["member_id"].value
    session_id = api.request["session_id"].value
    society_id = api.request["society_id"].value
    token = api.request["token"].value
    
    # Check if the user is logged in
    if db.check_session(member_id, session_id):
        # Check if the token is valid
        try:
            sql = "SELECT * FROM join_token WHERE(society_id = " + society_id + " AND token = '" + token + "');"
            db.cur.execute(sql)
            if db.cur.rowcount != 0:
                # Insert the user into the database
                sql = "INSERT INTO member_society(member_id, society_id) VALUES(" + member_id + ", " + society_id + ");"
                db.cur.execute(sql)
                # Remove the qr code and database entry for the token
                path = "/var/www/html/img/" + token + ".png"
                subprocess.call(["rm", path])
                sql = "DELETE FROM join_token WHERE(token = '" + token + "')";
                db.cur.execute(sql)
                response = api.set_returncode(0)
            else:
                # Invalid join token
                response = api.set_returncode(8)
        except Exception as e:
            # Database error
            response = api.set_returncode(6)
            api.update_response("dberror", str(e))
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

