package com.landsem.common.entiry;

/**
 * get data failed reason
 * 
 *  2013-11-25
 */
public class FailedReason {

    private FailedType failedType;
    private Throwable  cause;

    public FailedReason(FailedType failedType, String cause) {
        this.failedType = failedType;
        this.cause = new Throwable(cause);
    }

    public FailedReason(FailedType failedType, Throwable cause) {
        this.failedType = failedType;
        this.cause = cause;
    }

    /**
     * get failedType
     * 
     * @return the failedType
     */
    public FailedType getFailedType() {
        return failedType;
    }

    /**
     * get cause
     * 
     * @return the cause
     */
    public Throwable getCause() {
        return cause;
    }

    public static enum FailedType {
        /** get image from network or save image to sdcard error **/
        ERROR_IO,
        /** get image with out of memory error **/
        ERROR_OUT_OF_MEMORY,
        /** reserved field, it's no use now, waiting to be perfect^_^ **/
        ERROR_UNKNOWN,
    }
}
