#!/usr/bin/env python3

import http
import database
import os
import re

database = database.Database()

err = ""
redirect = ""
sql = ""
show_form = ""
nameList = []
varList = []
varLength = []

print("Content-Type: text/html\r\n\r\n");

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

if os.environ['REQUEST_METHOD'] == 'POST':
    if 'table' in http.post:
        sql = "SHOW columns FROM %s" % http.post['table'].value
        database.cur.execute(sql)
        show_form += """<form action="edit.py" method="POST">"""
        if database.cur.rowcount > 0:
            query = database.cur.fetchall()
            for row in query:
                name = [row[0]]
                nameList += name
                vartype = row[1]
                varLength += re.findall(r'\d+', vartype)

                if row[3] == 'PRI':
                    varList += ['hidden']
                elif vartype.find('timestamp') >= 0:
                    varList += ['hidden']
                    varLength += ['10']
                elif vartype.find('int') >= 0:
                    varList += ['number']
                elif vartype.find('char') >= 0:
                    varList += ['text']
                elif vartype.find('varchar') >= 0:
                    varList += ['text']
                elif vartype == 'date':
                    varList += ['date']
                    varLength += ['10']
                else:
                    varList += ['text']
                    varLength += ['10']
            sql = "SELECT"
            for name in nameList:
                sql += " %s," % name
            sql = sql[:-1]
            sql += " FROM %s WHERE %s = %s" % (http.post['table'].value, nameList[0], http.post['edit'].value)

            if 'second' in http.post:
                sql += " AND WHERE %s = %s" % (nameList[1], http.post['key'].value)

            database.cur.execute(sql)
            query = database.cur.fetchone();

            for i in range(len(varList)):
                show_form += """<input name="%s" type="%s" maxlength="%s" value="%s">""" % (nameList[i], varList[i], varLength[i], query[i])

        
            show_form += """<input type="hidden" name="insert_table" value="%s">""" % http.post['table'].value
            show_form += """<input type="submit" name="makeedit" value="submit">"""
            show_form += """</form>"""

if 'insert_table' in http.post:
    sql = "SHOW COLUMNS FROM %s" % http.post['insert_table'].value
    database.cur.execute(sql)
    query = database.cur.fetchall()
    
    sql = "UPDATE %s SET" % http.post['insert_table'].value 
    for row in query:
        if row[3] != 'PRI':
            if row[1].find('char') >= 0:
                sql += " %s = '%s'," % (row[0], http.post[row[0]].value)
            elif row[1].find('date') >= 0:
                sql += " %s = '%s'," % (row[0], http.post[row[0]].value)
            elif row[1].find('timestamp') >= 0:
                pass
            else:
                sql += " %s = %s," % (row[0], http.post[row[0]].value)
    sql = sql[:-1]
    sql += " WHERE %s=%s" % (query[0][0], http.post[query[0][0]].value)
    if query[1][3] == 'PRI':
        sql += " AND %s=%s" % (query[1][0], http.post[query[1][0]].value)
    print(sql)
    database.cur.execute(sql)   

database.close()

print("""  <html>
    <head>
    %s
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit</title>
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
    </body>
    <a href="home.py">Back to Home</a>
    %s
    </body>
    </html>""" % (redirect, show_form))
