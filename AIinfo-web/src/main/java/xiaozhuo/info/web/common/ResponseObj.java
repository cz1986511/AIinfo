package com.idmaker.oa.server.model.vo.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idmaker.oa.server.exception.BaseException;
import com.idmaker.oa.server.model.vo.ApplyBaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenzhuo
 * @date   2021-02-06 16:38
 */
public class ResponseObj<T> {
    @Setter
    @Getter
    private ResponseStatus status;
    @Setter @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Page page;
    @Setter @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;


    public static ResponseObj setResultCodeAndMesage(ResultCode resultCode, String message) {
        ResponseObj responseObj= new ResponseObj();
        responseObj.setStatus(new ResponseStatus(resultCode,message));
        return responseObj;
    }

    public static ResponseObj success(Object data){
        ResponseObj responseObj = new ResponseObj();
        responseObj.setData(data);
        return responseObj;
    }
    public static ResponseObj success(){
        return new ResponseObj();
    }
    public ResponseObj(){
        this.status=new ResponseStatus();
    }
    public static ResponseObj authFail(){
        ResponseObj responseObj= new ResponseObj();
        ResponseStatus responseStatus= new ResponseStatus(ResultCode.FAIL_AUTH_FAIL);
        responseObj.setStatus(responseStatus);
        return responseObj;
    }
    public static ResponseObj setException(BaseException e){
        ResponseObj responseObj= new ResponseObj();
        responseObj.setStatus(new ResponseStatus(e));
        return responseObj;
    }
    public static ResponseObj setResultCode(ResultCode resultCode){
        ResponseObj responseObj= new ResponseObj();
        responseObj.setStatus(new ResponseStatus(resultCode));
        return responseObj;
    }
    public static ResponseObj setResultCode(ResultCode resultCode, String message){
        ResponseObj responseObj= new ResponseObj();
        responseObj.setStatus(new ResponseStatus(resultCode,message));
        return responseObj;
    }
}
