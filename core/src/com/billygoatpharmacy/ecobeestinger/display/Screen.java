package com.billygoatpharmacy.ecobeestinger.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;

public class Screen extends Table 
{
	final float SPEED = 5.0f;
	
	protected Stage mStage;
	protected RunnableAction mShowAction;
	protected StingerLabel mTitle;
	
	private float mXDestination;
	private float mYDestination;
	private float mXDirection;
	
	private Boolean mHasBeenInitialized;
	
	public Screen()
	{
		super();
		mHasBeenInitialized = false;
	}
	
	public Screen(RunnableAction showAction)
	{
		mShowAction = showAction;
	}
	
	public void show(float nWidth, float nHeight, Stage newStage)
	{
		this.setWidth(nWidth);
		this.setHeight(nHeight);
		this.mStage = newStage;
		this.setFillParent(true);
		//this.setDebug(true);
		
		mStage.addActor(this);
		mXDirection = -1.0f;
		
		if(mHasBeenInitialized == false)
		{
			mShowAction.run();
			mHasBeenInitialized = true;
		}
	}
	
	public void hide()
	{
		mXDirection = -1.0f;
	}
	
	public void setTitle(CharSequence txt)
	{
		mTitle = new StingerLabel(txt, this.getWidth(), null, new Skin(Gdx.files.internal("uiskin.json")), Align.center, false, 3f);
		
		Logger.log(this.getClass().getName(), "Title Width: " + mTitle.getPrefWidth());
		this.add(mTitle).padBottom(50);
		this.top();
		this.row();
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
