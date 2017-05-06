/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.torrent;

import io.delimeat.config.domain.Config;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.util.UrlHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TorrentFileWriter_Impl implements TorrentWriter {

	@Autowired
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
