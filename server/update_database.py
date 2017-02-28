#!C:\Python\python.exe

import pymysql
import connect

print("Content-type: text/html\n\n")
#conn = pymysql.connect(host='localhost', port=3306, user='root', passwd='', db='societies')
conn = connect.get_conn()
cur = conn.cursor()
cur.execute("SELECT * FROM member")
#print(cur.description)

print()

for row in cur:
    print(row, "<br>")
	

cur.close()
conn.close()
