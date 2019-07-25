package main.java.org.dowloader;


public class InputReceiver {
	final static String URL_FILE = "https://drive.google.com/uc?export=download&id=1-e9j8D2LBeN8tkk26EODoV_iJbMPfFK-";
	final static String FILE_NAME = "Ashe.jpg";
	
	
	public static void main(String[] args) {
		Downloader downloader = new Downloader();
		downloader.downloadFile(URL_FILE);
	}

}
