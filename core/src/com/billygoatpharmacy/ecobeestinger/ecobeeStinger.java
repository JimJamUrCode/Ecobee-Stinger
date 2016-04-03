package com.billygoatpharmacy.ecobeestinger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;
import com.billygoatpharmacy.ecobeestinger.screens.GetPinScreen;

public class ecobeeStinger extends ApplicationAdapter {
	//Static vars
	public static Stage sStage;
	
	//Screen Stuffs
	private Screen mGetPinScreen;
	
	@Override
	public void create () 
	{
		sStage = new Stage(new ExtendViewport(1366, 768));
		EcobeeAPI.init();
	}

	@Override
	public void render () 
	{
		ScreenNavigator.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose() {
	    
	}
	
	@Override
	public void resize (int width, int height) {
	    //Adjusting the size of the stage to fit our ExtendViewport
		sStage.getViewport().update(width, height, true);
		Gdx.input.setInputProcessor(sStage);
		
		//Initializing the Screen Navigator
		ScreenNavigator.init(sStage);
		
		if(mGetPinScreen == null)
			mGetPinScreen = new GetPinScreen();
		
		ScreenNavigator.showScreen(mGetPinScreen);
	}
}
