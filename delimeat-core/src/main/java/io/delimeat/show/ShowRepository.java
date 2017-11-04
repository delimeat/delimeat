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

import org.springframework.data.repository.Repository;

import io.delimeat.show.entity.Show;

public interface ShowRepository extends Repository<Show, Long> {

	public Show findOne(Long id);
	
	public Show save(Show show);
	
	public void delete(Long id);
	
	public List<Show> findAll();
}
