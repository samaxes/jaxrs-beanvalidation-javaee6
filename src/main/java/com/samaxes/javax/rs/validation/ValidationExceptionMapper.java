/*
 * $Id$
 *
 * JAX-RS Bean Validation I18N
 * https://github.com/samaxes/jaxrs-beanvalidation-javaee6
 *
 * Copyright (c) 2012 samaxes.com
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
package com.samaxes.javax.rs.validation;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<MethodConstraintViolationException> {

    @Override
    public Response toResponse(MethodConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<String, String>();
        for (MethodConstraintViolation<?> methodConstraintViolation : ex.getConstraintViolations()) {
            errors.put(methodConstraintViolation.getParameterName(), methodConstraintViolation.getMessage());
        }
        return Response.status(Status.PRECONDITION_FAILED).entity(errors).build();
    }
}
