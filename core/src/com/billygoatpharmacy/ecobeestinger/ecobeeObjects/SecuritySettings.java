package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class SecuritySettings 
{
	public String userAccessCode;/** The 4-digit user access code for the thermostat. The code must be set when enabling access control. See the callout above for more information. **/
	public Boolean allUserAccess;/** The flag for determining whether there are any restrictions on the thermostat regarding access control. Default value is false. If all other values are true this value will default to true. **/
	public Boolean programAccess;/** The flag for determining whether there are any restrictions on the thermostat regarding access control to the Thermostat.Program. Default value is false, unless allUserAccess is true. **/
	public Boolean detailsAccess;/** The flag for determining whether there are any restrictions on the thermostat regarding access control to the Thermostat system and settings. Default value isfalse, unless allUserAccess is true. **/
	public Boolean quickSaveAccess;/** The flag for determining whether there are any restrictions on the thermostat regarding access control to the Thermostat quick save functionality. Default value is false, unless allUserAccess is true. **/
	public Boolean vacationAccess;/** The flag for determining whether there are any restrictions on the thermostat regarding access control to the Thermostat vacation functionality. Default value is false, unless allUserAccess is true. **/
}
