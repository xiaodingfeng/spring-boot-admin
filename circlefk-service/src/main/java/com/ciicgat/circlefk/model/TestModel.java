package com.ciicgat.circlefk.model;


import com.alibaba.excel.annotation.ExcelProperty;

import java.io.Serializable;

public class TestModel implements Serializable {
    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "内容")
    private String content;
    @ExcelProperty(value = "提交人")
    private String submitAdminName;
    @ExcelProperty(value = "标题")
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubmitAdminName() {
        return submitAdminName;
    }

    public void setSubmitAdminName(String submitAdminName) {
        this.submitAdminName = submitAdminName;
    }
}
