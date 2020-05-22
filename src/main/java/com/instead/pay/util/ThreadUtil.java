package com.instead.pay.util;

import com.instead.pay.commercial.model.Commercial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author vring
 * @ClassName TheardUtil.java
 * @Description
 * @createTime 2019/12/14 14:48
 */
@Slf4j
@Component
public class ThreadUtil {
    private static ThreadLocal<Commercial> commercialThreadLocal = new ThreadLocal<>();

    private static ThreadUtil instance = new ThreadUtil();

    public static ThreadUtil getInstance() {
        return instance;
    }

    public void bind(Commercial commercial) {
        commercialThreadLocal.set(commercial);
    }

    public Commercial get() {
        return commercialThreadLocal.get();
    }

    public void remove() {
        if (get() != null) commercialThreadLocal.remove();
    }
}
