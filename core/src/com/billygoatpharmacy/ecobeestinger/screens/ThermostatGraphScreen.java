package com.billygoatpharmacy.ecobeestinger.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.Screen;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.display.ecobee.graph.LineGraph;
import com.billygoatpharmacy.ecobeestinger.display.ecobee.graph.LineGraphDataSeries;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.ecobeestinger.display.utils.TextButtonClickListener;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.ThermostatRuntimeResposeData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPIHttpCallback;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.RuntimeReport;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.RuntimeSensorMetadata;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.RuntimeSensorReport;
import com.billygoatpharmacy.fileTools.FileManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ThermostatGraphScreen extends Screen
{
	private LineGraph mLineGraph;
	private Table mGraphTable;
	private Table mRightGraphControlPanel;
	private boolean mCanUpdateGraph;//Variable used to control when the graph visuals can be updated.
	private boolean mCanUpdateGraphData;//Variable used to control when the graph data can be updated.

	private String mThermostatId;//The thermostat id that will be used to make an http request to get data
	private ThermostatRuntimeResposeData mRuntimeData;

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

		mCanUpdateGraph = false;
		mCanUpdateGraphData = false;
//		this.setDebug(true);
	}

	public void setThermostatID(String id)
	{
		mThermostatId = id;
	}

	/**Only call this function once you have the data for the graph lined up.
	 */
	private void setupLineGraph()
	{
		//Setting up controls for the graph
		mRightGraphControlPanel = new Table();
		mRightGraphControlPanel.setWidth(getWidth() * .1f);

		//Setting up the table that the graph and its controls will sit in
		mGraphTable = new Table();
		mGraphTable.add(mLineGraph).height(getHeight() * .85f).width(this.getWidth() * .9f).pad(0, 0, 0, 10);
		mGraphTable.add(mRightGraphControlPanel);

		//Adding everything to the graph table
		addZoomButtons();
		addTimeButtons();


		//Adding to the screen and allowing updates
		this.add(mGraphTable);
		mCanUpdateGraph = true;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);

		if(mCanUpdateGraphData)
			updateGraphData();

		if(mCanUpdateGraph)
			mLineGraph.update(delta);
	}
	
	public void onShow()//Create stuff here
	{
		this.setTitle("Graph", true);
		this.row();

		getThermostatRuntimeReport();
		//getFakeRuntimeReport();
	}
	
	public void onHide()//Destroy stuff here
	{
		
	}

	//Ecobee functions
	private void getFakeRuntimeReport()
	{
		mLineGraph = new LineGraph();
		LineGraph.createTestDataSet(mLineGraph);
		setupLineGraph();
	}
	/**Gets the runtime report for the current thermostat
	 *retreives the last 30 days of data, including remote sensors
	 */
	private void getThermostatRuntimeReport()
	{
		EcobeeAPI.getThermostatRuntimeReport(getThermostatRuntimeReportCallback(), mThermostatId);
	}

	private EcobeeAPIHttpCallback getThermostatRuntimeReportCallback()
	{
		return new EcobeeAPIHttpCallback() {
			@Override
			public void done(String response)
			{

				mRuntimeData = mJson.fromJson(ThermostatRuntimeResposeData.class, response);

				if(mRuntimeData.error != null && mRuntimeData.error != "") {
					Logger.log(this.getClass().getName(), "Error Getting Thermostat Data: " + mJson.toJson(mRuntimeData));
				}
				else
				{
					Logger.log(this.getClass().getName(), "Ecobee Thermostats Received...");

					mCanUpdateGraphData = true;
				}
			}
		};
	}

	//Visual Functions
	private void updateGraphData()
	{
		mCanUpdateGraphData = false;
		mLineGraph = new LineGraph();

		ArrayList<LineGraphDataSeries> allDataSeries = new ArrayList<LineGraphDataSeries>();

		float maxX = 0;
		float minX = 0;
		float maxY = 0;
		float minY = 0;

		RuntimeReport currentReport;
		String[] csvColumns = mRuntimeData.columns.split(",");

		//Create our data series based on the number of columns we have received
		for(int g = 0; g < csvColumns.length; g++)
		{
			if(csvColumns[g].contains("zone") == false)
				allDataSeries.add(new LineGraphDataSeries(csvColumns[g], LineGraph.LINECOLORS[g]));
		}

		RuntimeSensorMetadata[] md = mRuntimeData.sensorList[0].sensors;
		int colorsChosen = allDataSeries.size();
		for(int h = 0; h < md.length; h++)
			allDataSeries.add(new LineGraphDataSeries(md[h].sensorName, LineGraph.LINECOLORS[colorsChosen + h]));


		for(int i = 0; i < mRuntimeData.reportList.length; i++)
		{
			currentReport = mRuntimeData.reportList[i];

			for(int irow = 0; irow < currentReport.rowCount; irow++)
			{
				String[] currentRow = currentReport.rowList[irow].split(",");

				//Parse the current date
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				float currDate;
				try {
					currDate = format.parse(currentRow[0] + " " + currentRow[1]).getTime();
				} catch (ParseException e) {
					currDate = new Date().getTime();
				}

				if (irow == 0) {
					maxX = currDate;
					minX = currDate;
				}
				if (currDate > maxX)
					maxX = currDate;
				if (currDate < minX)
					minX = currDate;

				float currYVal;

				for(int rowVar = 2; rowVar < currentRow.length; rowVar++)
				{
//					if(currentReport.rowList[irow].contains(",,"))
//						Logger.log(this.getClass().getName(), "Val: " + currentReport.rowList[irow]);
					if(currentRow[rowVar] == null || currentRow[rowVar].isEmpty())
						currYVal = 0f;//In the future, need to find the surrounding average TODO
					else
						currYVal = Float.parseFloat(currentRow[rowVar]);
					allDataSeries.get(rowVar-2).addDataPoint(currDate, currYVal);

					if (irow == 0) {
						maxY = currYVal;
						minY = currYVal;
					}
					if (currYVal > maxY)
						maxY = currYVal;
					if (currYVal < minY)
						minY = currYVal;
				}
			}
		}
		mLineGraph.init(maxX, minX, maxY, minY, allDataSeries);
		setupLineGraph();
//		LineGraph.createTestDataSet(mLineGraph);
	}

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
