package com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request;

import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Page;

public class PinRequest
{
	public String response_type;
	public String scope;
	public String client_id;
	
	public PinRequest(String responseType, String nScope, String clientID)
	{
		this.response_type = responseType;
		this.scope = nScope;
		this.client_id = clientID;
	}
}
