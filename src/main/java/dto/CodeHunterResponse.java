package dto;

/**
 * 代码异常处理出参
 */
public class CodeHunterResponse {

    /**
     *  异常类型
     */
    private String exceptionType;

    /**
     *  异常行开始位置
     */
    private Integer exceptionLinesBegin;

    /**
     *  异常行结束位置
     */
    private Integer exceptionLinesEnd;

    /**
     *  异常错误码
     */
    private Integer errorCode;

    public CodeHunterResponse() {
    }

    public CodeHunterResponse(String exceptionType, Integer exceptionLinesBegin, Integer exceptionLinesEnd, Integer errorCode) {
        this.exceptionType = exceptionType;
        this.exceptionLinesBegin = exceptionLinesBegin;
        this.exceptionLinesEnd = exceptionLinesEnd;
        this.errorCode = errorCode;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public Integer getExceptionLinesBegin() {
        return exceptionLinesBegin;
    }

    public void setExceptionLinesBegin(Integer exceptionLinesBegin) {
        this.exceptionLinesBegin = exceptionLinesBegin;
    }

    public Integer getExceptionLinesEnd() {
        return exceptionLinesEnd;
    }

    public void setExceptionLinesEnd(Integer exceptionLinesEnd) {
        this.exceptionLinesEnd = exceptionLinesEnd;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
