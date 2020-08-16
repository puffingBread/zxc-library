package org.humor.zxc.library.commons.web.exception;

/***
 *  Date: 2020/4/23
 *  Time: 17:53
 *  @author xuzz
 */
public class ArgException extends RuntimeException {

    private static final long serialVersionUID = -1L;

    private String code;

    public ArgException() {
    }

    public ArgException(String code) {
        this.code = code;
    }

    public ArgException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ArgException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public ArgException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
