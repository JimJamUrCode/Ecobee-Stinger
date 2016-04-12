package com.billygoatpharmacy.ecobeestinger.display.ecobee.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.billygoatpharmacy.ecobeestinger.display.ScreenNavigator;
import com.billygoatpharmacy.ecobeestinger.display.utils.StingerLabel;
import com.billygoatpharmacy.ecobeestinger.display.utils.TextButtonClickListener;

/**
 * Created by jhard_000 on 4/11/2016.
 */
public class LineGraphSeriesLabel extends CheckBox
{
    private LineGraphDataSeries mDataSeries;
    private Color mLabelColor;
    private StingerLabel mLabel;
    private CheckBox mCheckbox;

    public LineGraphSeriesLabel(String text, Skin skin, Color color, LineGraphDataSeries dataSeries)
    {
        super(text, skin);
        this.mDataSeries = dataSeries;
        this.mLabelColor = color;

        //Creating the checkbox protion of this label
        TextureAtlas textureAtlas = skin.getAtlas();
        TextureRegion fontRegion = textureAtlas.findRegion("Arial");

        BitmapFont font = new BitmapFont(Gdx.files.internal("Arial.fnt"), fontRegion);
        TextureRegion upstate = textureAtlas.findRegion("checkboxChecked");
        TextureRegion downstate = textureAtlas.findRegion("checkboxUnchecked");

        CheckBoxStyle style = new CheckBoxStyle();
        style.font = font;
        style.fontColor = mLabelColor;
        style.checkboxOn = new TextureRegionDrawable(upstate);
        style.checkboxOff = new TextureRegionDrawable(downstate);

        this.setStyle(style);
        this.setChecked(true);
        this.addListener(chhkbxClicked());
        //Creating the text portion of this label

//        mLabel = new StingerLabel(label, null, null, ScreenNavigator.sUISkin, Align.center, true, 1.2f);
//        mLabel.setColor(mLabelColor);
    }

    private ClickListener chhkbxClicked()
    {
        return new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(mDataSeries.getmIsVisible() == true)
                    mDataSeries.setmIsVisible(false);
                else
                    mDataSeries.setmIsVisible(true);

            }
        };
    }
}
