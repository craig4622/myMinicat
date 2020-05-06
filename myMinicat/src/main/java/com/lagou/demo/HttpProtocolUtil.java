package com.lagou.demo;

/**
 * @ClassName HttpProtocolUtil
 * @Description TODO
 * @Author xsq
 * @Date 2020/4/27 10:18
 **/
public class HttpProtocolUtil {


    public static String getHttpHeaderInfo200(long length) {
        return "HTTP/1.1 200 OK\n" + "Content-Type: text/html;charset=UTF-8\n"
                + "Content-Length: " + length + "\n" + "\r\n";
    }

    public static String getHttpHeaderInfo404() {
        String str404 = "<h1>404 not found</h1>";
        return "HTTP/1.1 404 NOT Found\n" + "Content-Type: text/html;charset=UTF-8\n"
                + "Content-Length: " + str404.getBytes().length + "\n" + "\r\n" + str404;
    }
}
