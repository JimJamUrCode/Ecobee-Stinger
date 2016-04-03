package com.billygoatpharmacy.ecobeestinger.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.ThermostatsResposeData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPIHttpCallback;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Thermostat;

public class AllThermostatsScreen extends Screen 
{
	private Skin mAuthButtonSkin;
	private ClickListener mAuthBtnClickListener;
	private ThermostatsResposeData mThermostatData;
	
	public AllThermostatsScreen()
	{
		super();
		mShowAction = new RunnableAction() {
			@Override
			public void run()
			{
				onShow();
			}
		};
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
	}
	
	public void onShow()//Create stuff here
	{
		this.setTitle("Thermostats");
		mAuthButtonSkin = new Skin(Gdx.files.internal("uiskin.json"));
		getAllThermostats();
	}
	
	public void onHide()//Destroy stuff here
	{
		
	}
	
	//Visual Functions	
	public void showAllThermostats()
	{
		Logger.log(this.getClass().getName(), "Showing all thermostats...");
		
		CharSequence txt = "Select a thermostat to view:"; 
		StingerLabel authDescriptionLbl = new StingerLabel(txt, this.getWidth(), null, mAuthButtonSkin, Align.center, true, 1.2f);
		this.add(authDescriptionLbl).height(200).width(this.getWidth());
		this.row();
		
		//Iterate through all available thermostats adding a button for each one
		//TODO:
		for(Thermostat stat: mThermostatData.thermostatList)
		{
			TextButton button = new TextButton(stat.name, mAuthButtonSkin, "default");
			button.addListener(authorizeBtnClick());
			button.setColor(Color.GREEN);
			this.add(button).height(75).width(225);	
			this.row();
		}
	}
	
	//Ecobee functions
	private void getAllThermostats()
	{
		EcobeeAPI.getAllThermostats(getAllThermostatsCallback());
	}
	
	private EcobeeAPIHttpCallback getAllThermostatsCallback()
	{
		return new EcobeeAPIHttpCallback() {
			@Override
			public void done(String response)
			{
				mThermostatData = mJson.fromJson(ThermostatsResposeData.class, response);
				
				if(mThermostatData.error != null && mThermostatData.error != "")
					Logger.log(this.getClass().getName(), "Error Getting Thermostat Data: " + mJson.toJson(mThermostatData));
				else
					Logger.log(EcobeeAPI.class.getName(), "Ecobee Thermostats Received...");
				
				showAllThermostats();
			}
		};
	}
	
	//Button Handlers
	private ClickListener authorizeBtnClick()
	{
		if(mAuthBtnClickListener == null)
		{
			mAuthBtnClickListener = new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{ 
					//TODO:
				}
			};
		}
		return mAuthBtnClickListener;
	}
}
