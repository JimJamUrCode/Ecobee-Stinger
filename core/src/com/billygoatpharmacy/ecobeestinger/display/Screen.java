package com.billygoatpharmacy.ecobeestinger.display;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.Timer;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.ecobeestinger.ecobeeStinger;

public class Screen extends Table 
{
	public RunnableAction mShowAction;
	protected StingerLabel mTitle;
	protected Json mJson;

	private Boolean mHasBeenInitialized;
	public Boolean mNeedsShow;

	public Screen()
	{
		super();
		
		mJson = new Json();
		mJson.setTypeName(null);
		mJson.setUsePrototypes(false);
		mJson.setIgnoreUnknownFields(true);
		mJson.setOutputType(OutputType.json);
		
		mHasBeenInitialized = false;
		mNeedsShow = false;
	}
	
	/**Resizes this screen to the new dimensions of the view port
	 * If the stage has been initialized in the past, the onShow function
	 * will not be called.
	 */
	public void resize(Stage aStage)
	{
		Logger.log(this.getClass().getName(), "Base Class Resizing Screen...");
		this.setWidth(aStage.getWidth());
		this.setHeight(aStage.getHeight());
	}

	/**Shows the screen, if not already visible
	 */
	public void show()
	{
		Logger.log(this.getClass().getName(), "Base Class Show Screen...");
		mNeedsShow = false;
		this.setFillParent(true);

		ecobeeStinger.sStage.addActor(this);

		if(mHasBeenInitialized == false)
		{
			Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					mShowAction.run();
				}
			}, .01f);

			mHasBeenInitialized = true;
		}
	}
	
	/**This will at some point hide the screen, but for now, the screen
	 * navigator handles positioning for each visible screen.
	 */
	public void hide()
	{
	}
	
	/**Allows each screen to set its own title
	 * You can repeatedly call this function on the same screen to reset the title.
	 * @param txt The text that will appeat as the title
	 * @param includeBackBtn Should a page back button be displayed near the top of the screen?
	 */
	public void setTitle(CharSequence txt, Boolean includeBackBtn)
	{
		Logger.log(this.getClass().getName(), "Adding Title...: " + txt);
		Table titleTable = new Table();
		this.top();
		this.add(titleTable).width(getWidth());

		if(mTitle == null)
		{
			mTitle = new StingerLabel(txt, getWidth(), null, ScreenNavigator.sUISkin, Align.center, false, 2f);
		}

		if(includeBackBtn)
		{
			TextButton button = new TextButton("<", ScreenNavigator.sUISkin, "default");
			button.setColor(new Color().set(.3f, .3f, .3f, 1f));
			button.addListener(backBtnClick());
			titleTable.add(button).height(getHeight() * .1f).width(getWidth() * .1f);
			titleTable.add(mTitle).height(getHeight() * .1f).width(getWidth() * .8f).colspan(2);
			titleTable.add().height(getHeight() * .1f).width(getWidth() * .1f);
		}
		else if(txt != null && txt != "")
		{
			titleTable.add(mTitle).height(getHeight() * .1f).width(getWidth());
		}
		else
			mTitle.setText(txt);
		this.row();
	}
	
	/**Sets a new X and Y coordinate for the screen, the screen will then 
	 * move to that position on its own.
	 * @param newXDestination The new x destination
	 * @param newYDestination Then new y destination
	 */
	public void setDestination(float newXDestination, float newYDestination)
	{
		MoveToAction action = new MoveToAction();
		action.setPosition(newXDestination, newYDestination);
		action.setDuration(1f);
		action.setInterpolation(Interpolation.exp10Out);
		this.addAction(action);
	}
	
	/**Update function for the sceen. This is called by the ScreenNavigator Class
	 * 
	 * @param delta The change in time from this frame to last
	 */
	public void update(float delta)
	{

	}

	//Button Listeners
	private ClickListener backBtnClick()
	{
		return new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				//Transition to the previous screen
				ScreenNavigator.goBackAScreen();
			}
		};
	}
}
