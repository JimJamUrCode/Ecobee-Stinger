package ecobeeTools
{
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.SharedObject;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	
	import tools.Logger;

	public class EcobeeAPI
	{
		private static const STORAGELOCATION:String = "ecobeeStinger";
		private var baseURL:String = "http://localhost:3000/";
		private var mHasRegisteredApp:Boolean;
		private var mMakeRequestCallback:Function;
		private var mPinResponseData:Object;
		private var mLoginResponse:Object;
		
		//URL Request Variables
		private var mLoader:URLLoader;
		private var mLoaderCallback:Function;
		
		public function EcobeeAPI()
		{
		}
		
		public function getPin(callback:Function):void
		{
			mMakeRequestCallback = callback;
			var url:String = baseURL + "login/StingerGetPinCode";
			makeGetRequest(getPinCallback, url);
		}
		
		private function getPinCallback(e:Event):void
		{
			removeLoaderCallbacks();
			mPinResponseData = JSON.parse(e.target.data);
			Logger.log(this, "Success: " + JSON.stringify(mPinResponseData));
			
			if(mMakeRequestCallback != null)
			{
				mMakeRequestCallback(mPinResponseData);
				mMakeRequestCallback = null;
			}
		}
		
		public function login(callback:Function):void
		{
			mMakeRequestCallback = callback;
			var refreshToken:String = getRefreshToken();
			var variables:URLVariables = new URLVariables();
			
			if(refreshToken == null || refreshToken == "")//User needs to register the app with the ecobee account
			{
				variables.authcode = mPinResponseData.code;
				makePostRequest(loginCallback, baseURL + "login/StingerLogin", variables);//post the authcode
			}
			else//Login using the refresh token
			{
				variables.refreshtoken = refreshToken;
				makePostRequest(loginCallback, baseURL + "login/StingerLoginRefresh", variables);//post the authcode
			}
		}
		
		private function loginCallback(e:Event):void
		{
			removeLoaderCallbacks();
			mLoginResponse = JSON.parse(e.target.data);
			
			if(mLoginResponse.error == null)
				saveRefreshToken(mLoginResponse.registerResultObject.refresh_token + "");
			
			Logger.log(this, "Login Data: " + JSON.stringify(mLoginResponse));
			if(mMakeRequestCallback != null)
			{
				mMakeRequestCallback(mLoginResponse);
				mMakeRequestCallback = null;
			}
		}
		
		private function makePostRequest(callback:Function, url:String, urlVars:URLVariables):void
		{			
			mLoaderCallback = callback;
			
			var request:URLRequest = new URLRequest();
			
			request.method = URLRequestMethod.POST;
			
			request.data = urlVars;
			request.url = url;
			
			mLoader = new URLLoader();
			mLoader.dataFormat = URLLoaderDataFormat.TEXT; //don't know if this is really needed 
			addLoaderCallbacks();
			
			mLoader.load(request);
		}
		
		private function makeGetRequest(callback:Function, url:String):void
		{
			mLoaderCallback = callback;
			
			var request:URLRequest = new URLRequest();
			request.method = URLRequestMethod.GET;
			request.url = url;
			
			mLoader = new URLLoader();
			mLoader.dataFormat = URLLoaderDataFormat.TEXT; //don't know if this is really needed 
			addLoaderCallbacks();
			
			mLoader.load(request);
		}
		
		private function addLoaderCallbacks():void
		{
			mLoader.addEventListener(Event.COMPLETE, mLoaderCallback);
			mLoader.addEventListener(IOErrorEvent.IO_ERROR, handleError);
			mLoader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, handleSecurityError);
		}
		
		private function removeLoaderCallbacks():void
		{
			mLoader.removeEventListener(Event.COMPLETE, mLoaderCallback);
			mLoader.removeEventListener(IOErrorEvent.IO_ERROR, handleError);
			mLoader.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, handleSecurityError);
		}
		
		private function getRefreshToken():String
		{
			var so:SharedObject = SharedObject.getLocal(STORAGELOCATION);
			return so.data["refreshToken"]; 
		}
		
		private function saveRefreshToken(token:String):void
		{
			var so:SharedObject = SharedObject.getLocal(STORAGELOCATION);
			so.data["refreshToken"] = token;
			so.flush();
		}
		
		public function hasRefreshToken():Boolean
		{
			var token:String = getRefreshToken();
			if(token != null && token != "" && token !== undefined)
				return true;
			else 
				return false;
		}
		
		private function handleError(e:IOErrorEvent):void
		{
			Logger.log(this, "Error: " + JSON.stringify(e.target));
		}
		
		private function handleSecurityError(e:SecurityErrorEvent):void
		{
			Logger.log(this, "Security Error: " + e.toString());
		}
	}
}