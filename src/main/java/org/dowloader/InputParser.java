package main.java.org.dowloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;


public class InputParser {
	Downloader downloader = null;
	File argFile;
	
	public InputParser(Downloader down, String[] args) {
		this.downloader = down;
		this.argFile = this.openArgsFile(args);
	}
	File openArgsFile(String[] args) {
		File argsFile = null;
		if (0 < args.length) {
			argsFile = new File(args[0]);
	    }
		return argsFile;
	}
	
	void computeArgsFile() {
		BufferedReader br = null;
		String fileUrl;
		if (this.argFile == null) return;
        try {
			br = new BufferedReader(new FileReader(this.argFile));
			while ((fileUrl = br.readLine()) != null) {
				this.downloader.downloadFile(new URL(fileUrl));
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}