package com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response;

import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Page;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.RuntimeReport;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.RuntimeSensorReport;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Status;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Thermostat;

public class ThermostatRuntimeResposeData
{
	public String startDate;/** The report start interval. **/
	public Integer startInterval;/** The report UTC end date. **/
	public String endDate;/** The report end interval. **/
	public Integer endInterval;/** The CSV list of column names from the request. **/
	public String columns;/** A list of runtime reports. **/
	public RuntimeReport[] reportList;/** A list of runtime sensor reports. Will be empty unless requested. **/
	public RuntimeSensorReport[] sensorList;/**  **/
	public float receivedAt;/**The last time this report was received. Should not receive more often than 15 min per ecobee api spec**/
	public Status status;
	public String error;
	public String error_description;
	public String error_uri;

	public ThermostatRuntimeResposeData()
	{
		
	}
}
