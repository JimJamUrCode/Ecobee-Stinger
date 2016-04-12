package com.billygoatpharmacy.fileTools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

public class FileManager 
{
	public static final String STORAGEDIR = "savedata/";
	
	public static Boolean saveFile(String fileName, String data)
	{
		try
		{
			data = Base64Coder.encodeString(data);
			FileHandle file = Gdx.files.local(STORAGEDIR + fileName);
			file.writeString(data, false);
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
	
	public static String readFile(String fileName, Boolean isAsset)
	{
		String data = "";
		try
		{
			FileHandle file;
			if(isAsset)
				file = Gdx.files.internal(fileName);
			else
				file = Gdx.files.local(STORAGEDIR + fileName);

			if(file.exists())
				data = file.readString();
			else
				data = "File doesnt exist";
			
			if(isAsset == false)
				data = Base64Coder.decodeString(data);
		}
		catch(Exception e)
		{
			return "";
		}
		
		return data;
	}
	
	public static Boolean doesFileExist(String fileName)
	{
		FileHandle file = Gdx.files.local(STORAGEDIR + fileName);
		return file.exists();
	}
}
