package xiaozhuo.info.web.common;

import lombok.Data;

/**
 * @author chenzhuo
 * @date   2021-02-19 14:14
 */
@Data
public class ResponseStatus {

    private int code;

    private String message;

    public ResponseStatus() {
        this.code = ResultCode.SUCCESS.getCode();
        this.message = ResultCode.SUCCESS.getDesc();
    }

    public ResponseStatus(Exception e) {
        this.message = e.getMessage();
    }

    public ResponseStatus(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getDesc();
    }

    public ResponseStatus(ResultCode resultCode, String message) {
        this.code = resultCode.getCode();
        this.message = message;
    }
}
