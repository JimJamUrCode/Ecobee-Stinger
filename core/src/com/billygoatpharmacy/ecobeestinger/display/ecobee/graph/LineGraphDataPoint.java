package com.billygoatpharmacy.ecobeestinger.display.ecobee.graph;

import java.util.Date;

/**
 * Created by jhard_000 on 4/6/2016.
 */
public class LineGraphDataPoint
{
    private float mxVal;
    private float myVal;

    public LineGraphDataPoint(float xVal, float yVal)
    {
        mxVal = xVal;
        myVal = yVal;
    }

    public float getXVal()
    {
        return mxVal;
    }

    public float getYVal()
    {
        return myVal;
    }
}
