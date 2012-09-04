/**
 * Copyright (c) 2006-2009, NEPOMUK Consortium
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice, 
 *       this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the 
 * 	documentation and/or other materials provided with the distribution.
 *
 *     * Neither the name of the NEPOMUK Consortium nor the names of its 
 *       contributors may be used to endorse or promote products derived from 
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 **/
package org.ubimix.commons.templates.velocity;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import org.ubimix.commons.templates.ITemplateProcessor;
import org.ubimix.commons.templates.ITemplateProvider;
import org.ubimix.commons.templates.TemplateException;
import org.ubimix.commons.templates.TemplateProcessor;

/**
 * This is a Velocity-based ({@linkplain link
 * http://jakarta.apache.org/velocity/}) implementation of the
 * {@link ITemplateProcessor} interface.
 * 
 * @author kotelnikov
 */
public class VelocityTemplateProcessor extends TemplateProcessor {

    protected VelocityEngine fEngine;

    /**
     * @param templateProvider
     * @param properties
     * @param logSystem
     * @throws TemplateException
     */
    public VelocityTemplateProcessor(
        ITemplateProvider templateProvider,
        Properties properties) throws TemplateException {
        super(templateProvider, properties);
        fEngine = new VelocityEngine();
        for (Iterator<?> iterator = properties.keySet().iterator(); iterator
            .hasNext();) {
            String key = (String) iterator.next();
            fEngine.setProperty(key, properties.get(key));
        }

        fEngine.setProperty(
            RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
            "org.apache.velocity.runtime.log.CommonsLogLogChute");
        fEngine.setProperty("resource.loader", "class");
        fEngine.setProperty(
            "class.resource.loader.class",
            SimpleResourceLoader.class.getName());
        fEngine
            .setProperty(ITemplateProvider.class.getName(), templateProvider);
        try {
            fEngine.init();
        } catch (Exception e) {
            throw new TemplateException(e);
        }

    }

    /**
     * @see com.cogniumsystems.commons.templates.ITemplateProcessor#close()
     */
    public void close() {
        fEngine = null;
    }

    /**
     * @see com.cogniumsystems.commons.templates.ITemplateProcessor#render(java.lang.String,
     *      java.util.Map, java.io.Writer)
     */
    public void render(
        String templateName,
        Map<String, Object> params,
        Writer writer) throws TemplateException {
        try {
            Template template = fEngine.getTemplate(templateName, "UTF-8");
            VelocityContext context = new VelocityContext();
            for (Iterator<Map.Entry<String, Object>> iterator = params
                .entrySet()
                .iterator(); iterator.hasNext();) {
                Map.Entry<String, Object> entry = iterator.next();
                context.put(entry.getKey(), entry.getValue());
            }
            template.merge(context, writer);
        } catch (Exception e) {
            throw new TemplateException(e);
        }
    }

}
