package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Device 
{
	public Integer deviceId;/** A unique ID for the device **/
	public String name;/** The user supplied device name **/
	public Sensor[] sensors;/** The list of Sensor Objects associated with the device. **/
	public Output[] outputs;/** Ths list of Output Objects associated with the device **/
}
