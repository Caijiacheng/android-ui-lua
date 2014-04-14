package org.snail.robot.utils;

import java.io.ByteArrayInputStream;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;


public class LoggerUtil 
{
	static private boolean _init = false;
	
	private LoggerUtil() 
	{
		
	}
	
	static final String LOGBACK_XML =
			    "<configuration debug='false'>" +
 				"<appender name='LOGCAT' class='ch.qos.logback.classic.android.LogcatAppender' >" +
 				"	<encoder>" +
 				"		<pattern> >>>[%thread]-%msg%n</pattern>" +
 				"	</encoder>" +
 				"</appender>" +	
			    
			    "  <root level='DEBUG'>" +
			    "    <appender-ref ref='LOGCAT' />" +
			    "  </root>" +
			    "</configuration>"
			    ;
	
	static public void init() 
	{
		if (!_init){
			LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
	        try {
	            // load a specific logback.xml
	            JoranConfigurator configurator = new JoranConfigurator();
	            configurator.setContext(lc);
	            lc.reset(); // override default configuration
	            
	            // hard-coded config
	            configurator.doConfigure(
	                new ByteArrayInputStream(LOGBACK_XML.getBytes())
	            );
	            
	        } catch (JoranException je) {
	            // StatusPrinter will handle this
	        }
	        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
	        _init = true;
		}
        
    }

	
	
	
}
