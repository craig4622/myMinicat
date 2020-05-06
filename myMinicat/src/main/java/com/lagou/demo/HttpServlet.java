package com.lagou.demo;

import java.io.IOException;

/**
 * @ClassName HttpServlet
 * @Description TODO
 * @Author xsq
 * @Date 2020/4/26 16:41
 **/
public abstract class HttpServlet implements Servlet {

    public abstract void doGet(Request request, Response response) throws IOException;

    public abstract void doPost(Request request, Response response) throws IOException;

    @Override
    public void service(Request request, Response response) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }
}
