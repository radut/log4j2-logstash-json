package org.apache.logging.log4j.core.layout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.jackson.LogStashLog4jJsonObjectMapper;

import java.util.*;

abstract class LogStashJacksonFactory extends JacksonFactory {

    static class JsonLog extends JacksonFactory.JSON {

        private final boolean locationInfo;
        private final boolean properties;

        public JsonLog(final boolean locationInfo, final boolean properties) {
            super(true, true);
            this.locationInfo = locationInfo;
            this.properties = properties;
        }

        @Override
        protected ObjectMapper newObjectMapper() {
            ObjectMapper objectMapper = new LogStashLog4jJsonObjectMapper();
            final SimpleFilterProvider filters = new SimpleFilterProvider();
            final Set<String> except = new HashSet<String>(2);
            if (!locationInfo) {
                except.add(this.getPropertNameForSource());
            }
            if (!properties) {
                except.add(this.getPropertNameForContextMap());
            }
            filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
            objectMapper.setFilterProvider(filters);
            return objectMapper;
        }

        ObjectWriter newWriter(final boolean compact) {
            return this.newObjectMapper().writer(compact ? this.newCompactPrinter() : this.newPrettyPrinter());
        }
    }
}
