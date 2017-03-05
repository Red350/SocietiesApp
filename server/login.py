#!/usr/bin/env python3

import uuid
import database
import http

data = http.get_request()
http.send_header()
conn = database.get_conn()
cur = conn.cursor()

member_id = data["member_id"].value
password = data["password"].value

# Clear any old session IDs
sql = "DELETE FROM session WHERE(member_id = " + member_id + ");"
cur.execute(sql)

# Check user's password
sql = "SELECT * FROM member WHERE( member_id = " + member_id + " AND pass_hash='" + password + "');"
cur.execute(sql)
if cur.rowcount == 1:
    # Generate random session ID for user
    session_id = str(uuid.uuid4().hex)

    # Store session ID for that user in the database
    sql = "INSERT INTO session VALUES(" + str(member_id) + ",'" + str(session_id) + "');"
    cur.execute(sql)
    conn.commit()

    response = {"return_code": "0", "session_id": session_id}
else:
    response = http.generate_error(2)

# Send response
http.send_response(response)

# Close db connection
conn.close()
