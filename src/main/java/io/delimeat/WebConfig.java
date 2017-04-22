package io.delimeat;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class WebConfig  {

	  @Bean
	  public Filter shallowEtagHeaderFilter() {
	    return new ShallowEtagHeaderFilter();
	  }
	  /*
	  @Bean
	  public Jackson2ObjectMapperBuilder objectMapperBuilder(){
		  return new Jackson2ObjectMapperBuilder()
				  		.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				  		.featuresToDisable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
				  		.featuresToDisable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
	  }
	  */
	
	
}
