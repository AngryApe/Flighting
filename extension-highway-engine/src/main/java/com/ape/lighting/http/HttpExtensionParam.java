package com.ape.lighting.http;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * http 扩展请求参数（暂不支持文件上传）
 *
 * @author <a href="mailto:pushu@2dfire.com">朴树</a>
 * @date 2020/1/3 9:26 下午
 */
@Data
public class HttpExtensionParam {
    /**
     * 请求路径：/user/info
     */
    private String path;
    /**
     * 请求参数：name=jack&gender=male
     */
    private String queryStr;
    /**
     * 请求头
     */
    private Map<String, String> headers = new HashMap<String, String>();
    /**
     * 请求体
     */
    private String body;
}
