package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class WeatherForecast 
{
	public int weatherSymbol;/** The int value used to map to a weatherSymbol. See list of mappings above. **/
	public String dateTime;/** The time stamp of the weather forecast. **/
	public String condition;/** A text value representing the current weather condition. **/
	public int temperature;/** The current temperature. **/
	public int pressure;/** The current barometric pressure. **/
	public int relativeHumidity;/** The current humidity. **/
	public int dewpoint;/** The dewpoint. **/
	public int visibility;/** The visibility in meters; 0 - 70,000. **/
	public int windSpeed;/** The wind speed as an int in mph * 1000. **/
	public int windGust;/** The wind gust speed. **/
	public String windDirection;/** The wind direction. **/
	public int windBearing;/** The wind bearing. **/
	public int pop;/** Probability of precipitation. **/
	public int tempHigh;/** The predicted high temperature for the day. **/
	public int tempLow;/** The predicted low temperature for the day. **/
	public int sky;/** The cloud cover condition. **/
}
