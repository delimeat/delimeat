package io.delimeat.core.processor;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.processor.validation.TorrentValidator;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class FeedProcessorFactory_Impl implements ProcessorFactory, BeanFactoryAware {

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

	@Override
	public Processor build(Show show, Config config) {
		final String processorId = processorTypes.get(show.getShowType());
		final FeedProcessor_Impl processor = (FeedProcessor_Impl)beanFactory.getBean(processorId);
	
		processor.setShow(show);
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
