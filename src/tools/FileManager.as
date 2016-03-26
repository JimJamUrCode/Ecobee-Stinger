package tools
{
	import flash.events.Event;
	import flash.filesystem.File;
	import flash.filesystem.FileMode;
	import flash.filesystem.FileStream;
	import flash.net.FileFilter;
	import flash.net.SharedObject;
	import flash.utils.ByteArray;
	
	public class FileManager
	{
		private var mFile:File;
		private var mFileLines:Array;
		private var mSuccessCallback:Function;
		private var mFailCallback:Function;
		
		public function FileManager()
		{
		}
		
		public function SelectFileToLoad(successCallback:Function, failCallback:Function):void 
		{
			mSuccessCallback = successCallback;
			mFailCallback = failCallback;
			
			mFile = new File();
			var txtFilter:FileFilter = new FileFilter("Results Files", "*.csv"); 
			mFile.browseForOpen("Open", [txtFilter]); 
			mFile.addEventListener(Event.SELECT, onResultsFileSelected);
			mFile.addEventListener(Event.CANCEL, onCanceled);
		}
		
		public function reloadFile(path:String, successCallback:Function, failureCallback:Function):void
		{
			mSuccessCallback = successCallback;
			mFailCallback = failureCallback;
			
			mFile = new File(path);
			onResultsFileSelected(new Event(""));
		}
		
		private function onCanceled(evt:Event):void
		{
			log("User Canceled File Selection");
			
			if(mFailCallback)
				mFailCallback();
			
			mFailCallback = null;
		}
		
		private function onResultsFileSelected(evt:Event):void 
		{ 
			var ba:ByteArray = new ByteArray();
			
			var fileStream:FileStream = new FileStream(); 
			fileStream.open(mFile, FileMode.READ); 
			fileStream.readBytes(ba);
			fileStream.close();
			
			loadResults(ba);
		} 
		
		private function loadResults(ba:ByteArray):void 
		{ 
			var str:String = ba.toString();
			mFileLines = str.split("\n");
			mFileLines.shift();
			mSuccessCallback(mFile, mFileLines);
		} 
		
		private function log(message:String):void
		{
			trace("[FileManager]: " + message);
		}
	}
}