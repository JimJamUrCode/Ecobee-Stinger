package com.billygoatpharmacy.ecobeestinger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;
import com.billygoatpharmacy.ecobeestinger.screens.AllThermostatsScreen;
import com.billygoatpharmacy.ecobeestinger.screens.GetPinScreen;
import com.billygoatpharmacy.ecobeestinger.screens.ThermostatGraphScreen;

public class ecobeeStinger extends ApplicationAdapter {
	//Static vars
	public static Stage sStage;
	
	//Screen Stuffs
	private GetPinScreen mGetPinScreen;
	private AllThermostatsScreen mAllThermostatsScreen;
	private ThermostatGraphScreen mThermostatGraphScreen;
	
	@Override
	public void create () 
	{
		sStage = new Stage(new ExtendViewport(1366, 768));
		EcobeeAPI.init();
		mGetPinScreen = new GetPinScreen();
		mAllThermostatsScreen = new AllThermostatsScreen();
		mThermostatGraphScreen = new ThermostatGraphScreen();

		ScreenNavigator.addScreen(mGetPinScreen);
		ScreenNavigator.addScreen(mAllThermostatsScreen);
		ScreenNavigator.addScreen(mThermostatGraphScreen);
	}

	@Override
	public void render () 
	{
		ScreenNavigator.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose() 
	{
	    
	}
	
	@Override
	public void resize (int width, int height) 
	{
	    //Adjusting the size of the stage to fit our ExtendViewport
		sStage.getViewport().update(width, height, true);
		Gdx.input.setInputProcessor(sStage);
		
		//Initializing the Screen Navigator
		ScreenNavigator.init(sStage);
		
		String currentScreen = ScreenNavigator.getCurrentScreen();
		if(currentScreen == "")
			ScreenNavigator.setCurrentScreen(GetPinScreen.class.getName());
		else
			ScreenNavigator.setCurrentScreen(currentScreen);
		
		ScreenNavigator.resizeScreen(width, height);
	}
}
