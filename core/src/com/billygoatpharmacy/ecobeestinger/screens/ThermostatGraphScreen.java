package com.billygoatpharmacy.ecobeestinger.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.display.ecobee.TherostatQuickInfoPanel;
import com.billygoatpharmacy.ecobeestinger.display.ecobee.graph.LineGraph;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.ecobeestinger.display.utils.TextButtonClickListener;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.ThermostatsResposeData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPIHttpCallback;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Thermostat;
import com.billygoatpharmacy.fileTools.FileManager;

public class ThermostatGraphScreen extends Screen
{
	private LineGraph mLineGraph;

	public ThermostatGraphScreen()
	{
		super();
		mShowAction = new RunnableAction() {
			@Override
			public void run()
			{
				onShow();
			}
		};
		mLineGraph = new LineGraph();
		LineGraph.createTestDataSet(mLineGraph);
//		this.setDebug(true);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		mLineGraph.update(delta);
	}
	
	public void onShow()//Create stuff here
	{
		this.setTitle("Graph", false).colspan(2);
		this.row();
		this.add(mLineGraph).height(mStage.getHeight() * .55f).width(this.getWidth() * .9f).pad(0,0,0,10);

		addZoomButtons();
//		getAllThermostats();
	}
	
	public void onHide()//Destroy stuff here
	{
		
	}
	
	//Visual Functions
	private void addZoomButtons()
	{
		Table nTable = new Table();
		nTable.setWidth(getWidth()*.1f);
//		nTable.setHeight(this.getHeight() *.1f);

		CharSequence txt = "Zoom:";
		StingerLabel authDescriptionLbl = new StingerLabel(txt, nTable.getWidth(), null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
		nTable.add(authDescriptionLbl);
		nTable.row();

		TextButton zoomOutBtn = new TextButton("-", ScreenNavigator.sUISkin, "default");
		zoomOutBtn.addListener(zoomBtnClick("-"));
		zoomOutBtn.setColor(Color.GREEN);
		nTable.add(zoomOutBtn).width(nTable.getWidth() * .8f).height(nTable.getWidth() *.8f);
		nTable.row();

		TextButton zoomInBtn = new TextButton("+", ScreenNavigator.sUISkin, "default");
		zoomInBtn.addListener(zoomBtnClick("+"));
		zoomInBtn.setColor(Color.GREEN);
		nTable.add(zoomInBtn).width(nTable.getWidth() * .8f).height(nTable.getWidth() * .8f);
		this.add(nTable);
	}

	//Button handlers
	private TextButtonClickListener zoomBtnClick(String id)
	{
		return new TextButtonClickListener(id)
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				String id = getId();

				if(id == "-")
					mLineGraph.zoom(1000*60*60);
				else
					mLineGraph.zoom((1000*60*60)*-1);
//				ScreenNavigator.setCurrentScreen(ThermostatGraphScreen.class.getName());
			}
		};
	}
}
