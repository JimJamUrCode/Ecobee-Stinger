package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Runtime 
{
	public String runtimeRev;/** The current runtime revision. Equivalent in meaning to the runtime revision number in the thermostat summary call. **/
	public Boolean connected;/** Whether the thermostat is currently connected to the server. **/
	public String firstConnected;/** The UTC date/time stamp of when the thermostat first connected to the ecobee server. **/
	public String connectDateTime;/** The last recorded connection date and time. **/
	public String disconnectDateTime;/** The last recorded disconnection date and time. **/
	public String lastModified;/** The UTC date/time stamp of when the thermostat was updated. Format: YYYY-MM-DD HH:MM:SS **/
	public String lastStatusModified;/** The UTC date/time stamp of when the thermostat last posted its runtime information. Format: YYYY-MM-DD HH:MM:SS **/
	public String runtimeDate;/** The UTC date of the last runtime reading. Format: YYYY-MM-DD **/
	public int runtimeInterval;/** The last 5 minute interval which was updated by the thermostat telemetry update. Subtract 2 from this interval to obtain the beginning interval for the last 3 readings. Multiply by 5 mins to obtain the minutes of the day. Range: 0-287 **/
	public int actualTemperature;/** The current temperature displayed on the thermostat. **/
	public int actualHumidity;/** The current humidity % shown on the thermostat. **/
	public int desiredHeat;/** The desired heat temperature as per the current running program or active event. **/
	public int desiredCool;/** The desired cool temperature as per the current running program or active event. **/
	public int desiredHumidity;/** The desired humidity set point. **/
	public int desiredDehumidity;/** The desired dehumidification set point. **/
	public String desiredFanMode;/** The desired fan mode. Values: auto, on or null if the HVAC system is off and the thermostat is not controlling a fan independently. **/
}
