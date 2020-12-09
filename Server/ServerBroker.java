import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import java.util.List;

public class ServerBroker {
	static final int DEFAULT_PORT = 5000;
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		boolean listening = true;
		int servPort;
		
		servPort = DEFAULT_PORT;
		
		try {
			serverSocket = new ServerSocket(servPort);
		}catch(IOException e) {
			System.err.println("Could not listen on port.");
			System.exit(-1);
		}
		SocketAddress clientAddress;
		int clientPort;
		List<ClientTriple> listOfClients = new CopyOnWriteArrayList<ClientTriple>();
		System.out.print("Server is up and running, waiting for clients");
		
		
		
		while(listening) {
			Socket clientSocket = serverSocket.accept();
			
			clientAddress = clientSocket.getRemoteSocketAddress();
			clientPort = clientSocket.getPort();
			System.out.println("Handling client at " + clientAddress + " with port# " + clientPort);
			System.out.println("Now loop back and wait for the next connection " );
			ServerThread clientConnection = new ServerThread(clientSocket,listOfClients);
			Thread t = new Thread(clientConnection);
			t.setDaemon(true);
			t.start();
		}
		serverSocket.close();
				

	}
	
	 public static void printListofClients(List<Socket> list) {
	    	for(int i = 0; i < list.size(); i++){
	    	    System.out.println(list.get(i));
	    	}
	    	
	        
	    }

}
