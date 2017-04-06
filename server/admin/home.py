#!/usr/bin/env python3

import http
import database
import os

database = database.Database()

err = ""
redirect = ""
sql = ""
second = 0
sndkey = ""
show_table = ""
select = ""
tableList = []
nameList = []

class no_cookie(Exception):
    def __init__(self, value):
        self.value = value
    def __str__(self):
     return repr(self.value)

def get_cookie():
    if 'HTTP_COOKIE' in os.environ:
        key, value = os.environ['HTTP_COOKIE'].split('=')
        cookie = {key: value}
    else:
        raise no_cookie("User not logged in")
    return cookie

try:
    cookie = get_cookie()
    sql = "SELECT session_id, admin_id FROM admin_session WHERE session_id = '%s'" % cookie['session_id']
    database.cur.execute(sql)
    if database.cur.rowcount == 0:
        redirect = """<meta http-equiv="refresh" content="0; url=login.py">"""
except no_cookie:
    redirect = """<meta http-equiv="refresh" content="0; url=login.py">"""


sql = "SHOW tables"
database.cur.execute(sql)

if database.cur.rowcount > 0:
    query = database.cur.fetchall()
    for row in query:
        tableList += row

    for name in tableList:
        select += "<option>%s</option>" % name


if os.environ['REQUEST_METHOD'] == 'POST':

    if 'logout' in http.post:
        print("Set-Cookie: session_id=0; Max-Age=0")
        redirect = """<meta http-equiv="refresh" content="0; url=login.py">"""
    if 'table' in http.post:
        #counts primary keys in a table
        sql = "SHOW index FROM %s WHERE key_name = 'PRIMARY'" % http.post['table'].value
        database.cur.execute(sql)
        if database.cur.rowcount > 1:
           query = database.cur.fetchall()
           row = query[1]
           second = 1
        #retrieves all columns names from a table
        sql = "SHOW columns FROM %s" % http.post['table'].value
        database.cur.execute(sql)

        if database.cur.rowcount > 0:
            query = database.cur.fetchall()
            #stores all column names
            for row in query:
                string = [row[0]]
                nameList += string

            show_table += "<table><tr>"

            sql = "SELECT"
            for name in nameList:
                #adds column name to sql statment
                sql += " %s," % name
                #adds column name to table header
                show_table += "<th>%s</th>" % name

            show_table += "<th>Options</th>"
            show_table += "</tr>"

            #removes last comma
            sql = sql[:-1]
            sql += " FROM %s" % http.post['table'].value

            database.cur.execute(sql)

            #generates table rows and columns
            if database.cur.rowcount > 0:
                query = database.cur.fetchall()
                for row in query:
                    show_table += "<tr>"
                    for column in row:
                        show_table += "<td>%s</td>" % column
                    
                    if second == 1:
                        sndkey = row[1]
                        
                    show_table += """<td><form method="POST" action="edit.py" id="edit"><button form="edit" name="edit" type="submit" value="%s">Edit</button><input type="hidden" name="key" value="%s"><input type="hidden" name="table" value="%s"></form></td>""" % (row[0], sndkey, http.post['table'].value)
                    show_table += "</tr>"
                    
            show_table += "</table>"

database.close()


print("""Content-Type: text/html\n\n
<html>
    <head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    %s
    <title>Home</title>
    <style type="text/css">
    body
    {
        color:#444;
        padding:0
        10px
    }

    h1,h2,h3{line-height:1.2}

    table
    {
        border-collapse: collapse;
        width: 100%%;
    }

    th, td
    {
        text-align: left;
        padding: 8px;
    }

    tr:nth-child(even){background-color: #f2f2f2}

    th
    {
        background-color: #4564a8;
        color: white;
    }
    </style>
    </head>
<body>
                <p>
                <form class="logout"action="home.py" method="post">
                    <input type="submit" name="logout" value="logout"> <br>
                </form>
                </p>
                <p>Hello There</p>
                <h3>Search</h3>
                <form class="search" action="home.py" method="post">
                    <select name="table">%s</select>
                    <input type="submit" name="search" value="search"> <br>
                </form>
                %s
                </body></html>""" % (redirect, select, show_table))
