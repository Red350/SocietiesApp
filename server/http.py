#!C:\Python\python.exe

import cgi
import cgitb
import json


def get_request():
    cgitb.enable()
    return cgi.FieldStorage()


def send_header():
    print("Content-type: text/html\n\n")


def send_response(response):
    json_data = json.dumps(response)
    print(json_data)


def generate_error(code):
    response = {"return_code": str(code)}

    response['error_msg'] = {
        1: "Invalid session id",
        2: "Invalid username or password"
    }[code]
    return response
