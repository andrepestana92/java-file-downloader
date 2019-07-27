package main.java.org.dowloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class InputParser {
	Downloader downloader = null;
	
	public InputParser(Downloader down) {
		this.downloader = down;
	}
	File openArgsFile(String[] args) {
		File argsFile = null;
		if (0 < args.length) {
			argsFile = new File(args[0]);
	    }
		return argsFile;
	}
	
	void computeArgsFile(File argsFile) {
		
		BufferedReader br = null;
		String fileUrl;
		
        try {
			br = new BufferedReader(new FileReader(argsFile));
			while ((fileUrl = br.readLine()) != null) {
				this.downloader.downloadFile(fileUrl);
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}