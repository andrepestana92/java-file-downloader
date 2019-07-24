package main.java.org.dowloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;


public class Downloader {
	final static String URL_FILE = "https://drive.google.com/uc?export=download&id=1-e9j8D2LBeN8tkk26EODoV_iJbMPfFK-";
	final static String FILE_NAME = "Ashe.jpg";
	
	
	public static void main(String[] args) {
		URL url;
		FileOutputStream fileOutputStream = null;
		ReadableByteChannel readableByteChannel  = null;
		try {
			url = new URL(URL_FILE);
			readableByteChannel = Channels.newChannel(url.openStream());
			fileOutputStream = new FileOutputStream(FILE_NAME);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		FileChannel fileChannel = fileOutputStream.getChannel();
		try {
			fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
