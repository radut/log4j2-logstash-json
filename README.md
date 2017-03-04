## log4j2-logstash-json
#### tested with 
* logstash : 5
* log4j2 : 2.7


fork from [github.com/majikthys/log4j2-logstash-jsonevent-layout](https://github.com/majikthys/log4j2-logstash-jsonevent-layout)

#### logstash conf

```
input {
	tcp {
		port => 5000
		codec => json
	}

}

## Add your filters / logstash plugins configuration here
filter{
}

output {
	elasticsearch {
		hosts => "elasticsearch:9200"
	}
	stdout {
		codec => rubydebug
	}
}
```



#### log4j2.xml

````
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Properties>
        <Property name="PID">????</Property>
        <Property name="LOG_EXCEPTION_CONVERSION_WORD">%xwEx</Property>
        <Property name="LOG_LEVEL_PATTERN">%5p</Property>
        <Property name="LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} ${LOG_LEVEL_PATTERN} ${sys:PID} --- [%t] %-80.80c : %m%n${sys:LOG_EXCEPTION_CONVERSION_WORD}</Property>
    </Properties>
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT" follow="true">
            <PatternLayout pattern="${LOG_PATTERN}" />
        </Console>

        <Socket name="LogStashSocket" host="localhost" port="5000" protocol="tcp">
            <LogStashJSONLayout>
                <KeyValuePair key="java_opts" value="${sys:JAVA_OPTS}" />
                <KeyValuePair key="environment_user" value="${env:USER}" />
            </LogStashJSONLayout>
        </Socket>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console" />
            <AppenderRef ref="LogStashSocket" />
        </Root>
    </Loggers>
</Configuration>
````

