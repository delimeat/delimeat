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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.show.domain.Episode;
import okhttp3.OkHttpClient;

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
	
	public static byte[] hexToBytes(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) 
									+ Character.digit(s.charAt(i + 1), 16));
		}
		return data;
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
	
	public static boolean equals(GuideEpisode guideEp, Episode episode){
		return Objects.equals(guideEp.getSeasonNum(), episode.getSeasonNum())
			&& Objects.equals(guideEp.getEpisodeNum(), episode.getEpisodeNum());
	}
	
	public static OkHttpClient httpClient(){
		return new OkHttpClient().newBuilder()
				.connectTimeout(2, TimeUnit.SECONDS)
				.readTimeout(2, TimeUnit.SECONDS)
				.writeTimeout(2, TimeUnit.SECONDS)
				.build();
	}
	
}
