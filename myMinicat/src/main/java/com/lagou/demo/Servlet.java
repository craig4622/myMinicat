package com.lagou.demo;

/**
 * @ClassName Servlet
 * @Description TODO
 * @Author xsq
 * @Date 2020/4/26 16:38
 **/
public interface Servlet {

    void init() throws Exception;

    void destory() throws Exception;

    void service(Request request, Response response) throws Exception;
}
