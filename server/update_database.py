#!C:\Python\python.exe

import cgi
import cgitb
import json
import connect

cgitb.enable()

print("Content-type: text/html\n\n")

form = cgi.FieldStorage()

data = {"name": form["name"].value, "num": form["num"].value}
json_data = json.dumps(data)
print(json_data)

# Query database
conn = connect.get_conn()
cur = conn.cursor()
cur.execute("SELECT * FROM member")

# Display query data
# print(cur.description)
# print()
# for row in cur:
#     print(row, "<br>")

cur.close()
conn.close()
