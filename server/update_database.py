#!/usr/bin/env python3

import cgi
import cgitb
import json
import database

cgitb.enable()

print("Content-type: text/html\n\n")

form = cgi.FieldStorage()

data = {"name": form["name"].value, "num": form["num"].value}
json_data = json.dumps(data)
print(json_data)

# Query database
conn = database.get_conn()
cur = conn.cursor()
cur.execute("SELECT * FROM member")

# Display query data
print(cur.description)
print()
for row in cur:
    print(row, "<br>")

cur.close()
conn.close()
