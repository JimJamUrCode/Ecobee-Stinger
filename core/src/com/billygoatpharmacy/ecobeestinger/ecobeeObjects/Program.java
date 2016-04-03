package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Program 
{
	public String[][] schedule;/** The Schedule object defining the program schedule. **/
	public Climate[] climates;/** The list of Climate objects defining all the climates in the program schedule. **/
	public String currentClimateRef;/** The currently active climate, identified by its ClimateRef. **/
}
