package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

/**
 * Created by jhard_000 on 4/10/2016.
 */
public class RuntimeSensorReport
{
    public String thermostatIdentifier;/** The thermostat identifier for the report. **/
    public RuntimeSensorMetadata[] sensors;/** The list of sensor metadata configured in the thermostat. **/
    public String[] columns;/** The list of column names returned in the data property. The sensor data column names match the sensorIdwithin the sensor metadata. The first two columns are the date and time, the following are the defined sensorIds. **/
    public String[] data;/** The list of CSV rows containing the column data as defined in the columnsproperty. **/
}
