package io.delimeat.core.processor.validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.InfoHash;
import io.delimeat.core.torrent.ScrapeResult;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentDao;
import io.delimeat.core.torrent.TorrentException;
import io.delimeat.core.torrent.UnhandledScrapeException;
import io.delimeat.util.DelimeatUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

public class TorrentSeederValidator_Impl implements TorrentValidator {

   private static final Log LOG = LogFactory.getLog(TorrentSeederValidator_Impl.class);
	private static final long MINSEEDERS = 20;

	private TorrentDao torrentDao;
	
	public TorrentDao getTorrentDao() {
		return torrentDao;
	}
	
	public void setTorrentDao(TorrentDao torrentDao) {
		this.torrentDao = torrentDao;
	}
	
	@Override
	public boolean validate(Torrent torrent, Show show, Config config)
			throws ValidationException {
     
     final InfoHash infoHash = torrent.getInfo().getInfoHash();
     ScrapeResult scrape = null;
     if(torrent.getTrackers() != null && torrent.getTrackers().isEmpty() == false){
       Iterator<String> it = torrent.getTrackers().iterator();
       while(scrape==null && it.hasNext()){
         scrape = scrape(it.next(),infoHash);
       }
     }
     else if(DelimeatUtils.isNotEmpty(torrent.getTracker())){
     		scrape = scrape(torrent.getTracker(),infoHash);
     }

     return (scrape != null && scrape.getSeeders() >= MINSEEDERS);
	}
   
   public ScrapeResult scrape(String tracker, InfoHash infohash){
       try{
         return getTorrentDao().scrape(new URI(tracker), infohash);
       }catch(SocketTimeoutException e){
         LOG.info("Timed out scraping tracker "+ tracker, e);
       } catch (UnhandledScrapeException e) {
         LOG.info("Unnable to scrape tracker "+ tracker, e);
       } catch (IOException e) {
         LOG.info("Unnable to scrape tracker "+ tracker, e);
       } catch (TorrentException e) {
         LOG.info("Unnable to scrape tracker "+ tracker, e);
       } catch (URISyntaxException e) {
         LOG.info("Unnable to scrape tracker "+ tracker, e);
       }  
     	return null;
   }
  
  
    @Override
    public FeedResultRejection getRejection() {
        return FeedResultRejection.INSUFFICENT_SEEDERS;
    }

}
