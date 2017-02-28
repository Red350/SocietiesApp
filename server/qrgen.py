#!C:\Python\python.exe

from qrcode import *

print("Content-type: text/html\n\n")

qr = QRCode(version=20, error_correction=ERROR_CORRECT_L)
qr.add_data("http://blog.matael.org/")
qr.make()   # Generate the QRCode itself

# im contains a PIL.Image.Image object
im = qr.make_image()

# To save it
im.save("filename.png")

print("I'm a py file")

