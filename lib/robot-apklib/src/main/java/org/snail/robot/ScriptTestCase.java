package org.snail.robot;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snail.robot.engine.LuaEngine;
import org.snail.robot.engine.ScriptEngine;
import org.snail.robot.engine.ScriptException;
import org.snail.robot.oper.IOper;
import org.snail.robot.oper.RobotiumImp;

import android.os.Bundle;
import android.test.InstrumentationTestRunner;

/*
 * 
 * 
 * run the test:
 * 
 * if : instrPkg = com.qihoo.robot.lib.app.instr
 * 		
 * do :
 * adb push xx.lua /sdcard/robot/script/test.lua
 * adb shell:
 * am instrument -w -e class com.qihoo.robot.ScriptTestCase#testScriptExec \ 
 * -e SCRIPT_PATH /sdcard/robot/script -e SCRIPT_FILE test.lua \ 
 * com.qihoo.robot.lib.app.instr/android.test.InstrumentationTestRunner
 * 
 */

public class ScriptTestCase extends InstrActivityTestCase 
{
	
	public final static String KEY_SCRIPT_EXEC_PATH = "SCRIPT_PATH";
	public final static String KEY_SCRIPT_EXEC_FILE = "SCRIPT_FILE"; 
	
	protected Logger LOG;
	protected Bundle mBundle;

	protected ScriptEngine mScriptEngine;
	protected IOper<?> mViewOper;
	
	public ScriptTestCase()
	{
		
	}
	
	public Bundle getBundle()
	{
		if (mBundle != null)
		{
			return mBundle;
					
		}
		
		if ( getInstrumentation() instanceof InstrumentationTestRunner)
		{
			
			String method = "getArguments";
			
			if (android.os.Build.VERSION.SDK_INT <= 17)
			{
				method = "getBundle";
			}
			InstrumentationTestRunner runner = (InstrumentationTestRunner)getInstrumentation();
			Method m;
			try {
				m = runner.getClass().getMethod(method, (Class[]) null);
				mBundle =  (Bundle) m.invoke(runner,new Object[]{});
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return mBundle;
	}

	public String getExecPath()
	{
		return getBundle().getString(KEY_SCRIPT_EXEC_PATH);
	}
	
	public String getExecFile()
	{
		return getBundle().getString(KEY_SCRIPT_EXEC_FILE);
	}
	
	
	public Logger getLogger()
	{
		if (LOG == null)
		{
			LOG = LoggerFactory.getLogger(this.getClass());
		}
		return LOG;
	}
	
	@Override
	protected void setUp() throws Exception 
	{
		getLogger().error("setup: {}", this.getClass());;
		super.setUp();
		mViewOper = 
				new RobotiumImp(getInstrumentation(), null);
		getScriptEngine();
	}
	
	public ScriptEngine getScriptEngine()
	{
		if (mScriptEngine == null)
		{
			mScriptEngine = 
					new LuaEngine(
							getInstrumentation().getContext().getApplicationInfo().dataDir);
			mScriptEngine.open();
		}
		return mScriptEngine;
	}
	
	public void interrupted()
	{
		getScriptEngine().cancel();
	}
	
	//
	public void testScriptExec() throws ScriptException, NoSuchMethodException 
	{
		getScriptEngine().exportMapping("launch", this, 
				this.getClass().getMethod("setLaunchActivity",  new Class[]{String.class}));
		getScriptEngine().exportMapping(
				"getviews", mViewOper, IOper.class.getMethod("getViews"));
		getScriptEngine().exportMapping(
				"log", LOG);		
		getScriptEngine().exportMapping("oper", mViewOper);
		getLogger().error("testScriptExec: {}/{}", getExecPath(), getExecFile());;
		getScriptEngine().run(getExecPath(), getExecFile(), null);
	}
	
	
	@Override
	protected void tearDown() throws Exception 
	{
		getLogger().error("tearDown: {}", this.getClass());;
		super.tearDown();
		getScriptEngine().close();
		mScriptEngine = null;
	}
	
}
