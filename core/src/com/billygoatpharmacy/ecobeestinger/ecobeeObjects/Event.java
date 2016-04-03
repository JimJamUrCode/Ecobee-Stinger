package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Event 
{
	public String type;/** The type of event. Values: hold, demandResponse, sensor, switchOccupancy, vacation, quickSave, today, autoAway, autoHome **/
	public String name;/** The unique event name. **/
	public Boolean running;/** Whether the event is currently active or not. **/
	public String startDate;/** The event start date in thermostat local time. **/
	public String startTime;/** The event start time in thermostat local time. **/
	public String endDate;/** The event end date in thermostat local time. **/
	public String endTime;/** The event end time in thermostat local time. **/
	public Boolean isOccupied;/** Whether there are persons occupying the property during the event. **/
	public Boolean isCoolOff;/** Whether cooling will be turned off during the event. **/
	public Boolean isHeatOff;/** Whether heating will be turned off during the event. **/
	public int coolHoldTemp;/** The cooling absolute temperature to set. **/
	public int heatHoldTemp;/** The heating absolute temperature to set. **/
	public String fan;/** The fan mode during the event. Values: auto, onDefault: based on current climate and hvac mode. **/
	public String vent;/** The ventilator mode during the vent. Values:auto, minontime, on, off. **/
	public int ventilatorMinOnTime;/** The minimum amount of time the ventilator equipment must stay on on each duty cycle. **/
	public Boolean isOptional;/** Whether this event is mandatory or the end user can cancel it. **/
	public Boolean isTemperatureRelative;/** Whether the event is using a relative temperature setting to the currently active program climate. See the Note at the bottom of this page for more information. **/
	public int coolRelativeTemp;/** The relative cool temperature adjustment. **/
	public int heatRelativeTemp;/** The relative heat temperature adjustment. **/
	public Boolean isTemperatureAbsolute;/** Whether the event uses absolute temperatures to set the values. Default: true for DRs. See the Note at the bottom of this page for more information. **/
	public int dutyCyclePercentage;/** Indicates the % scheduled runtime during a Demand Response event. Valid range is 0 - 100%. Default = 100, indicates no change to schedule. **/
	public int fanMinOnTime;/** The minimum number of minutes to run the fan each hour. Range: 0-60, Default: 0 **/
	public Boolean occupiedSensorActive;/** True if this calendar event was created because of the occupied sensor. **/
	public Boolean unoccupiedSensorActive;/** True if this calendar event was created because of the unoccupied sensor **/
	public int drRampUpTemp;/** Unsupported. Future feature. **/
	public int drRampUpTime;/** Unsupported. Future feature. **/
	public String linkRef;/** Unique identifier set by the server to link one or more events and alerts together. **/
	public String holdClimateRef;/** Used for display purposes to indicate what climate (if any) is being used for the hold. **/
}
