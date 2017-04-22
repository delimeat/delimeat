package io.delimeat.torrent;

import io.delimeat.config.domain.Config;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.util.UrlHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class TorrentFileWriter_Impl implements TorrentWriter {

	private UrlHandler urlHandler;
	
	@Override
	public void write(String fileName, byte[] bytes, Config config) throws TorrentException {
		final String outputDirectory;
		if( config.getOutputDirectory() != null && config.getOutputDirectory().length() > 0 ){
			outputDirectory = config.getOutputDirectory();
		}else {
			outputDirectory = System.getProperty("user.home");
		}	
		final String outputUrl = "file:" + outputDirectory + "/" + fileName;
		try{
			final URL url = new URL(outputUrl);

			try(OutputStream output = getUrlHandler().openOutput(url)){
				output.write(bytes);   
				output.flush();
			}

		}catch(IOException ex){
			throw new TorrentException("Unnable to write to " + outputUrl ,ex);
		}
	}

	public UrlHandler getUrlHandler() {
		return urlHandler;
	}

	public void setUrlHandler(UrlHandler urlHandler) {
		this.urlHandler = urlHandler;
	}


}
