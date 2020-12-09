import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Scanner;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientMain {
	static final int DEFAULT_PORT = 5001;
	static final String DEFAULT_IP = "127.0.0.1";
	public static void main(String[] args) throws IOException {
		
		 int bytesRead;
		    int current = 0;
		    FileOutputStream fos = null;
		    BufferedOutputStream bos = null;
		    Socket sock = null;
		//This client's own stuff
		ServerSocket ownSocket = null;
		int ownPort;
		
		//The broker's stuff
		Socket serverSocket;
		int servPort = 5000;
		//The requesting peer's stuff
		SocketAddress clientAddress;
		int clientPort;
		
		boolean listening = true;
	
		ownPort = DEFAULT_PORT;
		String server = DEFAULT_IP; //
		
		InetAddress serverAddress = InetAddress.getByName(server);
		
		Data dataObject = null;
		//Creating a listening socket
		
		//Client creates server
		DownloadThread clientConnection = new DownloadThread(ownPort, server);
		Thread t = new Thread(clientConnection);
		t.setDaemon(true);
		t.start();
		
		serverSocket = new Socket(serverAddress, servPort);
		System.out.println("Welcome to File-Downloader 9000 peer-to-peer file sharing program. You can download files from another"
				+ "\nuser as well as allow another user to download your own file.");
		System.out.println("For whoever is grading this, this is from folder 1");
		Scanner input = new Scanner(System.in);
		boolean loop = true;
		
		int choice;
		System.out.println("What would you like to do? \n");
		while(true) {
			System.out.println("1) Register a file  \n");
			System.out.println("2) Search for a file in our directory \n");
			System.out.println("3) Exit \n");
			choice = input.nextInt();
			
			switch(choice) {
			case 1:
				System.out.println("Please enter the file you would like to register: \n");
				input.nextLine();
				String fileName = input.nextLine();
				
				
				System.out.println(fileName);
				System.out.println("Connecting to server...");
				dataObject =  new Data("127.0.0.1",5001,fileName);
				dataObject.setData("register");
				
				sendInfoToServer(serverSocket,dataObject);
				System.out.println("Your file is now registered to the server");
				break;
			case 2: 
				try {
				//Searching for a file with the broker and download a file from a user
				System.out.println("Please enter the file you would like to download");
				input.nextLine();
				String searchFile = input.nextLine();
				dataObject = new Data();
				dataObject.setFileName(searchFile);
				dataObject.setData("search");
				
				
				sendInfoToServer(serverSocket,dataObject);
				
				dataObject = recieveFeedbackFromServer(serverSocket);
				String reception = dataObject.getData();
				System.out.println(reception + "\n");
				
				if(reception.equals("The file was not in the system")) {
					System.out.println("Sorry, search for another file");
					break;
				}
				
				InetAddress peerAddress = InetAddress.getByName("127.0.0.1");
				int peerPort = dataObject.getPort();
				Socket peerSock = new Socket(peerAddress,peerPort);
				dataObject.setData("Requesting download of file" + searchFile);
				dataObject.setFileName(searchFile);
				dataObject.setIP("127.0.0.1");//Should be this clients IP
				dataObject.setPort(peerPort);
				System.out.println("File found, now requesting file from host \n");
				
				sendInfoToServer(peerSock,dataObject);
				//Now write the data to a file 
				saveFile(peerSock,searchFile);
				System.out.println("File now downloaded.");
			
		        }catch(IOException ex) {
		            System.out.println("Exception occurred:");
		            ex.printStackTrace();
		        } 
				
				break;
			
			case 3:
				System.out.println("Thank you for using File-Downloader 9000. \nNow exiting program...");
				
				System.exit(0);
				break;
			default:
				System.out.println("This is not a valid option. Please select another option");
				break;
			}
		}
	}
	
	public static void sendInfoToServer(Socket clntSock, Data toSend) throws IOException {
		try {
			OutputStream os = clntSock.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(toSend);

		} catch (EOFException e) { // needed to catch when client is done
			System.out.println("in Send EOFException: goodbye client");
			clntSock.close(); // Close the socket. We are done with this client!
		} catch (IOException e) {
			System.out.println("in Send IOException: goodbye client at");
			clntSock.close(); // this requires the throws IOException
		}
	}
	public static Data recieveFeedbackFromServer(Socket clntSock) throws IOException {
		
		// client object
		Data fromServer = null;

		try {
			InputStream is = clntSock.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			fromServer = (Data) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException e) { // needed to catch when client is done
			System.out.println("in receive EOF: goodbye  ");
			clntSock.close(); // Close the socket. We are done with this client!
		} catch (IOException e) {
			System.out.println("in receive IO: goodbye ");
			clntSock.close(); // this requires the throws IOException
		}
		return fromServer;
	}
	private static void saveFile(Socket clientSock,String fileName) throws IOException {
		DataInputStream dis = new DataInputStream(clientSock.getInputStream());
		FileOutputStream fos = new FileOutputStream(fileName + ".txt");
		byte[] buffer = new byte[4096];
		
		int filesize = 1500000; // Send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			System.out.println("read " + totalRead + " bytes.");
			fos.write(buffer, 0, read);
		}
		
		fos.close();
		dis.close();
	}
}
