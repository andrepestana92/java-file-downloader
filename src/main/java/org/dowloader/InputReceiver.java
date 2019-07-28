package main.java.org.dowloader;

import org.apache.log4j.BasicConfigurator;

public class InputReceiver {
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		InputParser parser = new InputParser(new Downloader(new ChannelHandler()), args);
		parser.computeArgsFile();
	}
}
