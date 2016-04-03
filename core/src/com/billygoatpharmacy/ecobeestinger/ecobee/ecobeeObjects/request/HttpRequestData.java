package com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request;

public class HttpRequestData 
{
	private HttpRequestDataType[] mAllData;
	
	public HttpRequestData(HttpRequestDataType[] data)
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
