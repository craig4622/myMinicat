package com.lagou.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName Response
 * @Description TODO
 * @Author xsq
 * @Date 2020/4/26 16:58
 **/
public class Response {

    private OutputStream outputStream;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void output(String path) throws IOException {
        if (path.contains("demo1") || path.contains("demo2")) {
            path = "/webapps/" + path;
        }
        //获取静态资源的绝对路径
        String absolutePath = StaticAbsolutePathUtil.getAbsolutePath(path);
        File file = new File(absolutePath);
        if (file.exists() && file.isFile()) {
            //输出静态文件资源
            StaticAbsolutePathUtil.outputStaticResource(new FileInputStream(file), outputStream);
        } else {
            outputStream.write(HttpProtocolUtil.getHttpHeaderInfo404().getBytes());
        }
    }
}
