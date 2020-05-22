package com.instead.pay.util.Security;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author vring
 * @date 2019/12/12
 * <p>
 * 返回给前端的json数据格式
 */
@Data
@Component
public class UrlResponse implements Serializable {

    private boolean success;
    private String errorCode;
    private String mes;
    private Object data;
    private int total;

    public UrlResponse() {
    }

    public UrlResponse(boolean success, String errorCode, String mes, Object data) {
        this.success = success;
        this.errorCode = errorCode;
        this.mes = mes;
        this.data = data;
    }

    @Override
    public String toString() {
        return "UrlResponse{" +
                "success=" + success +
                ", code='" + errorCode + '\'' +
                ", message='" + mes + '\'' +
                ", data=" + data +
                ", total=" + total +
                '}';
    }
}

