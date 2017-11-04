package io.delimeat.torrent.entity;

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
	
	public static UdpAction get(int value){
		UdpAction[] actions = UdpAction.values();
		for(int i = 0; i < actions.length; i++){
			UdpAction action = actions[i];
			if(action.value() == value){
				return action;
			}
		}
		return null;
	}
}
