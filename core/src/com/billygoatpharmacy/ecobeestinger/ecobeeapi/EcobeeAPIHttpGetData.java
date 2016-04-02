package com.billygoatpharmacy.ecobeestinger.ecobeeapi;

public class EcobeeAPIHttpGetData 
{
	private EcobeeAPIHttpGetDataType[] mAllData;
	
	public EcobeeAPIHttpGetData(EcobeeAPIHttpGetDataType[] data)
	{
		mAllData = data;
	}
	
	public String toQueryString()
	{
		String resp = "";
		for(int i = 0; i < mAllData.length; i ++)
		{
			resp += mAllData[i].mName + "=" + mAllData[i].mValue;
			
			if(i+1 < mAllData.length)
				resp += "&";
		}
		return resp;
	}
}
