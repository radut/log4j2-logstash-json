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
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.logging.log4j.core.LogStashLogEvent;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

@JsonSerialize(converter = LogStashLogEvent.LogEventToLogStashLogEventConverter.class)
@JsonRootName(XmlConstants.ELT_EVENT)
@JsonFilter("org.apache.logging.log4j.core.impl.Log4jLogEvent")
@JsonPropertyOrder(
        {"@version", "timestamp", "timeMillis", "threadName", "level", "loggerName", "marker", "message", "thrown",//
                XmlConstants.ELT_CONTEXT_MAP, JsonConstants.ELT_CONTEXT_STACK, "loggerFQCN", "Source", "endOfBatch"})
abstract class LogStashLogEventMixIn extends LogEventJsonMixIn {

    private static final long serialVersionUID = 1L;

    @JsonProperty("@timestamp")
    public abstract String getTimestamp();

    @JsonProperty("@version")
    public abstract String getVersion();

    @JsonIgnore
    public abstract String getLoggerFqcn();

    @JsonIgnore
    public abstract long getNanoTime();

    @JsonIgnore
    public abstract boolean isEndOfBatch();

    @JsonProperty("mdc")
    public abstract ReadOnlyStringMap getContextData();


}
