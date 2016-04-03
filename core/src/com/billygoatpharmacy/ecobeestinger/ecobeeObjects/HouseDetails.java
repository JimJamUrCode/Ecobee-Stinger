package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class HouseDetails 
{
	public String style;/** The style of house. Values: other, apartment, condominium, detached, loft, multiPlex, rowHouse, semiDetached, townhouse, and 0 for unknown. **/
	public int size;/** The size of the house in square feet. **/
	public int numberOfFloors;/** The number of floors or levels in the house. **/
	public int numberOfRooms;/** The number of rooms in the house. **/
	public int numberOfOccupants;/** The number of occupants living in the house. **/
	public int age;/** The age of house in years. **/
	public int windowEfficiency;/** This field defines the window efficiency of the house. Valid values are in the range 1 - 7. Changing the value of this field alters the settings the thermostat uses for the humidifier when in 'frost Control' mode. See the NOTE above before updating this value. **/
}
