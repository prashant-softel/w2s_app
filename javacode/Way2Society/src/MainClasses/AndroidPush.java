package MainClasses;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.cj.api.x.JsonValue;
import com.mysql.cj.x.json.JsonArray; 

public class AndroidPush {

    /**
     * Replace SERVER_KEY with your SERVER_KEY generated from FCM
     * Replace DEVICE_TOKEN with your DEVICE_TOKEN
     */
    private static String SERVER_KEY = "AAAANBD03UQ:APA91bEVzH0nJMYLU36Dz8pWit2O9aMdCEHuEbRfh_G7m3ywJ-dsdmzLW_4xT3sbIiqbOUwwxNXS8JjLG5tsOJItD5wEBWXoBKo61qtC4FU1q6q3OxBdCDoUPM-B14FI4S4dqbYNLopG";
    //private static String DEVICE_TOKEN = "fzBV9S2b_sc:APA91bFWfC0WZQjZxbCLOvP-4frnuAvZCmRzTAe8Y39fK6YFZZLTXzoeUoXTxf3ngluPHHr7PHFWO7sID4X0l_-9_XP3rSB7Klvm35VdnCbG-79huydGAMtqzhDtGP4HKFDcZB_CwI0L";
    //private static String DEVICE_TOKEN = "fygigNfB9pw:APA91bHQ7lB5ebSl08AqLBuAusrBfvlu5UQATrGw7rSw9RNlbe-X1DSFcSSJPnuCnyPCyOYiQwEfEFM-w0TTEJPhTtTlqktgJkqafCjozuQaRUR7T_3nYNAvFJArrcihKQ_rFdMYkeIz";
     /**
     * USE THIS METHOD to send push notification
     */
    public static void main(String[] args) throws Exception {
        String title = "My First Notification";
        String message = "Hello, I'm push notification";
        String deviceID = "cse0xOPuL2w:APA91bETXsiVxKoM7ZDgqF5ip_Nr_c4M-OR88382wcWFtyJ9QBTn3ZnhIdZ64f04Hd9X_pEI0fPZuj0MLTdsIBjeuRGimgRS4co0vurTqWHMB0b0PQNFVKCbHuuvn5e7T5Uq4Gl14gVa";
        String sDetails = "{\"page_ref\" : 11, \"period_id\" : 2, \"bill_type\" : 0}";
        JSONObject objJson = new JSONObject(sDetails);
        sendPushNotification(title, message, deviceID, "11", "3", "ViewEventPage", "12");
    }

    /**
     * Sends notification to mobile, YOU DON'T NEED TO UNDERSTAND THIS METHOD
     */
    public static String sendPushNotification(String sTitle, String sMessage, String sDeviceToken, String sMapID, String sPageRef, String sPageName, String sDetails) throws Exception {
        
    	String sResponse = "";
    	
    	try
    	{
    		JSONObject objJsonAdditionalDetails = new JSONObject();
    		//System.out.println(" Data : sPageRef: " + sPageRef+ "Title :" +sTitle+ "sMessage :" +sMessage+ "sPageName :" +sPageName+ "sDetails :" +sDetails+ "sMapID :" +sMapID);
	    	objJsonAdditionalDetails.put("title", sTitle);
	    	objJsonAdditionalDetails.put("message", sMessage);
	    	objJsonAdditionalDetails.put("page_ref", sPageRef);
	    	objJsonAdditionalDetails.put("pagename", sPageName);
	    	objJsonAdditionalDetails.put("details", sDetails);
	    	objJsonAdditionalDetails.put("map_id", sMapID);
	    	objJsonAdditionalDetails.put("map_id", sMapID);
	    	
	    	//System.out.println(objJsonAdditionalDetails);

			JSONObject objJsonNotificationDetails = new JSONObject();
			objJsonNotificationDetails.put("title", sTitle);
			objJsonNotificationDetails.put("body", sMessage);
			objJsonNotificationDetails.put("click_action", "FCM_PLUGIN_ACTIVITY");
			objJsonNotificationDetails.put("icon", "https://way2society.com/images/notifyicon.png");     //add icon path in way2society
			
	    	if(sDeviceToken.isEmpty())
	    	{
	        	// System.out.println("Invalid DeviceID");
	        	 sResponse = "Invalid DeviceID";
	        }
	        else
	        {
	        	/*String pushMessage = "{\"data\":{\"title\":\"" +
	    			sTitle +
	                "\",\"message\":\"" +
	                sMessage +
	                "\"},\"to\":\"" +
	                sDeviceToken +
	                "\"}";*/
	        	
	        	/*String pushMessage = "{\"data\":" + objJsonAdditionalDetails + ",\"to\":\"" +
		                sDeviceToken + "\"}";*/
	        	
	        	String pushMessage = "{\"data\":" + objJsonAdditionalDetails + ",\"to\":\"" +
	                sDeviceToken + "\",\"notification\":" + objJsonNotificationDetails + "}";
	        	
	        	//System.out.println(pushMessage);
	        	
		        // Create connection to send FCM Message request.
		        URL url = new URL("https://fcm.googleapis.com/fcm/send");
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
		        //conn.setRequestProperty("click_action", "FCM_PLUGIN_ACTIVITY");
		        conn.setRequestProperty("Content-Type", "application/json");
		        conn.setRequestMethod("POST");
		        conn.setDoOutput(true);
		
		        // Send FCM message content.
		        OutputStream outputStream = conn.getOutputStream();
		        outputStream.write(pushMessage.getBytes());
		
		       // System.out.println(conn.getResponseCode());
		      //  System.out.println(conn.getResponseMessage());
		        
		        sResponse = Integer.toString(conn.getResponseCode());
	        }
    	}
    	catch(Exception ex)
    	{
    		sResponse = ex.getMessage();
    		 System.out.println("exception:"+sResponse);
    	}
    	
    	 return sResponse;    
    }
}
