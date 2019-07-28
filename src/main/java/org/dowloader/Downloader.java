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
	private ChannelHandler channelHandler;
	
	public Downloader(ChannelHandler cnHandler) {
		this.checkFolderCreation();
		this.logger = LogManager.getLogger(Downloader.class);
		this.channelHandler = cnHandler;
	}
	
	void downloadFile(URL urlObj) {
		FileOutputStream downloadedFile = null;
		ReadableByteChannel urlFile  = null;
		String fileName = null;
		try {
			urlFile = this.channelHandler.setUpUrlChannel(urlObj);
			fileName = Constants.DOWNLOAD_FOLDER + "/" + this.getFileName(urlObj);
			this.logger.info("Creating file with name " + fileName);
			downloadedFile = this.channelHandler.getFileOutputStream(fileName);
		} catch (MalformedURLException e) {
			this.logger.error("Url in a wrong format");
			return;
			
		} catch (ConnectException e) {
			this.logger.error("Could not connect to the host");
			return;
			
		} catch (IOException e) {
			this.logger.error("An internal error happened while reading file from url");
			return;
		}
		
		FileChannel fileChannel = this.channelHandler.getChannel(downloadedFile);
		Long received = 0L;
		try {
			received = this.channelHandler.downloadFromChannel(fileChannel, urlFile);
			downloadedFile.close();
			this.logger.info("Finished downloading file " + fileName);
		} catch (IOException e) {
			this.logger.warn("Couldn't complete the download. Removing it from disk");
			this.deleteFailedDonwloadedFile(fileName);
		}
		
		if (received != this.channelHandler.getUrlContentLength()) {
			this.logger.warn("Download was incomplete. Removing partial file from disk");
			this.deleteFailedDonwloadedFile(fileName);
		}
		
	}
	
	private String getFileName(URL urlObj) {
		String fileName = "";
		String raw = null;
		try {
			this.logger.info("Trying to get the url object name from Http(s) header");
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			raw = conn.getHeaderField("Content-Disposition");
			conn.disconnect();
		} catch (IOException e) {
			raw = null;
		}
		catch (ClassCastException e) {
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
		Path downloadFolder = Paths.get(Constants.DOWNLOAD_FOLDER);
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
