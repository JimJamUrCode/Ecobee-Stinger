package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class LimitSetting 
{
	public int limit;/** The value of the limit to set. For temperatures the value is expressed as degrees Fahrenheit, multipled by 10. For humidity values are expressed as a percentage from 5 to 95. See here for more information. **/
	public Boolean enabled;/** Boolean value representing whether or not alerts/reminders are enabled for this notification type or not. **/
	public String type;/** The type of notification. Possible values are: lowTemp, highTemp, lowHumidity, highHumidity, auxHeat, auxOutdoor **/
	public Boolean remindTechnician;/** Boolean value representing whether or not alerts/reminders should be sent to the technician/contractor associated with the thermostat. **/
}
