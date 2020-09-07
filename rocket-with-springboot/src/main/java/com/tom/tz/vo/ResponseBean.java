package com.tom.tz.vo;

/**
 * 给前端页面返回的应答实体
 */
public class ResponseBean {
    //状态码
    private Integer code;
    //返回信息
    private String message;
    //返回的数据
    private Object data;

    public ResponseBean(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
