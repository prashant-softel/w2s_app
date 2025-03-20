package ecryptDecryptAlgos;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static ecryptDecryptAlgos.EncryptionConstants.ENCRYPT_SPKEY;
import static ecryptDecryptAlgos.EncryptionConstants.ENCRYPT_INIT_VECTOR;
import static ecryptDecryptAlgos.EncryptionConstants.TOKEN_EXPIRATION_TIME_INMILISECONDS;
import CommonUtilityFunctions.MapUtility;
import MainClasses.ProjectConstants;
import MainClasses.TokenAuthentication;

import org.apache.commons.codec.binary.Base64;


public class KeyAuthentication extends ecryptDecrypt
{

	ProjectConstants m_objProjectConstants;
	static CommonUtilityFunctions.MapUtility m_objMapUtility;

	public KeyAuthentication() 
	{
		m_objMapUtility = new MapUtility();
	}
	
	public static HashMap<String,String> ValidateToken(String key,boolean bUseDefaultConstants,SecretKeySpec Spkey)
	{
		//using default constants
		if(bUseDefaultConstants)
		{	
			Spkey = ENCRYPT_SPKEY;
		}
		
		//decrypting  token
		String decrptedKey = decrypt(Spkey,ENCRYPT_INIT_VECTOR, key);
		 HashMap<String,String> map = new HashMap<>();  
		 //MapUtility objComm = new MapUtility();
		 
		 //convert decryptedkey to Hasmap
		 map = m_objMapUtility.stringToHashMap(decrptedKey);
		// map.put("chkVal", decrptedKey);
		 if(map.size() > 0)
		 {
			 
			 String value = map.get("tt");
			 
			 //check if token timestamp expired
			 boolean bIsTokenExpired = bIsTokenExpired(value);
			 if(bIsTokenExpired)
			 {
				 //return "Invalid";
				 map.put("status", "Expired");
			 }
			 else
			 {
				 map.put("status", "Valid");
				 //return "Valid";
			 }
			 
		 }
		 else
		 {
			//return "Invalid";
			map.put("status", "Invalid");
			 
		 }
		 return map;
		 
	}
	
	
		
	
	public static boolean bIsTokenExpired(String ts)
	{
		// token timestamp with TOKEN_EXPIRATION_TIME_INMILISECONDS for token expiration 
		 Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
		 Timestamp ts2 = Timestamp.valueOf(ts);
		 long tsTime1 = currentTimeStamp.getTime();
		 long tsTime2 = ts2.getTime();
		 long tsdiff = tsTime1 - tsTime2;
		 
		 if (tsdiff > TOKEN_EXPIRATION_TIME_INMILISECONDS) 
		 {
			 //token expired
		     return true;
		 }
		 else
		 {
			 //token valid
			 return false;
			 
		 }
	}
	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		String test = "OEJZdPPGBeNdz2v97A56GOZ3LJJfj9wcoBlE4ouijIRL5kNkgC3vVjsEdRuXnE9LWngrDjuDHscjO/xrD02z2A==";
		HashMap<String,String> map = ValidateToken(test, true,null);
	}

}
