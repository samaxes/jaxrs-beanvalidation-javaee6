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

import java.util.Locale;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;

public class LocaleAwareMessageInterpolator extends ResourceBundleMessageInterpolator {

    private Locale defaultLocale = Locale.getDefault();

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context) {
        return interpolate(messageTemplate, context, defaultLocale);
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context, final Locale locale) {
        return super.interpolate(messageTemplate, context, locale);
    }
}
