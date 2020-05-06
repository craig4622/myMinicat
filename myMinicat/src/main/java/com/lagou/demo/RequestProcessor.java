package com.lagou.demo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @ClassName RequestProcessor
 * @Description TODO
 * @Author xsq
 * @Date 2020/4/28 10:26
 **/
public class RequestProcessor extends Thread {

    private Socket socket;

    private Map<String, Class> servletMap;

    public RequestProcessor(Socket socket, Map<String, Class> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            //加载静态资源
            if (servletMap.get(request.getUrl()) == null) {
                response.output(request.getUrl());
            } else {
                //加载动态资源,反射获取对应的类执行对应的方法
                Class className = servletMap.get(request.getUrl());
                Object obj = className.newInstance();
                Method[] methods = className.getSuperclass().getMethods();
                Method method = null;
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().contains("service")) {
                        method = methods[i];
                        break;
                    }
                }
                method.invoke(obj, request, response);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
