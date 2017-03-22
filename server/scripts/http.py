import cgi
import cgitb
import json


def get_request():
    cgitb.enable()
    return cgi.FieldStorage()


def print_post():
    print(post)


def send_json_header():
    print("Content-type: application/json\n\n")


def send_response(response):
    json_data = json.dumps(response, sort_keys=True, indent=4)
    print(json_data)


def generate_returncode(code):
    response = {"return_code": str(code)}

    response['return_msg'] = {
        0: "Success",
        1: "Invalid session id",
        2: "Invalid username or password",
        3: "Email already in use",
        4: "Email not verified",
        5: "Incorrectly formatted request",
        6: "Database error"
    }[code]
    return response
 

def check_keys(keys):
    for key in keys:
        if key not in post:
            return False
    return True


post = get_request()

