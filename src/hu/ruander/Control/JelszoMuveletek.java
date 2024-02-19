package hu.ruander.Control;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JelszoMuveletek {

	// A jelszó minimum 8 karakter kell legyen és tartalmaznia kell kisbetűt, nagybetűt, számjegyet
	public static boolean erosJelszo(String jelszo) {
		
		if (jelszo.length() >= 8 && !jelszo.equals(jelszo.toLowerCase()) && !jelszo.equals(jelszo.toUpperCase())) {
			
			for (int i=0; i<=9; i++) {
				
				if (jelszo.contains(String.valueOf(i))) {
					
					return true;
				}
				
			}
			
		}
				
		return false;
	}

	// SHA256
	public static String titkosit(String jelszo) {
		
		String eredmeny = null;
		try {
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(jelszo.getBytes("UTF-8"));
			
			eredmeny = bytesToHex(hash);
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return eredmeny;
	}

	private static String bytesToHex(byte[] hash) {
		
		StringBuilder sb = new StringBuilder(2*hash.length);
		for (int i=0; i<hash.length; i++) {
			
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				
				sb.append('0');
				
			}
			sb.append(hex);
			
		}
		
		return sb.toString();
	}
	
	
	

}
