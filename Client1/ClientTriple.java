
public class ClientTriple {
	private String IPNumber;
	private int portNumber;
	private String fileName;
	
	public ClientTriple(String ip, int port, String file) {
		this.IPNumber = ip;
		this.portNumber = port;
		this.fileName = file;
	}
	public String getFileName() {
		return fileName;
	}
	public String getIP() {
		return IPNumber;
	}
	public int getPort() {
		return portNumber;
	}
	public void setFileName(String s) {
		this.fileName = s;
	}
	public void setIP(String i) {
		this.IPNumber = i;
	}
	public void setPort(int i) {
		this.portNumber =i;
	}
}
