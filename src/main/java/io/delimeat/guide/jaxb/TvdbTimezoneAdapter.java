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
package io.delimeat.guide.jaxb;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TvdbTimezoneAdapter extends XmlAdapter<String, String> {
   
   private final Map<String,String> mapping;
   
   public TvdbTimezoneAdapter(){
	   mapping = new HashMap<>();
	   	mapping.put("Televisa","Etc/GMT-6");
	   	mapping.put("TV Azteca","Etc/GMT-6");
	   	mapping.put("TV Chile","Etc/GMT-4");
	   	mapping.put("Record","Etc/GMT-3");
	   	mapping.put("Rede Globo","Etc/GMT-3");
	   	mapping.put("Setanta Ireland","Etc/GMT");
	   	mapping.put("Bubble Hits","Etc/GMT");
	   	mapping.put("Channel 6","Etc/GMT");
	   	mapping.put("Chorus Sports","Etc/GMT");
	   	mapping.put("City Channel","Etc/GMT");
	   	mapping.put("E4","Etc/GMT");
	   	mapping.put("RTÉ One","Etc/GMT");
	   	mapping.put("RTÉ Two","Etc/GMT");
	   	mapping.put("Sky News Ireland","Etc/GMT");
	   	mapping.put("The Den","Etc/GMT");
	   	mapping.put("UTV","Etc/GMT");
	   	mapping.put("PRO TV","Etc/GMT+1");
	   	mapping.put("STV","Etc/GMT");
	   	mapping.put("TeleG","Etc/GMT");
	   	mapping.put("Adult Channel","Etc/GMT");
	   	mapping.put("B4U Movies","Etc/GMT");
	   	mapping.put("B4U Music","Etc/GMT");
	   	mapping.put("BBC Four","Etc/GMT");
	   	mapping.put("BBC HD","Etc/GMT");
	   	mapping.put("BBC News","Etc/GMT");
	   	mapping.put("BBC One","Etc/GMT");
	   	mapping.put("BBC Parliament","Etc/GMT");
	   	mapping.put("BBC Prime","Etc/GMT");
	   	mapping.put("BBC Three","Etc/GMT");
	   	mapping.put("BBC Two","Etc/GMT");
	   	mapping.put("BBC World News","Etc/GMT");
	   	mapping.put("Bravo","Etc/GMT");
	   	mapping.put("Carlton Television","Etc/GMT");
	   	mapping.put("CBBC","Etc/GMT");
	   	mapping.put("CBeebies","Etc/GMT");
	   	mapping.put("Challenge","Etc/GMT");
	   	mapping.put("Channel 4","Etc/GMT");
	   	mapping.put("Chart Show TV","Etc/GMT");
	   	mapping.put("Classic FM TV","Etc/GMT");
	   	mapping.put("Disney Cinemagic","Etc/GMT");
	   	mapping.put("Eurosport","Etc/GMT");
	   	mapping.put("FEM","Etc/GMT");
	   	mapping.put("Five","Etc/GMT");
	   	mapping.put("Five Life","Etc/GMT");
	   	mapping.put("Five US","Etc/GMT");
	   	mapping.put("ITV","Etc/GMT");
	   	mapping.put("ITV1","Etc/GMT");
	   	mapping.put("ITV2","Etc/GMT");
	   	mapping.put("ITV3","Etc/GMT");
	   	mapping.put("ITV4","Etc/GMT");
	   	mapping.put("Kerrang! TV","Etc/GMT");
	   	mapping.put("Kiss","Etc/GMT");
	   	mapping.put("Liberty TV","Etc/GMT");
	   	mapping.put("Living","Etc/GMT");
	   	mapping.put("Magic","Etc/GMT");
	   	mapping.put("MATV","Etc/GMT");
	   	mapping.put("MTV Base","Etc/GMT");
	   	mapping.put("MTV Dance","Etc/GMT");
	   	mapping.put("MUTV","Etc/GMT");
	   	mapping.put("Paramount Comedy","Etc/GMT");
	   	mapping.put("PlayJam","Etc/GMT");
	   	mapping.put("Pub Channel","Etc/GMT");
	   	mapping.put("Q TV","Etc/GMT");
	   	mapping.put("Red Hot TV","Etc/GMT");
	   	mapping.put("Revelation TV","Etc/GMT");
	   	mapping.put("rmusic TV","Etc/GMT");
	   	mapping.put("S4/C","Etc/GMT");
	   	mapping.put("S4C2","Etc/GMT");
	   	mapping.put("Scuzz","Etc/GMT");
	   	mapping.put("Sky Box Office","Etc/GMT");
	   	mapping.put("Sky Cinema","Etc/GMT");
	   	mapping.put("Sky Movies","Etc/GMT");
	   	mapping.put("Sky News","Etc/GMT");
	   	mapping.put("Sky Sports","Etc/GMT");
	   	mapping.put("Sky Travel","Etc/GMT");
	   	mapping.put("Sky1","Etc/GMT");
	   	mapping.put("Sky2","Etc/GMT");
	   	mapping.put("Sky3","Etc/GMT");
	   	mapping.put("Smash Hits","Etc/GMT");
	   	mapping.put("TCM","Etc/GMT");
	   	mapping.put("The Amp","Etc/GMT");
	   	mapping.put("The Box","Etc/GMT");
	   	mapping.put("Travel Channel","Etc/GMT");
	   	mapping.put("UK Entertainment Channel","Etc/GMT");
	   	mapping.put("UKTV Documentary","Etc/GMT");
	   	mapping.put("UKTV Drama","Etc/GMT");
	   	mapping.put("UKTV Food","Etc/GMT");
	   	mapping.put("UKTV G2","Etc/GMT");
	   	mapping.put("UKTV Gold","Etc/GMT");
	   	mapping.put("UKTV History","Etc/GMT");
	   	mapping.put("UKTV Style","Etc/GMT");
	   	mapping.put("ORF 1","Etc/GMT");
	   	mapping.put("ORF 2","Etc/GMT");
	   	mapping.put("BR","Etc/GMT");
	   	mapping.put("Canvas/Ketnet","Etc/GMT");
	   	mapping.put("één","Etc/GMT");
	   	mapping.put("KanaalTwee","Etc/GMT");
	   	mapping.put("VijfTV","Etc/GMT");
	   	mapping.put("VT4","Etc/GMT");
	   	mapping.put("VTM","Etc/GMT");
	   	mapping.put("Danmarks Radio","Etc/GMT");
	   	mapping.put("TV 2","Etc/GMT");
	   	mapping.put("TV 3","Etc/GMT");
	   	mapping.put("YLE","Etc/GMT");
	   	mapping.put("Arte","Etc/GMT");
	   	mapping.put("Canal Plus","Etc/GMT");
	   	mapping.put("Fashion TV","Etc/GMT");
	   	mapping.put("France 2","Etc/GMT");
	   	mapping.put("France 3","Etc/GMT");
	   	mapping.put("France 4","Etc/GMT");
	   	mapping.put("France 5","Etc/GMT");
	   	mapping.put("France Ô","Etc/GMT");
	   	mapping.put("LCI","Etc/GMT");
	   	mapping.put("M6","Etc/GMT");
	   	mapping.put("Télétoon","Etc/GMT");
	   	mapping.put("TF1","Etc/GMT");
	   	mapping.put("TV5 Monde","Etc/GMT");
	   	mapping.put("3sat","Etc/GMT");
	   	mapping.put("BR-alpha","Etc/GMT");
	   	mapping.put("Das Erste","Etc/GMT");
	   	mapping.put("Deutsche Welle TV","Etc/GMT");
	   	mapping.put("KIKA","Etc/GMT");
	   	mapping.put("MDR","Etc/GMT");
	   	mapping.put("NDR","Etc/GMT");
	   	mapping.put("Phoenix","Etc/GMT");
	   	mapping.put("ProSieben","Etc/GMT");
	   	mapping.put("Radio Bremen","Etc/GMT");
	   	mapping.put("RBB","Etc/GMT");
	   	mapping.put("RTL Television","Etc/GMT");
	   	mapping.put("SAT.1","Etc/GMT");
	   	mapping.put("SWR","Etc/GMT");
	   	mapping.put("Volksmusik TV","Etc/GMT");
	   	mapping.put("WDR","Etc/GMT");
	   	mapping.put("ZDF","Etc/GMT");
	   	mapping.put("Canale 5","Etc/GMT");
	   	mapping.put("RAI","Etc/GMT");
	   	mapping.put("TVM","Etc/GMT");
	   	mapping.put("TMF","Etc/GMT+1");
	   	mapping.put("NTA","Etc/GMT+1");
	   	mapping.put("NRK Super","Etc/GMT+1");
	   	mapping.put("NRK1","Etc/GMT+1");
	   	mapping.put("NRK2","Etc/GMT+1");
	   	mapping.put("NRK3","Etc/GMT+1");
	   	mapping.put("TV 2","Etc/GMT+1");
	   	mapping.put("TV 2 Filmkanalen","Etc/GMT+1");
	   	mapping.put("TV 2 Nyhetskanalen","Etc/GMT+1");
	   	mapping.put("TV 2 Science Fiction","Etc/GMT+1");
	   	mapping.put("TV 2 Sport","Etc/GMT+1");
	   	mapping.put("TV 2 Zebra","Etc/GMT+1");
	   	mapping.put("TVNorge","Etc/GMT+1");
	   	mapping.put("Viasat 4","Etc/GMT+1");
	   	mapping.put("TV 4","Etc/GMT+1");
	   	mapping.put("TVN","Etc/GMT+1");
	   	mapping.put("TVN Turbo","Etc/GMT+1");
	   	mapping.put("TVP SA","Etc/GMT+1");
	   	mapping.put("TVP1","Etc/GMT+1");
	   	mapping.put("TVP2","Etc/GMT+1");
	   	mapping.put("RTP Açores","Etc/GMT");
	   	mapping.put("RTP África","Etc/GMT");
	   	mapping.put("RTP Internacional","Etc/GMT");
	   	mapping.put("RTP Madeira","Etc/GMT");
	   	mapping.put("RTP N","Etc/GMT");
	   	mapping.put("RTP1","Etc/GMT");
	   	mapping.put("SIC","Etc/GMT");
	   	mapping.put("SIC Comédia","Etc/GMT");
	   	mapping.put("SIC Mulher","Etc/GMT");
	   	mapping.put("SIC Notícias","Etc/GMT");
	   	mapping.put("SIC Radical","Etc/GMT");
	   	mapping.put("SIC Sempre Gold","Etc/GMT");
	   	mapping.put("TV Cabo","Etc/GMT");
	   	mapping.put("TVI","Etc/GMT");
	   	mapping.put("Antena 3","Etc/GMT+1");
	   	mapping.put("La Siete","Etc/GMT+1");
	   	mapping.put("Telecinco","Etc/GMT+1");
	   	mapping.put("TVE","Etc/GMT+1");
	   	mapping.put("TV4","Etc/GMT+1");
	   	mapping.put("Schweizer Fernsehen","Etc/GMT+1");
	   	mapping.put("ANT1","Etc/GMT+2");
	   	mapping.put("ERT","Etc/GMT+2");
	   	mapping.put("Mega Channel","Etc/GMT+2");
	   	mapping.put("KTN","Etc/GMT+3");
	   	mapping.put("Al Jazeera","Etc/GMT+3");
	   	mapping.put("Al Alam","Etc/GMT+3");
	   	mapping.put("Press TV","Etc/GMT+3");
	   	mapping.put("Abu Dhabi TV","Etc/GMT+4");
	   	mapping.put("Al Arabiyya","Etc/GMT+4");
	   	mapping.put("AAG TV","Etc/GMT+5");
	   	mapping.put("Aaj TV","Etc/GMT+5");
	   	mapping.put("ARY Digital","Etc/GMT+5");
	   	mapping.put("ARY One World","Etc/GMT+5");
	   	mapping.put("ARY Shopping Channel","Etc/GMT+5");
	   	mapping.put("ARY Zouq","Etc/GMT+5");
	   	mapping.put("Dawn News","Etc/GMT+5");
	   	mapping.put("GEO Super","Etc/GMT+5");
	   	mapping.put("Geo TV","Etc/GMT+5");
	   	mapping.put("Indus Music","Etc/GMT+5");
	   	mapping.put("Indus News","Etc/GMT+5");
	   	mapping.put("Indus Vision","Etc/GMT+5");
	   	mapping.put("Kashish TV","Etc/GMT+5");
	   	mapping.put("KTN","Etc/GMT+5");
	   	mapping.put("News One","Etc/GMT+5");
	   	mapping.put("PTV Bolan","Etc/GMT+5");
	   	mapping.put("PTV Home","Etc/GMT+5");
	   	mapping.put("PTV News","Etc/GMT+5");
	   	mapping.put("QTV","Etc/GMT+5");
	   	mapping.put("Royal News","Etc/GMT+5");
	   	mapping.put("Doordarshan National Gujarat","Etc/GMT+5");
	   	mapping.put("STAR Sports India","Etc/GMT+5");
	   	mapping.put("Zee Gujarati","Etc/GMT+5");
	   	mapping.put("Zee Movies","Etc/GMT+5");
	   	mapping.put("Alpha TV GUJARATI","Etc/GMT+5");
	   	mapping.put("Alpha TV PUNJABI","Etc/GMT+5");
	   	mapping.put("ATN Aastha Channel","Etc/GMT+5");
	   	mapping.put("CNBC TV18","Etc/GMT+5");
	   	mapping.put("Doordarshan National","Etc/GMT+5");
	   	mapping.put("Doordarshan News","Etc/GMT+5");
	   	mapping.put("Doordarshan Sports","Etc/GMT+5");
	   	mapping.put("ESPN India","Etc/GMT+5");
	   	mapping.put("ETV Gujarati","Etc/GMT+5");
	   	mapping.put("NDTV 24x7","Etc/GMT+5");
	   	mapping.put("NDTV India","Etc/GMT+5");
	   	mapping.put("Sab TV","Etc/GMT+5");
	   	mapping.put("Sahara ONE","Etc/GMT+5");
	   	mapping.put("Sanskar","Etc/GMT+5");
	   	mapping.put("SET MAX","Etc/GMT+5");
	   	mapping.put("Sony TV","Etc/GMT+5");
	   	mapping.put("STAR Gold","Etc/GMT+5");
	   	mapping.put("STAR News","Etc/GMT+5");
	   	mapping.put("STAR Plus","Etc/GMT+5");
	   	mapping.put("STAR Vijay","Etc/GMT+5");
	   	mapping.put("SUN music","Etc/GMT+5");
	   	mapping.put("SUN news","Etc/GMT+5");
	   	mapping.put("SUN TV","Etc/GMT+5");
	   	mapping.put("TEN Sports","Etc/GMT+5");
	   	mapping.put("Zee Muzic","Etc/GMT+5");
	   	mapping.put("Zee TV","Etc/GMT+5");
	   	mapping.put("ZOOM","Etc/GMT+5");
	   	mapping.put("Sirasa","Etc/GMT+5");
	   	mapping.put("Swarnawahini","Etc/GMT+5");
	   	mapping.put("TVRI","Etc/GMT+7");
	   	mapping.put("RCTI","Etc/GMT+7");
	   	mapping.put("CCTV","Etc/GMT+8");
	   	mapping.put("Channel [V]","Etc/GMT+8");
	   	mapping.put("Phoenix TV","Etc/GMT+8");
	   	mapping.put("TVB","Etc/GMT+8");
	   	mapping.put("TVBS","Etc/GMT+8");
	   	mapping.put("ESPN Hong Kong","Etc/GMT+8");
	   	mapping.put("STAR Movies","");
	   	mapping.put("ABC","Etc/GMT+8");
	   	mapping.put("ABS-CBN Broadcasting Company","Etc/GMT+8");
	   	mapping.put("ESPN Philippines","Etc/GMT+8");
	   	mapping.put("GMA","Etc/GMT+8");
	   	mapping.put("Studio 23","Etc/GMT+8");
	   	mapping.put("UNTV 37","Etc/GMT+8");
	   	mapping.put("Channel 5","Etc/GMT+8");
	   	mapping.put("Channel 8","Etc/GMT+8");
	   	mapping.put("Channel U","Etc/GMT+8");
	   	mapping.put("STAR Sports S.E.A","Etc/GMT+8");
	   	mapping.put("STAR Sports Singapore","Etc/GMT+8");
	   	mapping.put("STAR Sports Taiwan","Etc/GMT+8");
	   	mapping.put("ESPN Taiwan","Etc/GMT+8");
	   	mapping.put("ESPN Asia","Etc/GMT+8");
	   	mapping.put("Star World","Etc/GMT+8");
	   	mapping.put("STAR Sports Asia","Etc/GMT+8");
	   	mapping.put("STAR Sports Hong Kong","Etc/GMT+8");
	   	mapping.put("Animax","Asia/Tokyo");
	   	mapping.put("BS11","Asia/Tokyo");
	   	mapping.put("CBC","Asia/Tokyo");
	   	mapping.put("CTC","Asia/Tokyo");
	   	mapping.put("Fuji TV","Asia/Tokyo");
	   	mapping.put("MBS","Asia/Tokyo");
	   	mapping.put("NHK","Asia/Tokyo");
	   	mapping.put("NTV","Asia/Tokyo");
	   	mapping.put("TBS","Asia/Tokyo");
	   	mapping.put("Television Osaka","Asia/Tokyo");
	   	mapping.put("Tokyo MX","Asia/Tokyo");
	   	mapping.put("TV Asahi","Asia/Tokyo");
	   	mapping.put("TV Tokyo","Asia/Tokyo");
	   	mapping.put("TVA","Asia/Tokyo");
	   	mapping.put("WOWOW","Asia/Tokyo");
	   	mapping.put("ESPN Korea","Etc/GMT+9");
	   	mapping.put("EBS","Etc/GMT+9");
	   	mapping.put("KBS","Etc/GMT+9");
	   	mapping.put("SBS","Etc/GMT+9");
	   	mapping.put("Munhwa Broadcasting Corporation (MBC)","Etc/GMT+9");
	   	mapping.put("TV1","Australia/Sydney");
	   	mapping.put("Network Ten","Australia/Sydney");
	   	mapping.put("Nine Network","Australia/Sydney");
	   	mapping.put("Seven Network","Australia/Sydney");
	   	mapping.put("TVS Sydney","Australia/Sydney");
	   	mapping.put("TV One","Etc/GMT+12");
	   	mapping.put("TVNZ","Etc/GMT+12");  
   }
  
	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(String value) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public String unmarshal(String value) throws Exception {
		if(value == null || value.trim().length() == 0){
			return null;
		}
		return mapping.getOrDefault(value, "America/Los_Angeles");
	}

}
