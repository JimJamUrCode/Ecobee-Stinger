package tools
{
	import avmplus.getQualifiedClassName;

	public class Logger
	{
		public function Logger()
		{
		}
		
		public static function log(classObj:Object, text:String):void
		{
			var className:String = getQualifiedClassName(classObj);
			className = className.substr(className.lastIndexOf(":") + 1);
			
			trace("[" + className + "] " + text);
		}
	}
}