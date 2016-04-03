package com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response;

import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Page;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Status;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Thermostat;

public class ThermostatsResposeData 
{
	public Page page;
	public Thermostat[] thermostatList;
	public Status status;
	public String error;
	public String error_description;
	public String error_uri;
	
	public ThermostatsResposeData()
	{
		
	}
}
