#!/usr/bin/env python3

import database
import http

data = http.get_request()
http.send_header()
conn = database.get_conn()
cur = conn.cursor()
