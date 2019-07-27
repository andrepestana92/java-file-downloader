package main.java.org.dowloader;

import java.io.File;
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
		String fileName = null;
		try {
			url = new URL(urlString);
			readableByteChannel = Channels.newChannel(url.openStream());
			fileName = this.getFileName(url);
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
			fileOutputStream.close();
		} catch (IOException e) {
			this.deleteFailedDonwloadedFile(fileName);
		}
	}
	
	private String getFileName(URL url) {
		String fileName = "";
		String raw = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			raw = conn.getHeaderField("Content-Disposition");
		} catch (IOException e) {
			raw = null;
		}
		catch (ClassCastException e) {
			raw = null;
		}
		if(raw != null && raw.indexOf("=") != -1) {
		    fileName = raw.split("=")[1];
		    fileName = raw.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
		} else {
		    fileName = Paths.get(url.getPath()).getFileName().toString();
		}
		return fileName;
	}
	
	private void deleteFailedDonwloadedFile(String fileName) {
		File file = new File(fileName);
		file.delete();
		
	}
}
