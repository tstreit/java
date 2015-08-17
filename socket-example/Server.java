import java.net.*;
import java.io.*;

public class Server {

static ServerSocket serverSocket;
static Socket socket;
static DataOutputStream out;

public static void main(String[] args) throws Exception {

System.out.println("Starting Server....");
serverSocket = new ServerSocket(7777);
System.out.println("Server Started....");
socket = serverSocket.accept();
System.out.println("Connection from: " + socket.getInetAddress());
out = new DataOutputStream(socket.getOutputStream());
out.writeUTF("This is a test of Java sockets.");
System.out.println("Data has been sent.");
}

}