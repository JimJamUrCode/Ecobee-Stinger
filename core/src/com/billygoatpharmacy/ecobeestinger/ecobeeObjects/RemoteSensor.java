package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class RemoteSensor 
{
	public String id;/** The unique sensor identifier. It is composed of deviceName + deviceId separated by colons, for example: rs:100 **/
	public String name;/** The user assigned sensor name. **/
	public String type;/** The type of sensor. Values: thermostat, ecobee3_remote_sensor, monitor_sensor, control_sensor. **/
	public String code;/** The unique 4-digit alphanumeric sensor code. For ecobee3 remote sensors this corresponds to the code found on the back of the physical sensor. **/
	public Boolean inUse;/** This flag indicates whether the remote sensor is currently in use by a comfort setting. SeeClimate for more information. **/
	public RemoteSensorCapability[] capability;/** The list of remoteSensorCapability objects for the remote sensor. **/
}
