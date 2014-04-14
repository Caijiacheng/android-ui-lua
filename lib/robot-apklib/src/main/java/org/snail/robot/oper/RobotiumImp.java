package org.snail.robot.oper;

import java.util.ArrayList;
import java.util.List;

import org.snail.robot.view.ViewWrapper;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;

public class RobotiumImp implements IOper<ViewWrapper>
{
	private Solo _solo;
	
	public RobotiumImp(Instrumentation instr, Activity act)
	{
		_solo = new Solo(instr, act);
	}
	
	@Override
	public List<ViewWrapper> getViews() 
	{
		ArrayList<View> views = _solo.getViews();
		List<ViewWrapper> iviews = new ArrayList<ViewWrapper>();
		for (int i = 0 ; i<views.size(); i ++)
		{
			iviews.add(new ViewWrapper(views.get(i)));
		}
		return iviews;
	}

	@Override
	public void click(float x, float y) 
	{
		_solo.clickOnScreen(x, y);
	}

	@Override
	public void click(ViewWrapper v) 
	{
		_solo.clickOnView(v.raw());
	}

	@Override
	public void sleep(int millsec) 
	{
		try {
			Thread.sleep(millsec);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getCurAct() 
	{
		return _solo.getCurrentActivity().getClass().getName();
	}

	@Override
	public boolean waitForAct(String name) 
	{
		return _solo.waitForActivity(name);
	}

	@Override
	public boolean waitForAct(String name, int timeout) 
	{
		return _solo.waitForActivity(name, timeout);
	}
}
