package main.java.org.dowloader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

class ChannelHandler {
	ReadableByteChannel getUrlChannel(URL url) throws IOException {
		return Channels.newChannel(url.openStream());
	}
	
	FileOutputStream getFileOutputStream(String fileName) throws FileNotFoundException {
		return new FileOutputStream(fileName);
	}
	
	FileChannel getChannel(FileOutputStream downloadedFile) {
		return downloadedFile.getChannel();
	}
}
