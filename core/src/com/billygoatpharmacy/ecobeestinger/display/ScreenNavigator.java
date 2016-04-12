package com.billygoatpharmacy.ecobeestinger.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;
import com.billygoatpharmacy.ecobeestinger.ecobeeStinger;

public final class ScreenNavigator extends SpriteDrawable 
{
	private static Screen mCurrentScreen;
	private static Screen mLeavingScreen;
	private static Array<Screen> mAllScreens;
	private static Array<Screen> mScreenTrail;

	public static Skin sUISkin = new Skin(Gdx.files.internal("ecobee-StingerSkin.json"));

	/**Adds a screen to be referenced later on
	 * 
	 * @param newScreenClass The actual screen class that you want to add
	 */
	public static void addScreen(Screen newScreenClass)
	{
		Logger.log(ScreenNavigator.class.getName(), "Adding Screen: " + newScreenClass.getClass().getName());
		if(mAllScreens == null)
			mAllScreens = new Array<Screen>();
		if(mScreenTrail == null)
			mScreenTrail = new Array<Screen>();

		mAllScreens.add(newScreenClass);
	}
	
	/**Used to make a new screen transition on screen
	 * 
	 * @param setScreen Class name reference for the screen that should be set
	 */
	public static void setCurrentScreen(String setScreen)
	{		
		Logger.log(ScreenNavigator.class.getName(), "Setting current Screen: " + setScreen);
		Screen newScr = getScreenFromString(setScreen);
		float w = ecobeeStinger.sStage.getWidth();

		if(mScreenTrail.size > 0) {
			if (mScreenTrail.get(0).equals(newScr))
				return;

			mScreenTrail.get(mScreenTrail.size - 1).setDestination(0, ecobeeStinger.sStage.getHeight() * -1);
		}

		mCurrentScreen = newScr;
		mCurrentScreen.mNeedsShow = true;
		mCurrentScreen.setPosition(w, 0);//Getting the screen ready to show
		mCurrentScreen.setDestination(0, 0);//Starting the animation for the screen to move on screen
		mScreenTrail.add(mCurrentScreen);
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
		mCurrentScreen.resize(ecobeeStinger.sStage);
	}
	
	/**Used to get a screen based off of its class name
	 * 
	 * @param screenString Used to get the single instance of a screen
	 * @return
	 */
	public static Screen getScreenFromString(String screenString)
	{
		for(Screen scr: mAllScreens)
		{
			if(scr.getClass().getName() == screenString)
			{
//				Logger.log(ScreenNavigator.class.getName(), "Got Screen: " + scr.getClass().getName());
				return scr;
			}
		}
		
		return null;
	}

	/**Used to update the screens that the Screen Navigator is managing
	 * This needs to be called in the application render() function.
	 * @param delta The difference in time from the last from to this frame
	 */
	public static void update(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);

		ecobeeStinger.sStage.act(Gdx.graphics.getDeltaTime());

		if(mCurrentScreen != null) {
			if (mCurrentScreen.mNeedsShow)
				mCurrentScreen.show();
			mCurrentScreen.update(delta);
		}

		ecobeeStinger.sStage.draw();
	}

	public static void goBackAScreen() {
		float w = ecobeeStinger.sStage.getWidth();
		mScreenTrail.pop().setDestination(w,0);
		mCurrentScreen = mScreenTrail.get(mScreenTrail.size-1);
		mCurrentScreen.setDestination(0, 0);
	}
}
