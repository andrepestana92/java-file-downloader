package main.java.org.dowloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class InputParser {
	private Logger logger;
	private Downloader downloader = null;
	private File argFile = null;
	
	
	public InputParser(Downloader down, String[] args) {
		this.downloader = down;
		this.logger = LogManager.getLogger(InputParser.class);
		this.parseArgs(args);
	}
	void parseArgs(String[] args) {
		if (args.length >= 1) {
			this.argFile = new File(args[0]);
	    }
		String folderPath = null;
		if (args.length >= 2) {
			folderPath = args[1];
	    }
		else {
			folderPath = Constants.DOWNLOAD_FOLDER;
		}
		this.logger.info("Will save the downloaded files in the folder " + folderPath);
		this.downloader.setFolderPath(folderPath);
	}
	
	void computeDownloadList() {
		BufferedReader br = null;
		String fileUrl;
		if (this.argFile == null) {
			this.logger.error("No file with urls list provided");
			return;
		}
        try {
			br = new BufferedReader(new FileReader(this.argFile));
			while ((fileUrl = br.readLine()) != null) {
				this.logger.info("Trying to download file " + fileUrl);
				this.downloader.downloadFile(new URL(fileUrl));
	        }
		} catch (FileNotFoundException e) {
			this.logger.error("File with urls list not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}