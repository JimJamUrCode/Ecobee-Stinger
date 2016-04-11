package com.billygoatpharmacy.ecobeestinger.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.display.ecobee.ThermostatQuickInfoPanel;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.ecobeestinger.display.utils.TextButtonClickListener;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.ThermostatsResposeData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPIHttpCallback;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Thermostat;
import com.billygoatpharmacy.fileTools.FileManager;

public class AllThermostatsScreen extends Screen 
{
	private ThermostatsResposeData mThermostatData;
	private boolean mUpdateThermostatView;

	public static Boolean DEBUG = false;

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
		this.setTitle("Thermostats", false);
		mUpdateThermostatView = false;

		mThermostatData = EcobeeAPI.getThermostatsResponse();

		if (mThermostatData == null || mThermostatData.error != null)
			showRetryButton();
		else
			showAllThermostats();
	}
	
	public void onHide()//Destroy stuff here
	{
		
	}

	//Visual Functions

	/**If the getAllThermostats() function succeeds
	 *
	 */
	public void showAllThermostats()
	{
		Logger.log(this.getClass().getName(), "Showing all thermostats...");
		
		CharSequence txt = "Select a thermostat to view:"; 
		StingerLabel authDescriptionLbl = new StingerLabel(txt, this.getWidth(), null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
		this.add(authDescriptionLbl).height(getHeight() * .1f).width(this.getWidth());
		this.row();

		Table allThermostats = new Table();
		//Iterate through all available thermostats adding a button for each one
		for(Thermostat stat: mThermostatData.thermostatList)
			{
			ThermostatQuickInfoPanel quickInfo = new ThermostatQuickInfoPanel();
			quickInfo.setWidth(getWidth() / 3);
			quickInfo.setHeight(getHeight() * .5f);
			quickInfo.init(stat, thermostatButtonClicked(stat.identifier));
			allThermostats.add(quickInfo).height(getHeight() * .65f).width(this.getWidth() / 3).pad(0,10,0,10);
			//this.row();
		}

		ScrollPane scroller = new ScrollPane(allThermostats);
		scroller.setDebug(true);
		this.add(scroller).height(getHeight() * .8f);
	}

	/**If the get all thermostats function fails
	 *
	 */
	public void showRetryButton()
	{
		Logger.log(this.getClass().getName(), "Shwoing retry button...");

		CharSequence txt = "Failed to get thermostats:";
		StingerLabel authDescriptionLbl = new StingerLabel(txt, this.getWidth(), null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
		this.add(authDescriptionLbl).height(200).width(this.getWidth());
		this.row();

		TextButton button = new TextButton("Retry", ScreenNavigator.sUISkin, "default");
		button.addListener(retryBtnClick());
		button.setColor(Color.GREEN);
		this.add(button).height(75).width(225);
		this.row();
	}

	//Ecobee functions
	private void getAllThermostats()
	{
		if(DEBUG)//If in debug mode, use a cached request as to not ping the server a million times developing
		{
			String cachedThermostat = FileManager.readFile("ThermostatResponse.resp", false);
			if(cachedThermostat == "")
				EcobeeAPI.getAllThermostats(getAllThermostatsCallback());
			else
			{
				mThermostatData = mJson.fromJson(ThermostatsResposeData.class, cachedThermostat);
				getAllThermostatsCallback().done(cachedThermostat);
			}
		}
		else
		{
			EcobeeAPI.getAllThermostats(getAllThermostatsCallback());
		}
	}
	
	private EcobeeAPIHttpCallback getAllThermostatsCallback()
	{
		return new EcobeeAPIHttpCallback() {
			@Override
			public void done(String response)
			{
				mThermostatData = mJson.fromJson(ThermostatsResposeData.class, response);
				mUpdateThermostatView = true;

				if(mThermostatData.error != null && mThermostatData.error != "") {
					Logger.log(this.getClass().getName(), "Error Getting Thermostat Data: " + mJson.toJson(mThermostatData));
				}
				else
				{
					Logger.log(this.getClass().getName(), "Ecobee Thermostats Received...");
					FileManager.saveFile("ThermostatResponse.resp", response);
				}
			}
		};
	}
	
	//Button Handlers
	private ClickListener retryBtnClick()
	{
		return new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getAllThermostats();
			}
		};
	}

	private TextButtonClickListener thermostatButtonClicked(String id)
	{
		return new TextButtonClickListener(id)
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				String id = getId();
				ScreenNavigator.setCurrentScreen(ThermostatGraphScreen.class.getName());
			}
		};
	}


}
