package main.java.org.dowloader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class ChannelHandler {
	private URLConnection con;
	
	public ReadableByteChannel setUpUrlChannel(URL url) throws IOException {
		this.con = url.openConnection();
		this.con.setConnectTimeout(10000);
		this.con.setReadTimeout(10000);
		return Channels.newChannel(con.getInputStream());
	}
	
	FileOutputStream getFileOutputStream(String fileName) throws FileNotFoundException {
		return new FileOutputStream(fileName);
	}
	
	FileChannel getChannel(FileOutputStream downloadedFile) {
		return downloadedFile.getChannel();
	}
	
	public Long downloadFromChannel(FileChannel fileChannel, ReadableByteChannel urlFile) throws IOException {
		return fileChannel.transferFrom(urlFile, 0, Long.MAX_VALUE);
	}
	
	int getUrlContentLength() {
		return this.con.getContentLength();
	}
}
