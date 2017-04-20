package vts.vtsbackend.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import org.json.JSONArray;
//import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import sun.misc.BASE64Encoder;

public class getLocationClass {
	public static void main(String args[]){
		getLoc(21.0000,78.000);
	}
	
	
	public static String getLoc(double lat,double longi){
		int i = 0;
		String result = null;
//		shpName.add("India:coriddor_1");
	try {
		
			String webPage = "http://209.190.15.26:8686/location/location.asmx/LocationService?latitude="+lat+"&longitude="+longi;
		

			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			 result = sb.toString();
//			 JSONObject json = new JSONObject();
//			 JSONArray arr = json.getJSONArray("Location");
//			 json = arr.getJSONObject(0);
//			 result = json.getString("location");
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(result);
			JSONArray msg = (JSONArray) json.get("Location");
			json = (JSONObject) msg.get(0);
			result = (String) json.get("location");
//		System.out.println("Response from the server: " + json.get("location"));
	} catch (Exception e) {
		e.printStackTrace();
	} 
	return result;
	}
}