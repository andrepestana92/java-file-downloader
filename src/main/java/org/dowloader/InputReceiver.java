package main.java.org.dowloader;

public class InputReceiver {
	
	public static void main(String[] args) {
		InputParser parser = new InputParser(new Downloader());
		parser.computeArgsFile(parser.openArgsFile(args));
	}
}
