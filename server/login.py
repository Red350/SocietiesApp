#!C:\Python\python.exe

import connect
import http

data = http.get_request()

response = {"session_id": "abc123"}

http.send_response(response)

