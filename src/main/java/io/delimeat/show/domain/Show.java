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
package io.delimeat.show.domain;

import java.time.Instant;
import java.time.LocalTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Table(name="SHOW")
@NamedQueries({
	@NamedQuery(name="Show.findAll", query="SELECT s FROM Show s")
})
@Data
@EqualsAndHashCode(of={"showId","version"})
public class Show {


  	@Id
	@Column(name="SHOW_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SHOW_SEQ")
	@TableGenerator(name="SHOW_SEQ", table="SEQUENCE", pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT", pkColumnValue="SHOW_SEQ")
	private long showId;
	
	@Column(name="AIR_TIME", nullable=false)
	@Basic(optional=false)
	private LocalTime airTime;
	
	@Column(name="TIMEZONE", length=64, nullable=false)
	@Basic(optional=false)
	private String timezone;
	
	@Column(name="GUIDE_ID", length=255, nullable=false, unique=true)
	@Basic(optional=false)
	private String guideId;
	
	@Column(name="TITLE", length=255, nullable=false, unique=true)
	@Basic(optional=false)	
	private String title;
	

	@Column(name="AIRING", nullable=false)
	@Basic(optional=false)
	private boolean airing;
	
	@Column(name="SHOW_TYPE", nullable=false)
	@Basic(optional=false)
	@Enumerated(EnumType.STRING)
	private ShowType showType;
	
	@Column(name="LAST_GUIDE_UPDATE", nullable=true)
	@Basic(optional=true)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
	private Instant lastGuideUpdate;
	
	@Column(name="LAST_GUIDE_CHECK", nullable=true)
	@Basic(optional=true)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
	private Instant lastGuideCheck;
	
	@Column(name="ENABLED", nullable=false)
	@Basic(optional=false)	
	private boolean enabled;
	
	@Column(name="MIN_SIZE", nullable=false)
	@Basic(optional=false)
	private int minSize;
	
	@Column(name="MAX_SIZE", nullable=false)
	@Basic(optional=false)
	private int maxSize;
	
	@Version
	@Column(name="VERSION")
	@JsonIgnore
	private int version;

}
