package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Weather 
{
	public String timestamp;/** The time stamp in UTC of the weather forecast. **/
	public String weatherStation;/** The weather station identifier. **/
	public WeatherForecast[] forecasts;/** The list of latest weather station forecasts. **/
}
