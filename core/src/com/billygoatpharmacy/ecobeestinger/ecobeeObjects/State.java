package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class State 
{
	public int maxValue;/** The maximum value the sensor can generate. **/
	public int minValue;/** The minimum value the sensor can generate. **/
	public String type;/** Values: coolHigh, coolLow, heatHigh, heatLow, high, low, transitionCount, normal. **/
	public Action[] actions;/** The list of StateAction objects associated with the sensor. **/
}
