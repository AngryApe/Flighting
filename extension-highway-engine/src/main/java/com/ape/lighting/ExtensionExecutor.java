package com.ape.lighting;

/**
 * @author <a href="mailto:pushu@2dfire.com">朴树</a>
 * @date 2020/1/3 6:24 下午
 */
public abstract class ExtensionExecutor<E extends Extension> {

    /**
     * 执行扩展点
     *
     * @param extension
     * @param param
     * @return
     */
    public Object execute(E extension, Object param) {
        try {

            checkParam(param);

            return doInvoke(extension, param);

        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 参数检查
     *
     * @param param
     */
    protected void checkParam(Object param) {

    }

    /**
     * 调用远程RPC
     *
     * @param extension 扩展点配置
     * @param param     请求参数
     * @return
     */
    protected abstract Object doInvoke(E extension, Object param);

    /**
     * 获取协议
     *
     * @return
     */
    public String getProtocol() {
        return this.getClass().getSimpleName().replace("ExtensionExecutor", "").toLowerCase();
    }
}
