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
package io.delimeat.config.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(of={"configId","version"})
public class Config {

	@Id
	@Column(name="CONFIG_ID")
	@JsonIgnore
	private Long configId;
	
	@Column(name="OUTPUT_DIR",nullable=false)
	@Basic(optional=false)
	private String outputDirectory;
	
	@Column(name="SEARCH_INTERVAL",nullable=false)
	@Basic(optional=false)
	private int searchInterval;
	
	@Column(name="SEARCH_DELAY",nullable=false)
	@Basic(optional=false)
	private int searchDelay;
	
	@Column(name="PREFER_FILES",nullable=false)
	@Basic(optional=false)
	private boolean preferFiles;
	
	@Column(name="IGNORE_FOLDERS",nullable=false)
	@Basic(optional=false)
	private boolean ignoreFolders;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="IGNORED_FILE_TYPES", joinColumns=@JoinColumn(name="CONFIG_ID"))
	@Column(name="FILE_TYPE")
	@OrderColumn
	private List<String> ignoredFileTypes = new ArrayList<String>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="EXCLUDED_KEYWORDS", joinColumns=@JoinColumn(name="CONFIG_ID"))
	@Column(name="KEYWORD")
	@OrderColumn
	private List<String> excludedKeywords = new ArrayList<String>();

	@Version
	private Long version;

}
