package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Thermostat 
{
	public String identifier;/** The unique thermostat serial number. **/
	public String name;/** A user defined name for a thermostat. **/
	public String thermostatRev;/** The current thermostat configuration revision. **/
	public Boolean isRegistered;/** Whether the user registered the thermostat. **/
	public String modelNumber;/** The thermostat model number. Values: idtSmart, idtEms, siSmart, siEms, athenaSmart, athenaEms **/
	public String brand;/** The thermostat brand. **/
	public String features;/** The comma-separated list of the thermostat's additional features, if any. **/
	public String lastModified;/** The last modified date time for the thermostat configuration. **/
	public String thermostatTime;/** The current time in the thermostat's time zone **/
	public String utcTime;/** The current time in UTC. **/
	public Alert[] alerts;/** The list of Alert objects tied to the thermostat **/
	public Settings settings;/** The thermostat Setting object linked to the thermostat **/
	public Runtime runtime;/** The Runtime state object for the thermostat **/
	public ExtendedRuntime extendedRuntime;/** The ExtendedRuntime object for the thermostat **/
	public Electricity electricity;/** The Electricity object for the thermostat **/
	public Device[] devices;/** The list of Device objects linked to the thermostat **/
	public Location location;/** The Location object for the thermostat **/
	public Technician technician;/** The Technician object associated with the thermostat containing the technician contact information **/
	public Utility utility;/** The Utility object associated with the thermostat containing the utility company information **/
	public Management management;/** The Management object associated with the thermostat containing the management company information **/
	public Weather weather;/** The Weather object linked to the thermostat representing the current weather on the thermostat. **/
	public Event[] events;/** The list of Event objects linked to the thermostat representing any events that are active or scheduled. **/
	public Program program;/** The Program object for the thermostat **/
	public HouseDetails houseDetails;/** The houseDetails object contains contains the information about the house the thermostat is installed in. **/
	public Object oemCfg;/** The OemCfg object contains information about the OEM specific thermostat. **/
	public String equipmentStatus;/** The status of all equipment controlled by this Thermostat. Only running equipment is listed in the CSV String. Values: heatPump, heatPump2, heatPump3, compCool1, compCool2, auxHeat1, auxHeat2, auxHeat3, fan, humidifier, dehumidifier, ventilator, economizer, compHotWater, auxHotWater. Note: If no equipment is currently running an empty String is returned. If Settings.hasHeatPump is true, heatPump value will be returned for heating, compCool for cooling, and auxHeat for aux heat. If Settings.hasForcedAir or Settings.hasBoiler is true, auxHeat value will be returned for heating and compCool for cooling (heatPump will not show up for heating). **/
	public NotificationSettings notificationSettings;/** The NotificationSettings object containing the configuration for Alert and Reminders for the Thermostat. **/
	public Object privacy;/** The Privacy object containing the privacy settings for the Thermostat. Note: access to this object is restricted to callers with implict authentication. **/
	public Version version;/** The Version object containing the firmware version information for the Thermostat. For example: "3.5.0.3957". **/
	public SecuritySettings securitySettings;/** The SecuritySettings object containing the security settings for the Thermostat. **/
	public RemoteSensor[] remoteSensors;/** The list of RemoteSensor objects for the Thermostat. **/
}
