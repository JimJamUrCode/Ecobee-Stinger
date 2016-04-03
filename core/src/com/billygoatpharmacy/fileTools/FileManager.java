package com.billygoatpharmacy.fileTools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

public class FileManager 
{
	private static final String STORAGEDIR = "savedata/";
	
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
	
	public static String readFile(String fileName)
	{
		String data = "";
		try
		{
			FileHandle file = Gdx.files.local(STORAGEDIR + fileName);
			data = file.readString();
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
