package com.clf.cloud.gatewayserver.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * auth_query
 * @author 
 */
@Data
public class AuthQuery implements Serializable {
    private Long id;

    /**
     * 配置的uri
     */
    private String url;

    /**
     * 0:允许匿名访问,1:拒绝匿名访问
     */
    private Integer type;

    private static final long serialVersionUID = 1L;
}