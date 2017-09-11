package io.delimeat.torrent.udp.domain;

public enum UdpAction {

	CONNECT(0),
	ANNOUNCE(1),
	SCRAPE(2),
	ERROR(3);
	
	private final int value;
	
	UdpAction(int value){
		this.value = value;
	}
	
	public int value(){
		return value;
	}
}
