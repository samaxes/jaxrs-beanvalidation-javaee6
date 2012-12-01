/*
 * $Id$
 *
 * JAX-RS Bean Validation I18N
 * https://github.com/samaxes/jaxrs-beanvalidation
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
package com.samaxes.rs.beanvalidation;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.core.HttpHeaders;

import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.hibernate.validator.method.MethodValidator;
import org.jboss.resteasy.spi.validation.DoNotValidateRequest;
import org.jboss.resteasy.spi.validation.ValidateRequest;
import org.jboss.resteasy.spi.validation.ValidatorAdapter;
import org.jboss.resteasy.util.FindAnnotation;

public class RESTValidatorAdapter implements ValidatorAdapter {

    private final Validator validator;

    private final MethodValidator methodValidator;

    private final LocaleAwareMessageInterpolator interpolator = new LocaleAwareMessageInterpolator();

    public RESTValidatorAdapter() {
        Configuration<?> configuration = Validation.byDefaultProvider().configure();
        this.validator = configuration.messageInterpolator(interpolator).buildValidatorFactory().getValidator();
        this.methodValidator = validator.unwrap(MethodValidator.class);
    }

    @Override
    public void applyValidation(Object resource, Method invokedMethod, Object[] args) {
        // For the i8n to work, the first parameter of the method being validated must be a HttpHeaders
        if ((args != null) && (args[0] instanceof HttpHeaders)) {
            HttpHeaders headers = (HttpHeaders) args[0];
            List<Locale> acceptedLanguages = headers.getAcceptableLanguages();
            if ((acceptedLanguages != null) && (!acceptedLanguages.isEmpty())) {
                interpolator.setDefaultLocale(acceptedLanguages.get(0));
            }
        }

        ValidateRequest resourceValidateRequest = FindAnnotation.findAnnotation(invokedMethod.getDeclaringClass()
                .getAnnotations(), ValidateRequest.class);

        if (resourceValidateRequest != null) {
            Set<ConstraintViolation<?>> constraintViolations = new HashSet<ConstraintViolation<?>>(validator.validate(
                    resource, resourceValidateRequest.groups()));

            if (constraintViolations.size() > 0) {
                throw new ConstraintViolationException(constraintViolations);
            }
        }

        ValidateRequest methodValidateRequest = FindAnnotation.findAnnotation(invokedMethod.getAnnotations(),
                ValidateRequest.class);
        DoNotValidateRequest doNotValidateRequest = FindAnnotation.findAnnotation(invokedMethod.getAnnotations(),
                DoNotValidateRequest.class);

        if ((resourceValidateRequest != null || methodValidateRequest != null) && doNotValidateRequest == null) {
            Set<Class<?>> set = new HashSet<Class<?>>();
            if (resourceValidateRequest != null) {
                for (Class<?> group : resourceValidateRequest.groups()) {
                    set.add(group);
                }
            }

            if (methodValidateRequest != null) {
                for (Class<?> group : methodValidateRequest.groups()) {
                    set.add(group);
                }
            }

            Set<MethodConstraintViolation<?>> constraintViolations = new HashSet<MethodConstraintViolation<?>>(
                    methodValidator.validateAllParameters(resource, invokedMethod, args,
                            set.toArray(new Class<?>[set.size()])));

            if (constraintViolations.size() > 0) {
                throw new MethodConstraintViolationException(constraintViolations);
            }
        }
    }
}
