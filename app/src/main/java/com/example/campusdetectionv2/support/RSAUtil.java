package com.example.campusdetectionv2.support;
//package com.jetsum.util;  
  
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
  
/** 
 * RSA绠楁硶锛屽疄鐜版暟鎹殑鍔犲瘑瑙ｅ瘑銆?
 * @author ShaoJiang 
 * 
 */  
public class RSAUtil {  
      
    private static Cipher cipher;  
      
    static{  
        try {  
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
        }  
    }  
  
     
    /** 
     * 寰楀埌鍏挜 
     *  
     * @param key 
     *            瀵嗛挜瀛楃涓诧紙缁忚繃base64缂栫爜锛?
     * @throws Exception 
     */  
    
    public static PublicKey getPublicKey(String key) throws Exception {  
        byte[] keyBytes;  
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
        PublicKey publicKey = keyFactory.generatePublic(keySpec);  
        return publicKey;  
    }  
     
    /** 
     * 寰楀埌绉侀挜 
     *  
     * @param key 
     *            瀵嗛挜瀛楃涓诧紙缁忚繃base64缂栫爜锛?
     * @throws Exception 
     */  
    public static PrivateKey getPrivateKey(String key) throws Exception {  
        byte[] keyBytes;  
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);  
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);  
        return privateKey;  
    }  
  
    /** 
     * 寰楀埌瀵嗛挜瀛楃涓诧紙缁忚繃base64缂栫爜锛?
     *  
     * @return 
     */  
    public static String getKeyString(Key key) throws Exception {  
        byte[] keyBytes = key.getEncoded();  
        String s = (new BASE64Encoder()).encode(keyBytes);  
        return s;  
    }         
      
       
    /** 
     * 浣跨敤keystore瀵规槑鏂囪繘琛屽姞瀵?
     * @param publicKeystore 鍏挜鏂囦欢璺緞 
     * @param plainText      鏄庢枃 
     * @return 
     */  
    public static String encrypt(String plainText,InputStream is ){  
        try {             
        	
//            FileReader fr = new FileReader(publicKeystore); 
            InputStreamReader isr = new  InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);  
            String publicKeyString="";  
            String str;  
            while((str=br.readLine())!=null){  
                publicKeyString+=str;  
            } 
//            System.out.println("from RSA " + publicKeyString);
            isr.close();
            br.close();  
//            fr.close(); 
            cipher.init(Cipher.ENCRYPT_MODE,getPublicKey(publicKeyString));  
            byte[] enBytes = cipher.doFinal(plainText.getBytes());            
            return (new BASE64Encoder()).encode(enBytes);  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        } catch (BadPaddingException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }     
      
      
    /** 
     * 浣跨敤keystore瀵瑰瘑鏂囪繘琛岃В瀵?
     * @param privateKeystore  绉侀挜璺緞 
     * @param enStr  瀵嗘枃 
     * @return 
     */  
    public static String decrypt(String privateKeystore,String enStr){  
        try {  
            FileReader fr = new FileReader(privateKeystore);  
            BufferedReader br = new BufferedReader(fr);  
            String privateKeyString="";  
            String str;  
            while((str=br.readLine())!=null){  
                privateKeyString+=str;  
            }  
            br.close();  
            fr.close();           
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyString));  
            byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));  
            return new String(deBytes);  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        } catch (BadPaddingException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    
}  
