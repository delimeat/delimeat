package io.delimeat.core.processor.writer;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedException;
import io.delimeat.util.DelimeatUtils;
import io.delimeat.util.UrlHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class TorrentFileWriter_Impl implements TorrentWriter {

	private UrlHandler urlHandler;
	
	@Override
	public void write(String fileName, byte[] bytes, Config config) throws FeedException {
		final String outputDirectory;
		if( DelimeatUtils.isNotEmpty(config.getOutputDirectory()) ){
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
			throw new FeedException("Unnable to write to " + outputUrl ,ex);
		}
	}

	public UrlHandler getUrlHandler() {
		return urlHandler;
	}

	public void setUrlHandler(UrlHandler urlHandler) {
		this.urlHandler = urlHandler;
	}


}
