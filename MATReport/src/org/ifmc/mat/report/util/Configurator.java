package mat.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurator {
	private static Properties appProperties = null;
	private static Object o = new Object();
	static{
		synchronized(o){
			if(appProperties == null){
				init();
			}
		}
	}
	
	
	public static void init(){
		FileInputStream fIs = null;
		File propFile = null;
		try{
			System.out.println(System.getProperty("user.dir")); 
			String fileName = "config/MATReporting.properties";
			propFile = new File(fileName);
			if(propFile.canRead()){
				fIs = new FileInputStream(propFile);
			}

			appProperties = new Properties();
			appProperties.load(fIs);
			
		}catch(IOException ioE){
			ioE.printStackTrace();
			appProperties = null;
		}
		
	}
	
	public static String getPropertyValue(String key){
		if(appProperties == null){
			init();
		}
		return (String) appProperties.get(key);
	}

}
