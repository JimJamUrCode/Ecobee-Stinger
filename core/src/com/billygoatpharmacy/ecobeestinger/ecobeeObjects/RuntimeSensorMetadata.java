package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

/**
 * Created by jhard_000 on 4/10/2016.
 */
public class RuntimeSensorMetadata
{
    public String sensorId;/** The unique sensor identifier. It is composed of deviceName + deviceId + sensorId (from thermostat.device[].sensor[]) separated by colons. This value corresponds to the column name for the sensor reading values. **/
    public String sensorName;/** The user assigned sensor name. **/
    public String sensorType;/** The type of sensor. See Sensor Types. Values: co2, ctclamp, dryContact, humidity, plug, temperature **/
    public String sensorUsage;/** The usage configured for the sensor. Values: dischargeAir, indoor, monitor, outdoor **/

//    public String co2;/** Carbon Dioxide Sensor | Unit: ppm **/
//    public String ctclamp;/** Current Transducer | Unit: Wh **/
//    public String dryContact;/** Contact Sensor | Unit: binary: 1 or 0 **/
//    public String humidity;/** Humidity Sensor | Unit: %RH **/
//    public String temperature;/** Temperature Sensor | Unit: F **/
//    public String plug;/** ecobee Plug | Unit: Wh **/
//    public String pulesedElectricityMeter;/** Electricity Meter | Unit: Wh **/
}
