package vfs;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadConfig {

	
	public Properties readConfig(final String file_name)
	{
		Properties property = new Properties();
		try(FileInputStream is = new FileInputStream(file_name))
		{
			property.load(is);
			
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		return property;
	}
}
