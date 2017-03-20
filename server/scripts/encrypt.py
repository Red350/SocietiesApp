#!/usr/bin/env python

import hashlib
import binascii


def generate_hash(password, salt):
    pass_hash = hashlib.pbkdf2_hmac("sha256", bytes(password, 'utf-8'), bytes(salt, 'utf-8'), 100000)
    pass_hash = binascii.hexlify(pass_hash)
    pass_hash = pass_hash.decode("utf-8")
    return pass_hash

