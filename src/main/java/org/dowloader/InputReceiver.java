package main.java.org.dowloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputReceiver {
	
	public static void main(String[] args) {
		computeArgsFile(openArgsFile(args));
	}
	
	private static File openArgsFile(String[] args) {
		File argsFile = null;
		if (0 < args.length) {
			argsFile = new File(args[0]);
	    }
		return argsFile;
	}
	
	private static void computeArgsFile(File argsFile) {
		BufferedReader br = null;
		String fileUrl;
		Downloader downloader = new Downloader();
		
        try {
			br = new BufferedReader(new FileReader(argsFile));
			while ((fileUrl = br.readLine()) != null) {
				downloader.downloadFile(fileUrl);
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
	}

}
