#!C:\Python\python.exe

import http
import database
import os


err = ""
redirect = ""
sql = ""

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
    sql = "DELETE FROM admin_session WHERE session_id = '%s'" % cookie['session_id']
    database.cur.execute(sql)
    print("""Set-Cookie: session_id=%s; Max-Age=-1""" % cookie['session_id'])
    redirect = """<meta http-equiv="refresh" content="0; url=/admin/login.py">"""

database.close()

print("""Content-Type: text/html\n
<html>
    <head>
    <title>Home</title>
    %s
    </head>
<body>
                <form class="logut"action="home.py" method="post">
                    <label for="Submit"> Submit</label><input type="submit" value="Logout"> <br>
                </form>
<p>Hello There</p>%s</body>
</html>""" % (redirect, sql))



