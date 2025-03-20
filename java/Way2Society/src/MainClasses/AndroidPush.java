package MainClasses;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    public static String SERVER_KEY = "AAAAwnzF4hg:APA91bElM0K-jNhJPx5RxfrQTsoe320cAJ9wY54C-4ZrchS0rjBOTnsSMieE2ZYN51QZU4NCLEYDreSoeb6aZJr5UrpxwTSKxdNUXM9MvfDpGbKuUcXjjzeFx3ZMJfqLeAbtPM1l6TpA";
   /**
     * USE THIS METHOD to send push notification
     */
    public static void main(String[] args) throws Exception {
        String title = "My First Notification";
        String message = "Hello, I amm push notification";
        String deviceID = "eL6cZDRhl4k:APA91bEkvo-967JF8FD-ENb0MdoHA8jZ8iZzfF9086FKKmAgsHNXbHe68-pxdUMyx3XzEkqFJVudQlgg_6AxyXUWivl7Y_eGdyUbTSICbgylzJMA84hEbnk";
        String sDetails = "{\"page_ref\" : 11, \"period_id\" : 2, \"bill_type\" : 0}";
        JSONObject objJson = new JSONObject(sDetails);
        sendPushNotification(title, message, deviceID, "11694", "3", "ViewEventPage", "12");
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
	        	
	        	String pushMessage = "{\"data\":" + objJsonAdditionalDetails + ",\"to\":\"" +
	                sDeviceToken + "\",\"notification\":" + objJsonNotificationDetails + "}";
	        	
	        	//System.out.println(pushMessage);
	        	
		        // Create connection to send FCM Message request.
	        	//URL url = new URL("https://fcm.googleapis.com/fcm/send");
		        //URL url = new URL("https://fcm.googleapis.com/v1/projects/ionicfirebase-f2557/messages:send");
		        URL url = new URL("https://fcm.googleapis.com/fcm/send");
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
		        //conn.setRequestProperty("Authorization", "Bearer"+ SERVER_KEY);
		        conn.setRequestProperty("click_action", "FCM_PLUGIN_ACTIVITY");
		        conn.setRequestProperty("Content-Type", "application/json");
		        conn.setRequestMethod("POST");
		        conn.setDoOutput(true);
		
		        // Send FCM message content.
		        OutputStream outputStream = conn.getOutputStream();
		        outputStream.write(pushMessage.getBytes());
		
		       // System.out.println(conn.getResponseCode());
		      System.out.println(conn.getResponseMessage());
		        
		        sResponse = Integer.toString(conn.getResponseCode());
		        System.out.println(sResponse);
	        }
    	}
    	catch(Exception ex)
    	{
    		sResponse = ex.getMessage();
    		 System.out.println("exception:"+sResponse);
    	}
    	
    	 return sResponse;    
    }
    /*public static String sendPushNotification(String sTitle, String sMessage, String sDeviceToken, String sMapID, String sPageRef, String sPageName, String sDetails) throws Exception {
        String sResponse = "";
        
        try {
            if (sDeviceToken == null || sDeviceToken.trim().isEmpty()) {
                return "Invalid DeviceID";
            }

            JSONObject objJsonAdditionalDetails = new JSONObject();
            objJsonAdditionalDetails.put("title", sTitle);
            objJsonAdditionalDetails.put("message", sMessage);
            objJsonAdditionalDetails.put("page_ref", sPageRef);
            objJsonAdditionalDetails.put("pagename", sPageName);
            objJsonAdditionalDetails.put("details", sDetails);
            objJsonAdditionalDetails.put("map_id", sMapID);

            JSONObject objJsonNotificationDetails = new JSONObject();
            objJsonNotificationDetails.put("title", sTitle);
            objJsonNotificationDetails.put("body", sMessage);
            objJsonNotificationDetails.put("click_action", "FCM_PLUGIN_ACTIVITY");
            objJsonNotificationDetails.put("icon", "https://way2society.com/images/notifyicon.png");

            String pushMessage = new JSONObject()
                .put("data", objJsonAdditionalDetails)
                .put("to", sDeviceToken)
                .put("notification", objJsonNotificationDetails)
                .toString();
           // URL url = new URL("https://fcm.googleapis.com/fcm/send");
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            try (OutputStream outputStream = conn.getOutputStream()) {
                outputStream.write(pushMessage.getBytes("UTF-8"));
            }

            int responseCode = conn.getResponseCode();
            sResponse = "Response Code: " + responseCode;

            // Read response from the server
            try (java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                sResponse += "\nResponse Message: " + response.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sResponse = "Error: " + ex.getMessage();
        }

        return sResponse;
    }*/
    /*public static String sendPushNotification(String sTitle, String sMessage, String sDeviceToken, String sMapID, String sPageRef, String sPageName, String sDetails) throws Exception {
        String sResponse = "";

        try {
            JSONObject objJsonAdditionalDetails = new JSONObject();
            objJsonAdditionalDetails.put("title", sTitle);
            objJsonAdditionalDetails.put("message", sMessage);
            objJsonAdditionalDetails.put("page_ref", sPageRef);
            objJsonAdditionalDetails.put("pagename", sPageName);
            objJsonAdditionalDetails.put("details", sDetails);
            objJsonAdditionalDetails.put("map_id", sMapID);

            JSONObject objJsonNotificationDetails = new JSONObject();
            objJsonNotificationDetails.put("title", sTitle);
            objJsonNotificationDetails.put("body", sMessage);
            objJsonNotificationDetails.put("click_action", "FCM_PLUGIN_ACTIVITY");
            objJsonNotificationDetails.put("icon", "https://way2society.com/images/notifyicon.png");

            if (sDeviceToken.isEmpty()) {
                return "Invalid DeviceID";
            }

            String pushMessage = "{\"data\":" + objJsonAdditionalDetails + ",\"to\":\"" +
                    sDeviceToken + "\",\"notification\":" + objJsonNotificationDetails + "}";

            URL url = new URL("https://fcm.googleapis.com/fcm/send"); // For Legacy API
            //URL url = new URL("https://fcm.googleapis.com/v1/projects/ionicfirebase-f2557/messages:send"); // For FCM v1 API

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + SERVER_KEY); // Use "Bearer " for FCM v1 API
            //conn.setRequestProperty("Authorization", "Bearer " + SERVER_KEY);

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(pushMessage.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = conn.getResponseCode();

           
            BufferedReader in;
            if (responseCode >= 200 && responseCode < 300) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream())); // Success response
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream())); // Error response
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            sResponse = response.toString();
            System.out.println("FCM Response: " + sResponse);
        } catch (Exception ex) {
            sResponse = "Error: " + ex.getMessage();
            System.out.println(sResponse);
        }

        return sResponse;
    }*/


}
