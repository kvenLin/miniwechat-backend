package com.clf.cloud.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: clf
 * @Date: 2020-03-01
 * @Description: 封装Json格式的Map
 */
public class PageUtils {

    /**
     * 分页信息Json的重构
     * @param page 这里是使用的Mybatis-plus内部的IPage, 需要依赖引入
     * @param title
     * @return
     */
    public static Map<String, Object> getPageResult(IPage page, String title) {
        HashMap<String, Object> result = Maps.newHashMap();
        result.put("totalSize", page.getTotal());
        result.put("totalPages", page.getPages());
        result.put("pageSize", page.getSize());
        result.put("nowPage", page.getCurrent());
        result.put(title, page.getRecords());
        return result;
    }
}
