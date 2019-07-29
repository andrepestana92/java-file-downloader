package main.java.org.dowloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Downloader {
	private Logger logger;
	private StreamHandler streamHandler;
	private String folderPath = Constants.DOWNLOAD_FOLDER;
	
	public Downloader(StreamHandler cnHandler) {
		this.logger = LogManager.getLogger(Downloader.class);
		this.streamHandler = cnHandler;
	}
	
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	
	public Boolean downloadFile(URL urlObj) {
		FileOutputStream downloadedFile = null;
		ReadableByteChannel urlFile  = null;
		String fileName = null;
		this.checkFolderCreation();
		try {
			urlFile = this.streamHandler.setUpUrlChannel(urlObj);
			fileName = this.folderPath + "/" + this.getFileName(urlObj);
			this.logger.info("Creating file with name " + fileName);
			downloadedFile = this.streamHandler.getFileOutputStream(fileName);
		} catch (MalformedURLException e) {
			this.logger.error("Url in a wrong format");
			return false;
			
		} catch (ConnectException e) {
			this.logger.error("Could not connect to the host");
			return false;
			
		} catch (IOException e) {
			this.logger.error("An internal error happened while reading file from url");
			return false;
		}
		
		FileChannel fileChannel = this.streamHandler.getChannel(downloadedFile);
		Long received = 0L;
		try {
			received = this.streamHandler.downloadFromChannel(fileChannel, urlFile);
			this.streamHandler.closeFileStream(downloadedFile);
			this.logger.info("Finished downloading file " + fileName);
		} catch (IOException e) {
			this.logger.warn("Couldn't complete the download. Removing it from disk");
			this.deleteFailedDonwloadedFile(fileName);
			return false;
		}
		
		int originalSize = this.streamHandler.getUrlContentLength(urlObj);
		if (originalSize > 0 && received != originalSize) {
			this.logger.warn("Download was incomplete. Removing partial file from disk");
			this.deleteFailedDonwloadedFile(fileName);
			return false;
		}
		return true;
	}
	
	private String getFileName(URL urlObj) {
		String fileName = "";
		String raw = null;
		/* Some http(s) urls have the file name on its header via the Content-Disposition field.
		 * This is used when the filename is not  present in the url, like Google Drive files.*/
		try {
			this.logger.info("Trying to get the url object name from Http(s) header");
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			raw = conn.getHeaderField("Content-Disposition");
			conn.disconnect();
		} catch (IOException e) {
			raw = null;
		/* If the url belongs to a ftp server, then the code will return this Exception */
		} catch (ClassCastException e) {
			this.logger.info("Couldn't get the name from Header. Will get it from url path instead");
			raw = null;
		}
		if(raw != null && raw.indexOf("=") != -1) {
		    fileName = raw.split("=")[1];
		    fileName = raw.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
		    this.logger.info("Got the url object name from Header");
		} else {
		    fileName = Paths.get(urlObj.getPath()).getFileName().toString();
		    this.logger.info("Got the url object name from the path");
		}
		return fileName;
	}
	
	private void deleteFailedDonwloadedFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}
	
	private void checkFolderCreation() {
		Path downloadFolder = Paths.get(this.folderPath);
		if (!Files.exists(downloadFolder)) {
            try {
            	this.logger.info("Download folder still doesn't exist. Creating a new one");
				Files.createDirectory(downloadFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
