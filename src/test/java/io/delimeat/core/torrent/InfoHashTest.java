package io.delimeat.core.torrent;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.delimeat.util.bencode.BDictionary;
import io.delimeat.util.bencode.BList;
import io.delimeat.util.bencode.BencodeException;

public class InfoHashTest {
  
  	@Test
  	public void byteArrayConstructorTest(){
     InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
     Assert.assertEquals("601492e054f9540eb0129c35deb385baa2faf0fe",infoHash.getHex());
   }
  
  	@Test
  	public void dictionaryConstructorTest() throws IOException, BencodeException{
		BDictionary infoDict = new BDictionary();
		infoDict.put("name", "NAME");
		infoDict.put("length", 987654321L);
		BList filesList = new BList();
		BDictionary fileDict1 = new BDictionary();
		BList pathList1 = new BList();
		pathList1.add("1_part_1");
		pathList1.add("1_file_name");
		fileDict1.put("length", 1234);
		fileDict1.put("path", pathList1);
		filesList.add(fileDict1);
		BDictionary fileDict2 = new BDictionary();
		BList pathList2 = new BList();
		pathList2.add("2_part_1");
		pathList2.add("2_file_name");
		fileDict2.put("length", 56789);
		fileDict2.put("path", pathList2);
		filesList.add(fileDict2);
		infoDict.put("files", filesList);
     
     	InfoHash infoHash = new InfoHash(infoDict);
     
     	Assert.assertEquals("ab835ef1b726e2aa4d1c6df6b91278d651b228a7", infoHash.getHex());
   }
  
  	@Test
  	public void getBytesTest() throws UnsupportedEncodingException{
     InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());

     String encodedInfoHash = URLEncoder.encode(new String(infoHash.getBytes()), "ISO-8859-1");

     Assert.assertEquals("%60%14%3F%3FT%3FT%0E%3F%12%3F5%3F%3F%3F%3F%3F%3F%3F",encodedInfoHash);
   }
  
  	@Test
  	public void nullEqualsTest(){
   	InfoHash infoHash = new InfoHash("INFO_HASH".getBytes()); 
     	Assert.assertFalse(infoHash.equals(null));
   }
  
  	@Test
  	public void notInfoHashEqualsTest(){
   	InfoHash infoHash = new InfoHash("INFO_HASH".getBytes()); 
     	Assert.assertFalse(infoHash.equals(new Object()));
   }
  
  	@Test
  	public void selfEqualsTest(){
   	InfoHash infoHash = new InfoHash("INFO_HASH".getBytes()); 
     	Assert.assertTrue(infoHash.equals(infoHash));
   } 
  
  	@Test
  	public void matchingEqualsTest(){
   	InfoHash infoHash = new InfoHash("INFO_HASH".getBytes()); 
     	InfoHash infoHash1 = new InfoHash("INFO_HASH".getBytes()); 
     	Assert.assertTrue(infoHash.equals(infoHash1));
   } 
  	@Test
  	public void notMatchingEqualsTest(){
   	InfoHash infoHash = new InfoHash("INFO_HASH".getBytes()); 
     	InfoHash infoHash1 = new InfoHash("OTHER_VALUE".getBytes()); 
     	Assert.assertFalse(infoHash.equals(infoHash1));
   } 
  
  	@Test
  	public void toStringTest(){
     InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
     Assert.assertEquals("InfoHash{value=601492e054f9540eb0129c35deb385baa2faf0fe}",infoHash.toString());
   }
  	
}
