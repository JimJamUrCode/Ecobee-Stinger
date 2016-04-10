package com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request.PinAuthenticationRequest;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request.PinRequest;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Selection;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request.ThermostatRequest;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.PinRequestResponseData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.ThermostatsResposeData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.PinAuthenticationResponseData;
import com.billygoatpharmacy.fileTools.FileManager;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EcobeeAPI {

	public static EcobeeConfig sEcobeeConfig;
	
	private static PinRequestResponseData sTempAuthCode;
	private static PinAuthenticationResponseData sPinAuthResponse;
	private static ThermostatsResposeData sThermostatsResponse;
	
	private static EcobeeAPIHttpCallback mExternalCallback;
	private static EcobeeAPIHttpCallback mLoginExternalCallback;
	
	private static Json sJson;
	
	public static void init()
	{
		sJson = new Json();
		sJson.setTypeName(null);
		sJson.setUsePrototypes(false);
		//sJson.setIgnoreUnknownFields(true);
		sJson.setOutputType(OutputType.json);

		sEcobeeConfig = sJson.fromJson(EcobeeConfig.class, FileManager.readFile("ecobeeConfig.cfg", true));
	}
	
	public static Boolean login(EcobeeAPIHttpCallback loginAction)
	{
		mLoginExternalCallback = loginAction;
		if(FileManager.doesFileExist("accessData.dat"))
		{
			sPinAuthResponse = sJson.fromJson(PinAuthenticationResponseData.class, FileManager.readFile("accessData.dat", false));

			long timeDiff = (new Date().getTime() - sPinAuthResponse.receieved_on) / 1000;
			if(sPinAuthResponse != null && sPinAuthResponse.refresh_token != null && sPinAuthResponse.refresh_token != "" &&
					sPinAuthResponse.expires_in > timeDiff && sPinAuthResponse.receieved_on != 0)
			{
				loginAction.done(sJson.toJson(sPinAuthResponse));
				return true;
			}
			else if(sPinAuthResponse == null || sPinAuthResponse.refresh_token == null)//No way to authenticate, user needs to repin auth
			{
				//Pin authentication need to occur
				return false;
			}
			else if(sPinAuthResponse.expires_in < timeDiff || sPinAuthResponse.receieved_on == 0)//The access codes have expired, try and get re-issued
			{
				//note sure what to do here, or how to get access tokens without the user needing to go through the pin flow.
				//Setting the temp code to our refresh code so that our pre-existing function will work without changes.
				sTempAuthCode = new PinRequestResponseData();
				sTempAuthCode.code = sPinAuthResponse.refresh_token;
				getAccessTokenFromRefreshToken(loginCallback(), "refresh_token");
				return true;
			}
			else
			{
				return false;//Can't login
			}
		}
		else
		{
			return false;//Can't login
		}
	}
	
	private static EcobeeAPIHttpCallback loginCallback()
	{
		return new EcobeeAPIHttpCallback() {
			@Override
			public void done(String response)
			{
				PinAuthenticationResponseData data = sJson.fromJson(PinAuthenticationResponseData.class, response);

				Logger.log(EcobeeAPI.class.getName(), sJson.toJson(data));
				mLoginExternalCallback.done(response);
				mLoginExternalCallback = null;
			}
		};
	}
	
	/**Function used to get a pin code from the ecobee servers.
	 * EcobeeAPIHttpCallback is an extended runnable action and
	 *  essentially works like a callback. This is needed due 
	 *  to the async nature of http commands.
	 **/
	public static void getPin(EcobeeAPIHttpCallback action)
	{	
		Logger.log(EcobeeAPI.class.getName(), "GET Ecobee Pin...");
		mExternalCallback = action;
		
		//Creating parameters for the request
		PinRequest pinRequest = new PinRequest("ecobeePin", sEcobeeConfig.scope, sEcobeeConfig.appkey);
		
		try
		{
			String queryString = getQueryString(pinRequest);//"json=" + sJson.toJson(pinRequest);
			
			Array<HttpHeader> headers = new Array<HttpHeader>();
			headers.add(new HttpHeader("Accept", "application/json"));
			headers.add(new HttpHeader("User-Agent", sEcobeeConfig.user_agent));
			headers.add(new HttpHeader("Content-Type", "application/json"));
			
			makeGetRequest(sEcobeeConfig.http_root + "authorize", queryString, headers, getGetPinCallback());
		}
		catch(Exception e)
		{
			Logger.log(EcobeeAPI.class.getName(), "Illegal Access Exception: " + e.toString());
			PinRequestResponseData resp = new PinRequestResponseData();
			resp.error = "Illegal Access Exceptions";
			mExternalCallback.done(sJson.toJson(resp));
		}		
	}
	
	/**Internal action callback for the getPin() function. This 
	 * allows the EcobeeAPI.java class to intercept vital information
	 * before passing it back to the caller.
	 **/
	private static EcobeeAPIHttpCallback getGetPinCallback()
	{
		return new EcobeeAPIHttpCallback() 
		{
			@Override
			public void done(String response)
			{
				Logger.log(EcobeeAPI.class.getName(), "Ecobee Pin Received...");

				sTempAuthCode = sJson.fromJson(PinRequestResponseData.class, response);
				mExternalCallback.done(response);
				mExternalCallback = null;
			}
		};
	}
	
	/**Function used to get an access token from the ecobee server. 
	 * This function can only be called once the user has used a 
	 * pin code to register the app.
	 **/
	public static void getAccessToken(EcobeeAPIHttpCallback action, String grantType)
	{		
		Logger.log(EcobeeAPI.class.getName(), "POST Ecobee Pin Auth...");
		
		mExternalCallback = action;
		PinAuthenticationRequest req = new PinAuthenticationRequest();
		req.grant_type = grantType;
		req.code = sTempAuthCode.code;
		req.client_id = sEcobeeConfig.appkey;

		try
		{
			String queryString = getQueryString(req);//"json=" + sJson.toJson(req);

			//Setting Headers
			Array<HttpHeader> headers = new Array<HttpHeader>();
			headers.add(new HttpHeader("Content-Length", "" + queryString.length()));
			headers.add(new HttpHeader("Accept", "application/json"));
			headers.add(new HttpHeader("User-Agent", sEcobeeConfig.user_agent));
			headers.add(new HttpHeader("Content-Type", "application/x-www-form-urlencoded"));

			makePostRequest(sEcobeeConfig.http_root + "token", queryString, headers, getAccessTokenCallback());
		}
		catch(Exception e)
		{
			Logger.log(EcobeeAPI.class.getName(), "Illegal Access Exception: " + e.toString());
			PinAuthenticationRequest resp = new PinAuthenticationRequest();
			resp.error = "Illegal Access Exceptions";
			mExternalCallback.done(sJson.toJson(resp));
		}
	}

	public static void getAccessTokenFromRefreshToken(EcobeeAPIHttpCallback action, String grantType)
	{
		Logger.log(EcobeeAPI.class.getName(), "POST attemping reauthentication...");

		mExternalCallback = action;
		PinAuthenticationRequest req = new PinAuthenticationRequest();
		req.grant_type = grantType;
		req.refresh_token = sTempAuthCode.code;
		req.client_id = sEcobeeConfig.appkey;

		try
		{
			String queryString = getQueryString(req);//"json=" + sJson.toJson(req);

			//Setting Headers
			Array<HttpHeader> headers = new Array<HttpHeader>();
			headers.add(new HttpHeader("Content-Length", "" + queryString.length()));
			headers.add(new HttpHeader("Accept", "application/json"));
			headers.add(new HttpHeader("User-Agent", sEcobeeConfig.user_agent));
			headers.add(new HttpHeader("Content-Type", "application/x-www-form-urlencoded"));

			makePostRequest(sEcobeeConfig.http_root + "token", queryString, headers, getAccessTokenCallback());
		}
		catch(Exception e)
		{
			Logger.log(EcobeeAPI.class.getName(), "Illegal Access Exception: " + e.toString());
			PinAuthenticationRequest resp = new PinAuthenticationRequest();
			resp.error = "Illegal Access Exceptions";
			mExternalCallback.done(sJson.toJson(resp));
		}
	}

	/**Internal action callback for the getAccessToken() function.
	 * 
	 **/
	private static EcobeeAPIHttpCallback getAccessTokenCallback()
	{
		return new EcobeeAPIHttpCallback() 
		{
			@Override
			public void done(String response)
			{
				Logger.log(EcobeeAPI.class.getName(), "Ecobee Access Token Received...");

				sPinAuthResponse = sJson.fromJson(PinAuthenticationResponseData.class, response);
				sPinAuthResponse.receieved_on = new Date().getTime();

				if(sPinAuthResponse.error != null && sPinAuthResponse.error != "")
					FileManager.saveFile("errorAccess.dat", sJson.toJson(sPinAuthResponse));
				else
					FileManager.saveFile("accessData.dat", sJson.toJson(sPinAuthResponse));
				
				mExternalCallback.done(response);
				mExternalCallback = null;
			}
		};
	}
	
	/**Function used to get an access token from the ecobee server. 
	 * This function can only be called once the user has used a 
	 * pin code to register the app.
	 **/
	public static void getAllThermostats(EcobeeAPIHttpCallback action)
	{		
		Logger.log(EcobeeAPI.class.getName(), "POST Ecobee Pin Auth...");
		
		mExternalCallback = action;
		ThermostatRequest req = new ThermostatRequest();
		req.selection = new Selection();
		req.selection.includeAlerts = true;
		req.selection.selectionType = Selection.SelectionType.registered;
		req.selection.selectionMatch = "includeEvents";
		req.selection.includeSettings = true;
		req.selection.includeRuntime = true;
		
		try
		{
			String queryString = "json=" + sJson.toJson(req);//getQueryString(req);
			
			Array<HttpHeader> headers = new Array<HttpHeader>();
			headers.add(new HttpHeader("Accept", "application/json"));
			headers.add(new HttpHeader("Authorization", "Bearer " + sPinAuthResponse.access_token));
			headers.add(new HttpHeader("User-Agent", sEcobeeConfig.user_agent));
			headers.add(new HttpHeader("Content-Type", "application/json;charset=UTF-8"));
			
			makeGetRequest(sEcobeeConfig.http_root + sEcobeeConfig.api_root + "thermostat", queryString, headers, getAllThermostatsCallback());
		}
		catch(Exception e)
		{
			Logger.log(EcobeeAPI.class.getName(), "Illegal Access Exception: " + e.toString());
			PinRequestResponseData resp = new PinRequestResponseData();
			resp.error = "Illegal Access Exceptions";
			mExternalCallback.done(sJson.toJson(resp));
		}		
	}

	/**Internal action callback for the getAllThermostats() function.
	 * 
	 **/
	private static EcobeeAPIHttpCallback getAllThermostatsCallback()
	{
		return new EcobeeAPIHttpCallback() 
		{
			@Override
			public void done(String response)
			{
				Logger.log(EcobeeAPI.class.getName(), "Ecobee Thermostats Received...");

				sThermostatsResponse = sJson.fromJson(ThermostatsResposeData.class, response);
				
				mExternalCallback.done(response);
				mExternalCallback = null;
			}
		};
	}
	
	
	
	/*General Functions*/
	/**Function to create an http GET request. This get request is specific to the ecobee api.
	 **/
	private static void makeGetRequest(String path, String data, Array<HttpHeader> headers, final EcobeeAPIHttpCallback action)
	{
		HttpRequestBuilder build = new HttpRequestBuilder();
		build.newRequest();
		build.method("GET");
		build.url(path);
		
		//Adding all headers
		for(HttpHeader header: headers)
			build.header(header.mHeaderName, header.mHeaderValue);
		
		build.content(data);
		HttpRequest httpGetRequest = build.build();
	    
	    Logger.log(EcobeeAPI.class.getName(), "HTTP GET Request: ", httpGetRequest);

	    Gdx.net.sendHttpRequest(httpGetRequest, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) 
			{
				String status = httpResponse.getResultAsString();
				Logger.log(EcobeeAPI.class.getName(), "HTTP GET Response: " + status.replaceAll("\n", ""));
				action.done(status);
			}
			
			@Override
			public void failed(Throwable t) {
				Logger.log(EcobeeAPI.class.getName(), "HTTP GET Failed..." + t.toString());
				action.done("Error: " + t.toString());
			}
			
			@Override
			public void cancelled() {
				Logger.log(EcobeeAPI.class.getName(), "HTTP GET Cancelled...");
				action.done("HTTP GET Cancelled");
			}
		}); 
	}	
	
	/**Used to get query strings for ecobee objects
	 *  
	 */
	public static String getQueryString(Object data) throws IllegalArgumentException, IllegalAccessException
	{
		Logger.log(EcobeeAPI.class.getName(), "Creating Query String...");
		Class<?> aClass = data.getClass();
		Field[] declaredFields = aClass.getDeclaredFields();
		Map<String, String> logEntries = new HashMap<String, String>();

		String queryString = "";

		for (Field field : declaredFields)
		{
			field.setAccessible(true);

			Object[] arguments = new Object[]{
			field.getName(),
			field.getType().getSimpleName(),
			String.valueOf(field.get(data))
			};

			String template = "{0}={2}&";
			String property = MessageFormat.format(template, arguments);

			if(property.contains("null") == false) {
				queryString += property;
				Logger.log(EcobeeAPI.class.getName(), property);
			}
		}

		queryString = queryString.substring(0, queryString.lastIndexOf("&"));
		Logger.log(EcobeeAPI.class.getName(), "Entire Query String: " + queryString);
		return queryString;
	}
	
	/**Function to create an http POST request. This get request is specific to the ecobee api.
	 **/
	private static void makePostRequest(String path, String data, Array<HttpHeader> headers, final EcobeeAPIHttpCallback action)
	{
		HttpRequestBuilder build = new HttpRequestBuilder();
		build.newRequest();
		build.method("POST");
		build.url(path);

		//Adding all headers
		for(HttpHeader header: headers)
			build.header(header.mHeaderName, header.mHeaderValue);

		build.content(data);
		HttpRequest httpPostRequest = build.build();
	    
	    Logger.log(EcobeeAPI.class.getName(), "HTTP POST Request: ", httpPostRequest);

	    Gdx.net.sendHttpRequest(httpPostRequest, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) 
			{
				HttpStatus status = httpResponse.getStatus();
				if (status.getStatusCode() >= 200 && status.getStatusCode() < 300) 
				{
					String response = httpResponse.getResultAsString();
					Logger.log(EcobeeAPI.class.getName(), "HTTP POST Response" + status.getStatusCode() + ": " + response.replaceAll("\n", ""));
					action.done(response);
	            } else 
	            {
					String response = httpResponse.getResultAsString();
					Logger.log(EcobeeAPI.class.getName(), "HTTP POST Response" + status.getStatusCode() + ": " + response.replaceAll("\n", ""));
	            	Logger.log(EcobeeAPI.class.getName(), " HTTP POST Response: Something went wrong!");
	            }
			}
			
			@Override
			public void failed(Throwable t) {
				Logger.log(EcobeeAPI.class.getName(), "HTTP POST Failed..." + t.toString());
//				action.done("Error: " + t.toString());
			}
			
			@Override
			public void cancelled() {
				Logger.log(EcobeeAPI.class.getName(), "HTTP POST Cancelled...");
//				action.done("HTTP GET Cancelled");
			}
		}); 
	}
}
