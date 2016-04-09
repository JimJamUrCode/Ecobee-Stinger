package com.billygoatpharmacy.ecobeestinger.display.utils;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.xml.soap.Text;

/**
 * Created by jhard_000 on 4/4/2016.
 */
public class TextButtonClickListener extends ClickListener
{
    private String mId;

    public TextButtonClickListener(String id)
    {
        super();
        mId = id;
    }

    public String getId()
    {
        return mId;
    }
}
