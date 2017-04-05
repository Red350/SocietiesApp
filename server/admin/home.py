#!C:\Python\python.exe

import http
import database
import os


err = ""
redirect = ""
sql = ""
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
        redirect = """<meta http-equiv="refresh" content="0; url=/admin/login.py">"""
except no_cookie:
    redirect = """<meta http-equiv="refresh" content="0; url=/admin/login.py">"""


sql = "SHOW tables"
database.cur.execute(sql)

if database.cur.rowcount > 0:
    query = database.cur.fetchall()
    for row in query:
        tableList += row

    for name in tableList:
        select += "<option>%s</option>" % name


if os.environ['REQUEST_METHOD'] == 'POST':
    if 'table' in http.post:
        sql = "SHOW columns FROM %s" % http.post['table'].value
        database.cur.execute(sql)

        if database.cur.rowcount > 0:
            query = database.cur.fetchall()
            for row in query:
                string = [row[0]]
                nameList += string

            show_table += "<table><tr>"

            sql = "SELECT"
            for name in nameList:
                sql += " %s," % name
                show_table += "<th>%s</th>" % name

            sql = sql[:-1]
            sql += " FROM %s" % http.post['table'].value
            show_table += "</tr>"

            database.cur.execute(sql)

            if database.cur.rowcount > 0:
                query = database.cur.fetchall()
                for row in query:
                    show_table += "<tr>"
                    for column in row:
                        show_table += "<td>%s</td>" % column
                    show_table += "</tr>"
            show_table += "</table>"

database.close()


print("""Content-Type: text/html\n
<html>
    <head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
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
    %s
    </head>
<body>
                <p>
                <form class="logut"action="home.py" method="post">
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