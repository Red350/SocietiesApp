#!/usr/bin/env python3

import http
import database
import os
import encrypt
import uuid

database = database.Database()

err = ""
user = ""
passwd = ""
redirect = ""

if os.environ['REQUEST_METHOD'] == 'POST':
    if 'user' not in http.post:
        err += 'Username required'
    else:
        user = http.post['user'].value
    if 'pass' not in http.post:
        err += ' Password required'
    else:
        passwd = http.post['pass'].value

    if 'user' in http.post and 'pass' in http.post:
        sql = "SELECT pass_hash, salt, admin_id FROM admin WHERE email like '%s'" % user
        database.cur.execute(sql)

        if database.cur.rowcount > 0:
            row = database.cur.fetchone()
            if encrypt.generate_hash(passwd, row[1]) == row[0]:
                sql = "DELETE FROM admin_session WHERE admin_id = %s" % row[2]
                database.cur.execute(sql)
                # assign a value
                sesh = str(uuid.uuid4().hex)
                sql = "INSERT INTO admin_session(admin_id, session_id)VALUES(%d,'%s')" % (row[2], sesh)
                database.cur.execute(sql)
                if(database.cur.rowcount > 0):
                    print("Set-Cookie: session_id=%s; Max-Age=%d" % (sesh, 1 * 24 * 60 * 60))
                    redirect = """<meta http-equiv="refresh" content="0; url=home.py">"""
                else:
                    err += "Database error"
        else:
            err += "Username or Password incorrect"

database.close()

output = """<html>
            <head>
            <meta name="viewport" content="width=device-width, initial-scale=1">
<style type="text/css">
    body
    {
        margin:40px auto;
        max-width:650px;
        line-height:1.6;
        font-size:18px;
        color:#444;
        padding:0
        10px
    }

    h1,h2,h3{line-height:1.2}

    </style>
            %s
            <title>Login</title>
            </head>
            <body>
                <p><form class="reg"action="login.py" method="post">
                    <label for="user">User Name</label><input type="text" name="user" value="%s"> <br>
                    <label for="pass">Password</label><input type="password" name="pass"> <br>
                    <label for="Submit">Submit</label><input type="submit" value="Submit"> <br>
                </form></p>
                <p>%s</p>
            </body>
            </html>""" % (redirect, user, err)

print("Content-Type: text/html\n")
print(output)
