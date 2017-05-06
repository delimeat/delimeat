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

   private static final String DEFAULT = "US/Pacific";
  
	@Override
	public String marshal(String value) throws Exception {
		return null;
	}

	@Override
	public String unmarshal(String value) throws Exception {
		if(value == null || value.trim().length() == 0){
			return null;
		}
		return LIST.containsKey(value) ? LIST.get(value) : DEFAULT;
	}
	
	public static final Map<String,String> LIST = new HashMap<String,String>() {
		private static final long serialVersionUID = 1L;

		{
	    	put("Televisa","Etc/GMT-6");
	    	put("TV Azteca","Etc/GMT-6");
	    	put("TV Chile","Etc/GMT-4");
	    	put("Record","Etc/GMT-3");
	    	put("Rede Globo","Etc/GMT-3");
	    	put("Setanta Ireland","Etc/GMT");
	    	put("Bubble Hits","Etc/GMT");
	    	put("Channel 6","Etc/GMT");
	    	put("Chorus Sports","Etc/GMT");
	    	put("City Channel","Etc/GMT");
	    	put("E4","Etc/GMT");
	    	put("RTÉ One","Etc/GMT");
	    	put("RTÉ Two","Etc/GMT");
	    	put("Sky News Ireland","Etc/GMT");
	    	put("The Den","Etc/GMT");
	    	put("UTV","Etc/GMT");
	    	put("PRO TV","Etc/GMT+1");
	    	put("STV","Etc/GMT");
	    	put("TeleG","Etc/GMT");
	    	put("Adult Channel","Etc/GMT");
	    	put("B4U Movies","Etc/GMT");
	    	put("B4U Music","Etc/GMT");
	    	put("BBC Four","Etc/GMT");
	    	put("BBC HD","Etc/GMT");
	    	put("BBC News","Etc/GMT");
	    	put("BBC One","Etc/GMT");
	    	put("BBC Parliament","Etc/GMT");
	    	put("BBC Prime","Etc/GMT");
	    	put("BBC Three","Etc/GMT");
	    	put("BBC Two","Etc/GMT");
	    	put("BBC World News","Etc/GMT");
	    	put("Bravo","Etc/GMT");
	    	put("Carlton Television","Etc/GMT");
	    	put("CBBC","Etc/GMT");
	    	put("CBeebies","Etc/GMT");
	    	put("Challenge","Etc/GMT");
	    	put("Channel 4","Etc/GMT");
	    	put("Chart Show TV","Etc/GMT");
	    	put("Classic FM TV","Etc/GMT");
	    	put("Disney Cinemagic","Etc/GMT");
	    	put("Eurosport","Etc/GMT");
	    	put("FEM","Etc/GMT");
	    	put("Five","Etc/GMT");
	    	put("Five Life","Etc/GMT");
	    	put("Five US","Etc/GMT");
	    	put("ITV","Etc/GMT");
	    	put("ITV1","Etc/GMT");
	    	put("ITV2","Etc/GMT");
	    	put("ITV3","Etc/GMT");
	    	put("ITV4","Etc/GMT");
	    	put("Kerrang! TV","Etc/GMT");
	    	put("Kiss","Etc/GMT");
	    	put("Liberty TV","Etc/GMT");
	    	put("Living","Etc/GMT");
	    	put("Magic","Etc/GMT");
	    	put("MATV","Etc/GMT");
	    	put("MTV Base","Etc/GMT");
	    	put("MTV Dance","Etc/GMT");
	    	put("MUTV","Etc/GMT");
	    	put("Paramount Comedy","Etc/GMT");
	    	put("PlayJam","Etc/GMT");
	    	put("Pub Channel","Etc/GMT");
	    	put("Q TV","Etc/GMT");
	    	put("Red Hot TV","Etc/GMT");
	    	put("Revelation TV","Etc/GMT");
	    	put("rmusic TV","Etc/GMT");
	    	put("S4/C","Etc/GMT");
	    	put("S4C2","Etc/GMT");
	    	put("Scuzz","Etc/GMT");
	    	put("Sky Box Office","Etc/GMT");
	    	put("Sky Cinema","Etc/GMT");
	    	put("Sky Movies","Etc/GMT");
	    	put("Sky News","Etc/GMT");
	    	put("Sky Sports","Etc/GMT");
	    	put("Sky Travel","Etc/GMT");
	    	put("Sky1","Etc/GMT");
	    	put("Sky2","Etc/GMT");
	    	put("Sky3","Etc/GMT");
	    	put("Smash Hits","Etc/GMT");
	    	put("TCM","Etc/GMT");
	    	put("The Amp","Etc/GMT");
	    	put("The Box","Etc/GMT");
	    	put("Travel Channel","Etc/GMT");
	    	put("UK Entertainment Channel","Etc/GMT");
	    	put("UKTV Documentary","Etc/GMT");
	    	put("UKTV Drama","Etc/GMT");
	    	put("UKTV Food","Etc/GMT");
	    	put("UKTV G2","Etc/GMT");
	    	put("UKTV Gold","Etc/GMT");
	    	put("UKTV History","Etc/GMT");
	    	put("UKTV Style","Etc/GMT");
	    	put("ORF 1","Etc/GMT");
	    	put("ORF 2","Etc/GMT");
	    	put("BR","Etc/GMT");
	    	put("Canvas/Ketnet","Etc/GMT");
	    	put("één","Etc/GMT");
	    	put("KanaalTwee","Etc/GMT");
	    	put("VijfTV","Etc/GMT");
	    	put("VT4","Etc/GMT");
	    	put("VTM","Etc/GMT");
	    	put("Danmarks Radio","Etc/GMT");
	    	put("TV 2","Etc/GMT");
	    	put("TV 3","Etc/GMT");
	    	put("YLE","Etc/GMT");
	    	put("Arte","Etc/GMT");
	    	put("Canal Plus","Etc/GMT");
	    	put("Fashion TV","Etc/GMT");
	    	put("France 2","Etc/GMT");
	    	put("France 3","Etc/GMT");
	    	put("France 4","Etc/GMT");
	    	put("France 5","Etc/GMT");
	    	put("France Ô","Etc/GMT");
	    	put("LCI","Etc/GMT");
	    	put("M6","Etc/GMT");
	    	put("Télétoon","Etc/GMT");
	    	put("TF1","Etc/GMT");
	    	put("TV5 Monde","Etc/GMT");
	    	put("3sat","Etc/GMT");
	    	put("BR-alpha","Etc/GMT");
	    	put("Das Erste","Etc/GMT");
	    	put("Deutsche Welle TV","Etc/GMT");
	    	put("KIKA","Etc/GMT");
	    	put("MDR","Etc/GMT");
	    	put("NDR","Etc/GMT");
	    	put("Phoenix","Etc/GMT");
	    	put("ProSieben","Etc/GMT");
	    	put("Radio Bremen","Etc/GMT");
	    	put("RBB","Etc/GMT");
	    	put("RTL Television","Etc/GMT");
	    	put("SAT.1","Etc/GMT");
	    	put("SWR","Etc/GMT");
	    	put("Volksmusik TV","Etc/GMT");
	    	put("WDR","Etc/GMT");
	    	put("ZDF","Etc/GMT");
	    	put("Canale 5","Etc/GMT");
	    	put("RAI","Etc/GMT");
	    	put("TVM","Etc/GMT");
	    	put("TMF","Etc/GMT+1");
	    	put("NTA","Etc/GMT+1");
	    	put("NRK Super","Etc/GMT+1");
	    	put("NRK1","Etc/GMT+1");
	    	put("NRK2","Etc/GMT+1");
	    	put("NRK3","Etc/GMT+1");
	    	put("TV 2","Etc/GMT+1");
	    	put("TV 2 Filmkanalen","Etc/GMT+1");
	    	put("TV 2 Nyhetskanalen","Etc/GMT+1");
	    	put("TV 2 Science Fiction","Etc/GMT+1");
	    	put("TV 2 Sport","Etc/GMT+1");
	    	put("TV 2 Zebra","Etc/GMT+1");
	    	put("TVNorge","Etc/GMT+1");
	    	put("Viasat 4","Etc/GMT+1");
	    	put("TV 4","Etc/GMT+1");
	    	put("TVN","Etc/GMT+1");
	    	put("TVN Turbo","Etc/GMT+1");
	    	put("TVP SA","Etc/GMT+1");
	    	put("TVP1","Etc/GMT+1");
	    	put("TVP2","Etc/GMT+1");
	    	put("RTP Açores","Etc/GMT");
	    	put("RTP África","Etc/GMT");
	    	put("RTP Internacional","Etc/GMT");
	    	put("RTP Madeira","Etc/GMT");
	    	put("RTP N","Etc/GMT");
	    	put("RTP1","Etc/GMT");
	    	put("SIC","Etc/GMT");
	    	put("SIC Comédia","Etc/GMT");
	    	put("SIC Mulher","Etc/GMT");
	    	put("SIC Notícias","Etc/GMT");
	    	put("SIC Radical","Etc/GMT");
	    	put("SIC Sempre Gold","Etc/GMT");
	    	put("TV Cabo","Etc/GMT");
	    	put("TVI","Etc/GMT");
	    	put("Antena 3","Etc/GMT+1");
	    	put("La Siete","Etc/GMT+1");
	    	put("Telecinco","Etc/GMT+1");
	    	put("TVE","Etc/GMT+1");
	    	put("TV4","Etc/GMT+1");
	    	put("Schweizer Fernsehen","Etc/GMT+1");
	    	put("ANT1","Etc/GMT+2");
	    	put("ERT","Etc/GMT+2");
	    	put("Mega Channel","Etc/GMT+2");
	    	put("KTN","Etc/GMT+3");
	    	put("Al Jazeera","Etc/GMT+3");
	    	put("Al Alam","Etc/GMT+3");
	    	put("Press TV","Etc/GMT+3");
	    	put("Abu Dhabi TV","Etc/GMT+4");
	    	put("Al Arabiyya","Etc/GMT+4");
	    	put("AAG TV","Etc/GMT+5");
	    	put("Aaj TV","Etc/GMT+5");
	    	put("ARY Digital","Etc/GMT+5");
	    	put("ARY One World","Etc/GMT+5");
	    	put("ARY Shopping Channel","Etc/GMT+5");
	    	put("ARY Zouq","Etc/GMT+5");
	    	put("Dawn News","Etc/GMT+5");
	    	put("GEO Super","Etc/GMT+5");
	    	put("Geo TV","Etc/GMT+5");
	    	put("Indus Music","Etc/GMT+5");
	    	put("Indus News","Etc/GMT+5");
	    	put("Indus Vision","Etc/GMT+5");
	    	put("Kashish TV","Etc/GMT+5");
	    	put("KTN","Etc/GMT+5");
	    	put("News One","Etc/GMT+5");
	    	put("PTV Bolan","Etc/GMT+5");
	    	put("PTV Home","Etc/GMT+5");
	    	put("PTV News","Etc/GMT+5");
	    	put("QTV","Etc/GMT+5");
	    	put("Royal News","Etc/GMT+5");
	    	put("Doordarshan National Gujarat","Etc/GMT+5");
	    	put("STAR Sports India","Etc/GMT+5");
	    	put("Zee Gujarati","Etc/GMT+5");
	    	put("Zee Movies","Etc/GMT+5");
	    	put("Alpha TV GUJARATI","Etc/GMT+5");
	    	put("Alpha TV PUNJABI","Etc/GMT+5");
	    	put("ATN Aastha Channel","Etc/GMT+5");
	    	put("CNBC TV18","Etc/GMT+5");
	    	put("Doordarshan National","Etc/GMT+5");
	    	put("Doordarshan News","Etc/GMT+5");
	    	put("Doordarshan Sports","Etc/GMT+5");
	    	put("ESPN India","Etc/GMT+5");
	    	put("ETV Gujarati","Etc/GMT+5");
	    	put("NDTV 24x7","Etc/GMT+5");
	    	put("NDTV India","Etc/GMT+5");
	    	put("Sab TV","Etc/GMT+5");
	    	put("Sahara ONE","Etc/GMT+5");
	    	put("Sanskar","Etc/GMT+5");
	    	put("SET MAX","Etc/GMT+5");
	    	put("Sony TV","Etc/GMT+5");
	    	put("STAR Gold","Etc/GMT+5");
	    	put("STAR News","Etc/GMT+5");
	    	put("STAR Plus","Etc/GMT+5");
	    	put("STAR Vijay","Etc/GMT+5");
	    	put("SUN music","Etc/GMT+5");
	    	put("SUN news","Etc/GMT+5");
	    	put("SUN TV","Etc/GMT+5");
	    	put("TEN Sports","Etc/GMT+5");
	    	put("Zee Muzic","Etc/GMT+5");
	    	put("Zee TV","Etc/GMT+5");
	    	put("ZOOM","Etc/GMT+5");
	    	put("Sirasa","Etc/GMT+5");
	    	put("Swarnawahini","Etc/GMT+5");
	    	put("TVRI","Etc/GMT+7");
	    	put("RCTI","Etc/GMT+7");
	    	put("CCTV","Etc/GMT+8");
	    	put("Channel [V]","Etc/GMT+8");
	    	put("Phoenix TV","Etc/GMT+8");
	    	put("TVB","Etc/GMT+8");
	    	put("TVBS","Etc/GMT+8");
	    	put("ESPN Hong Kong","Etc/GMT+8");
	    	put("STAR Movies","");
	    	put("ABC","Etc/GMT+8");
	    	put("ABS-CBN Broadcasting Company","Etc/GMT+8");
	    	put("ESPN Philippines","Etc/GMT+8");
	    	put("GMA","Etc/GMT+8");
	    	put("Studio 23","Etc/GMT+8");
	    	put("UNTV 37","Etc/GMT+8");
	    	put("Channel 5","Etc/GMT+8");
	    	put("Channel 8","Etc/GMT+8");
	    	put("Channel U","Etc/GMT+8");
	    	put("STAR Sports S.E.A","Etc/GMT+8");
	    	put("STAR Sports Singapore","Etc/GMT+8");
	    	put("STAR Sports Taiwan","Etc/GMT+8");
	    	put("ESPN Taiwan","Etc/GMT+8");
	    	put("ESPN Asia","Etc/GMT+8");
	    	put("Star World","Etc/GMT+8");
	    	put("STAR Sports Asia","Etc/GMT+8");
	    	put("STAR Sports Hong Kong","Etc/GMT+8");
	    	put("Animax","JST");
	    	put("BS11","JST");
	    	put("CBC","JST");
	    	put("CTC","JST");
	    	put("Fuji TV","JST");
	    	put("MBS","JST");
	    	put("NHK","JST");
	    	put("NTV","JST");
	    	put("TBS","JST");
	    	put("Television Osaka","JST");
	    	put("Tokyo MX","JST");
	    	put("TV Asahi","JST");
	    	put("TV Tokyo","JST");
	    	put("TVA","JST");
	    	put("WOWOW","JST");
	    	put("ESPN Korea","Etc/GMT+9");
	    	put("EBS","Etc/GMT+9");
	    	put("KBS","Etc/GMT+9");
	    	put("SBS","Etc/GMT+9");
	    	put("Munhwa Broadcasting Corporation (MBC)","Etc/GMT+9");
	    	put("TV1","AET");
	    	put("Network Ten","AET");
	    	put("Nine Network","AET");
	    	put("Seven Network","AET");
	    	put("TVS Sydney","AET");
	    	put("TV One","Etc/GMT+12");
	    	put("TVNZ","Etc/GMT+12");    	
	    }
	};

}
