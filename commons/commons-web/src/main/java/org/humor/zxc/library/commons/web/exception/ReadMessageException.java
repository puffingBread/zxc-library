package org.humor.zxc.library.commons.web.exception;

/**
 * @author xuzz
 * 自定义可读异常
 *
 */
public class ReadMessageException extends RuntimeException {

	private static final long serialVersionUID = -1L;

	private String code;

	public ReadMessageException() {
	}

	public ReadMessageException(String code) {
		this.code = code;
	}

	public ReadMessageException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ReadMessageException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public ReadMessageException(Throwable cause) {
		super(cause);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
