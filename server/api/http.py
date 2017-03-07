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


def generate_returncode(code):
    response = {"return_code": str(code)}

    response['return_msg'] = {
        0: "Success",
        1: "Invalid session id",
        2: "Invalid username or password",
        3: "Email already in use",
        4: "Email not verified"
    }[code]
    return response
 

post = get_request()
send_header()
