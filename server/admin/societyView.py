#!/usr/bin/env python3

import http
import database
import os

database = database.Database()

err = ""
redirect = ""
sql = ""
show_table = ""


print("Content-Type: text/html\n")

redirect = database.check_cookie(os.environ)

print("\n\n")
    
if os.environ['REQUEST_METHOD'] == 'POST':
    if 'logout' in http.post:
        print("Set-Cookie: session_id=0; Max-Age=0\n")
        redirect = """<meta http-equiv="refresh" content="0; url=login.py">"""
        
    if 'society_id' in http.post:
        sql =  "SELECT society_id, name, email, description, chair_id FROM society WHERE society_id = %s" % http.post['society_id'].value
        
        database.cur.execute(sql)
        if database.cur.rowcount > 0:
            query = database.cur.fetchone()
            #Displays society details            
            show_table += "<table>"
            show_table += "<tr><th>Column Name</th><th>Column Value</th></tr>"
            show_table += "<tr><th>society_id</th><td>%s</td></tr>" % query[0]
            show_table += "<tr><th>name</th><td>%s</td></tr>" % query[1]
            show_table += "<tr><th>email</th><td>%s</td></tr>" % query[2]
            show_table += "<tr><th>description</th><td>%s</td></tr>" % query[3]

            show_table += "<tr><th>Society Chair</th><td>"

            #Displays society chair
            sql = "SELECT member.name, member.email FROM member INNER JOIN society ON chair_id = member_id WHERE society_id = %s" % http.post['society_id'].value
            database.cur.execute(sql)
            if database.cur.rowcount > 0:
                societies = database.cur.fetchall()
                for soc in societies:
                    show_table += "%s %s<br>" % (soc[0], soc[1])

            show_table += "</td></tr>"

            #Displays society committee members
            show_table += "<th>Society Committee Members</th><td>"
            sql = "SELECT member.name, member.email FROM member INNER JOIN committee_society USING (member_id) INNER JOIN society USING (society_id) WHERE society.society_id = %s" % http.post['society_id'].value
            database.cur.execute(sql)
            if database.cur.rowcount > 0:
                societies = database.cur.fetchall()
                for soc in societies:
                    show_table += "%s %s<br>" % (soc[0], soc[1]) 
            show_table += "</td></tr>"
            
            #Displays society members
            show_table += "<th>Society Members</th><td>"
            sql = "SELECT member.name, member.email FROM member INNER JOIN member_society USING (member_id) INNER JOIN society USING (society_id) WHERE society.society_id = %s" % http.post['society_id'].value
            database.cur.execute(sql)
            if database.cur.rowcount > 0:
                societies = database.cur.fetchall()
                for soc in societies:
                    show_table += "%s %s<br>" % (soc[0], soc[1])
            show_table += "</td></tr>"

            show_table += "</table>"
        

print("""

<html>
    <head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    %s
    <title>View</title>
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

    tr:nth-child(even) th {background-color: #4564a8}

    th
    {
        background-color: #5274bf;
        color: white;
    }
    
    div.table
    {
        width: 60%%;
        margin: auto;
    }
    </style>
    </head>
<body>
                <p>
                <form class="logout" action="home.py" method="post">
                    <input type="submit" name="logout" value="logout"> <br>
                </form>
                </p>
                <div class="table">
                %s
                </div>
                </body></html>""" % (redirect, show_table))

