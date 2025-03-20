package MainClasses;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtility
{
	public EmailUtility() 
	{
		//super(bAccessRoot, dbName);
		//super();
		
	}
    public static String sendEmail(String toEmail,String subject,int Amount, String message) throws AddressException,MessagingException
    {
    	String status="0";
    	
    	try
    	{
    		//String host="cs10.webhostbox.net"; 
    		String host="103.50.162.146"; 
    		String port="465";
    		port="587";
    		//host
    	String userName="no-reply@way2society.com";	
    	String password="Society@1234!";
    	Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = new Authenticator() 
        {
            public PasswordAuthentication getPasswordAuthentication() 
            {
                return new PasswordAuthentication(userName, password);
            }
        };
 
        Session session = Session.getInstance(properties, auth);
        Message msg = new MimeMessage(session);
        String FineDate= GetTodayDate();
        
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toEmail) };
        msg.addRecipients(Message.RecipientType.TO, toAddresses);
        InternetAddress[] CCAddress ={new InternetAddress("sujitkumar0304@gmail.com")};
        msg.addRecipients(Message.RecipientType.CC, CCAddress);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        //message.setSubject("HTML Message");  
        msg.setContent("<table  style='border-collapse:collapse'><tr>"
        		+ "<tr><td >Dear Member,</td></tr>"
        		+ "<tr><td>This notice is being send to inform you that management has imposed a fine of Rs.  <b>"+Amount+".00</b> for following violation :</td></tr>"
        		+ "<tr><td><br></td></tr>"
        		+ "<tr><td> "+message+" </td></tr>"
        		+ "<tr><td><br><br></td></tr>"
        		+ "<tr><td>Charges will be reflected in your next maintenance bill.</td></tr>"
        		+ "<tr><td> If you have any questions, please contact society Manager or Secretary.</td></tr>"
        		+ "<tr><td><br></td></tr>"
        		+ "<tr><td>From </td></tr>"
        		+ "<tr><td>Managing Committee.</td>"
        		+ "</tr></table>","text/html" );  
      
        
        // sends the e-mail
        Transport.send(msg);
        status="1";
    	}
		catch(Exception e)
    	{
			System.out.println("exception:"+e.getMessage());
			status="0";
    	}
    	
		return status;
    }
	 public static String sendGenericMail(String toEmail,String subject,String message) throws AddressException,MessagingException
    {
    	String status="0";
    	try
    	{
    		/*String host="smtp.gmail.com"; 
    		String port="587";
    		//port="587";
	    	String userName="sujitkumar0304@gmail.com";	
	    	String password="9869752739";
	    	Properties properties = new Properties();
	        properties.put("mail.smtp.host", host);
	        properties.put("mail.smtp.port", port);
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");*/
    	//String host="cs10.webhostbox.net";
    		String host="103.50.162.146";
    		String port="587";
    		//port="587";
    		//host
    	String userName="no-reply@way2society.com";	
    	String password="Society@1234!";
    	Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
	        Authenticator auth = new Authenticator() 
	        {
	            public PasswordAuthentication getPasswordAuthentication() 
	            {
	                return new PasswordAuthentication(userName, password);
	            }
	        };
	        Session session = Session.getInstance(properties, auth);
	        Message msg = new MimeMessage(session);
	        String FineDate= GetTodayDate();
	        msg.setFrom(new InternetAddress(userName));
	        InternetAddress[] toAddresses = { new InternetAddress(toEmail) };
	        msg.addRecipients(Message.RecipientType.TO, toAddresses);
	        msg.setSubject(subject);
	        msg.setSentDate(new Date());
	        //message.setSubject("HTML Message");  
	        msg.setContent(message,"text/html" );      
	        // sends the e-mail
	        Transport.send(msg);
	        status="1";
	    }
		catch(Exception e)
	    {
			System.out.println("exception:"+e.getMessage());
			status="0";
	    }
    	
		return status;
    }

	 public static String encryptData(String mailToEmail)
	 {
		 return mailToEmail;		 
	 }
	 
	 //returns Status
	public static String sendActivationEmail(String mailToEmail, String mailToName, boolean bLoginExist, String sCode) throws AddressException,MessagingException
	{
		String status="0";
		String subject = "Activation code";
		String onclickURL = "";
		String encryptedEmail = encryptData(mailToEmail);
		String sMsgString = "";
		
		if(bLoginExist)
		{
			onclickURL = "http://way2society.com/login.php?mCode=" + sCode + "&u=" + encryptedEmail;
		}
		else
		{
			onclickURL = "http://way2society.com/newuser.php?reg&u="+mailToEmail+"&n="+ mailToName +"&tkn="+ encryptedEmail +"&c="+ sCode;
			onclickURL = onclickURL + "&URL=http://way2society.com/Dashboard.php?View=MEMBER";
	
		}
		sMsgString = "<table  style='border-collapse:collapse'><tr>"
			+ "<tr><td >Dear Member,</td></tr>"
			+ "<tr><td>Your activation code is <b>"+sCode+"</b></td></tr>"
			+ "<tr><td><br></td></tr>"
			+ "<tr><td> please click on this link <a href = \""+onclickURL+"\" </td></tr>"
			+ "<tr><td><br><br></td></tr>"
			+ "<tr><td> If you have any questions, please contact society Manager or Secretary.</td></tr>"
			+ "<tr><td><br></td></tr>"
			+ "<tr><td>From </td></tr>"
			+ "<tr><td>Way2Society.com</td>"
			+ "</tr></table>";  
		
		System.out.println("sMsgString:"+ sMsgString);

		return sendGenericMail(mailToEmail, subject, sMsgString);
		//System.out.println("Status : "+status );
	}
	//Sending Bill Emails 
	public static String sendBillEmail(String mEmail, int iUnit_id, int iPeriodID, int iBillType,String BillFor ) throws AddressException,MessagingException
	{
		
		String status="0";
		String subject ="";
		if(iBillType == 0)
		{
			 subject = "Maintenance Bill For "+BillFor;
		}
		else
		{
			 subject = "Supplementary Bill For " +BillFor;
		}	
		String onclickURL = "";
		String encryptedEmail = encryptData(mEmail);
		String sMsgString = "";
		
		
		//Maintenance_bill.php?UnitID=247&PeriodID=93&BT=0
			onclickURL = "http://way2society.com/Maintenance_bill.php?UnitID="+iUnit_id+"&PeriodID="+ iPeriodID +"&BT="+ iBillType;
			onclickURL = onclickURL + "&URL=http://way2society.com/Dashboard.php?View=MEMBER";
	
		
		sMsgString = "<table  style='border-collapse:collapse'><tr>"
			+ "<tr><td >Dear Member,</td></tr>"
			+ "<tr><td><br></td></tr>"
			+ "<tr><td> please click on this link View Bill <a href = \""+onclickURL+"\"> View Bill</a> </td></tr>"
			+ "<tr><td><br><br></td></tr>"
			+ "<tr><td> If you have any questions, please contact society Manager or Secretary.</td></tr>"
			+ "<tr><td><br></td></tr>"
			+ "<tr><td>From </td></tr>"
			+ "<tr><td>Way2Society.com</td>"
			+ "</tr></table>";  
		
		System.out.println("sMsgString:"+ sMsgString);

		return sendGenericMail(mEmail, subject, sMsgString);
		//System.out.println("Status : "+status );
	}
	 
	
	 
	 public static String GetTodayDate() throws ParseException
	{
		DateFormat output = new SimpleDateFormat("dd-MM-yyyy");
		Date date=new Date();
		return output.format(date);
		
	}
    public static void main(String[] args) throws Exception 
    {
    EmailUtility obj=new EmailUtility();
    String email;
    String host="smtp.gmail.com";
    String port="587";
    //final String userName="2017poojakale@gmail.com";  
    //final String password="08379027702";
    final String userName="rohit.shinde2010@gmail.com";  
    final String password="Vertex04!";
    String toAddress="sujitkumar0304@gmail.com";
    String subject="Fine Notice";
    String message="sended email";
    int Amount=500;
    System.out.println("email started");
    email = EmailUtility.sendEmail(toAddress,subject,Amount,message);
    System.out.println("Result:"+email);
    	
    	
    }
    }
    