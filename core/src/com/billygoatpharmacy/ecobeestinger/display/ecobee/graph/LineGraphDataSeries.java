package com.billygoatpharmacy.ecobeestinger.display.ecobee.graph;

import com.badlogic.gdx.graphics.Color;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;

import java.util.ArrayList;

/**
 * Created by jhard_000 on 4/6/2016.
 */
public class LineGraphDataSeries
{
    private ArrayList<LineGraphDataPoint> mDataPoints;
    private String mSeriesName;
    private Boolean mIsVisible;
    private Color mSeriesColor;

    public LineGraphSeriesLabel mLegendLbl;

    public LineGraphDataSeries(String seriesName, Color seriesColor)
    {
        super();
        mDataPoints = new ArrayList<LineGraphDataPoint>();
        mSeriesName = seriesName;
        mSeriesColor = seriesColor;
        mIsVisible = true;
        mLegendLbl = new LineGraphSeriesLabel(mSeriesName, ScreenNavigator.sUISkin, mSeriesColor, this);
    }

    public void addDataPoint(float xVal, float yVal)
    {
        mDataPoints.add(new LineGraphDataPoint(xVal, yVal));
    }

    public String getmSeriesName()
    {
        return mSeriesName;
    }

    public Color getSeriesColor()
    {
        return mSeriesColor;
    }

    public ArrayList<LineGraphDataPoint> getAllDataPoints()
    {
        return mDataPoints;
    }

    //Getters and Setters
    public void setmIsVisible(Boolean mIsVisible) {
        this.mIsVisible = mIsVisible;
    }

    public Boolean getmIsVisible() {
        return mIsVisible;
    }
}
