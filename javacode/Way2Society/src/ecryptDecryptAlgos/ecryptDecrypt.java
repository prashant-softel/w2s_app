package ecryptDecryptAlgos;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
//import static ecryptDecryptAlgos.EncryptionConstants.ENCRYPT_SPKEY;
//import static ecryptDecryptAlgos.EncryptionConstants.ENCRYPT_KEY;


public class ecryptDecrypt  extends EncryptionConstants
{
	
	private String String;



	public ecryptDecrypt()
	{
		ENCRYPT_SPKEY = ecryptDecrypt.generate128BitAesKey(EncryptionConstants.ENCRYPT_KEY);
	}
	 public static SecretKeySpec generate128BitAesKey(String myKey)
	 {
		 /****
		  * generate128BitAesKey() is used to generate a secret key of 128Bit size  for AES algorithm.
		  * Message digests are used to protect the integrity of a 
		  * piece of data or media to detect changes and alterations to any part of a message.
		  */
		 
	        MessageDigest sha = null;
	        SecretKeySpec secretKey = null;
	        
	        try 
	        {
	            
	        	byte[] key = myKey.getBytes("UTF-8");
	            sha = MessageDigest.getInstance("SHA-1");
	            key = sha.digest(key);
	            key = Arrays.copyOf(key, 16); // use only first 128 bit
	            secretKey = new SecretKeySpec(key, "AES");
	        } 
	        catch (NoSuchAlgorithmException e) 
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } 
	        catch (UnsupportedEncodingException e) 
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        
			return secretKey;
	        
	              
	    
	 }
	 
	public static String encrypt(SecretKeySpec skeySpec, String initVector, String value)
	{
		// Performs Encryption

        try 
        {
        	//Create a random initialization vector
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            //SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            // Encrypt cipher	
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
           
            //generating encoded string url safe
            //return Base64.encodeBase64String(encrypted);
            return Base64.encodeBase64URLSafeString(encrypted);
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }

        return null;
    }
	
	

    public static String decrypt(SecretKeySpec skeySpec, String initVector, String encrypted)
    {
    	 // Performs decryption
    	try 
    	{
    		IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            //SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            
            // Decrypt cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        

        return null;
    }
    
    
    
	
	public static void main(String[] args) throws NoSuchAlgorithmException 
	{
		// TODO Auto-generated method stub
		
		String key = "Bar12345Bar12345"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV
        //String res = encrypt(EncryptionConstants.ENCRYPT_KEY, EncryptionConstants.ENCRYPT_INIT_VECTOR, "Hello World");	
        //System.out.println(decrypt(EncryptionConstants.ENCRYPT_KEY, EncryptionConstants.ENCRYPT_INIT_VECTOR,res ));
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "demo");
        map.put("fname", "fdemo");
        
        Calendar calendar = Calendar.getInstance();
        String ourJavaTimestampObject = new Timestamp(System.currentTimeMillis()).toString();
        
        map.put("tt",ourJavaTimestampObject);
        String test = map.toString();
        
        SecretKeySpec key2 = generate128BitAesKey(ourJavaTimestampObject);
        String res2 = encrypt(key2, initVector,test);	
        System.out.println(decrypt(key2, initVector,res2 ));
        //int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
        //System.out.println("maxKeyLen :" + maxKeyLen);
		

	}

}
