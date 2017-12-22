package com.danlu.dleye.client.entity;

import java.io.Serializable;

/**
 * 接口返回Json时封装对象
 * <p>
 * <strong>前端ajax接口基本规范</strong><br />
 * 1. 任何ajax方法，都必须使用<code>{@link JsonResult#getJson()}</code>返回结果；<br />
 * 2. 前端调用服务方法，得到<code>JsonResult</code>对象的json；<br />
 * 3. {@link JsonResult#getStatus()}为0表示服务方法执行成功，通过{@link #getData()}得到执行结果；<br />
 * 4. {@link JsonResult#getStatus()}不为0表示服务方法执行失败，通过{@link #getMsg()}获取消息描述信息（错误信息）。<br />
 * <p>
 * <strong>消息代码{@link #getStatus()}、消息描述{@link #getMsg()}使用规范</strong><br />
 * 1. 绝大部分服务方法不需要使用消息代码。<br />
 *    &nbsp;&nbsp;&nbsp;如<strong>前端ajax接口基本规范</strong>中所示，前端通过<code>getSatus()</code>确定服务方法是否执行成功，
 *    如果执行失败，通过<code>getMsg()</code>得到错误描述即可；<br />
 *    &nbsp;&nbsp;&nbsp;服务器端发生异常执行失败时，详细的错误描述、堆栈信息应当记录到数据库或者服务端的文件日志中，用于问题排查处理；
 *    然后通过<code>setError(String errCode, String msg)</code>返回对用户友好的、业务型的描述信息，前端开发者通过这个信息可以大致了解、定位问题所在，详细的排查处理则通过服务端日志完成。
 *    前端可以选择直接或者根据场景稍加补充，在用户界面展示这个错误消息，向用户解释执行状况。<br />
 * @Filename: JsonResult.java
 * @Version: 1.0
 * @Author: tarzan 陈家川
 * @Email: chenjiachuan@danlu.com
 *
 */
public class JsonResult<T> implements Serializable {

    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -9036716327185612872L;

    private int status = 0;

    private T data = null;

    private String msg = "";

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public void setError(final int errCode, final String msg) {
        setStatus(errCode);
        setMsg(msg);
    }
    
    public void setSuccess(int succCode,String msg,T date){
    	setStatus(succCode);
    	setMsg(msg);
    	setData(date);
    }

    /** 
     * <p>Title: toString</p> 
     * <p>Description: </p> 
     * @return 
     * @see java.lang.Object#toString() 
     */
    @Override
    public String toString()
    {
        return "JsonResult [status=" + status + ", data=" + data + ", msg=" + msg + "]";
    }
    
}
