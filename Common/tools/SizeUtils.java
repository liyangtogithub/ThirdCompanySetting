package com.landsem.common.tools;

/**
 * SizeUtils
 * 
 *  2013-5-15
 */
public class SizeUtils {

    /** gb to byte **/
    public static final long GB_2_BYTE = 1073741824;
    /** mb to byte **/
    public static final long MB_2_BYTE = 1048576;
    /** kb to byte **/
    public static final long KB_2_BYTE = 1024;
    
    public static String convertFileSize(long size) {
        if (size >= GB_2_BYTE) {
            return String.format("%.1f GB", (float) size / GB_2_BYTE);
        } else if (size >= MB_2_BYTE) {
            float f = (float) size / MB_2_BYTE;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= KB_2_BYTE) {
            float f = (float) size / KB_2_BYTE;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }
    
    
    
}
