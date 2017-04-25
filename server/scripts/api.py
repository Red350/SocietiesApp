import cgi
import cgitb
import json
import logging

class Api:
    request = cgi.FieldStorage()
    cgitb.enable()
    response = {}

    # Set up logging and log the request
    logging.basicConfig(filename='/var/www/html/log/socapi.log',
                        level=logging.DEBUG,
                        format='%(asctime)s %(message)s')
    logging.info("Received: " + str(request))

    def __init__(self, header):
        if header == "json":
            print("Content-type: application/json\n\n")
    
    def set_returncode(self, code):
        self.response["return_code"] = str(code)
    
        self.response['return_msg'] = {
            0: "Success",
            1: "Invalid session id",
            2: "Invalid username or password",
            3: "Email already in use",
            4: "Email not verified",
            5: "Incorrectly formatted request",
            6: "Database error",
            7: "Invalid permissions for this action",
            8: "Invalid join token",
            9: "Already a member"
        }[code]

    def update_response(self, key, value):
        self.response[key] = value

    def send_response(self):
        json_data = json.dumps(self.response, sort_keys=True, indent=4)
        logging.info("Sent: " + json_data)
        print(json_data)
     
    def check_keys(self, keys):
        for key in keys:
            if key not in self.request:
                return False
        return True
    
