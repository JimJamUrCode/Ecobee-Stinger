package com.billygoatpharmacy.ecobeestinger.display.ecobee.graph;

import java.util.ArrayList;

/**
 * Created by jhard_000 on 4/6/2016.
 */
public class LineGraphDataSeries
{
    private ArrayList<LineGraphDataPoint> mDataPoints;

    public LineGraphDataSeries()
    {
        super();
        mDataPoints = new ArrayList<LineGraphDataPoint>();
    }

    public void addDataPoint(float xVal, float yVal)
    {
        mDataPoints.add(new LineGraphDataPoint(xVal, yVal));
    }

    public ArrayList<LineGraphDataPoint> getAllDataPoints()
    {
        return mDataPoints;
    }
}
