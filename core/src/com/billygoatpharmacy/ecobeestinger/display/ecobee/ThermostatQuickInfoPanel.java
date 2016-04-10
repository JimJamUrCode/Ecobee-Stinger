package com.billygoatpharmacy.ecobeestinger.display.ecobee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.ecobeestinger.display.utils.TextButtonClickListener;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Thermostat;

public class ThermostatQuickInfoPanel extends Table
{
    private ShapeRenderer shapeRenderer;

    public ThermostatQuickInfoPanel()
    {
//        this.setDebug(true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);
        drawLine(batch);
    }

    public void drawLine(Batch batch)
    {
        batch.end();
        Gdx.gl.glLineWidth(2);

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GOLDENROD);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
        batch.begin();
    }

    public void init(Thermostat stat, TextButtonClickListener listener)
    {
        this.setSkin(ScreenNavigator.sUISkin);
        shapeRenderer = new ShapeRenderer();

        Table headerTable = new Table();
        headerTable.setSkin(this.getSkin());
//        headerTable.setDebug(true);
        //Create a thermostat label
        CharSequence txt = stat.name;
        StingerLabel tstatLbl = new StingerLabel(txt, this.getWidth(), null, this.getSkin(), Align.center, true, 1.2f);
        headerTable.add(tstatLbl).height(this.getHeight()*.15f).width(this.getWidth() * .9f).colspan(2);
        headerTable.row();

        //Temperature lbl
        StingerLabel tempLbl = new StingerLabel("" + stat.runtime.actualTemperature / 10, this.getWidth(), null, this.getSkin(), Align.right, true, 3f);
        headerTable.add(tempLbl).height(this.getHeight()*.3f).width(this.getWidth() * .5f).center();

        //Temp Units lbl
        String tempUnitsTxt = "°F";
        if(stat.settings.useCelsius)
            tempUnitsTxt = "°C";
        headerTable.add(tempUnitsTxt, "calibri-font", Color.CHARTREUSE).height(this.getHeight() * .1f).width(this.getWidth() * .5f);
        this.add(headerTable).colspan(2);
        this.row();

        //heat set temp lbl
        StingerLabel desiredHeatTempTxt = new StingerLabel("Desired Heat Temp:", this.getWidth(), null, this.getSkin(), Align.center, true, 1f);
        this.add(desiredHeatTempTxt).height(this.getHeight() * .1f).width(this.getWidth() * .75f).right();
        StingerLabel desireHeatTempLbl = new StingerLabel(stat.runtime.desiredHeat/10 + tempUnitsTxt, this.getWidth(), null, this.getSkin(), Align.center, true, 1f, Color.FIREBRICK);
        this.add(desireHeatTempLbl).height(this.getHeight() * .1f).width(this.getWidth() *.25f).left().center();
        this.row();

        //cool set temp lbl
        StingerLabel desiredCoolTempTxt = new StingerLabel("Desired Cool Temp:", this.getWidth(), null, this.getSkin(), Align.center, true, 1f);
        this.add(desiredCoolTempTxt).height(this.getHeight() * .1f).width(this.getWidth() * .75f).right();
        StingerLabel desiredCoolTempLbl = new StingerLabel(stat.runtime.desiredCool/10 + tempUnitsTxt, this.getWidth(), null, this.getSkin(), Align.center, true, 1f, Color.SKY);
        this.add(desiredCoolTempLbl).height(this.getHeight() * .1f).width(this.getWidth() * .25f).left().center();
        this.row();

        //Humidity lbl
        StingerLabel humidity = new StingerLabel("Humidity:", this.getWidth(), null, this.getSkin(), Align.center, true, 1f);
        this.add(humidity).height(this.getHeight() * .1f).width(this.getWidth() * .75f).right();
        StingerLabel humidityLbl = new StingerLabel(stat.runtime.actualHumidity + "%", this.getWidth(), null, this.getSkin(), Align.center, true, 1f, Color.CORAL);
        this.add(humidityLbl).height(this.getHeight() * .1f).width(this.getWidth() *.25f).left().center();
        this.row();

        //Create a button for the user to click to view more detailed information about this thermostat
        TextButton button = new TextButton("View Details", this.getSkin(), "default");
        button.addListener(listener);
        button.setColor(Color.GREEN);
        this.add(button).height(this.getHeight()*.15f).width(this.getWidth() * .9f).colspan(2);
        this.row();
    }
}
