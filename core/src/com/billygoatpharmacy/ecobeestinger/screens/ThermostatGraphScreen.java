package com.billygoatpharmacy.ecobeestinger.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.display.ecobee.graph.LineGraph;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.ecobeestinger.display.utils.TextButtonClickListener;

public class ThermostatGraphScreen extends Screen
{
	private LineGraph mLineGraph;
	private Table mRightGraphControlPanel;

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
		mRightGraphControlPanel = new Table();
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
		this.add(mLineGraph).height(mStage.getHeight() * .55f).width(this.getWidth() * .9f).pad(0, 0, 0, 10);

		mRightGraphControlPanel.setWidth(getWidth() * .1f);
		this.add(mRightGraphControlPanel);
		addZoomButtons();
		addTimeButtons();
//		getAllThermostats();
	}
	
	public void onHide()//Destroy stuff here
	{
		
	}
	
	//Visual Functions
	private void addZoomButtons()
	{
		float panelW = mRightGraphControlPanel.getWidth();

		CharSequence txt = "Zoom:";
		StingerLabel authDescriptionLbl = new StingerLabel(txt, panelW, null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
		mRightGraphControlPanel.add(authDescriptionLbl);
		mRightGraphControlPanel.row();

		TextButton zoomOutBtn = new TextButton("-", ScreenNavigator.sUISkin, "default");
		zoomOutBtn.addListener(zoomBtnClick("-"));
		zoomOutBtn.setColor(Color.GREEN);
		mRightGraphControlPanel.add(zoomOutBtn).width(mRightGraphControlPanel.getWidth() * .8f).height(panelW * .8f);
		mRightGraphControlPanel.row();

		TextButton zoomInBtn = new TextButton("+", ScreenNavigator.sUISkin, "default");
		zoomInBtn.addListener(zoomBtnClick("+"));
		zoomInBtn.setColor(Color.GREEN);
		mRightGraphControlPanel.add(zoomInBtn).width(mRightGraphControlPanel.getWidth() * .8f).height(panelW * .8f);
	}

	private void addTimeButtons()
	{
		float panelW = mRightGraphControlPanel.getWidth();
		mRightGraphControlPanel.row();

		CharSequence txt = "Time:";
		StingerLabel authDescriptionLbl = new StingerLabel(txt, panelW, null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
		mRightGraphControlPanel.add(authDescriptionLbl);
		mRightGraphControlPanel.row();

		TextButton zoomOutBtn = new TextButton("-", ScreenNavigator.sUISkin, "default");
		zoomOutBtn.addListener(timeBtnClick("-"));
		zoomOutBtn.setColor(Color.GREEN);
		mRightGraphControlPanel.add(zoomOutBtn).width(mRightGraphControlPanel.getWidth() * .8f).height(panelW * .8f);
		mRightGraphControlPanel.row();

		TextButton zoomInBtn = new TextButton("+", ScreenNavigator.sUISkin, "default");
		zoomInBtn.addListener(timeBtnClick("+"));
		zoomInBtn.setColor(Color.GREEN);
		mRightGraphControlPanel.add(zoomInBtn).width(mRightGraphControlPanel.getWidth() * .8f).height(panelW * .8f);
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

	private TextButtonClickListener timeBtnClick(String id)
	{
		return new TextButtonClickListener(id)
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				String id = getId();

				if(id == "-")
					mLineGraph.scroll(1000 * 60*1.1f*-1);
				else
					mLineGraph.scroll((1000*60)*1.1f);
//				ScreenNavigator.setCurrentScreen(ThermostatGraphScreen.class.getName());
			}
		};
	}
}
