package com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request;

import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi.EcobeeAPI;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jhard_000 on 4/10/2016.
 */
public class ThermostatRuntimeRequestColumnGenerator
{
    public Boolean auxHeat1;/**  **/
    public Boolean auxHeat2;/**  **/
    public Boolean auxHeat3;/**  **/
    public Boolean compCool1;/**  **/
    public Boolean compCool2;/**  **/
    public Boolean compHeat1;/**  **/
    public Boolean compHeat2;/**  **/
    public Boolean dehumidifier;/**  **/
    public Boolean dmOffset;/**  **/
    public Boolean economizer;/**  **/
    public Boolean fan;/**  **/
    public Boolean humidifier;/**  **/
    public Boolean outdoorHumidity;/**  **/
    public Boolean outdoorTemp;/**  **/
    public Integer sky;/**  **/
    public Boolean ventilator;/**  **/
    public Boolean wind;/**  **/
    public Boolean zoneAveTemp;/**  **/
    public Boolean zoneCalendarEvent;/**  **/
    public Boolean zoneCoolTemp;/**  **/
    public Boolean zoneHeatTemp;/**  **/
    public Boolean zoneHumidity;/**  **/
    public Boolean zoneHumidityHigh;/**  **/
    public Boolean zoneHumidityLow;/**  **/
    public Boolean zoneHvacMode;/**  **/
    public Boolean zoneOccupancy;/**  **/

    public ThermostatRuntimeRequestColumnGenerator()
    {

    }

    public static String getCSV(Object data) throws IllegalArgumentException, IllegalAccessException
    {
        Class<?> aClass = data.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        Map<String, String> logEntries = new HashMap<String, String>();

        String queryString = "";

        for (Field field : declaredFields)
        {
            field.setAccessible(true);

            Object[] arguments = new Object[]{
                    field.getName(),
                    field.getType().getSimpleName(),
                    String.valueOf(field.get(data))
            };

            if(arguments[2] == "true") {
                String template = "{0},";
                String property = MessageFormat.format(template, arguments);

                queryString += property;
                Logger.log(EcobeeAPI.class.getName(), property);
            }
        }

        queryString = queryString.substring(0, queryString.lastIndexOf(","));
        return queryString;
    }
}
