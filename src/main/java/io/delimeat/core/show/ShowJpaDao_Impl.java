package io.delimeat.core.show;

import io.delimeat.core.show.Episode;
import io.delimeat.core.show.ShowConcurrencyException;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.show.ShowNotFoundException;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

public class ShowJpaDao_Impl implements ShowDao {

	@PersistenceContext
	protected EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public Show read(long showId) throws ShowNotFoundException, ShowException {
		try{
			
			return em.getReference(Show.class, showId);
		
		} catch (EntityNotFoundException ex){
			throw new ShowNotFoundException(ex);
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}

	@Override
	public Show createOrUpdate(Show show) throws ShowConcurrencyException, ShowException {
		try{
			
			return em.merge(show);
			
		} catch (OptimisticLockException ex){
			throw new ShowConcurrencyException(ex);
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}

	}

	@Override
	public void delete(long showId) throws ShowNotFoundException, ShowException {
		try{
			final Show show = read(showId);
			show.setNextEpisode(null);
			show.setPreviousEpisode(null);
        	deleteEpisodes(show);
        	/*
			for(Episode ep: readAllEpisodes(showId)){
				deleteEpisode(ep.getEpisodeId());
			}
         */
			em.remove(show);
			
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}

	@Override
	public List<Show> readAll() throws ShowException {
		try{
        
         return em.createNamedQuery("Show.getAll",Show.class).getResultList();
		
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}
  
	@Override
	public Show readAndLock(long showId) throws ShowNotFoundException, ShowException {
		try{
        
         Show show = read(showId);
         em.lock(show, LockModeType.PESSIMISTIC_WRITE);
			return show;
		
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}

	@Override
	public List<Episode> readAllEpisodes(long showId)
			throws ShowNotFoundException, ShowException {
		try{
        		Show show = read(showId);
	         return em.createNamedQuery("Show.getAllEpisodes",Episode.class).setParameter("show", show).getResultList();
			
			} catch (PersistenceException ex){
				throw new ShowException(ex);
			}
	}
  
	@Override
	public Episode readEpisode(long episodeId) throws ShowNotFoundException, ShowException {
		try{
			
			return em.getReference(Episode.class, episodeId);
		
		} catch (EntityNotFoundException ex){
			throw new ShowNotFoundException(ex);
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}

	@Override
	public Episode createOrUpdateEpisode(Episode episode) throws ShowConcurrencyException, ShowException {
		try{
			
			return em.merge(episode);
			
		} catch (OptimisticLockException ex){
			throw new ShowConcurrencyException(ex);
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}

	}

	@Override
	public void deleteEpisode(long episodeId) throws ShowNotFoundException, ShowException {
		try{
			
			em.remove(readEpisode(episodeId));
			
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}

    @Override
    public List<Episode> createOrUpdateEpisodes(List<Episode> episodes)
            throws ShowConcurrencyException, ShowException {
        final List<Episode> createdOrUpdatedEpisodes = new ArrayList<Episode>();
        for(Episode episode: episodes){
          createdOrUpdatedEpisodes.add(createOrUpdateEpisode(episode));
        }
        return createdOrUpdatedEpisodes;
    }

    @Override
    public Episode readNextEpisode(Episode episode) throws ShowNotFoundException, ShowException {
		try{	         
			return em.createNamedQuery("Show.getNextEpisode",Episode.class)
          			.setParameter("show", episode.getShow())
          			.setParameter("seasonNum",episode.getSeasonNum())
          			.setParameter("episodeNum",episode.getEpisodeNum())
          			.setMaxResults(1)
          			.getSingleResult();
			
			} catch(NoResultException ex){  
				return null;
    		} catch (PersistenceException ex){
				throw new ShowException(ex);
			}
    }

    @Override
    public void deleteEpisodesAfter(Episode episode) throws ShowException {
		try{	         
			em.createNamedQuery("Show.deleteEpisodesAfter",Episode.class)
          			.setParameter("show", episode.getShow())
          			.setParameter("seasonNum",episode.getSeasonNum())
          			.setParameter("episodeNum",episode.getEpisodeNum())
          			.executeUpdate();
			
			} catch (PersistenceException ex){
				throw new ShowException(ex);
			}
        
    }

    @Override
    public void deleteEpisodes(Show show) throws ShowException {
		try{	         
			em.createNamedQuery("Show.deleteEpisodes",Episode.class)
          			.setParameter("show", show)
          			.executeUpdate();
			
			} catch (PersistenceException ex){
				throw new ShowException(ex);
			}
        
    }

}
