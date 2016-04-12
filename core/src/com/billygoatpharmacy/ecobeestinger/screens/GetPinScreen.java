package com.billygoatpharmacy.ecobeestinger.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.ThermostatsResposeData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.PinAuthenticationResponseData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.PinRequestResponseData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPIHttpCallback;
import com.billygoatpharmacy.fileTools.FileManager;

public class GetPinScreen extends Screen 
{
	private ClickListener mAuthBtnClickListener;
	private PinRequestResponseData mPinCodeData;
	private StingerLabel mPinCodeLbl;
	private Boolean mShouldGetAllThermostats;

	public GetPinScreen()
	{
		super();
		mShowAction = new RunnableAction() {
			@Override
			public void run()
			{
				onShow();
			}
		};
		mShouldGetAllThermostats = false;
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);

		if(mShouldGetAllThermostats)
		{
			mShouldGetAllThermostats = false;
			Timer.schedule(new Timer.Task(){
				@Override
				public void run()
				{
					getAllThermostats();
				}
			},1f);
		}
	}
	
	public void onShow()//Create stuff here
	{
		this.setTitle("Register ecobeeStinger", false);

		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				attemptEcobeeLogin();
			}
		}, 1f);
	}
	
	public void onHide()//Destroy stuff here
	{
		
	}
	
	//Visual Functions
	public void addLoggingInText()
	{
		Logger.log(this.getClass().getName(), "Adding 'Logging in' Text...");
		float nwidth = this.getWidth();
		
		CharSequence txt = "Logging in...";
		StingerLabel descriptionLbl = new StingerLabel(txt, nwidth, null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
		this.add(descriptionLbl).height(this.getHeight()*.5f).width(nwidth);
		this.row();
	}

	public void addGettingThermostatsText()
	{
		Logger.log(this.getClass().getName(), "Adding 'Getting Thermostat' Text...");
		float nwidth = this.getWidth();

		CharSequence txt = "Getting thermostats...";
		StingerLabel descriptionLbl = new StingerLabel(txt, nwidth, null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
		this.add(descriptionLbl).height(this.getHeight()*.5f).width(nwidth);
		this.row();
	}
	
	public void addLoggingInErrorText(String error)
	{
		this.clearChildren();
		Logger.log(this.getClass().getName(), "Error logging in: " + error);
		float nwidth = this.getWidth();
		
		CharSequence txt = "Error logging in: " + error;
		StingerLabel descriptionLbl = new StingerLabel(txt, nwidth, null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
		this.add(descriptionLbl).height(this.getHeight()*.5f).width(nwidth);
		this.row();
		
		TextButton button = new TextButton("Re-Authorize App", ScreenNavigator.sUISkin, "default");
		button.addListener(reAuthorizeBtnClick());
		button.setColor(Color.GREEN);
		this.add(button).height(75).width(225);	
		this.row();
	}
	
	public void addDescriptionText()
	{
		Logger.log(this.getClass().getName(), "Adding Pin Description Text...");
		float nwidth = this.getWidth();
		
		CharSequence txt = "Go to the ecobee portal and register this app using the pin code below:";
		StingerLabel descriptionLbl = new StingerLabel(txt, nwidth, null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
		this.add(descriptionLbl).height(this.getHeight() * .16f).width(nwidth);
		this.row();
	}
	
	public void addPinCodeText()
	{
		Logger.log(this.getClass().getName(), "Adding Pin Code...");
		CharSequence txt = "Pin Code: ";
		mPinCodeLbl = new StingerLabel(txt, this.getWidth(), null, ScreenNavigator.sUISkin, Align.center, true, 1.5f);
		mPinCodeLbl.setColor(Color.YELLOW);
		this.add(mPinCodeLbl).width(this.getWidth()).padBottom(this.getHeight() * .07f);
		this.row();
	}
	
	public void updatePinCode()
	{
		CharSequence txt = "Pin Code: " + mPinCodeData.ecobeePin;
		mPinCodeLbl.setText(txt);
	}
	
	public void addAuthorizeInfo()
	{
		Logger.log(this.getClass().getName(), "Adding Authorization Description...");
		
		CharSequence txt = "After registering this app through the ecobee portal, grant authorization by clicking the button below:"; 
		StingerLabel authDescriptionLbl = new StingerLabel(txt, this.getWidth(), null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
		this.add(authDescriptionLbl).height(200).width(this.getWidth());
		this.row();
		
		TextButton button = new TextButton("Authorize App", ScreenNavigator.sUISkin, "default");
		button.addListener(authorizeBtnClick());
		button.setColor(Color.GREEN);
		this.add(button).height(75).width(225);	
		this.row();
	}

	/**Attempts to login to the users ecobee account using pin authentication and the ecobee api.
	 * If the app has never been authorized, pin authentication login will be presented.
	 * Otherwise the app will attempt to refesh its access token and login automatically.
	 */
	private void attemptEcobeeLogin() {
		if (AllThermostatsScreen.DEBUG)
		{
			Logger.log(EcobeeAPI.class.getName(), "In DEBUG mode, no need to login...");
//			ScreenNavigator.setCurrentScreen(AllThermostatsScreen.class.getName());

			Screen scr = new Screen();
			scr.setTitle("New Screen", false);
			ScreenNavigator.addScreen(scr);
			ScreenNavigator.setCurrentScreen(scr.getClass().getName());
		}
		else {
			Boolean success = EcobeeAPI.login(attemptEcobeeLoginCallback());

			if (success) {
				Logger.log(EcobeeAPI.class.getName(), "Attempting Ecobee Login...");
				addLoggingInText();
			} else {
				Logger.log(EcobeeAPI.class.getName(), "No stored credentials, redirecting to pin code authentication...");
				startPinAuthorizationProcess();
			}
		}
	}

	private EcobeeAPIHttpCallback attemptEcobeeLoginCallback()
	{
		return new EcobeeAPIHttpCallback() {
			@Override
			public void done(String response)
			{
				Logger.log(EcobeeAPI.class.getName(), "Ecobee Attempted Login Got A Response...");

				PinAuthenticationResponseData data = mJson.fromJson(PinAuthenticationResponseData.class, response);

				if(data.error != null)//Error happened while logging in
					addLoggingInErrorText(data.error_description);
				else {
					if (data.access_token != null && data.access_token != "")
					{
						Logger.log(EcobeeAPI.class.getName(), "Login was a success...");
//						ScreenNavigator.setCurrentScreen(AllThermostatsScreen.class.getName());

						mShouldGetAllThermostats = true;
					}
				}
			}
		};
	}

	private void addfakeScreen()
	{
		Screen scr = new Screen();
		scr.mShowAction = new RunnableAction(){
			@Override
			public void run()
			{

			}
		};
		scr.setTitle("New Screen", true);
		ScreenNavigator.addScreen(scr);
		ScreenNavigator.setCurrentScreen(scr.getClass().getName());
	}

	private void startPinAuthorizationProcess()
	{
		this.clearChildren();

		this.setTitle("Register ecobeeStinger", true);
		//Setting up visuals
		addDescriptionText();
		addPinCodeText();
		addAuthorizeInfo();

		//Making a request to the ecobee servers to get a pin code for the user
		getPinFromEcobee();
	}

	private void getPinFromEcobee()
	{
		EcobeeAPI.getPin(getPinCallback());
	}
	
	private EcobeeAPIHttpCallback getPinCallback()
	{
		return new EcobeeAPIHttpCallback() {
			@Override
			public void done(String response)
			{
				Logger.log(this.getClass().getName(), "Ecobee Pin Received...");
				
				mPinCodeData = mJson.fromJson(PinRequestResponseData.class, response);
				
				Logger.log(this.getClass().getName(), mJson.toJson(mPinCodeData));
				
				//Add a textfield to the screen that shows the user the pin
				updatePinCode();
			}
		};
	}

	/**Once the user has authorized the application using the pin code presented, the application
	 * will then ask the ecobee servers for an access token
	 */
	private void getAccessTokenFromEcobee()
	{
		EcobeeAPI.getAccessToken(getAccessTokenCallback(), "ecobeePin");
	}
	
	private EcobeeAPIHttpCallback getAccessTokenCallback()
	{
		return new EcobeeAPIHttpCallback() {
			@Override
			public void done(String response)
			{
				PinAuthenticationResponseData data = mJson.fromJson(PinAuthenticationResponseData.class, response);
				
				Logger.log(this.getClass().getName(), mJson.toJson(data));
			}
		};
	}

	/**Used to get all of the thermostats for a user account. This is here because it makes it so
	 * we can load all of the tstats before the user even sees the next screen.
	 * @return
	 */
	private void getAllThermostats()
	{
		addGettingThermostatsText();

		if(AllThermostatsScreen.DEBUG)//If in debug mode, use a cached request as to not ping the server a million times developing
		{
			String cachedThermostat = FileManager.readFile("ThermostatResponse.resp", false);
			if(cachedThermostat == "")
				EcobeeAPI.getAllThermostats(getAllThermostatsCallback());
			else
			{
				EcobeeAPI.setThermostatsResponseData(mJson.fromJson(ThermostatsResposeData.class, cachedThermostat));
				ScreenNavigator.setCurrentScreen(AllThermostatsScreen.class.getName());
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
				ScreenNavigator.setCurrentScreen(AllThermostatsScreen.class.getName());
			}
		};
	}

	//Button Listeners
	private ClickListener authorizeBtnClick()
	{
		if(mAuthBtnClickListener == null)
		{
			mAuthBtnClickListener = new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{ 
					getAccessTokenFromEcobee();
				}
			};
		}
		return mAuthBtnClickListener;
	}
	
	private ClickListener reAuthorizeBtnClick()
	{
		if(mAuthBtnClickListener == null)
		{
			mAuthBtnClickListener = new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{ 
					startPinAuthorizationProcess();
				}
			};
		}
		return mAuthBtnClickListener;
	}
}
