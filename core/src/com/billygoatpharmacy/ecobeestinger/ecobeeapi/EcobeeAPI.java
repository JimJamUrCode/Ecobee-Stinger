package com.billygoatpharmacy.ecobeestinger.ecobeeapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.billygoatpharmacy.ecobeestinger.Logger;
import com.billygoatpharmacy.ecobeestinger.ecobeeObjects.HttpGetPinResponseData;

public class EcobeeAPI {

	private static String USER_AGENT = "demo-api-app";
	private static String APIROOT = "api/1/";
	private static String ROOT = "https://www.ecobee.com/";
	private static String SCOPE = "smartWrite";
	private static String APPKEY = "UUViLdsY2RgRNTAcrQUJXQm7lOxL87We";
	
	public EcobeeAPI()
	{
		
	}
	
	public static void getPin(EcobeeAPIHttpCallback action)
	{		
		EcobeeAPIHttpGetDataType request_type = new EcobeeAPIHttpGetDataType("response_type", "ecobeePin");
		EcobeeAPIHttpGetDataType scope = new EcobeeAPIHttpGetDataType("scope", SCOPE);
		EcobeeAPIHttpGetDataType client_id = new EcobeeAPIHttpGetDataType("client_id", APPKEY);
		
		EcobeeAPIHttpGetDataType[] dt = {request_type, scope, client_id};
		EcobeeAPIHttpGetData data = new EcobeeAPIHttpGetData(dt);
		
		Json json = new Json();
		json.setOutputType(OutputType.json);
		Logger.log(EcobeeAPI.class.getName(), "GET Ecobee Pin...");
		
		makeGetRequest("home/authorize", data, action);
	}
	
	public static void makeGetRequest(String path, EcobeeAPIHttpGetData data, final EcobeeAPIHttpCallback action)
	{
		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.GET);
		
		//Adjusting the path to contain the get data
		path = ROOT + path;
		path += "?" + data.toQueryString();
	    httpRequest.setUrl(path);
	    
	    //Setting Headers
	    httpRequest.setHeader("User-Agent", USER_AGENT);
	    httpRequest.setHeader("Accept", "application/json");
	    httpRequest.setHeader("Content-Type", "application/json");
	    
	    Json js = new Json();
	    js.setOutputType(OutputType.json);
	    Logger.log(EcobeeAPI.class.getName(), "HTTP GET Request: " + js.toJson(httpRequest));

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
	
	public static void makePostRequest(String path, EcobeeAPIHttpGetData data)
	{
	    //Items used for post
//    	httpRequest.setHeader("Content-Length",  content.length() + "");
//    	httpRequest.setContent(content);
	}
}
