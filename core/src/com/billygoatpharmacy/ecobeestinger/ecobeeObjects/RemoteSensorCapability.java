package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class RemoteSensorCapability 
{
	public String id;/** The unique sensor capability identifier. For example: 1 **/
	public String type;/** The type of sensor capability. Values: adc, co2, dryContact, humidity, temperature, occupancy, unknown. **/
	public String value;/** The data value for this capability, always a String. Temperature values are expressed as degrees Fahrenheit, multiplied by 10. For example, a temperature of 72F would be returned as the value "720". Occupancy values are "true" or "false". Humidity is expressed as a % value such as "45". Unknown values are returned as "unknown". **/
}
