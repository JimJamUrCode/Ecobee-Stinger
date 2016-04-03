package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Climate 
{
	public String name;/** The unique climate name. The name may be changed without affecting the program integrity so long as uniqueness is maintained. **/
	public String climateRef;/** The unique climate identifier. Changing the identifier is not possible and it is generated on the server for each climate. If this value is not supplied a new climate will be created. For the default climates and existing user created climates the climateRef should be supplied - see note above. **/
	public Boolean isOccupied;/** A flag indicating whether the property is occupied by persons during this climate **/
	public Boolean isOptimized;/** A flag indicating whether ecobee optimized climate settings are used by this climate. **/
	public String coolFan;/** The cooling fan mode. Default: on. Values:auto, on. **/
	public String heatFan;/** The heating fan mode. Default: on. Values:auto, on. **/
	public String vent;/** The ventilator mode. Default: off. Values:auto, minontime, on, off. **/
	public Integer ventilatorMinOnTime;/** The minimum time, in minutes, to run the ventilator each hour. **/
	public String owner;/** The climate owner. Default: system. Values:adHoc, demandResponse, quickSave, sensorAction, switchOccupancy, system, template, user. **/
	public String type;/** The type of climate. Default: program. Values: calendarEvent, program. **/
	public Integer colour;/** The integer conversion of the HEX color value used to display this climate on the thermostat and on the web portal. **/
	public Integer coolTemp;/** The cool temperature for this climate. **/
	public Integer heatTemp;/** The heat temperature for this climate. **/
	public RemoteSensor[] sensors;/** The list of sensors in use for the specific climate. The sensors listed here are used for temperature averaging within that climate. Only the sensorId and name are listed in the climate. **/
}
