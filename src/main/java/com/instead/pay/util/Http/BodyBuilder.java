package com.instead.pay.util.Http;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

public interface BodyBuilder extends ResponseEntity.HeadersBuilder<BodyBuilder> {
    //设置正文的长度，以字节为单位，由Content-Length标头
    BodyBuilder contentLength(long contentLength);

    //设置body的MediaType 类型
    BodyBuilder contentType(MediaType contentType);

    //设置响应实体的主体并返回它。
    <T> ResponseEntity<T> body(@Nullable T body);
}