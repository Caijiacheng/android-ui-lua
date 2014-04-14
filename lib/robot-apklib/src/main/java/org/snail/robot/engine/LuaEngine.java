package org.snail.robot.engine;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaJavaAPI;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LuaEngine implements ScriptEngine 
{
	Logger LOG;

	LuaState _ls;
	volatile boolean _isRunning; 

	public static class LuaScriptException extends ScriptException
	{
		private static final long serialVersionUID = 1L;
		public LuaScriptException() {
		}

		public LuaScriptException(String detailMessage) {
			super(detailMessage);
		}

		public LuaScriptException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}

		public LuaScriptException(Throwable throwable) {
			super(throwable);
		}
	}

	public LuaEngine(String libpath) 
	{
		LuaState.loadLuaLibrary(libpath);
		LOG = LoggerFactory.getLogger(getClass());
	}

	@Override
	public void open() 
	{
		if (_ls != null)
			return;
		_ls = LuaStateFactory.newLuaState();
		_ls.openLibs();
	}

	@Override
	public void close() 
	{
		_ls.close();
		_ls = null;
	}

	private LuaScriptException _lastScriptErr;

	public void onErrorHandle(String msg)
	{
		LOG.error("Lua Stack: {}",msg);
		_lastScriptErr = new LuaScriptException(msg);
	}


	JavaFunction _errHandle;
	static final String GLOBAL_ERROR_HANDLE = "_G_ERR_HANDLE_";
	static final String GLOBAL_ERROR_INVOKE = "_G_ERR_INVOKE_";

	protected void initErrHandle() throws LuaException
	{
		if (_errHandle == null)
		{
			_errHandle = new JavaFunction(_ls) {

				@Override
				public int execute() throws LuaException 
				{

					String msg = getParam(2).getString();
					if (msg == null)
					{
						throw new LuaException("the error msg is not string");
					}
					onErrorHandle(msg);
					return 0;
				}
			};
			_errHandle.register(GLOBAL_ERROR_INVOKE);

			String[] s = new String[]{
					"function " + GLOBAL_ERROR_HANDLE + "(msg)",
					"	" + GLOBAL_ERROR_INVOKE + "(debug.traceback(msg , 2))",
					"end"
			};

			StringBuilder sb = new StringBuilder();
			for (String ss : s)
			{
				sb.append(ss + "\n");
			}

			if ( _ls.LloadString(sb.toString()) != 0 || _ls.pcall(0, 0, 0) != 0)
			{
				String err = _ls.toString(-1);
				_ls.pop(1);
				throw new LuaException("init ErrorHandle: " + err);
			}			
		}
	}


	@Override
	public int run(String executablePath, String scriptfilename,
			Map<String, Object> args) throws ScriptException 
			{
		_isRunning = true;

		try
		{
			initErrHandle();

			String script = executablePath + "/" +  scriptfilename;

			if (!new File(script).isFile())
			{
				throw new LuaScriptException("Script: " + script + " is not exist!");
			}

			if (_ls.LloadFile(script) != 0)
			{
				String err = _ls.toString(-1);
				_ls.pop(1);
				throw new LuaScriptException(String.format("LoadScript %s failed! Exception: %s", script, err));
			}else
			{
				_lastScriptErr = null;
				_ls.getGlobal(GLOBAL_ERROR_HANDLE);
				_ls.insert(-2);
				if (_ls.pcall(0, 0, -2) != 0)
				{				
					String err = _ls.toString(-1);
					_ls.pop(1);
					LOG.error("ExecScript {} failed! {}", script, err);
				}
				if (_lastScriptErr != null)
				{
					throw _lastScriptErr;
				}
			}

		}catch(Throwable e)
		{
			if (!(e instanceof ScriptException))
			{
				throw new ScriptException(e);
			}else
			{
				throw (ScriptException)e;
			}

		}
		finally
		{
			_isRunning = false;
		}

		return 0;
			}

	@Override
	public void cancel() 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRunning() 
	{
		return _isRunning;
	}

	
	@SuppressWarnings("unused")
	private LuaObject listToTable(Collection<?> arrays) throws LuaException
	{
		int cnt = 0;
		_ls.newTable();
		LuaObject table = _ls.getLuaObject(-1);
		_ls.pop(1);
		for (Object obj : arrays)
		{
			table.push();
			_ls.pushNumber(++cnt);
			_ls.pushObjectValue(obj);
			_ls.setTable(-3);
			_ls.pop(1);
			
		}
		return table;
	}
	
	@Override
	public void exportMapping(final String mapping, final Object obj, final Method method) throws ScriptException
	{

		try {
			new JavaFunction(_ls) {
				@Override
				public int execute() throws LuaException 
				{

					Class<?>[] parameters = method.getParameterTypes();

					if (parameters.length != L.getTop() - 1)
					{
						throw new LuaException(
								"NotMatch Paramter Length. method:" + 
										getParam(0).toString() + " expect: aramlen:" + parameters.length + " actual:" + (L.getTop()-1));
					}

					Object[] objs = new Object[parameters.length];

					for (int i = 0; i <parameters.length; i++)
					{
						for (Class<?> paramtype : parameters)
						{
							objs[i] = LuaJavaAPI.compareTypes(L, paramtype, i+2);
						}
					}

					try {
						//XXX: i don't know where to do getviews
//						if (mapping.equals("getviews"))
//						{
//							Object retobj = method.invoke(obj, objs);
//							if (retobj instanceof Collection<?>)
//							{
//								L.pushObjectValue(listToTable((Collection<?>) retobj));
//								return 1;
//							}
//						}
						L.pushObjectValue(method.invoke(obj, objs)); 
					} catch (Exception e) {
						throw new LuaException(e);
					}
					return 1;
				}
			}.register(mapping);
		} catch (LuaException e) {
			throw new ScriptException(e);
		}

	}

	@Override
	public void exportMapping(String mapping, Object obj)
			throws ScriptException 
	{
		try
		{
			_ls.pushObjectValue(obj);
			_ls.setGlobal(mapping);
		}
		catch (LuaException e)
		{
			throw new ScriptException(e);
		}
	}



}
