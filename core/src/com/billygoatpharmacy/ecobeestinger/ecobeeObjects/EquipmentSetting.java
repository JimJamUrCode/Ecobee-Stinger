package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class EquipmentSetting 
{
	public String filterLastChanged;/** The date the filter was last changed for this equipment. String format: YYYY-MM-DD **/
	public int filterLife;/** The value representing the life of the filter. This value is expressed in month or hour, which is specified in the thefilterLifeUnits property. **/
	public String filterLifeUnits;/** The units the filterLife field is measured in. Possible values are: month, hour. month has a range of 1 - 12.hour has a range of 100 - 10000. **/
	public String remindMeDate;/** The date the reminder will be triggered. This is a read-only field and cannot be modified through the API. The value is calculated and set by the thermostat. **/
	public Boolean enabled;/** Boolean value representing whether or not alerts/reminders are enabled for this notification type or not. **/
	public String type;/** The type of notification. Possible values are: hvac, furnaceFilter, humidifierFilter, dehumidifierFilter, ventilator, ac, airFilter, airCleaner, uvLamp **/
	public Boolean remindTechnician;/** Boolean value representing whether or not alerts/reminders should be sent to the technician/contractor assoicated with the thermostat. **/
}
