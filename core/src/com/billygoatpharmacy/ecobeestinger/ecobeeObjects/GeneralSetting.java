package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class GeneralSetting 
{
	public Boolean enabled;/** Boolean value representing whether or not alerts/reminders are enabled for this notification type or not. **/
	public String type;/** The type of notification. Possible values are: temp **/
	public Boolean remindTechnician;/** Boolean value representing whether or not alerts/reminders should be sent to the technician/contractor assoicated with the thermostat. **/
}
