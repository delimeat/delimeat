package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.TorrentRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.ScrapeResult;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentDao;
import io.delimeat.core.torrent.TorrentException;
import io.delimeat.core.torrent.UnhandledScrapeException;

import java.net.SocketTimeoutException;
import java.net.URI;

public class TorrentSeederValidator_Impl implements TorrentValidator {

	private static final long MINSEEDERS = 20;

	private TorrentDao torrentDao;
	
	public TorrentDao getTorrentDao() {
		return torrentDao;
	}
	
	public void setTorrentDao(TorrentDao torrentDao) {
		this.torrentDao = torrentDao;
	}
	
	@Override
	public void validate(FeedResult result, Show show)
			throws TorrentValidatorException {
		// only scrap results not rejected
		if(result.getTorrentRejections().size()==0){
			TorrentDao dao = getTorrentDao();
			Torrent torrent = result.getTorrent();
			byte[] infoHash = torrent.getInfo().getInfoHash();
			ScrapeResult scrape = null;
			if(torrent.getTrackers()!=null && torrent.getTrackers().size()>0){
				for(String tracker: torrent.getTrackers()){
					try{
						scrape = dao.scrape(new URI(tracker), infoHash);
						break;
					}catch(SocketTimeoutException e){
						//move on to next tracker
					}/*
					catch(IOException e){
						//do nothing probably a timeout
					}*/
					catch(UnhandledScrapeException e){
						//do nothing, cant scrape
					}
					catch(TorrentException e){
						//do nothing move on
					}
					catch(Exception e){
						//this is unexpected throw it
						throw new TorrentValidatorException(e);
					}
				}
			}else if(torrent.getTracker()!=null){
				try{
					scrape = dao.scrape(new URI(torrent.getTracker()), infoHash);
				}catch(SocketTimeoutException e){
					//move on to next tracker
				}
				catch(UnhandledScrapeException e){
					//do nothing, cant scrape
				}
				catch(TorrentException e){
					//do nothing move on
				}
				catch(Exception e){
					//this is unexpected throw it
					throw new TorrentValidatorException(e);
				}
			}
			if(scrape!=null){
				result.setSeeders(scrape.getSeeders());
				result.setLeechers(scrape.getLeechers());
			}
			long resultSeeders = result.getSeeders();
			if(resultSeeders<MINSEEDERS){
				result.getTorrentRejections().add(TorrentRejection.INSUFFICENT_SEEDERS);
			}
		
		}
	}

}
