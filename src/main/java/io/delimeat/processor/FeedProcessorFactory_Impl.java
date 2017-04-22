package io.delimeat.processor;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.processor.validation.TorrentValidator;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.ShowType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class FeedProcessorFactory_Impl implements BeanFactoryAware, FeedProcessorFactory {

	private BeanFactory beanFactory; 
	
	private Map<ShowType, String> processorTypes = new HashMap<ShowType,String>();
	private TorrentValidator folderTorrentValidator;
	private Comparator<FeedResult> preferFilesComparator;
	private Comparator<FeedResult> maxSeedersComparator;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;		
	}
	
	public BeanFactory getBeanFactory(){
		return beanFactory;
	}
	
	public TorrentValidator getFolderTorrentValidator() {
		return folderTorrentValidator;
	}

	public void setFolderTorrentValidator(
			TorrentValidator folderTorrentValidator) {
		this.folderTorrentValidator = folderTorrentValidator;
	}

	public Comparator<FeedResult> getPreferFilesComparator() {
		return preferFilesComparator;
	}

	public void setPreferFilesComparator(
			Comparator<FeedResult> preferFilesComparator) {
		this.preferFilesComparator = preferFilesComparator;
	}

	public Comparator<FeedResult> getMaxSeedersComparator() {
		return maxSeedersComparator;
	}

	public void setMaxSeedersComparator(
			Comparator<FeedResult> maxSeedersComparator) {
		this.maxSeedersComparator = maxSeedersComparator;
	}
	
	public Map<ShowType, String> getProcessorTypes() {
		return processorTypes;
	}

	public void setProcessorTypes(Map<ShowType, String> processorTypes) {
		this.processorTypes = processorTypes;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.FeedProcessorFactory#build(io.delimeat.common.show.model.Episode, io.delimeat.common.config.model.Config)
	 */
	@Override
	public Processor build(Episode episode, Config config) {
		final String processorId = processorTypes.get(episode.getShow().getShowType());
		final FeedProcessor_Impl processor = (FeedProcessor_Impl)beanFactory.getBean(processorId);
	
		processor.setProcessEntity(episode);
		processor.setConfig(config);

		// set torrent validators
		if (config.isIgnoreFolders() == true) {
			processor.getTorrentValidators().add(folderTorrentValidator);
		}

		// set file comparator
		if (config.isPreferFiles() == true) {
			processor.setResultComparator(preferFilesComparator);
		} else {
			processor.setResultComparator(maxSeedersComparator);
		}

		return processor;
	}
	
}
