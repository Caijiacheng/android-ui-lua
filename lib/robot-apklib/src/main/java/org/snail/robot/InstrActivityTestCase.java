package org.snail.robot;

import java.lang.reflect.Method;

import org.snail.robot.utils.LoggerUtil;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityTestCase;
import android.test.UiThreadTest;

public class InstrActivityTestCase extends ActivityTestCase
{
    Class<? extends Activity> mActivityClass;
    boolean mInitialTouchMode = false;
    Intent mActivityIntent = null;
    boolean mInitialActivity = false;
    
    public InstrActivityTestCase()
    {
    	LoggerUtil.init();
    }
    
    
    @SuppressWarnings("unchecked")
	public void setLaunchActivity(String cls)
    {
    	
    	if (mInitialActivity)
    	{
    		throw new RuntimeException("The Activity had already initialed");
    	}
    	
    	try {
			mActivityClass = (Class<? extends Activity>) Class.forName(cls);
			getActivity();
		} catch (ClassNotFoundException e) 
		{
			throw new RuntimeException(e);
		}
    	
    }
    
    public void setLaunchActivity(String cls, Intent intent)
    {
    	//TODO: imp it to support
    	throw new UnsupportedOperationException();
    }
    
    @Override
    public Activity getActivity() {
        Activity a = super.getActivity();
        if (a == null) {
        	
        	if (mActivityClass == null)
        	{
        		throw new RuntimeException("Please invoke setTargetAct(Class act) first");
        	}
        	
            // set initial touch mode
            getInstrumentation().setInTouchMode(mInitialTouchMode);
            final String targetPackage = getInstrumentation().getTargetContext().getPackageName();
            // inject custom intent, if provided
            if (mActivityIntent == null) {
                a = launchActivity(targetPackage, mActivityClass, null);
            } else {
                a = launchActivityWithIntent(targetPackage, mActivityClass, mActivityIntent);
            }
            setActivity(a);
            mInitialActivity = true;
        }
        return a;
    }

    /**
     * Call this method before the first call to {@link #getActivity} to inject a customized Intent
     * into the Activity under test.
     * 
     * <p>If you do not call this, the default intent will be provided.  If you call this after
     * your Activity has been started, it will have no effect.
     * 
     * <p><b>NOTE:</b> Activities under test may not be started from within the UI thread.
     * If your test method is annotated with {@link android.test.UiThreadTest}, then you must call
     * {@link #setActivityIntent(Intent)} from {@link #setUp()}.
     *
     * <p>The default Intent (if this method is not called) is:
     *  action = {@link Intent#ACTION_MAIN}
     *  flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
     * All other fields are null or empty.
     *
     * @param i The Intent to start the Activity with, or null to reset to the default Intent.
     */
    public void setActivityIntent(Intent i) {
        mActivityIntent = i;
    }
    
    /**
     * Call this method before the first call to {@link #getActivity} to set the initial touch
     * mode for the Activity under test.
     * 
     * <p>If you do not call this, the touch mode will be false.  If you call this after
     * your Activity has been started, it will have no effect.
     * 
     * <p><b>NOTE:</b> Activities under test may not be started from within the UI thread.
     * If your test method is annotated with {@link android.test.UiThreadTest}, then you must call
     * {@link #setActivityInitialTouchMode(boolean)} from {@link #setUp()}.
     * 
     * @param initialTouchMode true if the Activity should be placed into "touch mode" when started
     */
    public void setActivityInitialTouchMode(boolean initialTouchMode) {
        mInitialTouchMode = initialTouchMode;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        mInitialTouchMode = false;
        mActivityIntent = null;
    }

    @Override
    protected void tearDown() throws Exception {
        // Finish the Activity off (unless was never launched anyway)
        Activity a = super.getActivity();
        if (a != null) {
            a.finish();
            setActivity(null);
        }
        mInitialActivity = false;
        
        // Scrub out members - protects against memory leaks in the case where someone 
        // creates a non-static inner class (thus referencing the test case) and gives it to
        // someone else to hold onto
        scrubClass(InstrActivityTestCase.class);

        super.tearDown();
    }

    /**
     * Runs the current unit test. If the unit test is annotated with
     * {@link android.test.UiThreadTest}, force the Activity to be created before switching to
     * the UI thread.
     */
    @Override
    protected void runTest() throws Throwable {
        try {
            Method method = getClass().getMethod(getName(), (Class[]) null);
            if (method.isAnnotationPresent(UiThreadTest.class)) {
                getActivity();
            }
        } catch (Exception e) {
            // eat the exception here; super.runTest() will catch it again and handle it properly
        }
        super.runTest();
    }
	
	
}