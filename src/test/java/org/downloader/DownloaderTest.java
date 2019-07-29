package test.java.org.downloader;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import main.java.org.dowloader.StreamHandler;
import main.java.org.dowloader.Downloader;

public class DownloaderTest {
	
	@Before
	public void setUp() {
		BasicConfigurator.configure();
	}
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	String urlTest = "https://github.com/andrepestana92/java-file-downloader/blob/master/README.md";
	String fileNameTest = "README.md";
	
	@Test
	public void testDownloadSuccess() throws IOException {
		URL url = new URL(this.urlTest);
		Downloader downloader = new Downloader(new StreamHandler());
		Boolean result = downloader.downloadFile(url);
		assertTrue(result);
		File file = new File("downloads/README.md");
		assertTrue(file.exists());
		file.delete();
	}
	
	@Test
	public void testDownloadException() throws IOException {
		StreamHandler mockStreamHandler = mock(StreamHandler.class);
		doThrow(IOException.class).when(mockStreamHandler).downloadFromChannel(any(), any());
		URL url = new URL(this.urlTest);
		Downloader downloader = new Downloader(mockStreamHandler);
		Boolean result = downloader.downloadFile(url);
		assertFalse(result);
		File file = new File("downloads/README.md");
		assertFalse(file.exists());
		
	}
	
	@Test
	public void testDownloadError() throws IOException {
		StreamHandler mockStreamHandler = mock(StreamHandler.class);
		doReturn(95L).when(mockStreamHandler).downloadFromChannel(any(), any());
		doReturn(100).when(mockStreamHandler).getUrlContentLength(any());
		URL url = new URL(this.urlTest);
		Downloader downloader = new Downloader(mockStreamHandler);
		Boolean result = downloader.downloadFile(url);
		assertFalse(result);
		File file = new File("downloads/README.md");
		assertFalse(file.exists());
	}
}
