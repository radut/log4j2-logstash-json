package org.apache.logging.log4j.core;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogStashLogEvent implements LogEvent {

    static final String LOG_STASH_ISO8601_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    static final DateFormat iso8601DateFormat = new SimpleDateFormat(LOG_STASH_ISO8601_TIMESTAMP_FORMAT);

    private LogEvent wrappedLogEvent;

    public LogStashLogEvent(LogEvent wrappedLogEvent) {
        this.wrappedLogEvent = wrappedLogEvent;
    }

    public String getVersion() {
        return "5";//LOGSTASH VERSION
    }

    public String getTimestamp() {
        return iso8601DateFormat.format(new Date(this.getTimeMillis()));
    }

    public Map<String, String> getContextMap() {
        return wrappedLogEvent.getContextMap();
    }

    public ReadOnlyStringMap getContextData() {
        return wrappedLogEvent.getContextData();
    }

    public ThreadContext.ContextStack getContextStack() {
        return wrappedLogEvent.getContextStack();
    }

    public String getLoggerFqcn() {
        return wrappedLogEvent.getLoggerFqcn();
    }

    public Level getLevel() {
        return wrappedLogEvent.getLevel();
    }

    public String getLoggerName() {
        return wrappedLogEvent.getLoggerName();
    }

    public Marker getMarker() {
        return wrappedLogEvent.getMarker();
    }

    public Message getMessage() {
        return wrappedLogEvent.getMessage();
    }

    public long getTimeMillis() {
        return wrappedLogEvent.getTimeMillis();
    }

    public StackTraceElement getSource() {
        return wrappedLogEvent.getSource();
    }

    public String getThreadName() {
        return wrappedLogEvent.getThreadName();
    }

    public long getThreadId() {
        return wrappedLogEvent.getThreadId();
    }

    public int getThreadPriority() {
        return wrappedLogEvent.getThreadPriority();
    }

    public Throwable getThrown() {
        return wrappedLogEvent.getThrown();
    }

    public ThrowableProxy getThrownProxy() {
        return wrappedLogEvent.getThrownProxy();
    }


    public boolean isEndOfBatch() {
        return wrappedLogEvent.isEndOfBatch();
    }


    public boolean isIncludeLocation() {
        return wrappedLogEvent.isIncludeLocation();
    }

    public void setEndOfBatch(boolean endOfBatch) {
        wrappedLogEvent.setEndOfBatch(endOfBatch);

    }

    public void setIncludeLocation(boolean locationRequired) {
        wrappedLogEvent.setIncludeLocation(locationRequired);
    }

    public long getNanoTime() {
        return wrappedLogEvent.getNanoTime();
    }

    public static class LogEventToLogStashLogEventConverter extends StdConverter<LogEvent, LogStashLogEvent> {

        @Override
        public LogStashLogEvent convert(LogEvent value) {

            return new LogStashLogEvent(value);
        }
    }
}
