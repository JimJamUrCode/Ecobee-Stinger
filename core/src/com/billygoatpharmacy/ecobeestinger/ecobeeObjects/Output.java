package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Output 
{
	public String name;/** The name of the outpute **/
	public int zone;/** The thermostat zone the output is associated with **/
	public int outputId;/** The unique output identifier number. **/
	public String type;/** The type of output. Values: compressor1, compressor2, dehumidifier, economizer, fan, heat1, heat2, heat3, heatPumpReversal, humidifer, none, occupancy, userDefined, ventilator, zoneCool, zoneFan, zoneHeat. **/
	public Boolean sendUpdate;/** Whether to send an update message. **/
	public Boolean activeClosed;/** If true, when this output is activated it will close the relay. Otherwise, activating the relay will open the relay. **/
	public int activationTime;/** Time to activate relay - in seconds. **/
	public int deactivationTime;/** Time to deactivate relay - in seconds. **/
}
