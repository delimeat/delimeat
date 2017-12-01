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
package io.delimeat.show;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;
import io.delimeat.util.AbstractJpaDao;

@Repository
public class EpisodeDao_Impl extends AbstractJpaDao<Long,Episode> implements EpisodeDao {

	public EpisodeDao_Impl(){
		super(Episode.class);
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeDao#findByStatus(java.util.List)
	 */
	@Override
	public List<Episode> findByStatus(List<EpisodeStatus> statuses) {
		return getEntityManager().createNamedQuery("Episode.findByStatus", Episode.class)
				.setParameter("list", statuses)
				.getResultList();
	}

}
