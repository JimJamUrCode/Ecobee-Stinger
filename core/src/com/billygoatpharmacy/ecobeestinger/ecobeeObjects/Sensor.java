package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Sensor 
{
	public String name;/** The sensor name **/
	public String manufacturer;/** The sensor manufacturer **/
	public String model;/** The sensor model **/
	public Integer zone;/** The thermostat zone the sensor is associated with **/
	public Integer sensorId;/** The unique sensor identifier **/
	public String type;/** The type of sensor. Values: adc, co2, dryCOntact, humidity, temperature, unknown. **/
	public String usage;/** The sensor usage type. Values: dischargeAir, indoor, monitor, outdoor. **/
	public Integer numberOfBits;/** The number of bits the adc has been configured to use. **/
	public Integer bconstant;/** Value of the bconstant value used in temperature sensors **/
	public Integer thermistorSize;/** The sensor thermistor value, ie. 10K thermistor=10000, 2.5K thermistor=2500 **/
	public Integer tempCorrection;/** The user adjustable temperature compensation applied to the temperature reading. **/
	public Integer gain;/** The sensor thermistor gain value. **/
	public Integer maxVoltage;/** The sensor thermistor max voltage in Volts, 5=5V, 10=10V. **/
	public Integer multiplier;/** The multiplier value used in sensors (1000x value). **/
	public State[] states;/** A list of SensorState objects **/
}
