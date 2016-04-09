package com.billygoatpharmacy.ecobeestinger.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.fileTools.FileManager;

public class Screen extends Table 
{
	final float SPEED = 5.0f;
	
	protected Stage mStage;
	protected RunnableAction mShowAction;
	protected StingerLabel mTitle;
	protected Json mJson;
	
	private float mXDestination;
	private float mYDestination;
	private float mXDirection;
	private Boolean mHasBeenInitialized;

	public Boolean mNeedsShowAndResize;

	public Screen()
	{
		super();
		
		mJson = new Json();
		mJson.setTypeName(null);
		mJson.setUsePrototypes(false);
		mJson.setIgnoreUnknownFields(true);
		mJson.setOutputType(OutputType.json);
		
		mHasBeenInitialized = false;
		mNeedsShowAndResize = false;
	}
	
	/**Resizes this screen to the new dimensions of the view port
	 * If the stage has been initialized in the past, the onShow function
	 * will not be called.
	 * @param newStage
	 */
	public void resize(Stage newStage)
	{
		Logger.log(this.getClass().getName(), "Resizing Screen...");
		mNeedsShowAndResize = false;
		this.mStage = newStage;
		this.setWidth(mStage.getWidth());
		this.setHeight(mStage.getHeight());
		
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
	
	/**This will at some point hide the screen, but for now, the screen
	 * navigator handles positioning for each visible screen.
	 */
	public void hide()
	{
		mXDirection = -1.0f;
	}
	
	/**Allows each screen to set its own title
	 * 
	 * @param txt
	 */
	public Cell setTitle(CharSequence txt,Boolean shouldEndRow)
	{
		mTitle = new StingerLabel(txt, mStage.getWidth(), null, ScreenNavigator.sUISkin, Align.center, false, 2f);
		
		Logger.log(this.getClass().getName(), "Title Width: " + mTitle.getPrefWidth());
		this.top();
		if(shouldEndRow == false)
			return this.add(mTitle).height(mStage.getHeight() * .1f);//padBottom(50);
		else
			this.add(mTitle).height(mStage.getHeight() * .1f);//padBottom(50);
			return this.row();
	}
	
	/**Sets a new X and Y coordinate for the screen, the screen will then 
	 * move to that position on its own. This does require that the update function
	 * be called on the screen navigator that manages this screen.
	 * @param newXDestination
	 * @param newYDestination
	 */
	public void setDestination(float newXDestination, float newYDestination)
	{
		mXDestination = newXDestination;
		mYDestination = newYDestination;
	}
	
	/**Updates the position of this screen based on the destination coordinates
	 * 
	 * @param delta
	 */
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
