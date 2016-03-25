package graph
{
	import flash.events.MouseEvent;
	
	import mx.charts.DateTimeAxis;
	import mx.charts.LineChart;
	import mx.core.FlexGlobals;
	import mx.managers.SystemManager;
	
	import spark.components.Application;
	import spark.components.Panel;

	public class ScrollingGraph
	{
		public static const GRAPHTIMEWINDOW:Number = (1000*60*60*18);//This is used to set the zoom of the graph
		private static const DELTADIVIDER:Number = (1000*60*60*18)/1000/60;//This is used to adjust slider sensitivity
		
		private var mLastX:Number = 0;
		private var mDateAxis:DateTimeAxis;
		private var mChart:LineChart;
		private var mGraphPanel:Panel;
		private var mLastDateInSet:Date;
		private var mFirstDateInSet:Date;
		private var mAdjusting:Boolean;
		
		public function ScrollingGraph(dateAxis:DateTimeAxis, linechart:LineChart, firstDataPoint:Date, lastDataPoint:Date, graphPanel:Panel = null)
		{
			mDateAxis = dateAxis;
			mChart = linechart;
			mFirstDateInSet = firstDataPoint;
			mLastDateInSet = lastDataPoint;
			mGraphPanel = graphPanel;
			mAdjusting = false;
		}
		
		public function mouseDownHandler(event:MouseEvent):void
		{
			mLastX = event.stageX;
			var sm:SystemManager = Application(FlexGlobals.topLevelApplication).systemManager as SystemManager;
			sm.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			sm.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
		}
		
		public function onAddTime():void 
		{
			mDateAxis.minimum = new Date(mDateAxis.minimum.getTime() + 1000*60*60*4);//add 4 hours
			mDateAxis.maximum = new Date(mDateAxis.maximum.getTime() + 1000*60*60*4);
		}
		
		private function mouseMoveHandler(event:MouseEvent):void
		{
			if(mAdjusting == false)
			{
				mAdjusting = true;
				
				var maxMinDiffHours:Number = ((DELTADIVIDER/1000)/60) / 60;
				
				//trace("Max Min Difference (Hours): " + maxMinDiffHours);
				
				//trace("Temp Chart Width: " + tempChart.width);
				var milliPerPixel:Number = mChart.width / DELTADIVIDER;
				trace("Graph Time Window: " + GRAPHTIMEWINDOW);
				var delta:Number = (mLastX - event.stageX) * milliPerPixel;// tempChart.width * viewportMax.getTime();
				//trace("Delta: " + delta);				
				
				if(delta < 1 && delta > -1)
				{
					trace("Breaking...");
					mAdjusting = false;
					return;
				}
				else if(delta > -1 && delta < 1)
				{
					trace("Breaking...");
					mAdjusting = false;
					return;
				}
				
				var mMaxAdjustedTime:Number = mDateAxis.maximum.getTime();
				var mMinAdjustedTime:Number = mDateAxis.minimum.getTime();
				
				delta = int(delta) * 3600000;
				trace("Max Date: " + mDateAxis.maximum.toString());
				trace("Min Date: " + mDateAxis.minimum.toString());
				trace("Diff in Date: " + (mMaxAdjustedTime - mMinAdjustedTime));
				//mLastDate = new Date(tempChart.dataProvider[tempChart.dataProvider.length - 1].date).getTime();
				trace("Last Date in set: " + mLastDateInSet.toString());
				
				if (mDateAxis.minimum.getTime() + delta < mFirstDateInSet.getTime())
				{
					trace("Reached Begining");
					mDateAxis.minimum = mFirstDateInSet;
					mDateAxis.maximum = new Date(mFirstDateInSet.getTime() + GRAPHTIMEWINDOW);
				}
				else if (mDateAxis.maximum.getTime() + delta  > mLastDateInSet.getTime())
				{
					var date:Date = new Date(mChart.dataProvider[mChart.dataProvider.length - 1].date);
					//trace(date);
					trace("Reached End");
					mDateAxis.maximum = date;
					mDateAxis.minimum = new Date(mDateAxis.maximum.getTime() - GRAPHTIMEWINDOW);
				}
				else
				{
					trace("Somewhere inbetween");
					
					var max:Date = new Date(mDateAxis.maximum.getTime() + delta);
					var min:Date = new Date(mDateAxis.minimum.getTime() + delta);
					trace("New Min Date: " + min);
					trace("New Max Date: " + max);
					mDateAxis.minimum = min;
					mDateAxis.maximum = max;
				}
				
				trace("Delta: " + delta);	
				
				setGraphLabel();
				
				//mChart.horizontalAxis = mDateAxis;
				mLastX = event.stageX;
				mAdjusting = false;
			}
		}
		
		private function setGraphLabel():void
		{
			if(mGraphPanel != null)
				mGraphPanel.title = mDateAxis.minimum.toUTCString() + " - " + mDateAxis.maximum.toUTCString();
//			mDateAxis = new DateTimeAxis();
//			mDateAxis.title = mDateAxis.minimum.toDateString() + " - " + mDateAxis.maximum.toDateString();
//			mDateAxis.dataUnits="minutes";
//			mDateAxis.dataInterval = 5;
//			mDateAxis.parseFunction = parseDate;
		}
		
		private function mouseUpHandler(event:MouseEvent):void
		{
			var sm:SystemManager = Application(FlexGlobals.topLevelApplication).systemManager as SystemManager
			sm.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			sm.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
		}
	}
}