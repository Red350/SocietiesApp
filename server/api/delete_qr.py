#!/usr/bin/env python3

import database
import http
import subprocess

db = database.Database()

# Delete old tokens and qr images from the server
# Command for testing:
# SELECT token, UNIX_TIMESTAMP(CURRENT_TIMESTAMP) - UNIX_TIMESTAMP(creation_time) FROM join_token WHERE(UNIX_TIMESTAMP(CURRENT_TIMESTAMP) - UNIX_TIMESTAMP(creation_time) > 300);
del_condition = "UNIX_TIMESTAMP(CURRENT_TIMESTAMP) - UNIX_TIMESTAMP(creation_time) > 300"
sql = "SELECT token, creation_time FROM join_token WHERE(" + del_condition + ");"
db.cur.execute(sql)
result = db.cur.fetchall()
# Delete images and database entries
# This is a bit inefficient, but I didn't to end up in a situation where a database entry was removed
# and an image left behind
for row in result:
    path = "/var/www/html/img/" + row[0] + ".png"
    subprocess.call(["rm", path])
    sql = "DELETE FROM join_token WHERE(token = '" + row[0] + "')";
    db.cur.execute(sql)
# Delete database entries
#sql = "DELETE FROM join_token WHERE(" + del_condition + ");"
#database.cur.execute(sql);
