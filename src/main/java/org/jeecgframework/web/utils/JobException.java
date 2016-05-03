package org.jeecgframework.web.utils;

/**
 * 自定义异常类
 */
class JobException extends RuntimeException {
    private static final long serialVersionUID = -6065011541320730491L;
    /**
     * Exception code
     */
    protected String          resultCode       = "UN_KNOWN_EXCEPTION";

    /**
     * Exception message
     */
    protected String          resultMsg        = "未知异常";

    public JobException(String message, Throwable e) {
        super(message, e);
        this.resultMsg = message;
    }

    /**
     * Instantiates a new DexcoderException.
     *
     * @param e the e
     */
    public JobException(Throwable e) {
        super(e);
        this.resultMsg = e.getMessage();
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public JobException(String message) {
        super(message);
        this.resultMsg = message;
    }

    /**
     * Constructor
     *
     * @param code    the code
     * @param message the message
     */
    public JobException(String code, String message) {
        super(message);
        this.resultCode = code;
        this.resultMsg = message;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
