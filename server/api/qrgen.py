#!/usr/bin/env python3

import database
from qrcode import *
import api
import uuid
import delete_qr    # Deletes old QR codes

db = database.Database()
api = api.Api("json")

# Ensure the correct request keys were sent
if api.check_keys(("member_id", "session_id", "society_id")):
    member_id = api.request["member_id"].value
    session_id = api.request["session_id"].value
    society_id = api.request["society_id"].value
    # Ensure user is a committee member for the society
    if db.check_session(member_id, session_id):
        if db.check_committee(member_id, society_id):
            token = str(uuid.uuid4().hex)
            qrdata = "{ \"token\": \"" + token + "\", \"society_id\": \"" + society_id + "\"}"
    
            # Generate qr code
            qr = QRCode(version=None, box_size=10, error_correction=ERROR_CORRECT_L)
            qr.add_data(qrdata)
            qr.make(fit=True)
    
            im = qr.make_image()
            im.save("/var/www/html/img/" + token + ".png")
    
            # Store the token in the database
            sql = "INSERT INTO join_token VALUES(" + society_id + ", '" + token + "', NULL)"
            db.cur.execute(sql)
    
            response = api.set_returncode(0)
            api.update_response("token", token)
        else:
            response = api.set_returncode(7)
    else:
        response = api.set_returncode(1)
else:
    response = api.set_returncode(5)

# Send response
api.send_response()

# Close db connection
db.close()

