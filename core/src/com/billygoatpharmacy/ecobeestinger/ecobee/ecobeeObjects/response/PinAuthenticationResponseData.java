package com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response;

public class PinAuthenticationResponseData 
{
	public String access_token;
	public String refresh_token;
	public String token_type;
	public int expires_in;
	public long receieved_on;//date in milli, helps determine if the refresh token is valid
	public String scope;
	public String error;
	public String error_description;
	public String error_uri;
	
	public PinAuthenticationResponseData()
	{
		
	}
}
