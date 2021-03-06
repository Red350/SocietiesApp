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

redirect = database.check_cookie(os.environ)

if os.environ['REQUEST_METHOD'] == 'POST':
    #Selects all column names
    if 'table' in http.post:
        sql = "SHOW columns FROM %s" % http.post['table'].value
        database.cur.execute(sql)
        show_form += """<form action="edit.py" method="POST">"""

        if database.cur.rowcount > 0:
            query = database.cur.fetchall()
            #filters out primary keys and timestamps and 
            #hashes and salts
            #Decides the correct input type for each column
            for row in query:
                name = [row[0]]
                if name[0].find('pass') >= 0:
                    continue
                if name[0].find('salt') >= 0:
                    continue
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

            #Retrieves data from columns
            sql = "SELECT"
            for name in nameList:
                sql += " %s," % name
            sql = sql[:-1]
            sql += " FROM %s WHERE %s = %s" % (http.post['table'].value, nameList[0], http.post['edit'].value)

            if 'second' in http.post:
                sql += " AND WHERE %s = %s" % (nameList[1], http.post['key'].value)

            database.cur.execute(sql)
            query = database.cur.fetchone();
            #Creates header for form
            show_form += "<table>"
            show_form += "<tr>"
            for name in nameList:
                show_form += "<th>%s</th>" % name
            show_form += "</tr><tr>"

            #Generates form
            for i in range(len(varList)):
                show_form += """<td><input name="%s" type="%s" maxlength="%s" value="%s"></td>""" % (nameList[i], varList[i], varLength[i], query[i])


        
            show_form += """</td><input type="hidden" name="insert_table" value="%s"></td>""" % http.post['table'].value
            show_form += """<td><input type="submit" name="makeedit" value="submit"></td>"""
            show_form += """</form>"""

            show_form += "</table>"

#Updates table with new values
if 'insert_table' in http.post:
    sql = "SHOW COLUMNS FROM %s" % http.post['insert_table'].value
    database.cur.execute(sql)
    query = database.cur.fetchall()
    
    sql = "UPDATE %s SET" % http.post['insert_table'].value 
    for row in query:
        if row[0].find('pass') >= 0:
            continue
        if row[0].find('salt') >= 0:
            continue
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
    database.cur.execute(sql)   
    if database.cur.rowcount > 0:
        print("Successfully Updated<br>")
    else:
        print("No changes/Failed to update<br>")

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
