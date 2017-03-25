#!C:\Python\python.exe

import http
import database
import os


err = ""
redirect = ""
sql = ""
show_result = ""
member_id = ""
name = ""
student_num = ""

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

if os.environ['REQUEST_METHOD'] == 'POST':
    if 'logout' in http.post:
        sql = "DELETE FROM admin_session WHERE session_id = '%s'" % cookie['session_id']
        database.cur.execute(sql)
        print("""Set-Cookie: session_id=%s; Max-Age=-1""" % cookie['session_id'])
        redirect = """<meta http-equiv="refresh" content="0; url=/admin/login.py">"""

    if 'search' in http.post:
        sql = "SELECT member_id, student_num, name, email, dob, mobile, emergency_ph, date_joined, full_part_time, verified FROM member"

        if 'member_id' in http.post:
            member_id = http.post['member_id'].value
        if 'name' in http.post:
            name = http.post['name'].value
        if 'student_num' in http.post:
            student_num = http.post['student_num'].value



        if 'member_id' in http.post:
            sql += " WHERE member_id = %s" % http.post['member_id'].value
            if'name' in http.post:
                sql += " AND name LIKE '%%%s%%'" % http.post['name'].value
                if 'student_num' in http.post:
                    sql += " AND student_num LIKE '%s%%'" % http.post['student_num'].value
        elif 'name' in http.post:
            sql += " WHERE name LIKE '%%%s%%'" % http.post['name'].value
            if'member_id' in http.post:
                sql += " AND member_id = %s" % http.post['member_id'].value
                if 'student_num' in http.post:
                    sql += " AND student_num LIKE '%s%%'" % http.post['student_num'].value
        elif 'student_num' in http.post:
            sql += " WHERE student_num LIKE '%s%%'" % http.post['student_num'].value
            if'member_id' in http.post:
                sql += " AND member_id = %s" % http.post['member_id'].value
                if 'student_num' in http.post:
                    sql += " AND name LIKE '%%%s%%'" % http.post['name'].value
        database.cur.execute(sql)
        if database.cur.rowcount > 0:
            show_result += "<table>"
            show_result += "<tr><th>member_id</th><th>student_num</th><th>name</th><th>email</th><th>dob</th><th>mobile</th><th>emergency_ph</th><th>date_joined</th><th>full_part_time</th></tr>"
            query = database.cur.fetchall()
            for row in query:
                show_row = "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>" % (row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], row[8])
                show_result += show_row
            show_result += "</table>"

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
                    <label for="member_id">ID number</label><input type="number" name="member_id" value="%s">
                    <label for="name">Name</label><input type="text" name="name" value="%s">
                    <label for="student_num">Student Number</label><input type="text" name="student_num" value="%s">
                    <input type="submit" name="search" value="search"> <br>
                </form>
                %s
                </body>
                </html>""" % (redirect, member_id, name, student_num, show_result))



