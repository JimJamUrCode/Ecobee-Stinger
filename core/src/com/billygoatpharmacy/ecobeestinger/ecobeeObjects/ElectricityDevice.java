package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class ElectricityDevice 
{
	public String name;/** The name of the device **/
	public ElectricityTier[] tiers;/** The list of Electricity Tiers containing the break down of daily electricity consumption of the device for the day, broken down per pricing tier. **/
	public String lastUpdate;/** The last date/time the reading was updated in UTC time. **/
	public String[] cost;/** The last three daily electricity cost reads from the device in cents with a three decimal place precision. **/
	public String[] consumption;/** The last three daily electricity consumption reads from the device in KWh with a three decimal place precision. **/
}
