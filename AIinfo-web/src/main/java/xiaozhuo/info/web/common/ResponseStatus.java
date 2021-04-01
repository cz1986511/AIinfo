package com.idmaker.oa.server.model.vo.common;

import com.idmaker.oa.server.exception.BaseException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

    public ResponseStatus(BaseException e) {
        this.code = e.getCode();
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
