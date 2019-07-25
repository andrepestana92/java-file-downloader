package main.java.org.dowloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;

public class Downloader {
	void downloadFile(String urlString) {
		URL url;
		FileOutputStream fileOutputStream = null;
		ReadableByteChannel readableByteChannel  = null;
		try {
			url = new URL(urlString);
			readableByteChannel = Channels.newChannel(url.openStream());
			String fileName = this.get_name(url);
			fileOutputStream = new FileOutputStream(fileName);
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
	
	private String get_name(URL url) {
		String fileName = "";
		String raw = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			raw = conn.getHeaderField("Content-Disposition");
		} catch (IOException e) {
			raw = null;
			e.printStackTrace();
		}
		if(raw != null && raw.indexOf("=") != -1) {
		    fileName = raw.split("=")[1];
		    fileName = raw.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
		} else {
		    fileName = Paths.get(url.getPath()).getFileName().toString();
		}
		return fileName;
	}
}
