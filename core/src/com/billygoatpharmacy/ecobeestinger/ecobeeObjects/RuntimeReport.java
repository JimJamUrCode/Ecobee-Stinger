package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

/**
 * Created by jhard_000 on 4/10/2016.
 */
public class RuntimeReport
{
    public String thermostatIdentifier;/** The thermostat identifier for the report. **/
    public Integer rowCount;/** The number of report rows in this report **/
    public String[] rowList;/** A list of CSV report strings based on the columns requested. **/
}
