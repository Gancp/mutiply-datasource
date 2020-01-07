package com.again.multiplydatasource.service;

public class HelloWord {
    private String name = "again";

    public String say() {
        return "hello " + name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
