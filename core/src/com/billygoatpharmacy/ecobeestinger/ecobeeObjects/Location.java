package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Location 
{
	public int timeZoneOffsetMinutes;/** The timezone offset in minutes from UTC. **/
	public String timeZone;/** The Olson timezone the thermostat resides in (e.gAmerica/Toronto). **/
	public Boolean isDaylightSaving;/** Whether the thermostat should factor in daylight savings when displaying the date and time. **/
	public String streetAddress;/** The thermostat location street address. **/
	public String city;/** The thermostat location city. **/
	public String provinceState;/** The thermostat location State or Province. **/
	public String country;/** The thermostat location country. **/
	public String postalCode;/** The thermostat location ZIP or Postal code. **/
	public String phoneNumber;/** The thermostat owner's phone number **/
	public String mapCoordinates;/** The lat/long geographic coordinates of the thermostat location. **/
}
