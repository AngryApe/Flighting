package com.ape.lighting.dubbo;

import com.ape.lighting.ExtensionExecutor;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * dubbo 扩展点执行器<br>
 * <strong color="lime">要求dubbo接口必须只有一个入参</strong>
 *
 * @author <a href="mailto:pushu@2dfire.com">朴树</a>
 * @date 2020/1/3 6:57 下午
 */
@Service
public class DubboExtensionExecutor extends ExtensionExecutor<DubboExtension> {

    private Map<String, GenericService> dubboServices = new ConcurrentHashMap<>(32);

    /**
     * 应用配置（注册中心）
     * TODO init
     */
    private ApplicationConfig applicationConfig = new ApplicationConfig();

    @Override
    protected Object doInvoke(DubboExtension extension, Object param) {
        GenericService rpcService = getRpcService(extension);
        if (rpcService == null) {
            return null;
        }
        return rpcService.$invoke(extension.getMethod(), new String[]{extension.getParamType()}, new Object[]{param});
    }

    private GenericService getRpcService(DubboExtension extension) {
        String serviceKey = String.join(":", extension.getApp(), extension.getClazz(), extension.getVersion());
        GenericService service = dubboServices.get(serviceKey);
        if (service == null) {
            service = buildGenericService(extension);
            dubboServices.put(serviceKey, service);
        }
        return service;
    }

    private GenericService buildGenericService(DubboExtension extension) {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface(extension.getClazz());
        reference.setVersion(extension.getVersion());
        reference.setGeneric(true);
        reference.setApplication(applicationConfig);
        reference.setTimeout(extension.getTimeoutMs());
        return reference.get();
    }


}
