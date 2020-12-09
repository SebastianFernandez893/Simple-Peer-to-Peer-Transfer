import java.io.Serializable;
import java.io.File;
public class Data implements Serializable {
	private String IPNumber;
	private int portNumber;
	private String fileName;
	private String data;
	private File file;
	private static final long serialVersionUID = 1L;
	public Data() {}
	public Data(String ip, int port, String file) {
		this.IPNumber = ip;
		this.portNumber = port;
		this.fileName = file;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File f) {
		this.file = f;
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
	public void setData(String s) {
		this.data = s;
	}
	public String getData() {
		return data;
	}
}
