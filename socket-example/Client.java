import java.net.*;
import java.io.*;

public class Client {

static Socket socket;
static DataInputStream in;

public static void main(String[] args) throws Exception {

System.out.println("Connecting....");
socket = new Socket("mumble.minecrunch.net",6669);
System.out.println("Connection successful.");
in = new DataInputStream(socket.getInputStream());
System.out.println("Receiving information.");
String test = in.readUTF();
System.out.println("Message from server: " + test);

}
}