package io.delimeat.torrent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SchemeComparator implements Comparator<String>{

	@Override
	public int compare(String trackerOne, String trackerTwo) {
		List<String> indexes = Arrays.asList("udp","https","http","magnet");
		
		String schemeOne = determineScheme(trackerOne);
		String schemeTwo = determineScheme(trackerTwo);
		Integer schemeOneIdx = indexes.indexOf(schemeOne) >= 0 ? indexes.indexOf(schemeOne) : 4;
		Integer schemeTwoIdx = indexes.indexOf(schemeTwo) >= 0 ? indexes.indexOf(schemeTwo) : 4;;
		
		if(schemeOneIdx < schemeTwoIdx){
			return -1;
		}else if (schemeOneIdx > schemeTwoIdx){
			return 1;
		}
		return 0;
	}
	
	public String determineScheme(String tracker){
		if(tracker == null || tracker.isEmpty()){
			return null;
		}
		
		int index = tracker.indexOf(":") >= 0 ? tracker.indexOf(":") : 0;
        String scheme = tracker.substring(0, index);
        
        if(scheme.isEmpty() == true){
        	return null;
        }
		return scheme;
	}
}
