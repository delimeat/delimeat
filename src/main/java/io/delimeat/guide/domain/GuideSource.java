package io.delimeat.guide.domain;

public enum GuideSource {

	TVDB(0), 
  	IMDB(1), 
  	TVRAGE(2), 
  	TMDB(3), 
  	TVMAZE(4);
     
  	private final int value;
  
  	private GuideSource(int value){
     this.value = value;
   }
  
  	public int getValue(){
     	return value;
   }
}
