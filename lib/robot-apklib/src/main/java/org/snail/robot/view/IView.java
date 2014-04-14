package org.snail.robot.view;

public interface IView 
{
	float height();
	float width();
	
	float left();
	float top();
	float right();
	float bottom();
	
	
	
	boolean canSee();
	boolean canClick();
	boolean canType();

	
	String text();
	String type();
}
