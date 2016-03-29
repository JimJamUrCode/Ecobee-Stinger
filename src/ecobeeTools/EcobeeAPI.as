package ecobeeTools
{
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.net.URLRequestHeader;
	import flash.net.URLRequestMethod;
	import flash.text.ReturnKeyLabel;

	public class EcobeeAPI
	{
		private var baseURL:String = "https://www.ecobee.com/";
		private var mHasRegisteredApp:Boolean;
		private var mGetPinCallback:Function;
		
		public function EcobeeAPI()
		{
		}
		
		public function getPin(callback:Function):void
		{
			mGetPinCallback = callback;
			var url:String = "http://localhost:3000/login/getPinCode"
			
			var request:URLRequest = new URLRequest();
			request.method = URLRequestMethod.GET;
			request.url = url;
			
			var loader:URLLoader = new URLLoader();
			loader.dataFormat = URLLoaderDataFormat.TEXT; //don't know if this is really needed 
			loader.addEventListener(Event.COMPLETE, handleSuccess);
			loader.addEventListener(IOErrorEvent.IO_ERROR, handleError);
			loader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, handleSecurityError);
			
			loader.load(request);
		}
		
		public function hasRegisteredApp():Boolean
		{
			return mHasRegisteredApp;
		}
		
		private function handleSuccess(e:Event):void
		{
			trace("Success: " + e.target.data);
			mGetPinCallback(e.target.data);
		}
		
		private function handleError(e:IOErrorEvent):void
		{
			trace("Error: " + e.toString());
		}
		
		private function handleSecurityError(e:SecurityErrorEvent):void
		{
			trace("Security Error: " + e.toString());
		}
	}
}