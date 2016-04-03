package com.billygoatpharmacy.ecobeestinger.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public final class ScreenNavigator extends SpriteDrawable 
{
	private static Screen mCurrentScreen;
	private static Screen mTransitioningScreen;
	
	private static Table mRootTable;
	private static Stage mStage;
	
	public static void init(Stage newStage)
	{
		if(mStage == null)
		{
			mStage = newStage;
			mCurrentScreen = null;
		}
	}
	
	public static void showScreen(Screen screen)
	{
		float w = mStage.getWidth();
		mTransitioningScreen = screen;
		mTransitioningScreen.show(w, mStage.getHeight(), mStage);
		mTransitioningScreen.setOriginX(0);
		mTransitioningScreen.setOriginY(0);
		
		
		mTransitioningScreen.setX(w);
		mTransitioningScreen.setY(0);
		
		mTransitioningScreen.setDestination(0, 0);
		
		if(mCurrentScreen != null)
			mCurrentScreen.setDestination(w*-1, 0);
		
	    mStage.addActor(mTransitioningScreen);
	}
	
	public static Stage getStage()
	{
		return mStage;
	}
	
	public static void update(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		
		mStage.act(Gdx.graphics.getDeltaTime());
		
		if(mTransitioningScreen != null)
			mTransitioningScreen.update(delta);
		
		if(mCurrentScreen != null)
			mCurrentScreen.update(delta);
		
		mStage.draw();
	}
}
