package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogStashLog4jJsonObjectMapper extends ObjectMapper {

    public LogStashLog4jJsonObjectMapper() {
        this.registerModule(new LogStashLog4jJsonModule());
        this.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

}
