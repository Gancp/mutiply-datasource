package com.again.mutiplydatasource.domain.ds1;

public class DemoOne {
    private Integer id;

    private String mark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }

    @Override
    public String toString() {
        return "DemoOne{" +
                "id=" + id +
                ", mark='" + mark + '\'' +
                '}';
    }
}