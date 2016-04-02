package com.billyg.myapp.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public final class ScreenNavigator extends SpriteDrawable 
{
	private static Screen mCurrentScreen;
	private static Screen mTransitioningScreen;
	private static Stage mStage;
	
	public static void init()
	{
		mStage = new Stage(new ExtendViewport(1366, 768));
		Gdx.input.setInputProcessor(mStage);
		mCurrentScreen = null;
	}
	
	public static void showScreen(Screen screen)
	{
		mTransitioningScreen = screen;
		
		mTransitioningScreen.setX(mStage.getWidth());
		mTransitioningScreen.setY(0);
		
		mTransitioningScreen.setOriginX(0);
		mTransitioningScreen.setOriginY(0);
		
		mTransitioningScreen.setDestination(0, 0);
		
		mTransitioningScreen.show(mStage.getWidth(), mStage.getHeight(), mStage);
		if(mCurrentScreen != null)
		{
			mCurrentScreen.setDestination(mStage.getWidth()*-1, 0);
		}
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
		mStage.draw();
		if(mTransitioningScreen != null)
			mTransitioningScreen.update(delta);
		
		if(mCurrentScreen != null)
			mCurrentScreen.update(delta);
		
		mStage.draw();
	}
	
	public static void resize(int nWidth, int nHeight)
	{
		mStage.getViewport().update(nWidth, nHeight, true);
		//mTransitioningScreen.resize(nWidth, nHeight);
	}
}
