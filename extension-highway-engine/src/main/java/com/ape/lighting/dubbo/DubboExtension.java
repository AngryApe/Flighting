package com.ape.lighting.dubbo;

import com.ape.lighting.Extension;
import lombok.Data;

/**
 * @author <a href="mailto:pushu@2dfire.com">朴树</a>
 * @date 2020/1/3 6:55 下午
 */
@Data
public class DubboExtension extends Extension {

    private String bizCode;

    private String clazz;

    private String method;

    private String paramType;

    private int timeoutMs;
}
