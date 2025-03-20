package ecryptDecryptAlgos;

import java.util.concurrent.TimeUnit;

import javax.crypto.spec.SecretKeySpec;

/***************   ENCRYPT_INIT_VECTOR ****************************
 * The AES algorithm also requires a parameter called the Initialiation Vector. 
 * The IV is used in the process to randomize the encrypted message and prevent the key from easy guessing.
 */


public class EncryptionConstants 
{
	protected static  String ENCRYPT_KEY = null;
	protected static SecretKeySpec ENCRYPT_SPKEY =null;
	
	
	protected static  String ENCRYPT_INIT_VECTOR = null;
	
	protected static  float TOKEN_EXPIRATION_TIME_INMILISECONDS = TimeUnit.DAYS.toMillis(1);     // 1 day to milliseconds.
	//protected static  float TOKEN_EXPIRATION_TIME_INMILISECONDS =TimeUnit.MINUTES.toMillis(15); // 15 min to milliseconds.
	
	public static void setEncryptParameters(String key,String vector,float expTime)
	{
		//set Encrypt Parameters Constant
		ENCRYPT_KEY = key;
		ENCRYPT_INIT_VECTOR = vector;
	    TOKEN_EXPIRATION_TIME_INMILISECONDS = expTime; 
		
	}
	
	
}
