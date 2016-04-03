package com.billygoatpharmacy.ecobeestinger.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.billygoatpharmacy.ecobeestinger.Logger;

public final class ScreenNavigator extends SpriteDrawable 
{
	private static Screen mCurrentScreen;
	private static Screen mLeavingScreen;
	
	private static Stage mStage;
	
	private static Array<Screen> mAllScreens;
	
	/**Used to set the initial stage
	 * 
	 * @param newStage
	 */
	public static void init(Stage newStage)
	{
		if(newStage != null)
			mStage = newStage;
	}
	
	/**Adds a screen to be referenced later on
	 * 
	 * @param newScreenClass The actual screen class that you want to add
	 */
	public static void addScreen(Screen newScreenClass)
	{
		Logger.log(ScreenNavigator.class.getName(), "Adding Screen: " + newScreenClass.getClass().getName());
		if(mAllScreens == null)
			mAllScreens = new Array<Screen>();
		
		mAllScreens.add(newScreenClass);
	}
	
	/**Used to make a new screen transition on screen
	 * 
	 * @param setScreen Class name reference for the screen that should be set
	 */
	public static void setCurrentScreen(String setScreen)
	{		
		Logger.log(ScreenNavigator.class.getName(), "Setting current Screen: " + setScreen);
		float w = mStage.getWidth();
		
		if(mCurrentScreen != null)
		{
			if(mCurrentScreen.equals(getScreenFromString(setScreen)))
				return;
			
			mLeavingScreen = mCurrentScreen;
			mLeavingScreen.setDestination(w*-1, 0);
		}
		
		mCurrentScreen = getScreenFromString(setScreen);
		mCurrentScreen.mNeedsShowAndResize = true;
		mCurrentScreen.setX(w);
		mCurrentScreen.setY(0);
		mCurrentScreen.setDestination(0, 0);
	}
	
	/**Returns the string name of the current screen
	 * 
	 * @return
	 */
	public static String getCurrentScreen()
	{
		if(mCurrentScreen == null)
			return "";
		else
			return mCurrentScreen.getClass().getName();
	}
	
	/**Used the resize the visible screen when the screen size changes
	 * 
	 * @param nWidth - The New width of the screen
	 * @param nHeight The New height of the screen
	 */
	public static void resizeScreen(int nWidth, int nHeight)
	{
		Logger.log(ScreenNavigator.class.getName(), "Resizing Screen...");
		mCurrentScreen.resize(mStage);
		
		mCurrentScreen.setX(mStage.getWidth());
		mCurrentScreen.setY(0);
		mCurrentScreen.setDestination(0, 0);
	}
	
	/**Used to get a screen based off of its class name
	 * 
	 * @param screenString Used to get the single instance of a screen
	 * @return
	 */
	private static Screen getScreenFromString(String screenString)
	{
		for(Screen scr: mAllScreens)
		{
			if(scr.getClass().getName() == screenString)
			{
				Logger.log(ScreenNavigator.class.getName(), "Got Screen: " + scr.getClass().getName());
				return scr;
			}
		}
		
		return null;
	}
	
	/**Get the current stage that the screen navigator is using
	 * 
	 * @return The current stage the Screen Navigator is using
	 */
	public static Stage getStage()
	{
		return mStage;
	}
	
	/**Used to update the screens that the Screen Navigator is managing
	 * This needs to be called in the application render() function.
	 * @param delta The difference in time from the last from to this frame
	 */
	public static void update(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		
		mStage.act(Gdx.graphics.getDeltaTime());
		
		if(mCurrentScreen.mNeedsShowAndResize)
		{
			mCurrentScreen.resize(mStage);
		}
		
		if(mLeavingScreen != null)
		{
			mStage.addActor(mLeavingScreen);
			mLeavingScreen.update(delta);
		}
		else
		{
			Logger.log(ScreenNavigator.class.getName(), "Transitioning Screen is null");
		}
		
		if(mCurrentScreen != null)
		{
			mStage.addActor(mCurrentScreen);
			mCurrentScreen.update(delta);
		}
		else
		{
			Logger.log(ScreenNavigator.class.getName(), "Current Screen is null");
		}
		
		mStage.draw();
	}
}
