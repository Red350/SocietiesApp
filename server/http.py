#!C:\Python\python.exe

import cgi
import cgitb
import json


def get_request():
    cgitb.enable()
    return cgi.FieldStorage()


def send_response(response):
    print("Content-type: text/html\n\n")
    json_data = json.dumps(response)
    print(json_data)
