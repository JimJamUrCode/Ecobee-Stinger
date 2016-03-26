package ecobeeTools
{
	import flash.text.ReturnKeyLabel;
	
	import mx.collections.ArrayCollection;

	public class EcobeeDataParser
	{
		private var mData:Array;
		private var mHeaders:Array;
		
		[Bindable]
		private var mImportedTempData:ArrayCollection;
		
		public function EcobeeDataParser()
		{
		}
		
		public function parseData(data:Array):void
		{
			mData = data;
			parseHeaders();
			parseDataByLine();
		}
		
		public function getImportedData():ArrayCollection
		{
			return mImportedTempData;
		}
		
		private function parseHeaders():void
		{
			mHeaders = mData.shift().split(",");//Removes the headers from the list and splits them into an array
		}
		
		public function getHeaders():Array
		{
			return mHeaders;
		}
		
		private function parseDataByLine():void
		{
			mImportedTempData = new ArrayCollection();
			var parts:Array = new Array();//Used to decifer the csv data
			var obj:Object;
			var partsLength:int = mHeaders.length-1;
			
			var len:int = mData.length;
			for(var i:int = 0; i < len; i++)
			{
				parts = mData[i].split(",");
				obj = new Object();
				var short:String = parts[0];
				
				var pattern:RegExp = /-/g;
				short = short.replace(pattern,"/");
				
				obj[mHeaders[0]] = short + " " + parts[1];
				for(var j:int = 2; j < partsLength; j++)
				{
					if(parts[j] != null && parts[j] != "" && parts[j] != "\r")
						obj[mHeaders[j]] = parts[j];
					else
						obj[mHeaders[j]] = calcAvg(i, j);
					
//						obj["avg"] = (Number(obj.downstairsTemp) + Number(obj.upstairsTemp)) /2;
				}
				mImportedTempData.addItem(obj);//Date, set temp
			}
		}
		
		private function calcAvg(index:int, property:int):Number
		{
			var below:Number = Number(checkIndexForItem(index-1, property, -1));
			var above:Number = Number(checkIndexForItem(index+1, property, 1));
			var avg:Number = (below + above) / 2;
			return avg;
		}
		
		private function checkIndexForItem(index:int, property:int, searchingDirection:int = -1):Object
		{
			if(index > mData.length-1)
				index = mData.length-1;
			else if (index < 0)
				index = 0;
			
			var parts:Array = mData[index].split(",");
			
			if(property == 4 || property == 15)//Calendar Item
				return 0;
			if(parts[property] == "" || parts[property] == "\r" || parts[property] == null)
				return checkIndexForItem((index + searchingDirection), property);
			
			return parts[property];
		}
		
		public function findMaxMinDate():Object
		{
			var rawData:Object;
			var dateString:String = "";
			var date:Date;
			var max:Date;
			var min:Date;
			
			for(var i:int = 0; i < mImportedTempData.length; i++)
			{
				rawData = mImportedTempData[i];
				dateString = rawData[mHeaders[0]];
				date = new Date(dateString);
				
				if(min == null)
					min = date;
				if(max == null)
					max = date;
				
				if(date.getTime() < min.getTime())
					min = date;
				if(date.getTime() > max.getTime())
					max = date;
			}
			
			return {maxDate:max, minDate:min};
		}
	}
}