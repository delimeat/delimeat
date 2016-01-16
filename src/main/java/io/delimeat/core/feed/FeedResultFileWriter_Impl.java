package io.delimeat.core.feed;

import io.delimeat.core.config.Config;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.util.UrlHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class FeedResultFileWriter_Impl implements FeedResultWriter {

	private UrlHandler urlHandler;
	
	@Override
	public void write(Torrent torrent, Config config) throws FeedException {
		final String outputDirectory = config.getOutputDirectory();
		final String fileName = torrent.getInfo().getName() + ".torrent";
		final String filePath = outputDirectory + System.getProperty("file.separator") + fileName;
		try{
			OutputStream output = getUrlHandler().openOutput(new URL("file:"+filePath));
			output.write(torrent.getBytes());
			output.flush();
			output.close();
		}catch(IOException ex){
			throw new FeedException(ex);
		}
	}

	public UrlHandler getUrlHandler() {
		return urlHandler;
	}

	public void setUrlHandler(UrlHandler urlHandler) {
		this.urlHandler = urlHandler;
	}


}
