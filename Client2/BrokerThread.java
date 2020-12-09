import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class BrokerThread implements Runnable{
	Socket connectionSocket;
	Data toSend = null, toReceive;
	SocketAddress clientAddress;
	int port;
	String name;
	FileInputStream fis;
	BufferedInputStream bis;
	OutputStream os = null;
	public BrokerThread(Socket sock){
		this.connectionSocket = sock;
	}
	public void run() {
		try {
			toReceive = recieveDataFromClient(connectionSocket);
			System.out.println("request received, writing file to packet");
			String fileName = toReceive.getFileName();
			sendFile(fileName);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	} 

	public static void sendBackToClient(Socket sock, Data fromClient) throws IOException{
		
		try {
			OutputStream os = sock.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(fromClient);
		}catch (EOFException e) { // needed to catch when client is done
			System.out.println("in Send EOFException: goodbye client at " + sock.getRemoteSocketAddress() + " with port# " + sock.getPort());
			sock.close(); // Close the socket. We are done with this client!
		}
		catch (IOException e) {
			
			System.out.println("in Send IOException: goodbye client at " + sock.getRemoteSocketAddress() + " with port# " + sock.getPort());
			sock.close(); //
		}
	}
	public static Data recieveDataFromClient(Socket clntSock) throws IOException{
		SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
		int port = clntSock.getPort();
		
		//client object
		Data fromClient = null;
		
		try {
			InputStream is = clntSock.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			fromClient = (Data) ois.readObject();
		}  catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EOFException e) { // needed to catch when client is done
			System.out.println("in receive EOF: goodbye client at " + clientAddress + " with port# " + port);
			clntSock.close(); // Close the socket. We are done with this client!
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("in receive IO: goodbye client at " + clientAddress + " with port# " + port);
			clntSock.close(); //
		}
		return fromClient;
		
	}
	public void sendFile(String file) throws IOException {
		DataOutputStream dos = new DataOutputStream(connectionSocket.getOutputStream());
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[4096];
		
		while (fis.read(buffer) > 0) {
			dos.write(buffer);
		}
		
		fis.close();
		dos.close();	
	}
}
