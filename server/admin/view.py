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
        
    if 'member_id' in http.post:
        sql =  "SELECT member_id, student_num, name, email, dob, mobile, emergency_ph, date_joined, full_part_time, verified FROM member WHERE member_id = %s" % http.post['member_id'].value
        
        database.cur.execute(sql)
        if database.cur.rowcount > 0:
            query = database.cur.fetchone()
            
            show_table += "<table>"
            show_table += "<tr><th>Column Name</th><th>Column Value</th></tr>"
            show_table += "<tr><th>member_id</th><td>%s</td></tr>" % query[0]
            show_table += "<tr><th>student_num</th><td>%s</td></tr>" % query[1]
            show_table += "<tr><th>name</th><td>%s</td></tr>" % query[2]
            show_table += "<tr><th>email</th><td>%s</td></tr>" % query[3]
            show_table += "<tr><th>dob</th><td>%s</td></tr>" % query[4]
            show_table += "<tr><th>mobile</th><td>%s<td></tr>" % query[5]
            show_table += "<tr><th>emergency_ph</th><td>%s</td></tr>" % query[6]
            show_table += "<tr><th>date_joined</th><td>%s</td></tr>" % query[7]
            show_table += "<tr><th>full_part_time</th><td>%s</td></tr>" % query[8]
            show_table += "<tr><th>verified</th><td>%s</td></tr>" % query[9]
            show_table += "<tr><th>Society Member</th><td>"
            sql = "SELECT society.name FROM society INNER JOIN member_society USING (society_id) INNER JOIN member USING (member_id) WHERE member_id = %s" % http.post['member_id'].value
            database.cur.execute(sql)
            if database.cur.rowcount > 0:
                societies = database.cur.fetchall()
                for soc in societies:
                    show_table += "%s<br>" % soc

            show_table += "</td></tr>"
            show_table += "<th>Society Committee Member</th><td>"
            sql = "SELECT society.name FROM society INNER JOIN committee_society USING (society_id) INNER JOIN member USING (member_id) WHERE member_id = %s" % http.post['member_id'].value
            database.cur.execute(sql)
            if database.cur.rowcount > 0:
                societies = database.cur.fetchall()
                for soc in societies:
                    show_table += "%s<br>" % soc
            show_table += "</td></tr>"

            show_table += "<th>Society Chairman</th><td>"
            sql = "SELECT society.name FROM society INNER JOIN member ON member_id = chair_id WHERE member_id = %s" % http.post['member_id'].value
            database.cur.execute(sql)
            if database.cur.rowcount > 0:
                societies = database.cur.fetchall()
                for soc in societies:
                    show_table += "%s<br>" % soc
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
        width: 50%%;
        margin: auto;
    }
    </style>
    </head>
<body>
                <p>
                <form class="logout"action="home.py" method="post">
                    <input type="submit" name="logout" value="logout"> <br>
                </form>
                </p>
                <div class="table">
                %s
                </div>
                </body></html>""" % (redirect, show_table))
