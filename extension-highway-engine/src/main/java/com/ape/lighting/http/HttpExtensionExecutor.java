package com.ape.lighting.http;

import com.ape.lighting.ExtensionExecutor;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:pushu@2dfire.com">朴树</a>
 * @date 2020/1/3 6:58 下午
 */
@Service
public class HttpExtensionExecutor extends ExtensionExecutor<HttpExtension> {

    @Override
    protected void checkParam(Object param) {
        if (!(param instanceof HttpExtensionParam)) {
            throw new RuntimeException("Wrong param type in HttpExtensionExecutor");
        }
    }

    @Override
    protected Object doInvoke(HttpExtension extension, Object param) {
        return null;
    }
}
