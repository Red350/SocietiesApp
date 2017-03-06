#!/usr/bin/env python3

import uuid
import database
import http
import encrypt

data = http.get_request()
http.send_header()
conn = database.get_conn()
cur = conn.cursor()

email = data["email"].value
password = data["password"].value


# Check user's password, and that they are verified
sql = "SELECT verified, pass_hash, salt, member_id FROM member WHERE( email = '" + email + "');"
cur.execute(sql)
if cur.rowcount == 1:
    row = cur.fetchone()
    if row[0] == 'N':
        response = http.generate_returncode(4)
    else:
        # Check the users password
        pass_hash = row[1]
        salt = row[2]
        check_hash = encrypt.generate_hash(password, salt)
        if pass_hash == check_hash:
            
            # Clear any old session IDs before generating a new one
            member_id = row[3]
            sql = "DELETE FROM session WHERE(member_id = " + str(member_id) + ");"
            cur.execute(sql)

            # Generate random session ID for user
            session_id = str(uuid.uuid4().hex)

            # Store session ID for that user in the database
            sql = "INSERT INTO session VALUES(" + str(member_id) + ",'" + str(session_id) + "');"
            cur.execute(sql)
            conn.commit()

            response = http.generate_returncode(0)
            response["session_id"] = session_id
        else:
            response = http.generate_returncode(2)
else:
    response = http.generate_returncode(2)

# Send response
http.send_response(response)

# Close db connection
conn.close()
