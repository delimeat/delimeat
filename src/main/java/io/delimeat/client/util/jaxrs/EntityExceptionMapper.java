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
package io.delimeat.client.util.jaxrs;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import io.delimeat.client.domain.ErrorDto;
import io.delimeat.guide.exception.GuideConcurrencyException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;

public class EntityExceptionMapper implements ExceptionMapper<GuideException>{

    /* (non-Javadoc)
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse(GuideException exception) {
      int status = 500;
      if(exception instanceof GuideNotFoundException){
			status = 404;
      }else if(exception instanceof GuideConcurrencyException){
    	  status = 409;
      }
      
      return Response.status(status)
        					.entity(new ErrorDto(status,exception.getMessage()))
        					.type(MediaType.APPLICATION_JSON_TYPE)
        					.build();
    }

}
