package main.java.org.dowloader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class StreamHandler {
	
	public ReadableByteChannel setUpUrlChannel(URL url) throws IOException {
		URLConnection con = url.openConnection();
		con.setConnectTimeout(Constants.TIMEOUT_LIMIT);
		con.setReadTimeout(Constants.TIMEOUT_LIMIT);
		return Channels.newChannel(con.getInputStream());
	}
	
	FileOutputStream getFileOutputStream(String fileName) throws FileNotFoundException {
		return new FileOutputStream(fileName);
	}
	
	FileChannel getChannel(FileOutputStream downloadedFile) {
		return downloadedFile.getChannel();
	}
	
	public Long downloadFromChannel(FileChannel fileChannel, ReadableByteChannel urlFile) throws IOException {
		/* The transferFrom method creates a connection between two channels and saves the content
		 * of the source directly in the file, without the need of buffering any significant 
		  amount of space in memory. */
		return fileChannel.transferFrom(urlFile, 0, Long.MAX_VALUE);
	}
	
	public int getUrlContentLength(URL url) {
		try {
			return url.openConnection().getContentLength();
		} catch (IOException e) {
			return 0;
		}
	}
	
	public Boolean closeFileStream(FileOutputStream downloadedFile){
		try {
			downloadedFile.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
