package com.billygoatpharmacy.ecobeestinger.display;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Screen extends Group 
{
	final float SPEED = 5.0f;
	
	protected Stage mStage;
	protected RunnableAction mShowAction;
	protected Label mTitle;
	
	private float mXDestination;
	private float mYDestination;
	private float mXDirection;
	
	public Screen()
	{
		super();
	}
	
	public Screen(RunnableAction showAction)
	{
		mShowAction = showAction;
	}
	
	public void show(float nWidth, float nHeight, Stage newStage)
	{
		this.mStage = newStage;
		this.setWidth(nWidth);
		this.setHeight(nHeight);
		
//		mTitle
		mStage.addActor(this);
		mXDirection = -1.0f;
		mShowAction.run();
	}
	
	public void hide()
	{
		mXDirection = -1.0f;
	}
	
	public void setDestination(float newXDestination, float newYDestination)
	{
		mXDestination = newXDestination;
		mYDestination = newYDestination;
	}
	
	public void update(float delta)
	{
		float xDifference = this.getX() - mXDestination;
		float yDifference = this.getY() - mYDestination;
		
		if(xDifference < 1 && xDifference > -1)//Done moving
		{
			this.setX(mXDestination);
		}
		else
		{
			float newX = ((xDifference*SPEED)*mXDirection) * delta;
			this.setX(this.getX() + newX);
		}
		
		if(yDifference < 1 && yDifference > -1)//Done moving
		{
			this.setY(mYDestination);
		}
		else
		{
			float newY = delta * (yDifference*SPEED);
			this.setY(newY);
		}
	}
}
