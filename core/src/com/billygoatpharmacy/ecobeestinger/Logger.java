package com.billygoatpharmacy.ecobeestinger;

import com.badlogic.gdx.utils.Json;

public class Logger 
{
	public Logger()
	{
		
	}
	
	public static void log(String source, String logString)
	{
		if(source == null)
			System.out.println("Source is null");
		
		source = source.substring(source.lastIndexOf(".")+1);
		System.out.println("[" + source + "]: " + logString);
	}
	
	public static void log(String source, String logString, Object objectToStringify)
	{
		Json js = new Json();
		logString += js.toJson(objectToStringify);
		
		if(source == null)
			System.out.println("Source is null");
		
		source = source.substring(source.lastIndexOf(".")+1);
		System.out.println("[" + source + "]: " + logString);
	}
}
