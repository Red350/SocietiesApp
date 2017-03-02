#!C:\Python\python.exe

from qrcode import *

print("Content-type: text/html\n\n")

qr = QRCode(version=20, error_correction=ERROR_CORRECT_L)
qr.add_data("Random data")
qr.make()

im = qr.make_image()

im.save("qr.png")
