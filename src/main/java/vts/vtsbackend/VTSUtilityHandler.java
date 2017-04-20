package vts.vtsbackend;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;

public class VTSUtilityHandler {
	
	public static String getClientAddressKey(SocketChannel clientChannel) {
		InetSocketAddress clientIPAddress;
		try {
			clientIPAddress = (InetSocketAddress)clientChannel.getRemoteAddress();
			return clientIPAddress.getAddress().getHostAddress()+":"+clientIPAddress.getPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception ex="+e);
			//e.printStackTrace();
		}
		return "";
	}
	
	public static boolean validDateFromDevice(Timestamp inTimestamp) {
		
		Instant currentTimestamp = Instant.now().plusSeconds(60*60*24); // added 1 day
		Timestamp nextDayTimestamp = Timestamp.from(currentTimestamp);
		
		if (inTimestamp.after(nextDayTimestamp)) {
			return false;
		} else 
			return true;
	}
	
	/**
	 * @param left
	 *            The most valued byte.
	 * @param right
	 *            The less valued byte.
	 * @return A short mixing the two parts.
	 */
	public static short byte2short(final byte left, final byte right) {
		return (short) ((left & 0xff) << 8 | right & 0xff);
	}
	
	public static Timestamp getParserDateTimestampDIKSUCHI(String date, String time) {
		Timestamp ts = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		try {
			java.util.Date parseTimestamp = sdf.parse(date + " " + time);
			ts = new Timestamp(parseTimestamp.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception ex="+e);
		}
		return ts;
	}
	
	public static Timestamp getParserDateTimestamp(String date, String time) {
		Timestamp ts = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			java.util.Date parseTimestamp = sdf.parse(date + " " + time);
			ts = new Timestamp(parseTimestamp.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception ex="+e);
		}
		return ts;
	}
	
	public static Timestamp getLocParserDateTimestamp(String date, String time) {
		Timestamp ts = null;
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy HHmmss");
		try {
			java.util.Date parseTimestamp = sdf.parse(date + " " + time);
			ts = new Timestamp(parseTimestamp.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Exception ex="+e);
		}
		return ts;
	}
	
	
	
	
	public static Timestamp getOrsacTimestamp(String date, String time) {
		Timestamp ts = null;
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy HHmmss");
		try {
			java.util.Date parseTimestamp = sdf.parse(date + " " + time);
			ts = new Timestamp(parseTimestamp.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Exception ex="+e);
		}
		return ts;
	}
	
	public static Timestamp getSA200ParserDateTimestamp(String date, String time) {
		Timestamp ts = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		try {
			java.util.Date parseTimestamp = sdf.parse(date + " " + time);
			ts = new Timestamp(parseTimestamp.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ts;
	}
	
	public static long compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {
		long milliseconds1 = oldTime.getTime();
		long milliseconds2 = currentTime.getTime();

		long diff = milliseconds2 - milliseconds1;
		long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);
		long diffHours = diff / (60 * 60 * 1000);
		long diffDays = diff / (24 * 60 * 60 * 1000);

		return Math.abs(diffMinutes);
	}
	
	public static String getVTSLat(Double latitude){
		String lat = Double.toString(latitude);
		lat = lat.replace(".", "#");
		String[] strlatSplt =lat.split("#");
		try{
			if (strlatSplt[0].length() > 2)
			{
				//// Latitude & Longitude Convertion
				//// latitude conversion
					lat = lat.replace("#", ".");
				double val1, val2, fval;
				val1 = Double.valueOf(lat.substring(0, 2));
				val2 = Double.valueOf(lat.substring(2, 7));
		
				fval = val2 / 60;
				lat = Double.toString(fval + val1);
		
				if (lat.length() > 9) {
					lat = lat.substring(0, 9);
				}
			}
			lat = lat.replace("#", ".");
		}catch(Exception e){
			System.out.println("e = "+e);
		}
		return lat;
	}
	
	public static String getVTSLong(Double longitude){
		String longi = Double.toString(longitude);
		longi = longi.replace(".", "#");
		String[] strlatSplt = longi.split("#");
		try{
			if (strlatSplt[0].length() > 2)
			{
				longi = longi.replace("#", ".");
				//// Latitude & Longitude Convertion
				//// latitude conversion
				double val1, val2, fval;
		
				val1 = 0;
				val2 = 0;
				fval = 0;
		
				val1 = Double.valueOf(longi.substring(0, 2));
				val2 = Double.valueOf(longi.substring(2, 7));
				fval = val2 / 60;
				longi = Double.toString(fval + val1);
		//
				if (longi.length() > 9) {
					longi = longi.substring(0, 9);
				}
			}
			longi = longi.replace("#", ".");
		}catch(Exception e){
			System.out.println("e = "+e);
		}
		return longi;
	}
}
