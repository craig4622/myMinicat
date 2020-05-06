package com.lagou.demo;

import java.io.IOException;

/**
 * @ClassName LagouServlet
 * @Description TODO
 * @Author xsq
 * @Date 2020/4/27 18:22
 **/
public class LagouServlet extends HttpServlet {

    @Override
    public void doGet(Request request, Response response) throws IOException {
        String getContent = "<h1>LagouServlet  get</h1>";
        response.getOutputStream().write((HttpProtocolUtil.getHttpHeaderInfo200(getContent.getBytes().length) + getContent).getBytes());
    }

    @Override
    public void doPost(Request request, Response response) throws IOException {
        String getContent = "<h1>LagouServlet  post</h1>";
        response.getOutputStream().write((HttpProtocolUtil.getHttpHeaderInfo200(getContent.getBytes().length) + getContent).getBytes());
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destory() throws Exception {

    }
}
