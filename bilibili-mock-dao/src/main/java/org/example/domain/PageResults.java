package org.example.domain;
import java.util.List;

public class PageResults<T> {
    private Integer total;

    private List<T> List;

    public PageResults(Integer total, List<T> list) {
        this.total = total;
        List = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public java.util.List<T> getList() {
        return List;
    }

    public void setList(java.util.List<T> list) {
        List = list;
    }
}
