package com.ape.lighting;

import com.ape.lighting.domain.ExtensionResponse;
import lombok.Data;

/**
 * @author <a href="mailto:pushu@2dfire.com">朴树</a>
 * @date 2020/1/3 11:29 上午
 */
@Data
public abstract class Extension {
    /**
     * 协议类型
     */
    private String protocol;
    /**
     * 应用
     */
    private String app;
    /**
     * 版本
     */
    private String version;

    /**
     * 响应类型
     */
    private String responseClass;

    /**
     * 获取RPC类型
     *
     * @return
     */
    public String getRpcType() {
        return this.getClass().getSimpleName().replace("Extension", "").toLowerCase();
    }

    /**
     * 获取响应类型
     *
     * @return
     */
    public <T extends ExtensionResponse> Class<T> getResponseType() {
        try {
            return (Class<T>) Class.forName(getResponseClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Not found extensionResponse type: " + responseClass);
        }
    }
}
