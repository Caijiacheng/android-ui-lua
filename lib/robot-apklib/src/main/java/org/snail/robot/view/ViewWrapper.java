package org.snail.robot.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class ViewWrapper implements IViewGroup
{
	private View mRaw;
	public ViewWrapper(View orige)
	{
		mRaw= orige;
	}
	@Override
	public float height() 
	{
		return mRaw.getHeight();
	}
	@Override
	public float width() 
	{
		return mRaw.getWidth();
	}
	@Override
	public float left() 
	{
		return mRaw.getX();
	}
	@Override
	public float top() 
	{
		return mRaw.getTop();
	}
	@Override
	public float right() 
	{		
		return mRaw.getRight();
	}
	@Override
	public float bottom() {
		return mRaw.getBottom();
	}
	@Override
	public boolean canSee() 
	{
		return mRaw.isShown();
	}
	@Override
	public boolean canClick() 
	{
		return mRaw.isClickable();
	}
	@Override
	public boolean canType() 
	{
		return (mRaw instanceof EditText);
	}
	@Override
	public String type() 
	{		
		return mRaw.getClass().getName();
	}
	@Override
	public IView[] getChildren() 
	{
		if (mRaw instanceof ViewGroup)
		{
			ViewGroup vg = (ViewGroup)mRaw;
			
			if (vg.getChildCount() != 0)
			{
				IView[] views = new IView[vg.getChildCount()];
				for (int i = 0; i < vg.getChildCount(); ++i)
				{
					views[i] = new ViewWrapper(vg.getChildAt(i));
				}
				return views;
			}
		}
		return new IView[0];
	}
	
	@Override
	public String text() 
	{
		if (mRaw instanceof TextView)
		{
			return ((TextView) mRaw).getText().toString();
		}
		return "";
	}
	
	public View raw()
	{
		return mRaw;
	}
	
}
