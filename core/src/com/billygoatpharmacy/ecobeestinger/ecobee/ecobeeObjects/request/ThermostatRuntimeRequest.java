package com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request;

import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Page;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Selection;

import java.util.Date;

public class ThermostatRuntimeRequest
{
	public Selection selection;/** The Selection Object. Must be selectionType = 'thermostats' and selectionMatch = a CSV of thermostat identifiers. **/
	public String startDate;/**The UTC report start date. **/
	public int startInterval;/**The 5 minute interval to begin the report on. The interval can be determined by multiplying the interval by 5. Range: 0-287 Default: 0 **/
	public String endDate;/**The UTC report end date. **/
	public int endInterval;/**The 5 minute interval to end the report on. The interval can be determined by multiplying the interval by 5. Range: 0-287 Default: 287 **/
	public String columns; /** A CSV string of column names. See Report Columns. No spaces in CSV. **/
	public Boolean includeSensors;/**Whether to include sensor runtime report data for those thermostats which have it. Default: false. **/
}
