/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.layout;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.core.util.StringBuilderWriter;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.*;


@Plugin(name = "LogStashJSONLayout", category = "Core", elementType = "layout", printObject = true)
public class LogStashJSONLayout extends AbstractJacksonLayout {

    private static final String CONTENT_TYPE = "application/json";

    private Map<String, String> additionalLogAttributes = new HashMap<String, String>();

    protected LogStashJSONLayout(final boolean locationInfo, final boolean properties, final boolean compact, boolean eventEol, final Charset charset, final Map<String, String> additionalLogAttributes) {
        super(null, new LogStashJacksonFactory.JsonLog(locationInfo, properties).newWriter(compact), charset, compact, false, eventEol, null, null);
        this.additionalLogAttributes = additionalLogAttributes;

    }


    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("version", "2.0");
        return result;
    }

    @Override
    /**
     * @return The content type.
     */ public String getContentType() {
        return CONTENT_TYPE + "; charset=" + this.getCharset();
    }

    /**
     * Creates a JsonLog Layout.
     *
     * @param locationInfo If "true", includes the location information in the generated JsonLog.
     * @param properties   If "true", includes the thread context in the generated JsonLog.
     * @param compact      If "true", does not use end-of-lines and indentation, defaults to "false".
     * @param eventEol     If "true", forces an EOL after each log event (even if compact is "true"), defaults to
     *                     "true". This allows one even per line, even in compact mode.
     * @param charset      The character set to use, if {@code null}, uses "UTF-8".
     * @return A JsonLog Layout.
     */
    @PluginFactory
    public static AbstractJacksonLayout createLayout(
            // @formatter:off
            @PluginAttribute(value = "locationInfo", defaultBoolean = true) final boolean locationInfo,
            @PluginAttribute(value = "properties", defaultBoolean = true) final boolean properties,
            @PluginAttribute(value = "compact", defaultBoolean = true) final boolean compact,
            @PluginAttribute(value = "eventEol", defaultBoolean = true) final boolean eventEol,
            @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset,
            @PluginElement("Pairs") final KeyValuePair[] pairs
            // @formatter:on
    ) {


        //Unpacke the pairs list
        final Map<String, String> additionalLogAttributes = new HashMap<String, String>();
        if (pairs != null && pairs.length > 0) {
            for (final KeyValuePair pair : pairs) {
                final String key = pair.getKey();
                if (key == null) {
                    AbstractLayout.LOGGER.error("A null key is not valid in MapFilter");
                    continue;
                }
                final String value = pair.getValue();
                if (value == null) {
                    AbstractLayout.LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
                    continue;
                }
                if (additionalLogAttributes.containsKey(key)) {
                    AbstractLayout.LOGGER.error("Duplicate entry for key: {} is forbidden!", key);
                }
                additionalLogAttributes.put(key, value);
            }

        }


        return new LogStashJSONLayout(locationInfo, properties, compact, eventEol, charset, additionalLogAttributes);

    }

    public static AbstractJacksonLayout createDefaultLayout() {
        return new LogStashJSONLayout(true, true, true, true, Charset.forName("UTF-8"), new HashMap<String, String>());
    }


    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilderWriter writer = new StringBuilderWriter();
        try {
            toSerializable(event, writer);
            return writer.toString();
        } catch (final IOException e) {
            // Should this be an ISE or IAE?
            LOGGER.error(e);
            return Strings.EMPTY;
        }
    }

    private static LogEvent convertMutableToLog4jEvent(final LogEvent event) {
        // TODO Jackson-based layouts have certain filters set up for Log4jLogEvent.
        // TODO Need to set up the same filters for MutableLogEvent but don't know how...
        // This is a workaround.
        if (event instanceof MutableLogEvent) {
           return event;
        } else {
            MutableLogEvent mutableLogEvent = new MutableLogEvent();
            mutableLogEvent.initFrom(event);
            return mutableLogEvent;
        }
    }

    public void toSerializable(final LogEvent event, final Writer writer) throws JsonGenerationException, JsonMappingException, IOException {
        MutableLogEvent mutableEvent = (MutableLogEvent) convertMutableToLog4jEvent(event);
        SortedArrayStringMap map = new SortedArrayStringMap();
        for (Map.Entry<String, String> entry : mutableEvent.getContextMap().entrySet()) {
            map.putValue(entry.getKey(),entry.getValue());
        }
        for (Map.Entry<String, String> entry : additionalLogAttributes.entrySet()) {
            map.putValue(entry.getKey(),entry.getValue());
        }
        mutableEvent.setContextData(map);
        objectWriter.writeValue(writer, mutableEvent);
        writer.write(eol);
        markEvent();
    }


}
