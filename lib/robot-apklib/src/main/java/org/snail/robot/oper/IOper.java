package org.snail.robot.oper;

import java.util.List;

import org.snail.robot.view.IView;

public interface IOper <T extends IView>  
{
	List<T> getViews();
	void click(T v);
	void click(float x, float y);
	void sleep(int millsec);
	
	String getCurAct();
	boolean waitForAct(String name, int timeout);
	boolean waitForAct(String name);
	
}
