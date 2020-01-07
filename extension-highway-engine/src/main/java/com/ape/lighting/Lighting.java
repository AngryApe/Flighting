package com.ape.lighting;

import com.alibaba.dubbo.rpc.RpcContext;
import com.dfire.magiceye.propagation.TextMapCodec;
import com.dfire.soa.promotion.platform.util.LogUtil;
import com.dfire.tpt.utils.log.format.KVJsonFormat;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Extension 执行器
 *
 * @author <a href="mailto:pushu@2dfire.com">朴树</a>
 * @date 2020/1/3 11:29 上午
 */
@Component
public class Lighting implements ApplicationContextAware {
    /**
     * 扩展点key分隔符
     */
    public static final String KEY_SPLITTER = ":";
    /**
     * 默认rpc版本号
     */
    public static final String DEFAULT_VERSION = "1.0.0";
    /**
     * 扩展点本地缓存
     */
    protected static Map<String, Extension> EXTENSION_CONFIGS = Maps.newConcurrentMap();
    /**
     * 扩展点执行器
     */
    protected static Map<String, ExtensionExecutor> EXECUTORS = Maps.newHashMap();

    /**
     * Spring 上下文
     */
    private static ApplicationContext springContext;

    /**
     * 扩展点执行入口
     *
     * @param bizCode
     * @param param
     * @return
     */
    public static Object invoke(String bizCode, String version, Object param) {
        Extension extension = EXTENSION_CONFIGS.get(String.join(KEY_SPLITTER, bizCode, version));
        if (extension == null) {
            LogUtil.EXECUTOR.error(LogUtil.formatLog(getJsonLog("Not Found Extension", bizCode, version)));
            throw new RuntimeException();
        }
        ExtensionExecutor executor = EXECUTORS.get(extension.getRpcType());
        if (executor == null) {
            KVJsonFormat jsonLog = getJsonLog("Not Found ExtensionExecutor", bizCode, version);
            jsonLog.add("rpcType", extension.getRpcType());
            LogUtil.EXECUTOR.error(LogUtil.formatLog(jsonLog));
            throw new RuntimeException();
        }
        try {
            return executor.execute(extension, param);
        } catch (Exception e) {
            KVJsonFormat jsonLog = getJsonLog("Execute Extension Error", bizCode, version);
            jsonLog.add("param", param);
            LogUtil.EXECUTOR.error(LogUtil.formatLog(jsonLog));
            throw new RuntimeException();
        }
    }

    /**
     * 扩展点执行入口
     *
     * @param bizCode
     * @param param
     * @return
     */
    public static Object invoke(String bizCode, Object param) {
        return invoke(bizCode, DEFAULT_VERSION, param);
    }

    private static KVJsonFormat getJsonLog(String title, String bizCode, String version) {
        return KVJsonFormat.title(title)
                .add("bizCode", bizCode)
                .add("version", version)
                .add("traceId", RpcContext.getContext().get(TextMapCodec.SPAN_CONTEXT_KEY));
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springContext = applicationContext;
        Map<String, ExtensionExecutor> executors = springContext.getBeansOfType(ExtensionExecutor.class);
        if (MapUtils.isNotEmpty(executors)) {
            for (ExtensionExecutor executor : executors.values()) {
                EXECUTORS.put(executor.getProtocol(), executor);
            }
        }
        
    }
}
