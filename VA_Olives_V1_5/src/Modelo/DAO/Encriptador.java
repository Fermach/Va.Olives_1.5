package Modelo.DAO;

import java.awt.List;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Esta clase encripta y desencripta palabras en función a una llave 
 * @author DESARROLLO1
 *
 */
public class Encriptador {
    
	private final  String KEY =  "bohemianrhapsody";
	private String dato;
	private Key aesKey ;
	private Cipher cipher;

	public Encriptador() {
		
	}
	public String encriptarCadena(String cadena) {
		
		aesKey = new SecretKeySpec(	KEY.getBytes(), "AES");
		byte[] encrypted = null;
	    try {
			cipher = Cipher.getInstance("AES");
			 try {
					cipher.init(Cipher.ENCRYPT_MODE, aesKey);
					
					try {
						encrypted = cipher.doFinal(cadena.getBytes());
						dato= new String(encrypted);
						return dato;
					} catch (IllegalBlockSizeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		       
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return "";
		
	}
	
	
	public String desenciptarCadena(String cadena) {
	
		 
		 Key aesKey = new SecretKeySpec(KEY.getBytes(), "AES");
         try {
			Cipher cipher = Cipher.getInstance("AES");
			 try {
					cipher.init(Cipher.DECRYPT_MODE, aesKey);
					//System.out.println(dato);
				    dato = new String(cipher.doFinal(cadena.getBytes()));
				   // System.out.println(dato);
				    return dato;
					
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
       // System.out.println("DATO: "+dato);
     return "";
		
		
	}
	
	

	
	/*
	public static void main(String[] args) {
		System.out.println(encriptarCadena("Administrador"));
		System.out.println(encriptarCadena("$imatec$31"));

		
	}*/
}
