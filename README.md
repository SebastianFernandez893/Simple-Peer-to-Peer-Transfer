# Simple-Peer-to-Peer-Transfer

Description 
---
A peer-to-peer file transfer system. User has the ability to register a file to a server, which stores the filename, IP address, and port number of the client. Another client can contact the server to learn the IP address of a client associated to a filename, and immediately download it from the other client. Created for my Computer Networks class. 

How to Run
---
### 1:Run the ServerBroker file.
Running this file will start the server which hosts the IP address, filename, and port information of the clients who upload folds.
### 2: Run Clients 1 and 2
The two clients are functionally the same, however are located in two seperate folders. Each must be instantiated from their own seperate folders. 
### 3: Register a file.
The following will appear on the terminal.
![Screenshotofregistration](https://user-images.githubusercontent.com/48099921/166401528-c445754b-58dd-456e-8f84-d7464a4c6663.PNG)
Type in 1 to upload a file.
Client 1 is able to register "MobyDick.txt" and "Reluctance.txt"
Client 2 is able to register "alice.txt" and "wordSmall.txt"
### 4: Download a file
On the opposite client from which you uploaded the file, search for a file in the directory by typing in 2. 
The following will appear on the terminal.
![searchscreenshot](https://user-images.githubusercontent.com/48099921/166401691-50930b46-776f-42b5-9011-abee0180df2c.PNG)

Search for the file you previously registered. After pressing "enter" the file will appear in the folder containing the respective downloaded client.



