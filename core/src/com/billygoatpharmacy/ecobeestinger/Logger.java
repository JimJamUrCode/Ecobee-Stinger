package com.billygoatpharmacy.ecobeestinger;

public class Logger 
{
	public Logger()
	{
		
	}
	
	public static void log(String source, String logString)
	{
		System.out.println("[" + source + "]: " + logString);
	}
}
