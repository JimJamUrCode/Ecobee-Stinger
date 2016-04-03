package com.billygoatpharmacy.ecobeestinger.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.RequestPinResponseData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.RequestPinAuthenticationResponseData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPIHttpCallback;

public class GetPinScreen extends Screen 
{
	private Skin mAuthButtonSkin;
	private ClickListener mAuthBtnClickListener;
	private RequestPinResponseData mPinCodeData;
	private StingerLabel mPinCodeLbl;
	private Json mJson;
	
	public GetPinScreen()
	{
		super();
		mJson = new Json();
		mJson.setTypeName(null);
		mJson.setUsePrototypes(false);
		mJson.setIgnoreUnknownFields(true);
		mJson.setOutputType(OutputType.json);
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
		this.setTitle("Register ecobeeStinger");
		mAuthButtonSkin = new Skin(Gdx.files.internal("uiskin.json"));
		
		attemptEcobeeLogin();
	}
	
	public void onHide()//Destroy stuff here
	{
		
	}
	
	//Visual Functions
	public void addLoggingInText()
	{
		Logger.log(this.getClass().getName(), "Adding Logging in Text...");
		float nwidth = this.getWidth();
		
		CharSequence txt = "Logging in...";
		StingerLabel descriptionLbl = new StingerLabel(txt, nwidth, null, mAuthButtonSkin, Align.center, true, 1.2f);
		this.add(descriptionLbl).height(this.getHeight()*.5f).width(nwidth);
		this.row();
	}
	
	public void addLoggingInErrorText(String error)
	{
		this.clearChildren();
		Logger.log(this.getClass().getName(), "Error logging in: " + error);
		float nwidth = this.getWidth();
		
		CharSequence txt = "Error logging in: " + error;
		StingerLabel descriptionLbl = new StingerLabel(txt, nwidth, null, mAuthButtonSkin, Align.center, true, 1.2f);
		this.add(descriptionLbl).height(this.getHeight()*.5f).width(nwidth);
		this.row();
		
		TextButton button = new TextButton("Re-Authorize App", mAuthButtonSkin, "default");
		button.addListener(reAuthorizeBtnClick());
		button.setColor(Color.GREEN);
		this.add(button).height(75).width(225);	
		this.row();
	}
	
	private void startAuthorizationProcess()
	{
		this.clearChildren();
		
		//Setting up visuals
		addDescriptionText();
		addPinCodeText();
		addAuthorizeInfo();
		
		//Making a request to the ecobee servers to get a pin code for the user
		getPinFromEcobee();
	}
	
	public void addDescriptionText()
	{
		Logger.log(this.getClass().getName(), "Adding Pin Description Text...");
		float nwidth = this.getWidth();
		
		CharSequence txt = "Go to the ecobee portal and register this app using the pin code below:";
		StingerLabel descriptionLbl = new StingerLabel(txt, nwidth, null, mAuthButtonSkin, Align.center, true, 1.2f);
		this.add(descriptionLbl).height(this.getHeight()*.16f).width(nwidth);
		this.row();
	}
	
	public void addPinCodeText()
	{
		Logger.log(this.getClass().getName(), "Adding Pin Code...");
		CharSequence txt = "Pin Code: ";
		mPinCodeLbl = new StingerLabel(txt, this.getWidth(), null, mAuthButtonSkin, Align.center, true, 1.5f);
		mPinCodeLbl.setColor(Color.YELLOW);
		this.add(mPinCodeLbl).width(this.getWidth()).padBottom(this.getHeight()*.07f);
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
		StingerLabel authDescriptionLbl = new StingerLabel(txt, this.getWidth(), null, mAuthButtonSkin, Align.center, true, 1.2f);
		this.add(authDescriptionLbl).height(200).width(this.getWidth());
		this.row();
		
		TextButton button = new TextButton("Authorize App", mAuthButtonSkin, "default");
		button.addListener(authorizeBtnClick());
		button.setColor(Color.GREEN);
		this.add(button).height(75).width(225);	
		this.row();
	}
	
	//Ecobee functions
	private void attemptEcobeeLogin()
	{
		Boolean success = EcobeeAPI.login(attemptEcobeeLoginCallback());
		
		if(success)
		{
			Logger.log(EcobeeAPI.class.getName(), "Attempting Ecobee Login...");
			addLoggingInText();
		}
		else
		{
			Logger.log(EcobeeAPI.class.getName(), "No stored credentials, redirecting to pin code authentication...");
			startAuthorizationProcess();
		}
	}
	
	private EcobeeAPIHttpCallback attemptEcobeeLoginCallback()
	{
		return new EcobeeAPIHttpCallback() {
			@Override
			public void done(String response)
			{
				Logger.log(EcobeeAPI.class.getName(), "Ecobee Attempted Login Got A Response...");
				
				RequestPinAuthenticationResponseData data = mJson.fromJson(RequestPinAuthenticationResponseData.class, response);
				
				if(data.error != null)//Error happened while logging in
					addLoggingInErrorText(data.error_description);
				else
				{
					if(data.access_token != null && data.access_token != "")
					{
						Logger.log(EcobeeAPI.class.getName(), "Login was a success...");
					}
				}
			}
		};
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
				Logger.log(EcobeeAPI.class.getName(), "Ecobee Pin Received...");
				
				mPinCodeData = mJson.fromJson(RequestPinResponseData.class, response);
				
				Logger.log(this.getClass().getName(), mJson.toJson(mPinCodeData));
				
				//Add a textfield to the screen that shows the user the pin
				updatePinCode();
			}
		};
	}
	
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
				RequestPinAuthenticationResponseData data = mJson.fromJson(RequestPinAuthenticationResponseData.class, response);
				
				Logger.log(this.getClass().getName(), mJson.toJson(data));
			}
		};
	}
	
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
					startAuthorizationProcess();
				}
			};
		}
		return mAuthBtnClickListener;
	}
}
