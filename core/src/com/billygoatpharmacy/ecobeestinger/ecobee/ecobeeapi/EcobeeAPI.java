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
import com.badlogic.gdx.utils.TimeUtils;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request.PinAuthenticationRequest;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request.PinRequest;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request.ThermostatRuntimeRequest;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request.ThermostatRuntimeRequestColumnGenerator;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.ThermostatRuntimeResposeData;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.Selection;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request.ThermostatRequest;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.PinRequestResponseData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.ThermostatsResposeData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.PinAuthenticationResponseData;
import com.billygoatpharmacy.fileTools.FileManager;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EcobeeAPI {

	public static EcobeeConfig sEcobeeConfig;
	
	private static PinRequestResponseData sTempAuthCode;
	private static PinAuthenticationResponseData sPinAuthResponse;
	private static ThermostatsResposeData sThermostatsResponseData;
	private static ThermostatRuntimeResposeData sThermostatRuntimeResponseData;

	private static EcobeeAPIHttpCallback sExternalCallback;
	private static EcobeeAPIHttpCallback sLoginExternalCallback;
	
	private static Json sJson;
	
	public static void init()
	{
		sJson = new Json();
		sJson.setTypeName(null);
		sJson.setUsePrototypes(false);
		sJson.setOutputType(OutputType.json);

		sEcobeeConfig = sJson.fromJson(EcobeeConfig.class, FileManager.readFile("ecobeeConfig.cfg", true));
	}
	
	public static Boolean login(EcobeeAPIHttpCallback loginAction)
	{
		sLoginExternalCallback = loginAction;
		if(FileManager.doesFileExist("accessData.dat"))
		{
			sPinAuthResponse = sJson.fromJson(PinAuthenticationResponseData.class, FileManager.readFile("accessData.dat", false));

			long timeDiff = (new Date().getTime() - sPinAuthResponse.receieved_on) / 1000;

			if(sPinAuthResponse == null || sPinAuthResponse.refresh_token == null)//No way to authenticate, user needs to repin auth
			{
				//Pin authentication need to occur
				return false;
			}
			else if(sPinAuthResponse.refresh_token != null && sPinAuthResponse.refresh_token != "" && sPinAuthResponse.expires_in > timeDiff &&
					sPinAuthResponse.receieved_on != 0)
			{
				loginAction.done(sJson.toJson(sPinAuthResponse));
				return true;
			}
			else if(sPinAuthResponse.expires_in < timeDiff || sPinAuthResponse.receieved_on == 0)//The access codes have expired, try and get re-issued
			{
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
				sLoginExternalCallback.done(response);
				sLoginExternalCallback = null;
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
		sExternalCallback = action;
		
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
			sExternalCallback.done(sJson.toJson(resp));
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
				sExternalCallback.done(response);
				sExternalCallback = null;
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
		
		sExternalCallback = action;
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
			sExternalCallback.done(sJson.toJson(resp));
		}
	}

	private static void getAccessTokenFromRefreshToken(EcobeeAPIHttpCallback action, String grantType)
	{
		Logger.log(EcobeeAPI.class.getName(), "POST attemping reauthentication...");

		sExternalCallback = action;
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
			sExternalCallback.done(sJson.toJson(resp));
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
				
				sExternalCallback.done(response);
				sExternalCallback = null;
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
		
		sExternalCallback = action;
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
			sExternalCallback.done(sJson.toJson(resp));
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

				sThermostatsResponseData = sJson.fromJson(ThermostatsResposeData.class, response);
				
				sExternalCallback.done(response);
				sExternalCallback = null;
			}
		};
	}

	public static void setThermostatsResponseData(ThermostatsResposeData data)
	{
		sThermostatsResponseData = data;
	}

	public static ThermostatsResposeData getThermostatsResponse()
	{
		return sThermostatsResponseData;
	}
	
	//*Function used to get runtime reports for a certain thermostat
	public static void getThermostatRuntimeReport(EcobeeAPIHttpCallback action, String thermostatsIDCSV)
	{
		sExternalCallback = action;

		ThermostatRuntimeResposeData data = sJson.fromJson(ThermostatRuntimeResposeData.class,FileManager.readFile("ThermostatRuntimeResponse.resp",false));

		float fifteenMin = 1000*60*15L;

		if(data != null && TimeUtils.millis() - data.receivedAt < fifteenMin)
		{
			Logger.log(EcobeeAPI.class.getName(), "Using cached ecobee thermostat runtime report...");
			sExternalCallback.done(sJson.toJson(data));
			sExternalCallback = null;
			return;
		}

		//CONTINUING ONLY IF 15 MINUTES HASNT PASSED SINCE THE LAST REQUEST
		Logger.log(EcobeeAPI.class.getName(), "Get ecobee thermostat runtime report...");
		//Create the start and end dates for the data that we want to receive
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
		String today = formatter.format(Calendar.getInstance().getTime());

		String aMonthAgo= formatter.format(new Date(Calendar.getInstance().getTime().getTime() - (1000*60*60*24*2L)));


		//Create list of things we want to get from ecobee
		ThermostatRuntimeRequestColumnGenerator gen = new ThermostatRuntimeRequestColumnGenerator();
		gen.outdoorTemp = true;
		gen.outdoorHumidity = true;
		gen.wind = true;
		gen.zoneCoolTemp = true;
		gen.zoneHeatTemp = true;
		gen.zoneAveTemp = true;
		gen.zoneHumidity = true;
//		gen.zoneHvacMode = true;
//		gen.zoneOccupancy = true;

		//Setup all of the request data
		ThermostatRuntimeRequest req = new ThermostatRuntimeRequest();
		Selection newSelection = new Selection();
		newSelection.selectionMatch = thermostatsIDCSV;
		newSelection.selectionType = Selection.SelectionType.thermostats;
		newSelection.includeRuntime = true;
		newSelection.includeExtendedRuntime = true;


		req.selection = newSelection;
		req.startDate = aMonthAgo;
		req.endDate = today;
		req.includeSensors = true;
		try
		{
			req.columns = ThermostatRuntimeRequestColumnGenerator.getCSV(gen);
		}
		catch (IllegalAccessException e)
		{
			req.columns = "zoneCoolTemp," + "zoneHeatTemp," + "zoneHumidity," + "zoneHvacMode,";
		}

		String queryString = "json=" + sJson.toJson(req);

		Array<HttpHeader> headers = new Array<HttpHeader>();
		headers.add(new HttpHeader("Accept", "application/json"));
		headers.add(new HttpHeader("Authorization", "Bearer " + sPinAuthResponse.access_token));
		headers.add(new HttpHeader("User-Agent", sEcobeeConfig.user_agent));
		headers.add(new HttpHeader("Content-Type", "application/json;charset=UTF-8"));

		makeGetRequest(sEcobeeConfig.http_root + sEcobeeConfig.api_root + "runtimeReport", queryString, headers, getThermostatRuntimeReportCallback());
	}

	/**Internal action callback for the getAllThermostats() function.
	 *
	 **/
	private static EcobeeAPIHttpCallback getThermostatRuntimeReportCallback()
	{
		return new EcobeeAPIHttpCallback()
		{
			@Override
			public void done(String response)
			{
				Logger.log(EcobeeAPI.class.getName(), "Ecobee Thermostat Runtime Report Received...");

				sThermostatRuntimeResponseData = sJson.fromJson(ThermostatRuntimeResposeData.class, response);

				if(sThermostatRuntimeResponseData != null && sThermostatRuntimeResponseData.error == null) {

					sThermostatRuntimeResponseData.receivedAt = TimeUtils.millis();
					FileManager.saveFile("ThermostatRuntimeResponse.resp", sJson.toJson(sThermostatRuntimeResponseData));
					sExternalCallback.done(response);
					sExternalCallback = null;
				}
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
		final HttpRequest httpGetRequest = build.build();
	    
	    Logger.log(EcobeeAPI.class.getName(), "HTTP GET Request: ", httpGetRequest);

	    Gdx.net.sendHttpRequest(httpGetRequest, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) 
			{
				String status = httpResponse.getResultAsString();

				if(status.length() < 5)
				{
					byte[] result = httpResponse.getResult();
					HttpStatus nst = httpResponse.getStatus();
					Logger.log(EcobeeAPI.class.getName(), "Waiting for more data: " + status.replaceAll("\n", ""));
				}
				else {
					Logger.log(EcobeeAPI.class.getName(), "HTTP GET Response: " + status.replaceAll("\n", ""));
					action.done(status);
				}
			}
			
			@Override
			public void failed(Throwable t) {
				Logger.log(EcobeeAPI.class.getName(), "HTTP GET Failed..." + t.toString());
				t.printStackTrace();
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
