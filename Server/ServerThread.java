import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ServerThread implements Runnable{
	Socket clientSocket;
	Data toSend, toReceive = null;
	SocketAddress clientAddress;
	int port;
	List<Socket> list; 
	List<ClientTriple> listOfTriples;
	
	public ServerThread(Socket connectionSocket, List<ClientTriple> list) {
		this.clientSocket = connectionSocket;
		this.clientAddress = clientSocket.getRemoteSocketAddress();
		this.port = clientSocket.getPort();
		this.listOfTriples = list;
		
	}
	
	public void run() {
		
		System.out.println("At thread level");
		try {
			while(true) {
				
				toReceive = recieveDataFromClient(clientSocket);
				//When the client wants to register a file
				
				
				if(toReceive.getData().equals("register")) {
					
					String filename = toReceive.getFileName();
					String IP = toReceive.getIP();
					int portnum = toReceive.getPort();
					System.out.println("Registering file " +filename + " at address " + IP + " and port " + portnum + "\n");
					ClientTriple client = new ClientTriple(IP,portnum, filename);
					listOfTriples.add(client);
					
				}
				//Client wants to search for file
				else if(toReceive.getData().equals("search")) {
					
					String filename = toReceive.getFileName();
					
					System.out.println("Searching for " + filename);
					toSend = new Data();
					//Searching through list to find file and associated port and IP Address
					for(int i =0; i<listOfTriples.size();i++) {
						
						
						if(listOfTriples.get(i).getFileName().equals(filename)) {
							System.out.println("File found");
							String returnIP = listOfTriples.get(i).getIP();
							int returnPort = listOfTriples.get(i).getPort();
							toSend.setFileName(filename);
							toSend.setIP(returnIP);
							toSend.setPort(returnPort);
							toSend.setData("The file was in the system");
						}
					}
					//Will only occur if the file was not found.
					if(toSend.getData()==null) {
						System.out.println("File not found");
						toSend.setData("The file was not in the system");
					}
					sendBackToClient(clientSocket, toSend);
					
				}
				
			}
		}catch (EOFException e) { // needed to catch when client is done
			System.out.println("goodbye to the client with IP: " + clientAddress
					+ " and port# " + port);
			try {
				clientSocket.close(); // Close the socket. We are done with this client!
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
	
}
