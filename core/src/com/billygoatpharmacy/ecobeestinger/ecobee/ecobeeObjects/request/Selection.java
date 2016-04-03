package com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request;

public class Selection 
{
	public enum SelectionType {
		none, user, registered, thermostats, managementSet
	}
	
	public SelectionType selectionType;/** The type of match data supplied: Values: none, thermostats, user, managementSet. **/
	public String selectionMatch;/** The match data based on selectionType (e.g. a list of thermostat idendifiers in the case of a selectionType of thermostats) **/
	public boolean includeRuntime;/** Include the thermostat runtime object. If not specified, defaults to false. **/
	public boolean includeExtendedRuntime;/** Include the extended thermostat runtime object. If not specified, defaults to false. **/
	public boolean includeElectricity;/** Include the electricity readings object. If not specified, defaults to false. **/
	public boolean includeSettings;/** Include the thermostat settings object. If not specified, defaults to false. **/
	public boolean includeLocation;/** Include the thermostat location object. If not specified, defaults to false. **/
	public boolean includeProgram;/** Include the thermostat program object. If not specified, defaults to false. **/
	public boolean includeEvents;/** Include the thermostat calendar events objects. If not specified, defaults to false. **/
	public boolean includeDevice;/** Include the thermostat device configuration objects. If not specified, defaults to false. **/
	public boolean includeTechnician;/** Include the thermostat technician object. If not specified, defaults to false. **/
	public boolean includeUtility;/** Include the thermostat utility company object. If not specified, defaults to false. **/
	public boolean includeManagement;/** Include the thermostat management company object. If not specified, defaults to false. **/
	public boolean includeAlerts;/** Include the thermostat's unacknowledged alert objects. If not specified, defaults to false. **/
	public boolean includeWeather;/** Include the current thermostat weather forecast object. If not specified, defaults to false. **/
	public boolean includeHouseDetails;/** Include the current thermostat house details object. If not specified, defaults to false. **/
	public boolean includeOemCfg;/** Include the current thermostat OemCfg object. If not specified, defaults to false. **/
	public boolean includeEquipmentStatus;/** Include the current thermostat equipment status information. If not specified, defaults tofalse. **/
	public boolean includeNotificationSettings;/** Include the current thermostat alert and reminders settings. If not specified, defaults tofalse. **/
	public boolean includePrivacy;/** Include the current thermostat privacy settings.Note: access to this object is restricted to callers with implict authentication, setting this value to true without proper credentials will result in an authentication exception. **/
	public boolean includeVersion;/** Include the current firmware version the Thermostat is running. If not specified, defaults to false. **/
	public boolean includeSecuritySettings;/** Include the current securitySettings object for the selected Thermostat(s). If not specified, defaults to false. **/
	public boolean includeSensors;/** Include the list of current thermostatRemoteSensor objects for the selected Thermostat(s). If not specified, defaults to false. **/
}
