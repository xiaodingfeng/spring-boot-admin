package com.ciicgat.circlefkbff.protocol;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Page<T>{
    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    private Long total = 0L;
    /**
     * 每页显示条数，默认 10
     */
    private Long size = 10L;

    /**
     * 当前页
     */
    private Long current = 1L;

    /**
     * 自动优化 COUNT SQL
     */
    private Boolean optimizeCountSql = true;

    private Boolean searchCount = true;
    /**
     * 是否命中count缓存
     */
    private Boolean hitCount = false;

    private String countId;

    private Long maxLimit;

    private Integer pages;

    private List<Object> orders;
}