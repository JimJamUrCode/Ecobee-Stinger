package com.billygoatpharmacy.ecobeestinger.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.HttpGetPinResponseData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPIHttpCallback;

public class GetPinScreen extends Screen 
{
	private Skin mAuthButtonSkin;
	private ClickListener mAuthBtnClickListener;
	private EcobeeAPIHttpCallback mGetPinCallback;
	private HttpGetPinResponseData mPinCodeData;
	
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
	}
	
	public void onShow()//Create stuff here
	{
		//Setup 'Authorize' button
		addAuthorizeInfo();
		
		addDescriptionText();
		
		getPinFromEcobee();
	}
	
	public void onHide()//Destroy stuff here
	{
		
	}
	
	private void getPinFromEcobee()
	{
		EcobeeAPI.getPin(getPinCallback());
	}
	
	private EcobeeAPIHttpCallback getPinCallback()
	{
		if(mGetPinCallback == null)
		{
			mGetPinCallback = new EcobeeAPIHttpCallback() {
				@Override
				public void done(String response)
				{
					Logger.log(EcobeeAPI.class.getName(), "Ecobee Pin Received...");
					Json json = new Json();
					json.setTypeName(null);
					json.setUsePrototypes(false);
					json.setIgnoreUnknownFields(true);
					json.setOutputType(OutputType.json);

					mPinCodeData = json.fromJson(HttpGetPinResponseData.class, response);
					
					Logger.log(this.getClass().getName(), json.toJson(mPinCodeData));
					
					//Add a textfield to the screen that shows the user the pin
					addPinCodeText();
				}
			};
		}
		
		return mGetPinCallback;
	}
	
	public void addDescriptionText()
	{
		Label descriptionLbl = new Label("Go to the ecobee portal and register this app using the pin code below:", mAuthButtonSkin);
		descriptionLbl.setWidth(this.getWidth());
		descriptionLbl.setAlignment(Align.center);
		descriptionLbl.setWrap(true);
		descriptionLbl.setFontScale(1.2f);
		descriptionLbl.setX(this.getWidth()/2 - descriptionLbl.getWidth()/2);
		descriptionLbl.setY(this.getHeight()*.66f);
		this.addActor(descriptionLbl);
	}
	
	public void addPinCodeText()
	{
		Label pincodeLbl = new Label("Pin Code: " + mPinCodeData.ecobeePin, mAuthButtonSkin);
		pincodeLbl.setAlignment(Align.center);
		pincodeLbl.setFontScale(1.5f);
		pincodeLbl.setColor(Color.YELLOW);
		pincodeLbl.setX(this.getWidth()/2 - pincodeLbl.getWidth()/2);
		pincodeLbl.setY(this.getHeight()*.56f);
		this.addActor(pincodeLbl);
	}
	
	public void addAuthorizeInfo()
	{
		mAuthButtonSkin = new Skin(Gdx.files.internal("uiskin.json"));
		
		Label authDescriptionLbl = new Label("After registering this app through the ecobee portal, grant authrization by clicking the button below:", mAuthButtonSkin);
		authDescriptionLbl.setWidth(this.getWidth());
		authDescriptionLbl.setAlignment(Align.center);
		authDescriptionLbl.setFontScale(1.2f);
		authDescriptionLbl.setX(this.getWidth()/2 - authDescriptionLbl.getWidth()/2);
		authDescriptionLbl.setY(this.getHeight()*.33f);
		authDescriptionLbl.setWrap(true);
		this.addActor(authDescriptionLbl);
		
		final TextButton button = new TextButton("Authorize App", mAuthButtonSkin, "default");
		button.setWidth(225);
		button.setHeight(75);

		button.setX(this.getWidth()/2 - button.getWidth()/2);
		button.setY(this.getHeight()*.20f);

		this.addActor(button);

		button.addListener(authorizeBtnClick());
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
					 
				}
			};
		}
		return mAuthBtnClickListener;
	}
}
