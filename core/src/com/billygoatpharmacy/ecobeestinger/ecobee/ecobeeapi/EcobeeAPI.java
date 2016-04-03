package com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeapi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request.HttpRequestData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.request.HttpRequestDataType;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.RequestPinResponseData;
import com.billygoatpharmacy.ecobeestinger.ecobee.ecobeeObjects.response.RequestPinAuthenticationResponseData;
import com.billygoatpharmacy.fileTools.FileManager;

public class EcobeeAPI {

	private static final String USER_AGENT = "demo-api-app";
	private static final String APIROOT = "api/1/";
	private static final String ROOT = "https://api.ecobee.com/";
	private static final String SCOPE = "smartWrite";
	private static final String APPKEY = "UUViLdsY2RgRNTAcrQUJXQm7lOxL87We";
	
	private static RequestPinResponseData sTempAuthCode;
	private static RequestPinAuthenticationResponseData sPinAuthResponse;
	
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
	}
	
	public static Boolean login(EcobeeAPIHttpCallback loginAction)
	{
		mLoginExternalCallback = loginAction;
		if(FileManager.doesFileExist("accessData.dat"))
		{
			sPinAuthResponse = sJson.fromJson(RequestPinAuthenticationResponseData.class, FileManager.readFile("accessData.dat"));
			
			if(sPinAuthResponse != null && sPinAuthResponse.refresh_token != null && sPinAuthResponse.refresh_token != "")
			{
				//Setting the temp code to our refresh code so that our pre-existing function will work without changes.
				sTempAuthCode = new RequestPinResponseData();
				sTempAuthCode.code = sPinAuthResponse.refresh_token;
				getAccessToken(loginCallback(), "refresh_token");
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
				RequestPinAuthenticationResponseData data = sJson.fromJson(RequestPinAuthenticationResponseData.class, response);

				Logger.log(this.getClass().getName(), sJson.toJson(data));
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
		HttpRequestDataType request_type = new HttpRequestDataType("response_type", "ecobeePin");
		HttpRequestDataType scope = new HttpRequestDataType("scope", SCOPE);
		HttpRequestDataType client_id = new HttpRequestDataType("client_id", APPKEY);
		
		HttpRequestDataType[] dt = {request_type, scope, client_id};
		HttpRequestData data = new HttpRequestData(dt);
		
		makeGetRequest(ROOT + "authorize", data, getGetPinCallback());
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

				sTempAuthCode = sJson.fromJson(RequestPinResponseData.class, response);
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
		HttpRequestDataType grant_type = new HttpRequestDataType("grant_type", grantType);
		HttpRequestDataType code = new HttpRequestDataType("code", sTempAuthCode.code);
		HttpRequestDataType client_id = new HttpRequestDataType("client_id", APPKEY);
		
		HttpRequestDataType[] dt = {grant_type, code, client_id};
		HttpRequestData data = new HttpRequestData(dt);
		
		makePostRequest(ROOT + "token", data, getAccessTokenCallback());
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

				sPinAuthResponse = sJson.fromJson(RequestPinAuthenticationResponseData.class, response);
				
				if(sPinAuthResponse.error != null && sPinAuthResponse.error != "")
					FileManager.saveFile("errorAccess.dat", sJson.toJson(sPinAuthResponse));
				else
					FileManager.saveFile("accessData.dat", sJson.toJson(sPinAuthResponse));
				mExternalCallback.done(response);
				mExternalCallback = null;
			}
		};
	}
	
	
	/*General Functions*/
	/**Function to create an http GET request. This get request is specific to the ecobee api.
	 **/
	private static void makeGetRequest(String path, HttpRequestData data, final EcobeeAPIHttpCallback action)
	{
		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.GET);
	    httpRequest.setUrl(path);
	    
	    //Setting Headers
	    httpRequest.setHeader("User-Agent", USER_AGENT);
	    httpRequest.setHeader("Accept", "application/json");
	    httpRequest.setHeader("Content-Type", "application/json");
	    httpRequest.setContent(data.toQueryString());
	    
	    Logger.log(EcobeeAPI.class.getName(), "HTTP GET Request: ", httpRequest);

	    Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) 
			{
				String status = httpResponse.getResultAsString();
				Logger.log(EcobeeAPI.class.getName(), "HTTP GET Response: " + status.replaceAll("\n", ""));
				action.done(status);
			}
			
			@Override
			public void failed(Throwable t) {
				// TODO Auto-generated method stub
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
	
	/**Function to create an http POST request. This get request is specific to the ecobee api.
	 **/
	private static void makePostRequest(String path, HttpRequestData data, final EcobeeAPIHttpCallback action)
	{
		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.POST);
	    httpRequest.setUrl(path);
	    
	    //Setting Headers
	    httpRequest.setHeader("User-Agent", USER_AGENT);
	    httpRequest.setHeader("Accept", "application/json");
	    httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
	    httpRequest.setHeader("Content-Length", "" + data.toQueryString().length());
	    httpRequest.setContent(data.toQueryString());
	    
	    Logger.log(EcobeeAPI.class.getName(), "HTTP POST Request: ", httpRequest);

	    Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) 
			{
				String status = httpResponse.getResultAsString();
				Logger.log(EcobeeAPI.class.getName(), "HTTP POST Response: " + status.replaceAll("\n", ""));
				action.done(status);
			}
			
			@Override
			public void failed(Throwable t) {
				Logger.log(EcobeeAPI.class.getName(), "HTTP POST Failed..." + t.toString());
				action.done("Error: " + t.toString());
			}
			
			@Override
			public void cancelled() {
				Logger.log(EcobeeAPI.class.getName(), "HTTP POST Cancelled...");
				action.done("HTTP GET Cancelled");
			}
		}); 
	}
}
