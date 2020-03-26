package com.clf.cloud.common.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 2020-03-01
 * @Description: 分页请求分类
 */
@Data
public class BasePageVO {
    @NotNull(message = "当前页码不能为空")
    private Integer nowPage = 1;
    @NotNull(message = "每页数据量不能为空")
    private Integer pageSize = 10;
}
