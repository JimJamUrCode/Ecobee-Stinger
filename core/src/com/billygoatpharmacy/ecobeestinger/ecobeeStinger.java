package com.billygoatpharmacy.ecobeestinger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.screens.GetPinScreen;

public class ecobeeStinger extends ApplicationAdapter {
	
	//Screen Stuffs
	private Screen mGetPinScreen;
	
	@Override
	public void create () {
		ScreenNavigator.init();
		mGetPinScreen = new GetPinScreen();
		ScreenNavigator.showScreen(mGetPinScreen);
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
	    // See below for what true means.
		ScreenNavigator.resize(width, height);
//	    mStage.getViewport().update(width, height, true);
	}
}
