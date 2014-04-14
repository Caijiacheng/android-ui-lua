package org.snail.robot.engine;

import java.lang.reflect.Method;
import java.util.Map;

public interface ScriptEngine 
{
	void open();
	void close();

	int run(String executablePath, 
			String scriptfilename, Map<String, Object> args) throws ScriptException;
	
	void cancel();
	boolean isRunning();
	
	
	void exportMapping(String mapping, Object obj, Method method) throws ScriptException;
	void exportMapping(String mapping, Object obj) throws ScriptException;
	
}
