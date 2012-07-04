/*******************************************************************************
 * Copyright (c) 2005,2006 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.webreformatter.commons.templates.velocity;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import org.webreformatter.commons.templates.ITemplateProvider;

/**
 * This resource loader loads templates used by velocity engines.
 * 
 * @author kotelnikov
 */
public class SimpleResourceLoader extends ResourceLoader {

    protected ITemplateProvider fProvider;

    /**
     * 
     *
     */
    public SimpleResourceLoader() {
        super();
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#commonInit(org.apache.velocity.runtime.RuntimeServices,
     *      org.apache.commons.collections.ExtendedProperties)
     */
    @Override
    public void commonInit(RuntimeServices rs, ExtendedProperties configuration) {
        super.commonInit(rs, configuration);
        fProvider = (ITemplateProvider) rs.getProperty(ITemplateProvider.class
            .getName());
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getLastModified(org.apache.velocity.runtime.resource.Resource)
     */
    @Override
    public long getLastModified(Resource resource) {
        return System.currentTimeMillis();
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getResourceStream(java.lang.String)
     */
    @Override
    public InputStream getResourceStream(String source)
        throws ResourceNotFoundException {
        try {
            return fProvider.getTemplate(source);
        } catch (IOException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#init(org.apache.commons.collections.ExtendedProperties)
     */
    @Override
    public void init(ExtendedProperties configuration) {
        //
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#isSourceModified(org.apache.velocity.runtime.resource.Resource)
     */
    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

}
