package org.apache.logging.log4j.core.jackson;

import org.apache.logging.log4j.core.LogEvent;

public class LogStashLog4jJsonModule extends Log4jJsonModule {

    private static final long serialVersionUID = 1L;

    LogStashLog4jJsonModule() {
        super(true,true);
    }


    @Override
    public void setupModule(final SetupContext context) {
        // Calling super is a MUST!
        super.setupModule(context);

        //OVERRIDE LogEvent.class mixin with our custom one.
        context.setMixInAnnotations(LogEvent.class, LogStashLogEventMixIn.class);
    }
}
