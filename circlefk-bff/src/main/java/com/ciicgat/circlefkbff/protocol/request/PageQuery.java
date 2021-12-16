package com.ciicgat.circlefkbff.protocol.request;

public class PageQuery {
    private Integer page = 1;
    private Integer rowsPerPage = 100;
    private final Integer startIndex = 0;

    public Integer getPage() {
        if (page == null || page <= 0) {
            return 1;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRowsPerPage() {
        if (rowsPerPage == null || rowsPerPage <= 0) {
            return 100;
        }
        return rowsPerPage;
    }

    public void setRowsPerPage(Integer rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public Integer getStartIndex() {
        return (this.getPage() - 1) * this.rowsPerPage;
    }
}
