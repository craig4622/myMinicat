package com.lagou.demo.connector;

import java.util.List;

/**
 * @ClassName Host
 * @Description TODO
 * @Author xsq
 * @Date 2020/4/30 14:04
 **/
public class Host {

    private String name;

    private String appBase;

    private List<Context> contextList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppBase() {
        return appBase;
    }

    public void setAppBase(String appBase) {
        this.appBase = appBase;
    }

    public List<Context> getContextList() {
        return contextList;
    }

    public void setContextList(List<Context> contextList) {
        this.contextList = contextList;
    }
}
