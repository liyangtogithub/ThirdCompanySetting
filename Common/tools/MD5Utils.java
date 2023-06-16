package com.landsem.common.tools;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apaches.commons.codec.digest.DigestUtils;

/**
 * DigestUtils
 * 
 *  2014-03-20
 */
public class MD5Utils {

//	public static final String APAD_KEY = "lqpdc_wgeiay";
	public static final String APAD_KEY = "landsem_video";
    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };


    /**
     * encode By MD5
     * 
     * @param str
     * @return String
     */
    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return new String(encodeHex(messageDigest.digest()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     * 
     * @param data a byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     */
    protected static char[] encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return out;
    }
    
	public static String hash(String apadKey,byte[] bytes) {
		if(StringUtils.isEmpty(apadKey)) apadKey = APAD_KEY;
		return hash(bytes, apadKey);
	}

	public static String hash(byte[] bytes, String key) {
		String digestStr = null;
		try {
			byte[] keyBytes = key.getBytes("UTF-8");
			byte[] toDigest = new byte[bytes.length + keyBytes.length];
			System.arraycopy(bytes, 0, toDigest, 0, bytes.length);
			System.arraycopy(keyBytes, 0, toDigest, bytes.length, keyBytes.length);
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] rs = digest.digest(toDigest);
			int j = rs.length;
			char[] chars = new char[j * 2];
			int k = 0;
			for (int i = 0; i < rs.length; i++) {
				byte b = rs[i];
				chars[k++] = DIGITS_LOWER[b >>> 4 & 0xf];
				chars[k++] = DIGITS_LOWER[b & 0xf];
			}
			digestStr = new String(chars);
		}
		catch (NoSuchAlgorithmException ex) {}
		catch (UnsupportedEncodingException e) {}
		return digestStr;
	}
	

	public static void main(String[] args) {
		System.out.println(md5sum("/init.rc"));
	}

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String md5sum(String filename) {
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String md5sum(byte[] data) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(data, 0, data.length);
			return toHexString(md5.digest());
		} catch (Exception e) {
			return null;
		}
	}
	

	/**
	 * md5 encode string
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String EncoderByMd5(String str)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {		
		 return DigestUtils.md5Hex(str);
	}
    
    
    
}
