package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class ElectricityTier 
{
	public String name;/** The tier name as defined by the Utility. May be an empty string if the tier is undefined or the usage falls outside the defined tiers. **/
	public String consumption;/** The last daily consumption reading collected. The reading format and precision is to three decimal places in kWh. **/
	public String cost;/** The daily cumulative tier cost in dollars if defined by the Utility. May be an empty string if undefined. **/
}
