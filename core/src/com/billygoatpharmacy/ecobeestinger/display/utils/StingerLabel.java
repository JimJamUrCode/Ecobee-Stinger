package com.billygoatpharmacy.ecobeestinger.display.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class StingerLabel extends Label 
{
	public StingerLabel(CharSequence text, Float nWidth, Float nHeight, Skin skin, int align, Boolean wrap, float scale)
	{
		super(text, skin);

		this.setFontScale(scale);
		this.setAlignment(align);
		this.setWrap(wrap);

		if(nWidth != null)
			this.setWidth(nWidth);

		if(nHeight != null)
			this.setHeight(nHeight);
	}

	public StingerLabel(CharSequence text, Float nWidth, Float nHeight, Skin skin, int align, Boolean wrap, float scale, Color color)
	{
		super(text, skin);
		
		this.setFontScale(scale);
		this.setAlignment(align);
		this.setWrap(wrap);
		this.setColor(color);

		if(nWidth != null)
			this.setWidth(nWidth);

		if(nHeight != null)
			this.setHeight(nHeight);
	}
}
