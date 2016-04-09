package com.billygoatpharmacy.ecobeestinger.display.ecobee.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class LineGraph extends Table
{
    private static final Color[] LINECOLORS = {Color.MAGENTA, Color.LIME, Color.ROYAL, Color.FIREBRICK};
    private static final float GRAPHLEFTPADDING = 120;//Inner portion of graph padding
    private static final float GRAPHTOPPADDING = 30f;//Inner portion of graph padding
    private static final int NUMYAXISLABELS = 5;

    private ShapeRenderer shapeRenderer;//The renderer used to draw all lines on the graph

    private ArrayList<LineGraphDataSeries> mAllDataSeries;//All of the data that this graph can potentially show

    private boolean mShouldDrawGraph;//If the graph is ready to be drawn

    //Graph axis variables
    private float mTempMaxX;//Used as a max for the graph window. This allows graph zooming
    private float mMaxX;//The max X number in the entire data set.
    private float mMinX;//The min X number in the entire data set.
    private float mMaxY;//The max Y number in the entire data set.
    private float mMinY;//The min Y number in the entire data set.
    private float mGraphX;//The lower left X position of the inner portion of the graph, point (0,0)
    private float mGraphY;//The lower left Y position of the inner portion of the graph, point (0,0)
    private float mGraphWidth;//The width of the inner graph
    private float mGraphHeight;//The height of the inner graph
    private final float mLineWidth = 2;//The line thickness of the shape renderer used to draw lines.
    private float mZeroDate;//The date that will be represented on the graph as x = 0
    private float mGraphTimeWindow = ((1000*60)*60*24)/6;//Show 4 hour time window to the user at the start
    private float mXPixelsPerMs;//The number of pixels to move for 1 millisecond of time currently displayed on the graph
    private float mYPixelsPerDeg;//The number of pixels to move for 1 degree displayed on the graph

    //Font for drawing labels that are present on the graph
    BitmapFont mAxisLabelFont = new BitmapFont(Gdx.files.internal("Arial.fnt"), false);

    //Vars used for scrolling
    private float mStartDragX;

    public LineGraph()
    {
        super();
        super.addListener(graphTouchedListener("graph"));
        this.setTouchable(Touchable.enabled);
        mShouldDrawGraph = false;
        //this.setDebug(true);
        shapeRenderer = new ShapeRenderer();
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

    //X-Axis drawables
    private void drawXAxisLabels(Batch batch)
    {
        mAxisLabelFont.setColor(Color.WHITE);
        float estimatedWidth = mGraphWidth / 3;

        String tempTxt = getMonthDayTimeText((long)mZeroDate);
        mAxisLabelFont.draw(batch, tempTxt, mGraphX, mGraphY - mAxisLabelFont.getLineHeight(), estimatedWidth, Align.left, false);

        tempTxt = getMonthDayTimeText((long) (mZeroDate + mGraphTimeWindow / 2));
        mAxisLabelFont.draw(batch, tempTxt, mGraphX + (mGraphWidth / 2) - estimatedWidth / 2, mGraphY - mAxisLabelFont.getLineHeight(), estimatedWidth, Align.center, false);

        tempTxt = getMonthDayTimeText((long) (mZeroDate + mGraphTimeWindow));
        mAxisLabelFont.draw(batch, tempTxt, mGraphX + mGraphWidth - estimatedWidth, mGraphY - mAxisLabelFont.getLineHeight(), estimatedWidth, Align.right, false);
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

    //Y-Axis drawables
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

    //This function is disgusting and needs to be re-written//TODO:
    private void drawYAxisLabels(Batch batch)
    {
        //mAxisLabelFont.setScale(.2f);
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

            mAxisLabelFont.setColor(newC);
            mAxisLabelFont.draw(batch, tempTxt, mGraphX-estimatedWidth,mGraphY + (mAxisLabelFont.getLineHeight()/2)-mLineWidth*2 + (degsDiff*yPxPerDeg),0,tempTxt.length(),estimatedWidth, Align.center, false);
        }
    }

    //Public graph manipulation functions
    public void zoom(int amount)
    {
        mGraphTimeWindow += amount;

        float oneHour = (1000*60*60);
        float twentyFourHours = oneHour * 24;

        if(mGraphTimeWindow < oneHour)
        {
            mGraphTimeWindow = oneHour;
            mTempMaxX = mZeroDate + mGraphTimeWindow;
        }
        else if(mGraphTimeWindow > twentyFourHours)
        {
            mGraphTimeWindow = twentyFourHours;
            mTempMaxX = mZeroDate + mGraphTimeWindow;
        }
        else
        {
//            if(amount > 0)//Zooming out, ie adding time to the display window
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

        mXPixelsPerMs = mGraphWidth / mGraphTimeWindow;

        //Setting up y axis variables
        mYPixelsPerDeg = mGraphHeight / (mMaxY - mMinY);

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

                //Check to see if the date falls in the visible window
                if(currX >= mZeroDate && currX <= (mZeroDate + mGraphTimeWindow))
                {
                    //calculate the difference from zerodate
                    float zeroDateDiffms = currX - mZeroDate;
                    float newX = (mXPixelsPerMs * zeroDateDiffms) + mGraphX;

                    float newY;
                    if(p == 0)
                        mLastYPos = ((currY - mMinY) * mYPixelsPerDeg) + mGraphY;

                    newY = ((currY - mMinY) * mYPixelsPerDeg) + mGraphY;

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
                mZeroDate += diff * 1000*60;// Move by a factor of one minute
                mTempMaxX = mZeroDate + mGraphTimeWindow;

                mStartDragX = x;
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

    private String getMonthDayTimeText(long millisecondDate)
    {
        float oneday = 1000*60*60*24;
        Calendar tempDate = Calendar.getInstance();
        tempDate.setTimeInMillis(millisecondDate);

        SimpleDateFormat formatter=new SimpleDateFormat("MMM dd, K:mm a");

        return formatter.format(tempDate.getTime());
    }

    private Color toRGB(int r, int g, int b, float a) {
        float RED = r / 255.0f;
        float GREEN = g / 255.0f;
        float BLUE = b / 255.0f;
        return new Color(RED, GREEN, BLUE, a);
    }
}
