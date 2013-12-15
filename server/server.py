import socket
import os
import os.path

HOST = ''
PORT = 9995
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.bind((HOST,PORT))
s.listen(5)
while 1:
    conn, addr = s.accept()
    if os.fork() == 0:
        break

print("Accepted connection")
print(addr[0])
while 1:
    data = conn.recv(1024)
    print(data)
    if data == b'check\n':
        if os.path.isfile(addr[0]):
            conn.sendall(bytes("no\n","UTF-8"))
        else:
            conn.sendall(bytes("yes\n","UTF-8"))
    elif data == b'mark\n':
        open(addr[0], 'a').close()
        break
    else:
        break

conn.close()
