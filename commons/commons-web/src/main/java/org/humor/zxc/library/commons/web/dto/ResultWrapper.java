package org.humor.zxc.library.commons.web.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultWrapper<T> implements Serializable {

	private final static String SUCCESS = "SUCCESS";
	private final static String FAIL = "FAIL";
	private static final long serialVersionUID = -3877497922136761884L;

	private Integer status;
	private String msg;
	private T data;
	private Error error;

	public ResultWrapper() {
	}

	public ResultWrapper(T data) {
		this.status = 200;
		this.msg = SUCCESS;
		this.data = data;
	}

	public ResultWrapper(Integer status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public static <T> ResultWrapper<T> success() {
		ResultWrapper<T> resultWrapper = new ResultWrapper<>();
		resultWrapper.setStatus(200);
		resultWrapper.setMsg(SUCCESS);

		return resultWrapper;
	}

	public static <T> ResultWrapper<T> success(T data) {
		ResultWrapper<T> resultWrapper = success();
		resultWrapper.setData(data);
		return resultWrapper;
	}

	public static <T> ResultWrapper<T> successMsg(String msg) {
		ResultWrapper<T> resultWrapper = success();
		resultWrapper.setMsg(msg);
		return resultWrapper;
	}

	public static <T> ResultWrapper<T> success(T data, String msg) {
		ResultWrapper<T> resultWrapper = success();
		resultWrapper.setData(data);
		resultWrapper.setMsg(msg);
		return resultWrapper;
	}

	/**
	 * 禁止返回没有描述信息的错误提示
	 * @param <T>
	 * @return
	 */
	@Deprecated
	public static <T> ResultWrapper<T> fail() {
		ResultWrapper<T> resultWrapper = new ResultWrapper<>();
		resultWrapper.setStatus(500);
		resultWrapper.setMsg(FAIL);

		return resultWrapper;
	}

	public static <T> ResultWrapper<T> fail(String errMsg, Integer status) {
		ResultWrapper<T> resultWrapper = new ResultWrapper<>();
		resultWrapper.setStatus(status);
		resultWrapper.setMsg(errMsg);
		return resultWrapper;
	}

	public static <T> ResultWrapper<T> failMsg(String msg) {
		ResultWrapper<T> resultWrapper = fail();
		resultWrapper.setMsg(msg);
		return resultWrapper;
	}

	public static <T> ResultWrapper<T> fail(T data) {
		ResultWrapper<T> resultWrapper = fail();
		resultWrapper.setData(data);
		return resultWrapper;
	}

	public static ResultWrapper fail(String code, String message) {
		Error error = new Error(code, message);
		ResultWrapper resultWrapper = fail();
		resultWrapper.setError(error);
		resultWrapper.setMsg(message);
		return resultWrapper;

	}

	public static ResultWrapper fail(Error error) {
		ResultWrapper resultWrapper = fail();
		resultWrapper.setError(error);
		resultWrapper.setMsg(error.getMessage());
		return resultWrapper;
	}

	public static <T> ResultWrapper<T> badRequest(Error error) {
		ResultWrapper<T> resultWrapper = new ResultWrapper<>();
		resultWrapper.setStatus(400);
		resultWrapper.setError(error);
		resultWrapper.setMsg(error.getMessage());
		return resultWrapper;
	}

	public static <T> ResultWrapper<T> badRequest(Error error, String errMsg) {
		ResultWrapper<T> resultWrapper = new ResultWrapper<>();
		resultWrapper.setStatus(400);
		resultWrapper.setError(error);
		resultWrapper.setMsg(errMsg);
		return resultWrapper;
	}

	public static <T> ResultWrapper<T> fail(Error error, String errmsg) {
		ResultWrapper<T> wrapper = fail();
		wrapper.setError(error);
		wrapper.setMsg(errmsg);
		return wrapper;
	}

	public Integer getStatus() {
		return status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	/**
	 * Errors, or more specifically Service Errors, are defined as a client passing invalid data to
	 * the service and the service correctly rejecting that data. Examples include invalid credentials,
	 * incorrect parameters, unknown version IDs, or similar. These are generally "4xx" HTTP error
	 * codes and are the result of a client passing incorrect or invalid data.
	 *
	 * Errors do not contribute to overall API availability.
	 */
	public static class Error {
        /**
         * One of a server-defined set of error codes
         */
        private String code;
        /**
         * A human-readable representation of the error
         */
        private String message;
        /**
         * The target of the error
         */
        private String target;

        /**
         * An array of details about specific errors that led to this reported error
         */
        private List<Error> details;
        /**
         * An object containing more specific information than the current object about the error
         */
        private InnerError innerError;

		public Error(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public Error(String code, String message, String target) {
			this.code = code;
			this.message = message;
			this.target = target;
		}

		public void addDetails(Error error) {
			if (details == null) {
				details = new ArrayList<>();
			}

			details.add(error);
		}

		public void addDetails(String code, String message) {
			if (details == null) {
				details = new ArrayList<>();
			}

			details.add(new Error(code, message));
		}

		public void addDetails(String code, String message, String target) {
			if (details == null) {
				details = new ArrayList<>();
			}

			details.add(new Error(code, message, target));
		}


		public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public List<Error> getDetails() {
            return details;
        }

        public void setDetails(List<Error> details) {
            this.details = details;
        }

        public InnerError getInnerError() {
            return innerError;
        }

        public void setInnerError(InnerError innerError) {
            this.innerError = innerError;
        }

	}

	public static class InnerError {
        /**
         * A more specific error code than was provided by the containing error
         */
	    private String code;
        /**
         * An object containing more specific information than the current object about the error
         */
	    private InnerError innerError;

        public InnerError(String code, InnerError innerError) {
            this.code = code;
            this.innerError = innerError;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public InnerError getInnerError() {
            return innerError;
        }

        public void setInnerError(InnerError innerError) {
            this.innerError = innerError;
        }
    }

}
