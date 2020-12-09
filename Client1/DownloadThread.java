import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class DownloadThread implements Runnable{
	ServerSocket connectionSocket;
	Data toSend = null, toReceive;
	SocketAddress clientAddress;
	int port;
	String ipString;
	InetAddress IP;
	int clientPort;
	boolean listening = true;
	DownloadThread(int port, String ip) throws IOException{
		this.port = port;
		this.ipString = ip;
		this.IP = InetAddress.getByName(ipString);
		connectionSocket = new ServerSocket(port);
	}
	public void run(){
			while(listening) {
				Socket clientSocket;
				try {
					clientSocket = connectionSocket.accept();
					clientAddress = clientSocket.getRemoteSocketAddress();
					clientPort = clientSocket.getPort();
					System.out.println("User at "+ clientAddress + " is now downloading a file");
					BrokerThread clientConnection = new BrokerThread(clientSocket);
					Thread t = new Thread(clientConnection);
					t.setDaemon(true);
					t.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
	}
}
