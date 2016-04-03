package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Action 
{
	public String type;/** Values: activateRelay, adjustTemp, doNothing, shutdownAC, shutdownAuxHeat, shutdownSystem, shutdownCompression, switchToOccupied, switchToUnoccupied, turnOffDehumidifer, turnOffHumidifier, turnOnCool, turnOnDehumidifier, turnOnFan, turnOnHeat, turnOnHumidifier. **/
	public Boolean sendAlert;/** Flag to enable an alert to be generated when the state is triggered **/
	public Boolean sendUpdate;/** Whether to send an update message. **/
	public int activationDelay;/** Delay in seconds before the action is triggered by the state change. **/
	public int deactivationDelay;/** The amount of time to wait before deactivating this state after the state has been cleared. **/
	public int minActionDuration;/** The minimum length of time to maintain action after sensor has been deactivated. **/
	public int heatAdjustTemp;/** The amount to increase/decrease current setpoint if the type = adjustTemp. **/
	public int coolAdjustTemp;/** The amount to increase/decrease current setpoint if the type = adjustTemp. **/
	public String activateRelay;/** The user defined relay to be activated, only used for type == activateRelay. **/
}
