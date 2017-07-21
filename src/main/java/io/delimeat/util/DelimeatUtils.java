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
package io.delimeat.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.show.domain.Episode;

public class DelimeatUtils {
	
	public static String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                sb.append('0');
            }
            sb.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return sb.toString();
	}
	
	public static String urlEscape(String value, String encoding){
		try{
			return URLEncoder.encode(value, encoding);
		}catch(UnsupportedEncodingException ex){
			throw new RuntimeException(ex);
		}
	}
	
	public static byte[] hashBytes(byte[] bytes, String algorithm){
		try{
			MessageDigest md = MessageDigest.getInstance(algorithm);
	        md.update(bytes, 0, bytes.length);
	        return  md.digest();
		}catch(NoSuchAlgorithmException ex){
			throw new RuntimeException(ex);
		}
	}

	
	public static byte[] inputStreamToBytes(InputStream input) throws IOException{
		try(ByteArrayOutputStream buffer = new ByteArrayOutputStream()){
			int nRead;
			byte[] data = new byte[1024];
	
			while ((nRead = input.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}
	
			buffer.flush();
			return buffer.toByteArray();
		}
	}
	
	public static boolean equals(GuideEpisode guideEp, Episode episode){
		return Objects.equals(guideEp.getSeasonNum(), episode.getSeasonNum())
			&& Objects.equals(guideEp.getEpisodeNum(), episode.getEpisodeNum());
	}
}
