package dto;

/**
 * 代码异常处理接口入参
 */
public class CodeHunterRequest {

    private String codeData;

    public CodeHunterRequest() {
    }

    public CodeHunterRequest(String codeData) {
        this.codeData = codeData;
    }

    public String getCodeData() {
        return codeData;
    }

    public void setCodeData(String codeData) {
        this.codeData = codeData;
    }
}
