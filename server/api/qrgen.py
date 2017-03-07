#!/usr/bin/env python3

import database
from qrcode import *
import http
import uuid

member_id = http.post["member_id"].value
session_id = http.post["session_id"].value
society_id = http.post["society_id"].value

# Send header
http.send_header()

# Ensure user is a committee member for the society
if database.check_session(member_id, session_id) and database.check_committee(member_id, society_id):
    token = str(uuid.uuid4().hex)
    qrdata = "{ \"token\": \"" + token + "\", \"society_id\": \"" + society_id + "\"}"
    print(qrdata)

    # Generate qr code
    qr = QRCode(version=20, error_correction=ERROR_CORRECT_L)
    qr.add_data(qrdata)
    qr.make()

    im = qr.make_image()
    im.save("/var/www/html/img/" + token + ".png")

    # Store the token in the database
    sql = "INSERT INTO join_token VALUES(" + society_id + ", '" + token + "', NULL)"
    print(sql)
    database.cur.execute(sql)

    response = http.generate_returncode(0)
else:
    response = http.generate_returncode(1)
    

# Send response
http.send_response(response)

# Close db connection
database.close()

