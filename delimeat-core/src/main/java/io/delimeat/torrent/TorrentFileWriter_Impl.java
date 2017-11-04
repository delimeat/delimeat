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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Component;

import io.delimeat.config.entity.Config;
import io.delimeat.torrent.exception.TorrentException;

@Component
public class TorrentFileWriter_Impl implements TorrentWriter {
	
	@Override
	public void write(String fileName, byte[] bytes, Config config) throws TorrentException {

		try{
			final URL url = buildUrl(fileName,config);
			System.out.println(url);
			File file = buildFile(url);
			writeToStream(new FileOutputStream(file), bytes);

		}catch(IOException ex){
			throw new TorrentException("Unnable to write " + fileName ,ex);
		}
	}
	
	public URL buildUrl(String fileName, Config config) throws MalformedURLException{
		final String outputDirectory;
		if( config.getOutputDirectory() != null && config.getOutputDirectory().length() > 0 ){
			outputDirectory = config.getOutputDirectory();
		}else {
			outputDirectory = System.getProperty("user.home");
		}	
		String outputUrl = "file:" + outputDirectory + "/" + fileName;
		return new URL(outputUrl);
	}
	
	public File buildFile(URL url) throws IOException{
		File file = new File(url.getFile());
		if (file.exists() == false) {
			if (file.getParent() != null) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		return file;
	}
	
	public void writeToStream(OutputStream output, byte[] bytes) throws IOException{
		try{
			output.write(bytes);
			output.flush();
		} finally{
			output.close();
		}
	}


}
