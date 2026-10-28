package com.ruoyi.common.restful;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonRestful implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private Object data;

    private JsonRestful(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    private<T> JsonRestful(ResponseCode responseCode, T data) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

//    private JsonResult(Integer code, String message) {
//        this.code = code;
//        this.message = message;
//    }

    /**
     * 返回成功信息
     * @param data      信息内容
     * @param <T>
     * @return
     */
    public static<T> JsonRestful success(T data) {
        return new JsonRestful(ResponseCode.SUCCESS, data);
    }

    /**
     * 返回成功信息
     * @return
     */
    public static JsonRestful success() {
        return new JsonRestful(ResponseCode.SUCCESS);
    }

    /**
     * 返回错误信息
     * @param responseCode      响应码
     * @return
     */
    public static JsonRestful error(ResponseCode responseCode) {
        return new JsonRestful(responseCode);
    }
}

