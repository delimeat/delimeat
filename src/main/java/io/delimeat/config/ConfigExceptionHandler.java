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
package io.delimeat.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.delimeat.config.exception.ConfigConcurrencyException;
import io.delimeat.config.exception.ConfigException;
import io.delimeat.util.ApiError;

@ControllerAdvice
public class ConfigExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ ConfigException.class })
    public ResponseEntity<Object> handleConfigException(final ConfigException ex, final WebRequest request) {
		
        final ApiError apiError = ApiError.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value()).error(ex.getMessage()).build();
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        
    }
	
	@ExceptionHandler({ ConfigConcurrencyException.class })
    public ResponseEntity<Object> handleConfigConcurrencyException(final ConfigConcurrencyException ex, final WebRequest request) {
		
        final ApiError apiError = ApiError.builder().status(HttpStatus.CONFLICT.value()).error(ex.getMessage()).build();
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        
    }
}
