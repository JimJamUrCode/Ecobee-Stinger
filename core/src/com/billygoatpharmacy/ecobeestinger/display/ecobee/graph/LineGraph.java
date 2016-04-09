package com.billygoatpharmacy.ecobeestinger.display.ecobee.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.display.utils.TextButtonClickListener;

import java.util.ArrayList;
import java.util.Calendar;


public class LineGraph extends Table
{
    public enum MONTH{
        January, February, March, April, May,June, July, August, September, October, November, December
    }

    private static final Color[] LINECOLORS = {Color.MAGENTA, Color.LIME, Color.ROYAL, Color.FIREBRICK};
    private static final float GRAPHLEFTPADDING = 120;
    private static final float GRAPHTOPPADDING = 30f;
    private static final int NUMYAXISLABELS = 5;

    private ShapeRenderer shapeRenderer;
    Matrix4 mFontMatrix = new Matrix4();
    SpriteBatch mSpriteFontBatch;

    private ArrayList<LineGraphDataSeries> mAllDataSeries;
    private boolean mShouldDrawGraph;
    MONTH[] mMonths = MONTH.values();

    //Graph axis variables
    private float mTempMaxX;//Used as a max for the graph window. This allows graph zooming
    private float mMaxX;
    private float mMinX;
    private float mMaxY;
    private float mMinY;
    private float mGraphX;
    private float mGraphY;
    private float mGraphWidth;
    private float mGraphHeight;
    private final float mLineWidth = 2;
    private float mZeroDate;//The date that will be represented on the graph as x = 0
    private float mGraphTimeWindow = ((1000*60)*60*24)/6;

    //Font for drawing labels that are present on the graph
    BitmapFont mYAxisLabelFont = new BitmapFont(Gdx.files.internal("Arial.fnt"), false);
    BitmapFont mXAxisLabelFont = new BitmapFont(Gdx.files.internal("Arial.fnt"), false);

    public LineGraph()
    {
        super();
        super.addListener(graphTouchedListener("graph"));
        this.setTouchable(Touchable.enabled);
        mShouldDrawGraph = false;
        //this.setDebug(true);
        mFontMatrix = new Matrix4();
        mSpriteFontBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    public void update(float delta)
    {
//        drawXAxisLabels();
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);

        if(mShouldDrawGraph)
        {
            //mShouldDrawGraph = false;

            drawYAxisLabels(batch);
            drawXAxisLabels(batch);

            batch.end();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(mLineWidth);
            Gdx.gl.glEnable(GL30.GL_BLEND);

            //Get variables that are commonly used once here
            float tempx = getX();
            float tempy = getY();
            float tempw = getWidth();
            float temph = getHeight();

            this.setBounds(tempx, tempy, tempw, temph);

            //Call all of our drawing functions
            drawBorder(tempx, tempy, tempw, temph);
            drawXandYAxis(tempx, tempy, tempw, temph);
//            drawAllXAxisIndicators();
            drawAllYAxisIndicators();
            drawAllDataSeries();
            drawXAxisIndicatorLine(mGraphX + (mGraphWidth / 2), Color.WHITE, false);

            shapeRenderer.end();
            batch.begin();
        }
    }

    private void drawBorder(float tempx, float tempy, float tempw, float temph) {
        shapeRenderer.setColor(Color.GOLDENROD);
        shapeRenderer.rect(tempx, tempy, tempw, temph);
    }

    private void drawXandYAxis(float tempx, float tempy, float tempw, float temph)
    {
        shapeRenderer.setColor(Color.ORANGE);

        mGraphX = tempx + GRAPHLEFTPADDING;
        mGraphY = tempy + temph * .12f;

        mGraphWidth = tempw - GRAPHLEFTPADDING;
        mGraphHeight = (temph * .88f) - GRAPHTOPPADDING;

        shapeRenderer.rect(mGraphX, mGraphY, mGraphWidth, mGraphHeight);
    }

    private void drawXAxisLabels(Batch batch)
    {
//        mFontMatrix.setToRotation(new Vector3(200, 200, 0), 180);
//        batch.setTransformMatrix(mFontMatrix);
        //Setting up y axis labels
        int numLabels = 2;
        float xPxPerMs = mGraphWidth /  mGraphTimeWindow;
        String tempTxt = "" + (int)mMinX;
        float unitsPerLbl = ((mTempMaxX -mZeroDate) - (mMinX-mZeroDate))/numLabels;

        tempTxt = getDateText((long) (mZeroDate + (0 * unitsPerLbl)));
        float estimatedWidth = mGraphWidth / 3;//tempTxt.length() * 20f;
        mXAxisLabelFont.setColor(Color.WHITE);
        mXAxisLabelFont.draw(batch, tempTxt, mGraphX, mGraphY - mYAxisLabelFont.getLineHeight(), estimatedWidth, Align.left, false);

        tempTxt = getDateText((long) (mZeroDate + (1 * unitsPerLbl)));
        estimatedWidth = mGraphWidth / 3;
        mXAxisLabelFont.setColor(Color.WHITE);
        mXAxisLabelFont.draw(batch, tempTxt, mGraphX + (mGraphWidth / 2) - estimatedWidth / 2, mGraphY - mYAxisLabelFont.getLineHeight(), estimatedWidth, Align.center, false);

        tempTxt = getDateText((long) (mZeroDate + (2 * unitsPerLbl)));
        estimatedWidth = mGraphWidth / 3;
        mXAxisLabelFont.setColor(Color.WHITE);
        mXAxisLabelFont.draw(batch, tempTxt, mGraphX + mGraphWidth - estimatedWidth, mGraphY - mYAxisLabelFont.getLineHeight(), estimatedWidth, Align.right, false);
    }

    private void drawAllXAxisIndicators()
    {
        int numLines = 4;
        float gapPerLine = (mGraphWidth/2) / numLines;
        float centerX = mGraphX + (mGraphWidth/2);

        for(int i = 1; i < numLines; i++)
        {
            drawXAxisIndicatorLine(centerX + (i * gapPerLine), Color.LIGHT_GRAY, true);
            drawXAxisIndicatorLine((centerX + (i * gapPerLine)*-1), Color.LIGHT_GRAY, true);
        }
    }

    private void drawXAxisIndicatorLine(float xVal, Color color, Boolean dashed)
    {
        if(dashed) {
            int dashHeight = 10;
            int dashGap = 15;
            float numDashes = (mGraphHeight - (mLineWidth * 2)) / (dashHeight + dashGap);
            float lastY = mGraphY + mLineWidth;
            float calculatedLineLength;
            shapeRenderer.setColor(new Color(color.r, color.g, color.b, .1f));
            for (int i = 0; i < numDashes; i++) {
                calculatedLineLength = lastY + dashHeight;
                if (mGraphY + mGraphHeight - mLineWidth < calculatedLineLength)
                    calculatedLineLength = mGraphY + mGraphHeight;
                shapeRenderer.rectLine(xVal, lastY, xVal, calculatedLineLength, .2f);
                lastY += dashHeight + dashGap;
            }
        }
        else {
            shapeRenderer.setColor(new Color(color.r, color.g, color.b, 1.0f));
            shapeRenderer.rectLine(xVal, mGraphY, xVal, mGraphY + mGraphHeight , .2f);
        }
    }

    private void drawAllYAxisIndicators()
    {
        float unitsPerLbl = (mMaxY - mMinY)/NUMYAXISLABELS;
        float yMinMaxDiff = (mMaxY - mMinY);
        float yPxPerDeg = mGraphHeight /  yMinMaxDiff;
        int startIndex = 1;
        for(int i = startIndex; i < NUMYAXISLABELS; i++)
        {
            float degsDiff = (i*unitsPerLbl);
            drawYAxisIndicatorLine(mGraphY + (degsDiff*yPxPerDeg), Color.LIGHT_GRAY, true);
//            mGraphY + (mYAxisLabelFont.getLineHeight()/2)-mLineWidth*2 + (degsDiff*yPxPerDeg)
        }
    }

    private void drawYAxisIndicatorLine(float yVal, Color color, Boolean dashed)
    {
        if(dashed) {
            int dashWidth = 10;
            int dashGap = 15;
            float numDashes = (mGraphWidth - (mLineWidth * 2)) / (dashWidth + dashGap);
            float lastX = mGraphX + mLineWidth;
            float calculatedLineLength;
            shapeRenderer.setColor(new Color(color.r, color.g, color.b, .1f));
            for (int i = 0; i < numDashes; i++) {
                calculatedLineLength = lastX + dashWidth;
                if (mGraphX + mGraphWidth - mLineWidth < calculatedLineLength)
                    calculatedLineLength = mGraphX + mGraphWidth;
                shapeRenderer.rectLine(lastX, yVal, calculatedLineLength, yVal, .2f);
                lastX += dashWidth + dashGap;
            }
        }
        else {
            shapeRenderer.setColor(new Color(color.r, color.g, color.b, 1.0f));
            shapeRenderer.rectLine(mGraphX, yVal, mGraphX + mGraphWidth, yVal , .2f);
        }
    }
    private void drawYAxisLabels(Batch batch)
    {
        //mYAxisLabelFont.setScale(.2f);
        //Setting up y axis labels

        float yMinMaxDiff = (mMaxY - mMinY);
        float yPxPerDeg = mGraphHeight /  yMinMaxDiff;
        String tempTxt = "" + (int)mMinY;
        float unitsPerLbl = (mMaxY - mMinY)/NUMYAXISLABELS;

        int redStart = 0;//0xFFFF00;
        int redStop = 255;
        if(mMaxY >= 90)
            redStop = 255;
        else if(mMaxY > 80)
            redStop = 150;
        else if(mMaxY > 70)
            redStop = 125;
        else if(mMaxY > 60)
            redStop = 75;
        else
            redStop = 0;

        if(mMinY >= 70)
            redStart = 255;

        int perunitred = Math.abs(redStart-redStop) / (NUMYAXISLABELS/2);
        int redDirection = 1;

        int greenStart = 90;
        int greenStop = 255;
        int greenDirection = 1;
        boolean greverse = false;
        int perunitgreen = Math.abs(greenStart - greenStop) / (NUMYAXISLABELS/2);
        if(mMaxY > 90)
            greverse = true;
        else if(mMaxY > 80)
            greverse = true;
        else if (mMaxY > 70)
            greverse = false;

        if(mMinY >= 70)
        {
            greenStart = 255;
            greenStop = 0;
            greverse = false;
            greenDirection = -1;
            perunitgreen = Math.abs(greenStart - greenStop) / NUMYAXISLABELS;
        }




        int greenMultiplier;

        int blueStart = 0;
        int blueStop = 0;
        if(mMinY > 70)
        {

        }
        else if(mMinY > 60)
            blueStop = 190;
        else if(mMinY > 50)
        {
            blueStart = 255;
            blueStop = 175;
        }
        else if(mMinY > 40)
        {
            blueStart = 255;
            blueStop = 200;
        }
        else if(mMaxY < 50)
        {
            blueStart = 255;
            blueStop = 200;
        }
        else
        {
            blueStart = 255;
            blueStop = 0;
        }

        int blueDirection = -1;
        int perunitblue = Math.abs(blueStart - blueStop) / (NUMYAXISLABELS/2);


        int startIndex = 0;
        greenMultiplier = startIndex;
        for(int i = startIndex; i <= NUMYAXISLABELS; i++)
        {
            tempTxt = "" + ((int)(mMinY + (i*unitsPerLbl)) + " -");
            float degsDiff = (i*unitsPerLbl);
            float estimatedWidth = tempTxt.length() * 20f;

            int newR = redStart + (redDirection* perunitred)*i;

            int newG = greenStart + (greenDirection * perunitgreen) * greenMultiplier;
            if (greenDirection == 1 && newG >= greenStop) {
                newG = greenStop;
                if (greverse)
                {
                    greenStop = greenStart;
                    greenStart = newG;
                    greenMultiplier = 0;
                    greenDirection *= -1;
                }
            }else if(greenDirection == -1 && newG <= greenStop)
                newG = greenStop;

            greenMultiplier++;

            int newB = blueStart + (blueDirection* perunitblue)*i;

            Color newC = new Color(toRGB(newR, newG, newB,1.0f));

            mYAxisLabelFont.setColor(newC);
            mYAxisLabelFont.draw(batch, tempTxt, mGraphX-estimatedWidth,mGraphY + (mYAxisLabelFont.getLineHeight()/2)-mLineWidth*2 + (degsDiff*yPxPerDeg),0,tempTxt.length(),estimatedWidth, Align.center, false);
        }
    }

    public void init(float maxX, float minX, float maxY, float minY, ArrayList<LineGraphDataSeries> data)
    {
        mTempMaxX = maxX;
        mMaxX = maxX;
        mMinX = minX;
        mMaxY = maxY;
        mMinY = minY;
        mAllDataSeries = data;
        mZeroDate = mMinX;
        mTempMaxX = mZeroDate + mGraphTimeWindow;
        mShouldDrawGraph = true;
    }

    public void zoom(int amount)
    {
        mGraphTimeWindow += amount;

        float diff;
        if(mGraphTimeWindow < (1000*60*60))
        {
            diff = (1000*60*60) - mGraphTimeWindow;
            mGraphTimeWindow = (1000*60*60);
            amount = 0;
            mTempMaxX = mZeroDate + mGraphTimeWindow;
        }
        else if(mGraphTimeWindow > (1000*60*60)*24)
        {
            mGraphTimeWindow = (1000*60*60)*24;
            amount = 0;
            mTempMaxX = mZeroDate + mGraphTimeWindow;
        }
        else
        {
            mZeroDate -= amount / 2;
            mTempMaxX += amount / 2;
        }
    }

    public void scroll(float amount)
    {
        mZeroDate += amount;
        mTempMaxX = mZeroDate + mGraphTimeWindow;

    }

    private void drawAllDataSeries()
    {
        ArrayList<LineGraphDataPoint> dps;

        //Setting up x axis variables
        float xPxPerms = mGraphWidth / mGraphTimeWindow;

        //Setting up y axis variables
        float yPxPerDeg = mGraphHeight / (mMaxY - mMinY);

        //Setting up loop variables
        int numDataSeries = mAllDataSeries.size();
        int dataPointsLength;

        for(int i = 0; i < numDataSeries; i++)
        {
            dps = mAllDataSeries.get(i).getAllDataPoints();
            dataPointsLength = dps.size();

            float mLastXPos = mGraphX;
            float mLastYPos = mGraphY;
            for(int p = 0; p < dataPointsLength; p++)
            {
                float currX = dps.get(p).getXVal();
                float currY = dps.get(p).getYVal();

                //Check to see if the date fall in the visible window
                if(currX >= mZeroDate && currX <= (mZeroDate + mGraphTimeWindow))
                {
                    //calculate the difference from zerodate
                    float zeroDateDiffms = currX - mZeroDate;
                    float newX;
                    if(p == 0)
                        newX = (xPxPerms * zeroDateDiffms) + mGraphX;
                    else
                        newX = (xPxPerms * zeroDateDiffms) + mGraphX;

                    float newY;
                    if(p == 0)
                        mLastYPos = ((currY - mMinY) * yPxPerDeg) + mGraphY;

                    newY = ((currY - mMinY) * yPxPerDeg) + mGraphY;

                    shapeRenderer.setColor(LINECOLORS[i]);
                    shapeRenderer.rectLine(mLastXPos, mLastYPos, newX, newY, 1);

                    shapeRenderer.setColor(LINECOLORS[i+1]);
                    shapeRenderer.circle(newX, newY, 4);
                    mLastXPos = newX;
                    mLastYPos = newY;
                }
            }
        }
    }

    private float mStartDragX;

    //Button handlers
    public InputListener graphTouchedListener(String id)
    {
        return new InputListener()
        {
//            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                mStartDragX = x;
                return true;
            }

//            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer)
            {
                float diff = mStartDragX - x;//difference in pixels
                mZeroDate += diff * 1000*60;
                mTempMaxX = mZeroDate + mGraphTimeWindow;

                mStartDragX = x;

                if(mZeroDate < mMinX)
                {
                    mZeroDate = mMinX;
                    mTempMaxX = mZeroDate + mGraphTimeWindow;
                }
                else if(mZeroDate + mGraphTimeWindow > mMaxX)
                {
                    mZeroDate = mMaxX - mGraphTimeWindow;
                    mTempMaxX = mZeroDate + mGraphTimeWindow;
                }

                Logger.log(this.getClass().getName(), "movement diff: " + diff);
            }
        };
    }

    //Helper Functions
    public static void createTestDataSet(LineGraph graph)
    {
        LineGraphDataPoint dp;
        LineGraphDataSeries ds;
        ArrayList<LineGraphDataSeries> allDataSeries = new ArrayList<LineGraphDataSeries>();

        float maxX = 0;
        float minX = 0;
        float maxY = 0;
        float minY = 0;

        for(int k = 0; k < 3; k++)
        {
            ds = new LineGraphDataSeries();
            float currentTime = TimeUtils.millis();
            float fiveminutes = (1000 * 60) * 5;
            for (int i = 8640; i > 0; i--)//One days worth of logs
            {
                float randomTemp = ((float) Math.random() * (39f - 36f + 1) + 36f);
                if (k == 0) {
                    maxX = currentTime;
                    minX = currentTime;
                    maxY = randomTemp;
                    minY = randomTemp;
                }

                if (currentTime - (fiveminutes * i) > maxX)
                    maxX = currentTime - (fiveminutes * i);
                if (currentTime - (fiveminutes * i) < minX)
                    minX = currentTime - (fiveminutes * i);

                if (randomTemp > maxY)
                    maxY = randomTemp;
                if (randomTemp < minY)
                    minY = randomTemp;

                ds.addDataPoint(currentTime - (fiveminutes * i), randomTemp);
            }

            allDataSeries.add(ds);
        }

        graph.init(maxX, minX, maxY, minY, allDataSeries);
    }

    private String getDateText(long millisecondDate)
    {
        float oneday = 1000*60*60*24;
        Calendar tempDate = Calendar.getInstance();
        tempDate.setTimeInMillis(millisecondDate);
        float xMinMaxDiff = (mTempMaxX - mMinX);

        String tempTxt;
//        if(xMinMaxDiff < oneday )
//        {
        String month = "" + mMonths[tempDate.get(Calendar.MONTH)];
        String day = "" + tempDate.get(Calendar.DAY_OF_MONTH);
        String min = "" + tempDate.get(Calendar.MINUTE);

        tempTxt = month + " " + day + ", ";
        if(min.length() < 2)
            min ="0" + min;

        tempTxt += "" + tempDate.get(Calendar.HOUR) + ":" + min;

        if(tempDate.get(Calendar.AM_PM) == 0)
            tempTxt += " AM";
        else
            tempTxt += " PM";
//        }
//        else
//        {
//            tempTxt = "" + tempDate.get(Calendar.DAY_OF_MONTH);
//            tempTxt += "/" + tempDate.get(Calendar.MONTH);
//            tempTxt += "/" + tempDate.get(Calendar.YEAR);
//        }
        return tempTxt;
    }

    private Color toRGB(int r, int g, int b, float a) {
        float RED = r / 255.0f;
        float GREEN = g / 255.0f;
        float BLUE = b / 255.0f;
        return new Color(RED, GREEN, BLUE, a);
    }
}
