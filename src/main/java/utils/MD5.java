package main.java.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	static MessageDigest md;
	

	public static String hash( String data ) {
		if( md == null ) {
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}		
		md.reset();
		md.update( data.getBytes() );
		return DatatypeConverter.printHexBinary( md.digest() );		
	}
}
